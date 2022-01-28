package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.setup.product.view.adapter.EtalaseListAdapter
import com.tokopedia.play.broadcaster.setup.product.view.model.EtalaseListModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 27/01/22
 */
internal class EtalaseListViewComponent(
    view: RecyclerView,
) : ViewComponent(view) {

    private val adapter = EtalaseListAdapter()

    init {
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
    }

    fun setEtalaseList(etalaseList: List<EtalaseListModel>) {
        adapter.setItemsAndAnimateChanges(etalaseList)
    }
}