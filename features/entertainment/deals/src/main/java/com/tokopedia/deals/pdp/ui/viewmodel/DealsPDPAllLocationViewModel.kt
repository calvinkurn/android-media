package com.tokopedia.deals.pdp.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.pdp.data.Outlet
import com.tokopedia.kotlin.extensions.view.ONE
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class DealsPDPAllLocationViewModel @Inject constructor(dispatcher: CoroutineDispatchers) :
    BaseViewModel(dispatcher.main) {

    private val _inputSearch = MutableSharedFlow<Pair<String, List<Outlet>>>(Int.ONE)

    val flowSearchResult: SharedFlow<List<Outlet>> =
        _inputSearch.flatMapConcat {
            flow {
                emit(getSearchResult(it.first, it.second))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    fun submitSearch(key: String, outlets: List<Outlet>) {
        _inputSearch.tryEmit(Pair(key, outlets))
    }

    private fun getSearchResult(key: String, outlets: List<Outlet>): List<Outlet> {
        return outlets.filter {
            it.name.trim().lowercase().contains(key.trim().lowercase()) ||
            it.district.trim().lowercase().contains(key.trim().lowercase())
        }
    }

    companion object {
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
    }
}
