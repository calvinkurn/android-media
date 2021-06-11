package com.tokopedia.play.broadcaster.view.partial

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.model.EtalaseLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayEtalaseViewHolder
import com.tokopedia.play.broadcaster.view.adapter.PlayEtalaseAdapter
import com.tokopedia.play_common.util.scroll.StopFlingScrollListener
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 26/04/21
 */
class EtalaseListViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.rv_etalase) {

    private val rvEtalase = rootView as RecyclerView

    private val etalaseAdapter = PlayEtalaseAdapter(object : PlayEtalaseViewHolder.Listener {
        override fun onEtalaseClicked(etalaseId: String, etalaseName: String, sharedElements: List<View>) {
            listener.onEtalaseClicked(this@EtalaseListViewComponent, etalaseId, etalaseName, sharedElements)
        }

        override fun onEtalaseBound(etalaseId: String) {
            listener.onEtalaseBound(this@EtalaseListViewComponent, etalaseId)
        }
    })

    init {
        rvEtalase.layoutManager = GridLayoutManager(rvEtalase.context, SPAN_COUNT, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

                override fun getSpanSize(position: Int): Int {
                    return if (etalaseAdapter.getItem(position) == EtalaseLoadingUiModel) SPAN_COUNT
                    else 1
                }
            }
        }
        rvEtalase.adapter = etalaseAdapter
        rvEtalase.addItemDecoration(PlayGridTwoItemDecoration(rvEtalase.context))
        rvEtalase.addOnScrollListener(StopFlingScrollListener())
    }

    fun refresh() {
        etalaseAdapter.notifyDataSetChanged()
    }

    fun setItems(etalaseList: List<EtalaseUiModel>) {
        etalaseAdapter.setItemsAndAnimateChanges(etalaseList)
    }

    interface Listener {

        fun onEtalaseClicked(view: EtalaseListViewComponent, etalaseId: String, etalaseName: String, sharedElements: List<View>)
        fun onEtalaseBound(view: EtalaseListViewComponent, etalaseId: String)
    }

    companion object {

        private const val SPAN_COUNT = 2
    }
}