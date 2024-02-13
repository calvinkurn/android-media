package com.tokopedia.feedplus.browse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationAction
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationData
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationMap
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChannelListState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 30/10/23
 */
internal class CategoryInspirationViewModel @AssistedInject constructor(
    @Assisted private val source: String,
    private val repository: FeedBrowseRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(source: String): CategoryInspirationViewModel
    }

    private val _uiState = MutableStateFlow(
        CategoryInspirationUiState.empty(ResultState.Loading)
    )

    private val jobMap = mutableMapOf<String, Job>()

    val uiState = _uiState.asStateFlow()

    fun onAction(action: CategoryInspirationAction) {
        when (action) {
            CategoryInspirationAction.Init -> onInit()
            is CategoryInspirationAction.LoadData -> onLoadData(action.menu)
            CategoryInspirationAction.LoadMoreData -> onLoadMoreData()
            is CategoryInspirationAction.SelectMenu -> onSelectMenu(action.menu)
        }
    }

    private fun onInit() {
        fetchTitle()
        viewModelScope.launch {
            _uiState.update { it.copy(state = ResultState.Loading) }

            loadContent(
                WidgetMenuModel.Empty.copy(group = source)
            )

            _uiState.update { it.copy(state = ResultState.Success) }
        }
    }

    private fun fetchTitle() {
        viewModelScope.launch {
            runCatching {
                repository.getCategoryInspirationTitle(source)
            }.onSuccess { title ->
                _uiState.update { it.copy(title = title) }
            }
        }
    }

    private fun onLoadData(menu: WidgetMenuModel) {
        viewModelScope.launchKeep("${LOAD_JOB_ID}_${menu.id}") {
            loadContent(menu)
        }
    }

    private fun onLoadMoreData() {
        val selectedMenuId = _uiState.value.selectedMenuId
        val selectedData = _uiState.value.items[selectedMenuId] ?: return

        viewModelScope.launchKeep("${LOAD_JOB_ID}_$selectedMenuId") {
            loadContent(selectedData.menu)
        }
    }

    private fun onSelectMenu(menu: WidgetMenuModel) {
        _uiState.update {
            it.copy(
                selectedMenuId = menu.id,
                items = it.items.updateById(menu.id) { data ->
                    data.copy(items = FeedBrowseChannelListState.initSuccess(emptyList()))
                }
            )
        }
    }

    private suspend fun loadContent(menu: WidgetMenuModel) {
        val data = _uiState.value.items[menu.id]
        if (data != null && !data.items.hasNextPage) return

        _uiState.update {
            it.copy(
                items = it.items.updateById(menu.id) { data ->
                    data.copy(
                        items = data.items.copy(state = ResultState.Loading)
                    )
                }
            )
        }

        val nextCursor = data?.items?.nextCursor.orEmpty()

        runCatching {
            repository.getWidgetContentSlot(menu.toRequest(nextCursor))
        }.onSuccess { response ->
            when (response) {
                is ContentSlotModel.TabMenus -> {
                    _uiState.update {
                        it.copy(
                            items = response.menus.associate { menu ->
                                menu.id to
                                    CategoryInspirationData(menu, FeedBrowseChannelListState.initLoading())
                            },
                            selectedMenuId = response.menus.firstOrNull()?.id.orEmpty()
                        )
                    }
                }
                is ContentSlotModel.ChannelBlock -> {
                    _uiState.update {
                        it.copy(
                            items = it.items.updateById(menu.id) { data ->
                                data.copy(
                                    items = FeedBrowseChannelListState.initSuccess(
                                        items = data.items.items + response.channels,
                                        nextCursor = response.nextCursor,
                                        hasNextPage = response.hasNextPage,
                                        config = response.config
                                    )
                                )
                            }
                        )
                    }
                }
                is ContentSlotModel.NoData -> {
                    _uiState.update {
                        it.copy(
                            items = it.items.updateById(menu.id) { data ->
                                data.copy(
                                    items = FeedBrowseChannelListState.initSuccess(
                                        data.items.items,
                                        nextCursor = response.nextCursor,
                                        hasNextPage = response.hasNextPage
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    private fun CoroutineScope.launchKeep(
        jobId: String,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val existingJob = jobMap[jobId]
        if (existingJob != null && existingJob.isActive) return

        val newJob = launch(block = block)
        jobMap[jobId] = newJob
    }

    private fun CategoryInspirationMap.updateById(
        id: String,
        onUpdate: (CategoryInspirationData) -> CategoryInspirationData
    ): CategoryInspirationMap {
        val data = get(id) ?: return this
        return this + (id to onUpdate(data))
    }

    companion object {
        private const val LOAD_JOB_ID = "load"
    }
}
