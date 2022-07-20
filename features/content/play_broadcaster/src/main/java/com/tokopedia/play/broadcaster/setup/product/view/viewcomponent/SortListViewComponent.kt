package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.setup.product.view.adapter.ProductListAdapter
import com.tokopedia.play.broadcaster.setup.product.view.adapter.SortListAdapter
import com.tokopedia.play.broadcaster.setup.product.view.itemdecoration.ProductListItemDecoration
import com.tokopedia.play.broadcaster.setup.product.view.model.SortListModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 02/02/22
 */
internal class SortListViewComponent(
    view: RecyclerView,
    eventBus: EventBus<in Event>,
) : ViewComponent(view) {

    private val adapter = SortListAdapter {
        eventBus.emit(Event.OnSelected(it))
    }

    init {
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(
            view.context,
            RecyclerView.VERTICAL,
            false,
        )
    }

    fun setSortList(sortList: List<SortListModel>) {
        adapter.setItemsAndAnimateChanges(sortList)
    }

    sealed class Event {
        data class OnSelected(val sort: SortListModel) : Event()
    }
}