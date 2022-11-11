package com.tokopedia.play.broadcaster.shorts.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.content.common.producttag.util.extension.combine
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.factory.PlayShortsMediaSourceFactory
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsMediaUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsOneTimeEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsToaster
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsCoverFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsTitleFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.util.oneTimeUpdate
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play_common.model.result.NetworkResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsViewModel @Inject constructor(
    private val repo: PlayShortsRepository,
    private val sharedPref: HydraSharedPreferences,
    private val exoPlayer: ExoPlayer,
    private val mediaSourceFactory: PlayShortsMediaSourceFactory,
) : ViewModel() {

    /** Public Getter */
    val shortsId: String
        get() = _shortsId.value

    val title: String
        get() = _titleForm.value.title

    val productSectionList: List<ProductTagSectionUiModel>
        get() = _productSectionList.value

    /** TODO: provide the correct max product here */
    val maxProduct: Int
        get() = 30

    val isAllMandatoryMenuChecked: Boolean
        get() = _titleForm.value.title.isNotEmpty()
//            && _productSectionList.value.any { it.products.isNotEmpty() }

    val selectedAccount: ContentAccountUiModel
        get() = _selectedAccount.value

    private val _media = MutableStateFlow(PlayShortsMediaUiModel.create(exoPlayer))
    private val _accountList = MutableStateFlow<List<ContentAccountUiModel>>(emptyList())
    private val _selectedAccount = MutableStateFlow(ContentAccountUiModel.Empty)
    private val _shortsId = MutableStateFlow("")
    private val _menuList = MutableStateFlow<List<DynamicPreparationMenu>>(emptyList())
    private val _productSectionList = MutableStateFlow<List<ProductTagSectionUiModel>>(emptyList())
    private val _tags = MutableStateFlow<NetworkResult<Set<PlayTagUiModel>>>(NetworkResult.Unknown)

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
        _shortsId,
        _media,
        _accountList,
        _selectedAccount,
        _menuListUiState,
        _titleForm,
        _coverForm,
        _tags,
    ) { shortsId, media, accountList, selectedAccount, menuListUiState, titleForm, coverForm, tags ->
        PlayShortsUiState(
            shortsId = shortsId,
            media = media,
            accountList = accountList,
            selectedAccount = selectedAccount,
            menuList = menuListUiState,
            titleForm = titleForm,
            coverForm = coverForm,
            tags = tags
        )
    }

    private val _uiEvent = MutableStateFlow(PlayShortsUiEvent.Empty)
    val uiEvent: Flow<PlayShortsUiEvent>
        get() = _uiEvent

    init {
        setupPreparationMenu()

        /** TODO: for mocking purpose, delete this later */
        submitAction(PlayShortsAction.SetMedia("/storage/emulated/0/Movies/VID_20221110_141411.mp4"))
        _shortsId.value = "123123"
    }

    fun submitAction(action: PlayShortsAction) {
        when (action) {
            is PlayShortsAction.PreparePage -> handlePreparePage(action.preferredAccountType)

            is PlayShortsAction.SetMedia -> handleSetMedia(action.mediaUri)
            is PlayShortsAction.StopMedia -> handleStopMedia()
            is PlayShortsAction.ReleaseMedia -> handleReleaseMedia()

            /** Title Form */
            is PlayShortsAction.OpenTitleForm -> handleOpenTitleForm()
            is PlayShortsAction.SubmitTitle -> handleSubmitTitle(action.title)
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
        }
    }

    private fun handlePreparePage(preferredAccountType: String) {
        viewModelScope.launchCatchError(block = {
            val lastSelectedAccount = sharedPref.getLastSelectedAccount()

            val selectedAccount = when {
                preferredAccountType.isNotEmpty() -> preferredAccountType
                lastSelectedAccount.isNotEmpty() -> lastSelectedAccount
                else -> ""
            }

            val accountList = repo.getAccountList()
        }) {
        }
    }

    private fun handleSetMedia(mediaUri: String) {
        if(mediaUri == _media.value.mediaUri) return

        _media.update {
            val mediaSource = mediaSourceFactory.create(mediaUri)
            it.exoPlayer.prepare(mediaSource)
            it.exoPlayer.playWhenReady = true

            it.copy(mediaUri = mediaUri)
        }
    }

    private fun handleStopMedia() {
        _media.value.exoPlayer.stop()
    }

    private fun handleReleaseMedia() {
        _media.value.exoPlayer.release()
    }

    private fun handleOpenTitleForm() {
        _titleForm.update { it.copy(state = PlayShortsTitleFormUiState.State.Editing) }
    }

    private fun handleSubmitTitle(title: String) {
        viewModelScope.launchCatchError(block = {
            if (_titleForm.value.state == PlayShortsTitleFormUiState.State.Loading) return@launchCatchError

            _titleForm.update {
                it.copy(
                    state = PlayShortsTitleFormUiState.State.Loading
                )
            }

            /** Will call real GQL here */
            delay(2000)

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
                    toaster = PlayShortsToaster.ErrorSubmitTitle(throwable) {
                        submitAction(PlayShortsAction.SubmitTitle(title = title))
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
        _uiEvent.oneTimeUpdate { it.copy(oneTimeEvent = PlayShortsOneTimeEvent.GoToSummary) }
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
}
