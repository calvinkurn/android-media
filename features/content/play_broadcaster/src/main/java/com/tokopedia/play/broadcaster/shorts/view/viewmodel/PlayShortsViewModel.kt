package com.tokopedia.play.broadcaster.shorts.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.domain.model.OnboardAffiliateRequestModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsMediaUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsCoverFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsTitleFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUploadUiState
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel.Companion.TYPE_SHORTS_AFFILIATE
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsInterspersingConfigUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.ProductVideoUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.error.DefaultErrorThrowable
import com.tokopedia.play_common.util.extension.combine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsViewModel @Inject constructor(
    dataStore: PlayBroadcastDataStore,
    private val repo: PlayShortsRepository,
    private val sharedPref: HydraSharedPreferences,
    private val accountManager: PlayShortsAccountManager,
    private val creationUploader: CreationUploader,
) : ViewModel() {

    /** Public Getter */
    val shortsId: String
        get() = _config.value.shortsId

    val title: String
        get() = _titleForm.value.title

    val maxTitleCharacter: Int
        get() = _config.value.maxTitleCharacter

    val productSectionList: List<ProductTagSectionUiModel>
        get() = _productSectionList.value

    val maxProduct: Int
        get() = _config.value.maxTaggedProduct

    val isAllMandatoryMenuChecked: Boolean
        get() = _titleForm.value.title.isNotEmpty() && _productSectionList.value.any { it.products.isNotEmpty() }

    val isAllowChangeAccount: Boolean
        get() = accountManager.isAllowChangeAccount(_accountList.value)

    val accountList: List<ContentAccountUiModel>
        get() = _accountList.value

    val selectedAccount: ContentAccountUiModel
        get() = _selectedAccount.value

    val isFormFilled: Boolean
        get() = _titleForm.value.title.isNotEmpty() ||
            _productSectionList.value.isNotEmpty() ||
            _coverForm.value.coverUri.isNotEmpty()

    val tncList: List<TermsAndConditionUiModel>
        get() = _config.value.tncList

    val isShowSetupCoverCoachMark: Boolean
        get() = sharedPref.isShowSetupCoverCoachMark()

    val uploadedCoverSource: Int
        get() = sharedPref.getUploadedCoverSource(selectedAccount.id, SOURCE)

    val productVideo: ProductVideoUiModel
        get() = _productVideo.value

    val coverUri: String
        get() = _coverForm.value.coverUri.ifEmpty { _media.value.mediaUri }

    val isCoverSelected: Boolean
        get() = _coverForm.value.cover !is CoverSetupState.Blank

    val mDataStore = dataStore
    private val isAfterUploadAutoGeneratedCover: Boolean
        get() = sharedPref.getSavedSelectedAutoGeneratedCover(selectedAccount.id, SOURCE) != -1

    private val _globalLoader = MutableStateFlow(false)
    private val _config = MutableStateFlow(PlayShortsConfigUiModel.Empty)
    private val _media = MutableStateFlow(PlayShortsMediaUiModel.Empty)
    private val _accountList = MutableStateFlow<List<ContentAccountUiModel>>(emptyList())
    private val _selectedAccount = MutableStateFlow(ContentAccountUiModel.Empty)
    private val _menuList = MutableStateFlow<List<DynamicPreparationMenu>>(emptyList())
    private val _productSectionList = MutableStateFlow<List<ProductTagSectionUiModel>>(emptyList())
    private val _tags = MutableStateFlow<NetworkResult<PlayTagUiModel>>(NetworkResult.Unknown)
    private val _uploadState = MutableStateFlow<PlayShortsUploadUiState>(PlayShortsUploadUiState.Unknown)
    private val _isAffiliate = MutableStateFlow(false)
    private val _bannerPreparation = MutableStateFlow<List<PlayBroadcastPreparationBannerModel>>(emptyList())
    private val _interspersingConfig = MutableStateFlow(PlayShortsInterspersingConfigUiModel.Empty)
    private val _productVideo = MutableStateFlow(ProductVideoUiModel.Empty)
    val isSelectedAccountAffiliate: Boolean
        get() = _selectedAccount.value.isUser && _isAffiliate.value

    private val _titleForm = MutableStateFlow(PlayShortsTitleFormUiState.Empty)
    private val _coverForm = MutableStateFlow(PlayShortsCoverFormUiState.Empty)

    private val _menuListUiState = combine(
        _menuList,
        _titleForm,
        _coverForm,
        _productSectionList
    ) { menuList, titleForm, coverForm, productSectionList ->
        menuList.map {
            when (it.menu) {
                DynamicPreparationMenu.Menu.Title -> {
                    it.copy(isChecked = titleForm.title.isNotEmpty())
                }
                DynamicPreparationMenu.Menu.Product -> {
                    it.copy(isChecked = productSectionList.isNotEmpty())
                }
                DynamicPreparationMenu.Menu.Cover -> {
                    it.copy(
                        isChecked = coverForm.coverUri.isNotEmpty(),
                        isEnabled = isAllMandatoryMenuChecked
                    )
                }
                else -> {
                    it
                }
            }
        }
    }

    val uiState: Flow<PlayShortsUiState> = combine(
        _globalLoader,
        _config,
        _media,
        _accountList,
        _selectedAccount,
        _menuListUiState,
        _titleForm,
        _coverForm,
        _productSectionList,
        _tags,
        _uploadState,
        _isAffiliate,
        _bannerPreparation,
        _interspersingConfig,
        _productVideo,
    ) { globalLoader,
        config,
        media,
        accountList,
        selectedAccount,
        menuListUiState,
        titleForm,
        coverForm,
        productSectionList,
        tags,
        uploadState,
        isAffiliate,
        bannerPreparation,
        interspersingConfig,
        productVideo ->
        PlayShortsUiState(
            globalLoader = globalLoader,
            config = config,
            media = media,
            accountList = accountList,
            selectedAccount = selectedAccount,
            menuList = menuListUiState,
            titleForm = titleForm,
            coverForm = coverForm,
            productSectionList = productSectionList,
            tags = tags,
            uploadState = uploadState,
            isAffiliate = isAffiliate,
            bannerPreparation = bannerPreparation,
            interspersingConfig = interspersingConfig,
            productVideo = productVideo,
        )
    }

    private val _uiEvent = MutableSharedFlow<PlayShortsUiEvent>()
    val uiEvent: Flow<PlayShortsUiEvent>
        get() = _uiEvent

    init {
        setupPreparationMenu()
    }

    fun submitAction(action: PlayShortsAction) {
        when (action) {
            is PlayShortsAction.PreparePage -> handlePreparePage(action.preferredAccountType)

            /** Media */
            is PlayShortsAction.SetMedia -> handleSetMedia(action.mediaUri)

            /** Account */
            is PlayShortsAction.ClickSwitchAccount -> handleClickSwitchAccount()
            is PlayShortsAction.SwitchAccount -> handleSwitchAccount(action.isRefreshAccountList)

            /** Title Form */
            is PlayShortsAction.OpenTitleForm -> handleOpenTitleForm()
            is PlayShortsAction.CloseTitleForm -> handleCloseTitleForm()
            is PlayShortsAction.UploadTitle -> handleUploadTitle(action.title)

            /** Cover Form */
            is PlayShortsAction.OpenCoverForm -> handleOpenCoverForm()
            is PlayShortsAction.CloseCoverForm -> handleCloseCoverForm()
            is PlayShortsAction.UpdateCover -> handleUpdateCover()

            /** Product */
            is PlayShortsAction.SetProduct -> handleSetProduct(action.productSectionList)

            is PlayShortsAction.ClickNext -> handleClickNext()

            /** Summary */
            is PlayShortsAction.LoadTag -> handleLoadTag()
            is PlayShortsAction.SelectTag -> handleSelectTag(action.tag)
            is PlayShortsAction.SwitchInterspersing -> handleSwitchInterspersing()
            is PlayShortsAction.ClickVideoPreview -> handleClickVideoPreview()
            is PlayShortsAction.UploadVideo -> handleUploadVideo(action.needCheckInterspersing)

            /** Shorts x Affiliate */
            is PlayShortsAction.SubmitOnboardAffiliateTnc -> handleSubmitOnboardAffiliateTnc()

            /** Others */
            is PlayShortsAction.SetShowSetupCoverCoachMark -> handleSetShowSetupCoverCoachMark()
            is PlayShortsAction.SetCoverUploadedSource -> handleSetCoverUploadedSource(action.source)
            is PlayShortsAction.ResetUploadState -> handleResetUploadState()
        }
    }

    private fun setupShortsAffiliateEntryPoint(needToShow: Boolean) {
        val banner = PlayBroadcastPreparationBannerModel(TYPE_SHORTS_AFFILIATE)
        if (needToShow) addBannerPreparation(banner)
        else removeBannerPreparation(banner)
    }

    private fun addBannerPreparation(data: PlayBroadcastPreparationBannerModel) {
        viewModelScope.launchCatchError(block = {
            if (_bannerPreparation.value.contains(data)) return@launchCatchError
            _bannerPreparation.update { it + data }
        }, onError = {})
    }

    private fun removeBannerPreparation(data: PlayBroadcastPreparationBannerModel) {
        viewModelScope.launchCatchError(block = {
            _bannerPreparation.update { it - data }
        }, onError = {})
    }

    private fun handleResetUploadState() {
        deleteCoverUploadedSource()
        deleteSavedAutoGeneratedCover()
    }

    private fun deleteCoverUploadedSource() {
        sharedPref.setUploadedCoverSource(-1, selectedAccount.id, SOURCE)
    }

    private fun deleteSavedAutoGeneratedCover() {
        mDataStore.getSetupDataStore().setFullCover(PlayCoverUiModel.empty())
        sharedPref.savedSelectedAutoGeneratedCover(-1, selectedAccount.id, SOURCE)
    }

    private fun handleCloseCoverForm() {
        _coverForm.update {
            it.copy(state = PlayShortsCoverFormUiState.State.Unknown)
        }
    }

    private fun handleOpenCoverForm() {
        _coverForm.update {
            it.copy(state = PlayShortsCoverFormUiState.State.Editing)
        }
    }

    private fun handlePreparePage(preferredAccountType: String) {
        viewModelScope.launchCatchErrorWithLoader(block = {
            val accountList = repo.getAccountList().apply {
                _accountList.update { this }
            }
            val bestEligibleAccount = accountManager.getBestEligibleAccount(accountList, preferredAccountType)

            setupConfigurationIfEligible(bestEligibleAccount)
        }) {
            _uiEvent.emit(PlayShortsUiEvent.ErrorPreparingPage)
        }
    }

    private fun handleSubmitOnboardAffiliateTnc() {
        viewModelScope.launchCatchErrorWithLoader(block = {

            val request = OnboardAffiliateRequestModel(
                channelID = _config.value.shortsId.toLong(),
                profileID = _selectedAccount.value.id,
            )

            repo.submitOnboardAffiliateTnc(request)

            checkIsSuccessSubmitAffiliate()
        }) {
            _uiEvent.emit(PlayShortsUiEvent.ErrorOnboardAffiliate(it))
        }
    }

    private fun handleSetMedia(mediaUri: String) {
        if (mediaUri == _media.value.mediaUri) return

        _media.update { it.copy(mediaUri = mediaUri) }
    }

    private fun handleClickSwitchAccount() {
        viewModelScope.launch {
            _uiEvent.emit(PlayShortsUiEvent.SwitchAccount)
        }
    }

    private fun handleSwitchAccount(isRefreshAccountList: Boolean) {
        viewModelScope.launchCatchErrorWithLoader(block = {
            if(isRefreshAccountList) {
                val accountList = repo.getAccountList()
                _accountList.update { accountList }
            }

            val newSelectedAccount = accountManager.switchAccount(_accountList.value, _selectedAccount.value.type)

            setupConfigurationIfEligible(newSelectedAccount)
        }) { throwable ->
            _uiEvent.emit(PlayShortsUiEvent.ErrorSwitchAccount(throwable))
        }
    }

    private fun handleOpenTitleForm() {
        _titleForm.update { it.copy(state = PlayShortsTitleFormUiState.State.Editing) }
    }

    private fun handleCloseTitleForm() {
        _titleForm.update { it.copy(state = PlayShortsTitleFormUiState.State.Unknown) }
    }

    private fun handleUploadTitle(title: String) {
        viewModelScope.launchCatchError(block = {
            if (_titleForm.value.state == PlayShortsTitleFormUiState.State.Loading) return@launchCatchError

            _titleForm.update {
                it.copy(
                    state = PlayShortsTitleFormUiState.State.Loading
                )
            }

            repo.uploadTitle(title, shortsId, selectedAccount.id)

            _titleForm.update {
                it.copy(
                    title = title,
                    state = PlayShortsTitleFormUiState.State.Unknown
                )
            }
        }) { throwable ->

            _titleForm.update {
                it.copy(
                    state = PlayShortsTitleFormUiState.State.Editing
                )
            }

            _uiEvent.emit(
                PlayShortsUiEvent.ErrorUploadTitle(throwable) {
                    submitAction(PlayShortsAction.UploadTitle(title = title))
                }
            )
        }
    }

    private fun handleUpdateCover() {
        val cover = mDataStore.getSetupDataStore().getSelectedCover() ?: return
        _coverForm.update {
            it.copy(cover = cover.croppedCover)
        }
    }

    private fun handleSetProduct(productSectionList: List<ProductTagSectionUiModel>) {
        viewModelScope.launchCatchError(block = {
            checkProductChangedAutoGeneratedCover(
                oldProduct = _productSectionList.value,
                newProduct = productSectionList,
            )
            mDataStore.getSetupDataStore().setProductTag(productSectionList)
            _productSectionList.update { productSectionList }
        }) {}
    }

    private suspend fun checkProductChangedAutoGeneratedCover(
        oldProduct: List<ProductTagSectionUiModel>,
        newProduct: List<ProductTagSectionUiModel>,
    ) {
        if (!isAfterUploadAutoGeneratedCover) return

        deleteSavedAutoGeneratedCover()
        _coverForm.value = PlayShortsCoverFormUiState.Empty
        if (oldProduct.isNotEmpty() && newProduct.isEmpty()) {
            _uiEvent.emit(PlayShortsUiEvent.AutoGeneratedCoverToaster(isToasterUpdate = false))
        } else if (oldProduct.isNotEmpty() && oldProduct != newProduct) {
            _uiEvent.emit(PlayShortsUiEvent.AutoGeneratedCoverToaster(isToasterUpdate = true))
        } else return
    }

    private fun handleClickNext() {
        viewModelScope.launch {

            _interspersingConfig.update { interspersingConfig ->
                val isInterspersingAllowed = _config.value.eligibleInterspersing &&
                    productSectionList.sumOf { it.products.size } == _config.value.productCountForInterspersing

                interspersingConfig.copy(
                    isInterspersingAllowed = isInterspersingAllowed,
                    isInterspersing = isInterspersingAllowed,
                )
            }

            _uiEvent.emit(PlayShortsUiEvent.GoToSummary)
        }
    }

    private fun handleLoadTag() {
        viewModelScope.launchCatchError(block = {
            if (_tags.value is NetworkResult.Loading || _tags.value is NetworkResult.Success) return@launchCatchError

            _tags.update { NetworkResult.Loading }

            val tags = repo.getTagRecommendation(shortsId)

            _tags.update { NetworkResult.Success(tags) }
        }) { throwable ->
            _tags.update { NetworkResult.Fail(throwable) }
        }
    }

    private fun handleSelectTag(tag: PlayTagItem) {

        if (!tag.isActive) return

        val tagState = _tags.value
        when (tagState is NetworkResult.Success) {
            true -> {
                _tags.update {
                    val newTags = tagState.data.tags.map {
                        if (it.isActive && it.tag == tag.tag) {
                            it.copy(isChosen = !it.isChosen)
                        } else {
                            it
                        }
                    }.toSet()

                    val finalTags = if (newTags.count { it.isChosen } == tagState.data.maxTags) {
                        newTags.map { it.copy(isActive = it.isChosen) }
                    } else {
                        newTags.map { it.copy(isActive = true) }
                    }.toSet()

                    NetworkResult.Success(
                        data = tagState.data.copy(
                            tags = finalTags
                        )
                    )
                }
            }
            else -> {}
        }
    }

    private fun handleSwitchInterspersing() {
        _interspersingConfig.update {
            it.copy(
                isInterspersing = !it.isInterspersing
            )
        }
    }

    private fun handleClickVideoPreview() {
        viewModelScope.launch {
            _uiEvent.emit(PlayShortsUiEvent.GoToVideoPreview)
        }
    }

    private fun handleUploadVideo(needCheckInterspersing: Boolean) {
        viewModelScope.launchCatchError(block = {
            if (_uploadState.value is PlayShortsUploadUiState.Loading) return@launchCatchError

            _uploadState.update { PlayShortsUploadUiState.Loading }

            if (needCheckInterspersing && _interspersingConfig.value.isInterspersing) {
                try {
                    val productVideo = _productVideo.updateAndGet {
                        repo.checkProductCustomVideo(_config.value.shortsId)
                    }

                    if (productVideo.hasVideo) {
                        _uploadState.update { PlayShortsUploadUiState.Unknown }

                        _uiEvent.emit(PlayShortsUiEvent.ShowInterspersingConfirmation)

                        return@launchCatchError
                    }
                } catch (throwable: Throwable) {
                    _uploadState.update { PlayShortsUploadUiState.Error(throwable) }
                    _uiEvent.emit(PlayShortsUiEvent.ErrorCheckInterspersing(throwable))

                    return@launchCatchError
                }
            }

            saveTag()
            updateStatus(PlayChannelStatusType.Queue)
            uploadMedia()

            _uploadState.update { PlayShortsUploadUiState.Success }
        }) { throwable ->
            _uploadState.update { PlayShortsUploadUiState.Error(throwable) }
            _uiEvent.emit(PlayShortsUiEvent.ErrorUploadMedia(throwable))
        }
    }

    private fun handleSetShowSetupCoverCoachMark() {
        sharedPref.setShowSetupCoverCoachMark()
    }

    private fun handleSetCoverUploadedSource(source: Int) {
        sharedPref.setUploadedCoverSource(source, selectedAccount.id, SOURCE)
    }

    private suspend fun checkIsUserAffiliate() {
        if (selectedAccount.isShop) {
            _isAffiliate.update { false }
            setupShortsAffiliateEntryPoint(false)
            return
        }
        val checkIsAffiliate = repo.getBroadcasterCheckAffiliate()
        _isAffiliate.update { checkIsAffiliate.isAffiliate }
        setupShortsAffiliateEntryPoint(!_isAffiliate.value)
    }

    private suspend fun checkIsSuccessSubmitAffiliate() {
        checkIsUserAffiliate()
        _uiEvent.emit(PlayShortsUiEvent.SuccessOnboardAffiliate)
    }

    private suspend fun setSelectedAccount(account: ContentAccountUiModel) {
        _selectedAccount.update { account }
        sharedPref.setLastSelectedAccountType(account.type)
        resetForm()
    }

    private fun setupPreparationMenu() {
        viewModelScope.launchCatchError(block = {
            val menuList = mutableListOf<DynamicPreparationMenu>().apply {
                add(DynamicPreparationMenu.createTitle(true))
                add(DynamicPreparationMenu.createProduct(true))
                add(DynamicPreparationMenu.createCover(false))
            }

            _menuList.update { menuList }
        }) { }
    }

    private suspend fun setupConfigurationIfEligible(account: ContentAccountUiModel) {
        if (account.isUnknown) {
            emitEventAccountNotEligible()
            return
        }

        val config = repo.getShortsConfiguration(account.id, account.type)
        _config.update { it.copy(tncList = config.tncList) }

        if (account.isUser && !account.hasAcceptTnc) {
            emitEventUGCOnboarding(account.hasUsername)
        } else if(config.isBanned) {
            emitEventAccountBanned()
        } else if (account.isShop && (!account.enable || !config.shortsAllowed)) {
            emitEventSellerNotEligible()
        } else if (account.isUser && !config.shortsAllowed) {
            emitEventAccountNotEligible()
        } else {
            val finalConfig = if (config.shortsId.isEmpty()) {
                val shortsId = repo.createShorts(account.id, account.type)
                config.copy(shortsId = shortsId)
            } else {
                config
            }

            _config.update { finalConfig }
            setSelectedAccount(account)
            checkIsUserAffiliate()
        }
    }

    private suspend fun resetForm() {
        _titleForm.update { PlayShortsTitleFormUiState.Empty }
        _productSectionList.update { emptyList() }
        _coverForm.update { PlayShortsCoverFormUiState.Empty }
        mDataStore.getSetupDataStore().setFullCover(PlayCoverUiModel.empty())
        _uiEvent.emit(PlayShortsUiEvent.ResetForm)
    }

    private fun emitEventAccountBanned() {
        viewModelScope.launch {
            _uiEvent.emit(PlayShortsUiEvent.AccountBanned)
        }
    }

    private fun emitEventSellerNotEligible() {
        viewModelScope.launch {
            _uiEvent.emit(PlayShortsUiEvent.SellerNotEligible)
        }
    }

    private fun emitEventAccountNotEligible() {
        viewModelScope.launch {
            _uiEvent.emit(PlayShortsUiEvent.AccountNotEligible)
        }
    }

    private fun emitEventUGCOnboarding(hasUsername: Boolean) {
        viewModelScope.launch {
            _uiEvent.emit(PlayShortsUiEvent.UGCOnboarding(hasUsername))
        }
    }

    private fun CoroutineScope.launchCatchErrorWithLoader(
        context: CoroutineContext = coroutineContext,
        block: suspend CoroutineScope.() -> Unit,
        onError: suspend (Throwable) -> Unit
    ) = launchCatchError(context, block = {
        _globalLoader.update { true }
        block()
        _globalLoader.update { false }
    }) {
        _globalLoader.update { false }
        onError(it)
    }

    private suspend fun saveTag() {
        val tagState = _tags.value
        if (tagState is NetworkResult.Success) {
            val selectedTag = tagState.data.tags.filter { it.isChosen }.map { it.tag }.toSet()

            if (selectedTag.isNotEmpty()) {
                val result = repo.saveTag(shortsId, selectedTag)

                if (!result) {
                    throw DefaultErrorThrowable("${DefaultErrorThrowable.DEFAULT_MESSAGE}: Error Tag")
                }
            }
        }
    }

    private suspend fun uploadMedia() {
        val uploadData = CreationUploadData.buildForShorts(
            creationId = shortsId,
            mediaUriList = listOf(_media.value.mediaUri),
            coverUri = _coverForm.value.coverUri,
            sourceId = _config.value.shortsVideoSourceId,
            authorId = selectedAccount.id,
            authorType = selectedAccount.type,
            isInterspersed = _interspersingConfig.value.isInterspersing,
        )

        creationUploader.upload(uploadData)
    }

    private suspend fun updateStatus(
        status: PlayChannelStatusType
    ) {
        repo.updateStatus(
            creationId = shortsId,
            authorId = selectedAccount.id,
            status = status,
        )
    }

    companion object {
        private const val SOURCE = "shorts"
    }
}
