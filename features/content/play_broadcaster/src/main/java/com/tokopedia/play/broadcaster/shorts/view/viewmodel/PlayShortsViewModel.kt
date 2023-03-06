package com.tokopedia.play.broadcaster.shorts.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsMediaUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsCoverFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsTitleFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUploadUiState
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel
import com.tokopedia.play_common.util.error.DefaultErrorThrowable
import com.tokopedia.play_common.util.extension.combine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsViewModel @Inject constructor(
    private val repo: PlayShortsRepository,
    private val sharedPref: HydraSharedPreferences,
    private val accountManager: PlayShortsAccountManager,
    private val playShortsUploader: PlayShortsUploader
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

    val isFirstSwitchAccount: Boolean
        get() = sharedPref.isFirstSwitchAccount()

    private val _globalLoader = MutableStateFlow(false)
    private val _config = MutableStateFlow(PlayShortsConfigUiModel.Empty)
    private val _media = MutableStateFlow(PlayShortsMediaUiModel.Empty)
    private val _accountList = MutableStateFlow<List<ContentAccountUiModel>>(emptyList())
    private val _selectedAccount = MutableStateFlow(ContentAccountUiModel.Empty)
    private val _menuList = MutableStateFlow<List<DynamicPreparationMenu>>(emptyList())
    private val _productSectionList = MutableStateFlow<List<ProductTagSectionUiModel>>(emptyList())
    private val _tags = MutableStateFlow<NetworkResult<Set<PlayTagUiModel>>>(NetworkResult.Unknown)
    private val _uploadState = MutableStateFlow<PlayShortsUploadUiState>(PlayShortsUploadUiState.Unknown)

    private val _titleForm = MutableStateFlow(PlayShortsTitleFormUiState.Empty)
    private val _coverForm = MutableStateFlow(PlayShortsCoverFormUiState.Empty)

    private val _menuListUiState = combine(
        _menuList,
        _titleForm,
        _coverForm,
        _productSectionList
    ) { menuList, titleForm, coverForm, productSectionList ->
        menuList.map {
            when (it.menuId) {
                DynamicPreparationMenu.TITLE -> {
                    it.copy(isChecked = titleForm.title.isNotEmpty())
                }
                DynamicPreparationMenu.PRODUCT -> {
                    it.copy(isChecked = productSectionList.isNotEmpty())
                }
                DynamicPreparationMenu.COVER -> {
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
    ) { globalLoader, config, media, accountList, selectedAccount, menuListUiState, titleForm, coverForm, productSectionList, tags, uploadState ->
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
            uploadState = uploadState
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
            is PlayShortsAction.UploadTitle -> handleUploadTitle(action.title)
            is PlayShortsAction.CloseTitleForm -> handleCloseTitleForm()

            /** Cover Form */
            is PlayShortsAction.OpenCoverForm -> handleOpenCoverForm()
            is PlayShortsAction.SetCover -> handleSetCover(action.cover)
            is PlayShortsAction.CloseCoverForm -> handleCloseCoverForm()

            /** Product */
            is PlayShortsAction.SetProduct -> handleSetProduct(action.productSectionList)

            is PlayShortsAction.ClickNext -> handleClickNext()

            /** Summary */
            is PlayShortsAction.LoadTag -> handleLoadTag()
            is PlayShortsAction.SelectTag -> handleSelectTag(action.tag)
            is PlayShortsAction.ClickUploadVideo -> handleClickUploadVideo()

            /** Others */
            is PlayShortsAction.SetNotFirstSwitchAccount -> handleSetNotFirstSwitchAccount()
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

    private fun handleCloseTitleForm() {
        if (_titleForm.value.state != PlayShortsTitleFormUiState.State.Loading) {
            _titleForm.update { it.copy(state = PlayShortsTitleFormUiState.State.Unknown) }
        }
    }

    private fun handleOpenCoverForm() {
        _coverForm.update {
            it.copy(state = PlayShortsCoverFormUiState.State.Editing)
        }
    }

    private fun handleSetCover(cover: CoverSetupState) {
        _coverForm.update {
            it.copy(cover = cover)
        }
    }

    private fun handleCloseCoverForm() {
        _coverForm.update {
            it.copy(state = PlayShortsCoverFormUiState.State.Unknown)
        }
    }

    private fun handleSetProduct(productSectionList: List<ProductTagSectionUiModel>) {
        _productSectionList.update { productSectionList }
    }

    private fun handleClickNext() {
        viewModelScope.launch {
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

    private fun handleSelectTag(tag: PlayTagUiModel) {
        val tagState = _tags.value
        when (tagState is NetworkResult.Success) {
            true -> {
                _tags.update {
                    NetworkResult.Success(
                        data = tagState.data.map {
                            if (it.tag == tag.tag) {
                                it.copy(isChosen = !it.isChosen)
                            } else {
                                it
                            }
                        }.toSet()
                    )
                }
            }
            else -> {}
        }
    }

    private fun handleClickUploadVideo() {
        viewModelScope.launchCatchError(block = {
            if (_uploadState.value is PlayShortsUploadUiState.Loading) return@launchCatchError

            _uploadState.update { PlayShortsUploadUiState.Loading }

            saveTag()
            uploadMedia()

            _uploadState.update { PlayShortsUploadUiState.Success }
        }) { throwable ->
            _uploadState.update { PlayShortsUploadUiState.Error(throwable) }
            _uiEvent.emit(PlayShortsUiEvent.ErrorUploadMedia(throwable))
        }
    }

    private fun handleSetNotFirstSwitchAccount() {
        sharedPref.setNotFirstSwitchAccount()
    }

    private fun setSelectedAccount(account: ContentAccountUiModel) {
        _selectedAccount.update { account }
        sharedPref.setLastSelectedAccountType(account.type)
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
            resetForm()
        }
    }

    private fun resetForm() {
        _titleForm.update { PlayShortsTitleFormUiState.Empty }
        _productSectionList.update { emptyList() }
        _coverForm.update { PlayShortsCoverFormUiState.Empty }
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
            val selectedTag = tagState.data.filter { it.isChosen }.map { it.tag }.toSet()

            if (selectedTag.isNotEmpty()) {
                val result = repo.saveTag(shortsId, selectedTag)

                if (!result) {
                    throw DefaultErrorThrowable("${DefaultErrorThrowable.DEFAULT_MESSAGE}: Error Tag")
                }
            }
        }
    }

    private fun uploadMedia() {
        val uploadData = PlayShortsUploadModel(
            shortsId = shortsId,
            authorId = selectedAccount.id,
            authorType = selectedAccount.type,
            mediaUri = _media.value.mediaUri,
            coverUri = _coverForm.value.coverUri,
            shortsVideoSourceId = _config.value.shortsVideoSourceId
        )
        playShortsUploader.upload(uploadData)
    }
}
