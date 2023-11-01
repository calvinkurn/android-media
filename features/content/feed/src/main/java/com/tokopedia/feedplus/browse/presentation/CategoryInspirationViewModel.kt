package com.tokopedia.feedplus.browse.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationData
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationMap
import com.tokopedia.feedplus.browse.presentation.model.FeedCategoryInspirationAction
import com.tokopedia.feedplus.browse.presentation.model.FeedCategoryInspirationUiState
import com.tokopedia.feedplus.browse.presentation.model.ItemListState
import com.tokopedia.feedplus.browse.presentation.model.isLoading
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
            FeedCategoryInspirationAction.LoadMoreData -> onLoadMoreData()
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

    private fun onLoadMoreData() {
        viewModelScope.launch {
            val selectedMenuId = _uiState.value.selectedMenuId
            val selectedData = _uiState.value.items[selectedMenuId] ?: return@launch
            loadContent(selectedData.menu)
        }
    }

    private fun onSelectMenu(menu: WidgetMenuModel) {
        _uiState.update {
            it.copy(
                selectedMenuId = menu.id,
                items = it.items.updateById(menu.id) { data ->
                    data.copy(items = ItemListState.initSuccess(emptyList()))
                }
            )
        }
    }

    private suspend fun loadContent(menu: WidgetMenuModel) {
        val data = _uiState.value.items[menu.id]
        if (data != null && (data.items.isLoading || !data.items.hasNextPage)) return

        Log.d("CategoryInspiration", "LoadContent in ViewModel")

        _uiState.update {
            it.copy(
                items = it.items.updateById(menu.id) { data ->
                    data.copy(
                        items = data.items.copy(state = ResultState.Loading)
                    )
                },
            )
        }

        val nextCursor = data?.items?.nextCursor.orEmpty()
        when (val response = repository.getWidgetContentSlot(menu.toRequest(nextCursor))) {
            is ContentSlotModel.TabMenus -> {
                _uiState.update {
                    it.copy(
                        items = response.menu.associate { menu ->
                            menu.id to
                                CategoryInspirationData(menu, ItemListState.initSuccess(emptyList()))
                        },
                        selectedMenuId = response.menu.firstOrNull()?.id.orEmpty()
                    )
                }
            }
            is ContentSlotModel.ChannelBlock -> {
                _uiState.update {
                    it.copy(
                        items = it.items.updateById(menu.id) { data ->
                            data.copy(
                                items = ItemListState.initSuccess(
                                    items = data.items.items + response.channels,
                                    nextCursor = response.nextCursor,
                                    hasNextPage = response.hasNextPage,
                                )
                            )
                        },
                    )
                }
            }
            is ContentSlotModel.NoData -> {
                _uiState.update {
                    it.copy(
                        items = it.items.updateById(menu.id) { data ->
                            data.copy(
                                items = ItemListState.initSuccess(
                                    data.items.items,
                                    nextCursor = response.nextCursor,
                                    hasNextPage = response.hasNextPage,
                                ),
                            )
                        }
                    )
                }
            }
        }
    }

    private fun CategoryInspirationMap.updateById(
        id: String,
        onUpdate: (CategoryInspirationData) -> CategoryInspirationData,
    ): CategoryInspirationMap {
        val data = get(id) ?: return this
        return this + (id to onUpdate(data))
    }

    private fun WidgetMenuModel.toRequest(nextCursor: String): WidgetRequestModel {
        return WidgetRequestModel(
            group = group,
            sourceType = sourceType,
            sourceId = sourceId,
            cursor = nextCursor,
        )
    }
}
