package com.tokopedia.feedplus.browse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.FeedCategoryInspirationAction
import com.tokopedia.feedplus.browse.presentation.model.FeedCategoryInspirationUiState
import com.tokopedia.feedplus.browse.presentation.model.ItemListState
import com.tokopedia.feedplus.browse.presentation.model.orInitLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 30/10/23
 */
internal class CategoryInspirationViewModel @Inject constructor(
    private val repository: FeedBrowseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FeedCategoryInspirationUiState.empty(ResultState.Loading)
    )
    val uiState get() = _uiState.asStateFlow()

    fun onAction(action: FeedCategoryInspirationAction) {
        when (action) {
            FeedCategoryInspirationAction.Init -> onInit()
            is FeedCategoryInspirationAction.LoadData -> onLoadData(action.menu)
            is FeedCategoryInspirationAction.SelectMenu -> onSelectMenu(action.menu)
        }
    }

    private fun onInit() {
        viewModelScope.launch {
            _uiState.update { it.copy(state = ResultState.Loading) }

            loadContent(
                WidgetMenuModel.Empty.copy(
//                group = "browse_channel_slot_channelBlock:content_browse_promo",
                    group = "browse_channel_slot_tabMenu:content_browse_category",
                )
            )

            _uiState.update { it.copy(state = ResultState.Success) }
        }
    }

    private fun onLoadData(menu: WidgetMenuModel) {
        viewModelScope.launch {
            loadContent(menu)
        }
    }

    private fun onSelectMenu(menu: WidgetMenuModel) {
        _uiState.update {
            it.copy(selectedMenuId = menu.id)
        }
    }

    private suspend fun loadContent(menu: WidgetMenuModel) {
        _uiState.update {
            val itemsInMenu = it.items[menu]
            it.copy(
                items = it.items + (
                    menu to itemsInMenu?.copy(state = ResultState.Loading).orInitLoading()
                )
            )
        }

        when (val response = repository.getWidgetContentSlot(menu.toRequest())) {
            is ContentSlotModel.TabMenus -> {
                _uiState.update {
                    it.copy(
                        items = response.menu.associateWith { ItemListState.initLoading() },
                        selectedMenuId = response.menu.firstOrNull()?.id.orEmpty()
                    )
                }
            }
            is ContentSlotModel.ChannelBlock -> {
                _uiState.update {
                    it.copy(
                        items = it.items + (menu to ItemListState.initSuccess(response.channels))
                    )
                }
            }
        }
    }

    private fun WidgetMenuModel.toRequest(): WidgetRequestModel {
        return WidgetRequestModel(
            group = group,
            sourceType = sourceType,
            sourceId = sourceId,
        )
    }
}
