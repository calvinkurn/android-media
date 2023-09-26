package com.tokopedia.content.product.picker.sgc.view.viewcomponent

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.util.eventbus.EventBus
import com.tokopedia.content.product.picker.sgc.view.adapter.SortListAdapter
import com.tokopedia.content.product.picker.sgc.model.SortListModel
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
