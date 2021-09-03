package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.RecomWidgetAdapter
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.RecomWidgetDataModel
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder

class RecomWidgetViewHolder(
    itemView: View,
) : AbstractViewHolder<RecomWidgetDataModel>(itemView) {


    private var recomWidgetAdapter: RecomWidgetAdapter? = null
    private var recomWidgetRecylerView: RecyclerView =
        itemView.findViewById<RecyclerView>(R.id.rvRecomWidgetView)


    override fun bind(element: RecomWidgetDataModel?) {
        if (recomWidgetAdapter == null) {
            recomWidgetAdapter = RecomWidgetAdapter()
        }
        recomWidgetAdapter?.recentViewItemHoldeDataList =
            element?.recomWidgetList?.recommendationItemList!!
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        recomWidgetRecylerView.layoutManager = layoutManager
        recomWidgetRecylerView.adapter = recomWidgetAdapter
    }


    companion object {
        val LAYOUT = R.layout.recom_widget_view
    }
}