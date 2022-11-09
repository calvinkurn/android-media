package com.tokopedia.play.broadcaster.shorts.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.producttag.util.extension.combine
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsToaster
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsTitleFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.util.oneTimeUpdate
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.lang.Exception
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsViewModel @Inject constructor(
    private val repo: PlayShortsRepository,
    private val sharedPref: HydraSharedPreferences
) : ViewModel() {

    /** Public Getter */
    val title: String
        get() = _titleForm.value.title

    val titleFormState: PlayShortsTitleFormUiState.State
        get() = _titleForm.value.state

    private val _mediaUri = MutableStateFlow("")
    private val _accountList = MutableStateFlow<List<ContentAccountUiModel>>(emptyList())
    private val _selectedAccount = MutableStateFlow<ContentAccountUiModel>(ContentAccountUiModel.Empty)
    private val _shortsId = MutableStateFlow<String>("")
    private val _menuList = MutableStateFlow<List<DynamicPreparationMenu>>(emptyList())

    private val _titleForm = MutableStateFlow(PlayShortsTitleFormUiState.Empty)

    private val _menuListUiState = kotlinx.coroutines.flow.combine(
        _menuList,
        _titleForm
    ) { menuList, titleForm ->
        /** Will update this validation with product checking as well */
        val isAllMandatoryChecked = titleForm.title.isNotEmpty()

        menuList.map {
            when (it.menuId) {
                DynamicPreparationMenu.TITLE -> {
                    it.copy(isChecked = titleForm.title.isNotEmpty())
                }
                DynamicPreparationMenu.PRODUCT -> {
                    /** Will handle this later */
                    it
                }
                DynamicPreparationMenu.COVER -> {
                    /** Will handle isChecked later */
                    it.copy(isEnabled = isAllMandatoryChecked)
                }
                else -> {
                    it
                }
            }
        }
    }

    val uiState: Flow<PlayShortsUiState> = combine(
        _shortsId,
        _mediaUri,
        _accountList,
        _selectedAccount,
        _menuListUiState,
        _titleForm
    ) { shortsId, mediaUri, accountList, selectedAccount, menuListUiState, titleForm ->
        PlayShortsUiState(
            shortsId = shortsId,
            mediaUri = mediaUri,
            accountList = accountList,
            selectedAccount = selectedAccount,
            menuList = menuListUiState,
            titleForm = titleForm
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

            /** Title Form */
            is PlayShortsAction.OpenTitleForm -> handleOpenTitleForm()
            is PlayShortsAction.SubmitTitle -> handleSubmitTitle(action.title)
            is PlayShortsAction.CloseTitleForm -> handleCloseTitleForm()
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
