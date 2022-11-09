package com.tokopedia.play.broadcaster.shorts.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.producttag.util.extension.combine
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsViewModel @Inject constructor(
    private val repo: PlayShortsRepository,
    private val sharedPref: HydraSharedPreferences
) : ViewModel() {

    private val _mediaUri = MutableStateFlow("")
    private val _accountList = MutableStateFlow<List<ContentAccountUiModel>>(emptyList())
    private val _selectedAccount = MutableStateFlow<ContentAccountUiModel>(ContentAccountUiModel.Empty)
    private val _title = MutableStateFlow<String>("")
    private val _shortsId = MutableStateFlow<String>("")
    private val _menuList = MutableStateFlow<List<DynamicPreparationMenu>>(emptyList())

    private val _menuListUiState = kotlinx.coroutines.flow.combine(
        _menuList,
        _title,
    ) { menuList, title ->
        menuList.map {
            when(it.menuId) {
                DynamicPreparationMenu.TITLE -> {
                    it.copy(isChecked = title.isNotEmpty())
                }
                DynamicPreparationMenu.PRODUCT -> {
                    /** Will handle this later */
                    it
                }
                DynamicPreparationMenu.COVER -> {
                    /** Will handle this later */
                    it
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
    ) { shortsId, mediaUri, accountList, selectedAccount, menuListUiState ->
        PlayShortsUiState(
            shortsId = shortsId,
            mediaUri = mediaUri,
            accountList = accountList,
            selectedAccount = selectedAccount,
            menuList = menuListUiState,
        )
    }

    fun submitAction(action: PlayShortsAction) {
        when (action) {
            is PlayShortsAction.PreparePage -> handlePreparePage(action.preferredAccountType)
        }
    }

    private fun handlePreparePage(preferredAccountType: String) {
        setupPreparationMenu()

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

    private fun setupPreparationMenu() {
        viewModelScope.launchCatchError(block = {
            val menuList = mutableListOf<DynamicPreparationMenu>().apply {
                add(DynamicPreparationMenu.createTitle(true))
                add(DynamicPreparationMenu.createProduct(true))
                add(DynamicPreparationMenu.createSchedule(false))
            }

            _menuList.update { menuList }
        }) { }
    }
}
