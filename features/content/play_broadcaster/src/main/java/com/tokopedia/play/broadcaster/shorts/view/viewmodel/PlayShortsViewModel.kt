package com.tokopedia.play.broadcaster.shorts.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.content.common.producttag.util.extension.combine
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.factory.PlayShortsMediaSourceFactory
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsMediaUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsBottomSheet
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsOneTimeEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsToaster
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsCoverFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsTitleFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.util.oneTimeUpdate
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
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
    private val exoPlayer: ExoPlayer,
    private val mediaSourceFactory: PlayShortsMediaSourceFactory,
    private val accountManager: PlayShortsAccountManager
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
        get() = _titleForm.value.title.isNotEmpty()

    /** TODO: uncomment this later */
//            && _productSectionList.value.any { it.products.isNotEmpty() }

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

    private val _globalLoader = MutableStateFlow(false)
    private val _config = MutableStateFlow(PlayShortsConfigUiModel.Empty)
    private val _media = MutableStateFlow(PlayShortsMediaUiModel.create(exoPlayer))
    private val _accountList = MutableStateFlow<List<ContentAccountUiModel>>(emptyList())
    private val _selectedAccount = MutableStateFlow(ContentAccountUiModel.Empty)
    private val _menuList = MutableStateFlow<List<DynamicPreparationMenu>>(emptyList())
    private val _productSectionList = MutableStateFlow<List<ProductTagSectionUiModel>>(emptyList())

    private val _titleForm = MutableStateFlow(PlayShortsTitleFormUiState.Empty)
    private val _coverForm = MutableStateFlow(PlayShortsCoverFormUiState.Empty)

    private val _menuListUiState = kotlinx.coroutines.flow.combine(
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
        _coverForm
    ) { globalLoader, config, media, accountList, selectedAccount, menuListUiState, titleForm, coverForm ->
        PlayShortsUiState(
            globalLoader,
            config = config,
            media = media,
            accountList = accountList,
            selectedAccount = selectedAccount,
            menuList = menuListUiState,
            titleForm = titleForm,
            coverForm = coverForm
        )
    }

    private val _uiEvent = MutableStateFlow(PlayShortsUiEvent.Empty)
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
            is PlayShortsAction.StartMedia -> handleStartMedia()
            is PlayShortsAction.StopMedia -> handleStopMedia()
            is PlayShortsAction.ReleaseMedia -> handleReleaseMedia()

            /** Account */
            is PlayShortsAction.ClickSwitchAccount -> handleClickSwitchAccount()
            is PlayShortsAction.SwitchAccount -> handleSwitchAccount()

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
            _uiEvent.oneTimeUpdate {
                it.copy(oneTimeEvent = PlayShortsOneTimeEvent.ErrorPreparingPage)
            }
        }
    }

    private fun handleSetMedia(mediaUri: String) {
        if (mediaUri == _media.value.mediaUri) return

        _media.update {
            val mediaSource = mediaSourceFactory.create(mediaUri)
            it.exoPlayer.prepare(mediaSource)

            it.copy(mediaUri = mediaUri)
        }
    }

    private fun handleStartMedia() {
        _media.value.exoPlayer.playWhenReady = true
    }

    private fun handleStopMedia() {
        _media.value.exoPlayer.playWhenReady = false
    }

    private fun handleReleaseMedia() {
        _media.value.exoPlayer.apply {
            stop()
            release()
        }
    }

    private fun handleClickSwitchAccount() {
        _uiEvent.oneTimeUpdate {
            it.copy(
                bottomSheet = PlayShortsBottomSheet.SwitchAccount
            )
        }
    }

    private fun handleSwitchAccount() {
        viewModelScope.launchCatchErrorWithLoader(block = {
            val newSelectedAccount = accountManager.switchAccount(_accountList.value, _selectedAccount.value.type)

            setupConfigurationIfEligible(newSelectedAccount)

        }) { throwable ->
            _uiEvent.oneTimeUpdate {
                it.copy(toaster = PlayShortsToaster.ErrorSwitchAccount(throwable))
            }
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

            _uiEvent.oneTimeUpdate {
                it.copy(
                    toaster = PlayShortsToaster.ErrorUploadTitle(throwable) {
                        submitAction(PlayShortsAction.UploadTitle(title = title))
                    }
                )
            }
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
        /** TODO: handle this */
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
        if(account.isUnknown) {
            emitEventAccountNotEligible()
            return
        }

        val config = repo.getShortsConfiguration(account.id, account.type)
        _config.update { it.copy(tncList = config.tncList) }

        if(account.isShop && (!account.enable || !config.shortsAllowed)) {
            emitEventSellerNotEligible()
        } else if (account.isUser && !config.shortsAllowed) {
            emitEventAccountNotEligible()
        } else if (account.isUser && !account.enable) {
            emitEventUGCOnboarding(account.hasUsername)
        } else {
            val finalConfig = if(config.shortsId.isEmpty()) {
                val shortsId = repo.createShorts(account.id, account.type)
                config.copy(shortsId = shortsId)
            }
            else {
                config
            }

            _config.update { finalConfig }
            setSelectedAccount(account)
        }
    }

    private fun emitEventSellerNotEligible() {
        _uiEvent.oneTimeUpdate {
            it.copy(bottomSheet = PlayShortsBottomSheet.SellerNotEligible)
        }
    }

    private fun emitEventAccountNotEligible() {
        _uiEvent.oneTimeUpdate {
            it.copy(bottomSheet = PlayShortsBottomSheet.AccountNotEligible)
        }
    }

    private fun emitEventUGCOnboarding(hasUsername: Boolean) {
        _uiEvent.oneTimeUpdate {
            it.copy(bottomSheet = PlayShortsBottomSheet.UGCOnboarding(hasUsername))
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
}
