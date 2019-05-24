package com.tokopedia.checkout.view.feature.emptycart2.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.checkout.domain.datamodel.recentview.RecentView
import com.tokopedia.checkout.view.feature.emptycart2.ActionListener
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.RecentViewItemUiModel
import com.tokopedia.checkout.view.feature.emptycart2.viewholder.RecentViewItemViewHolder

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class RecentViewAdapter(val listener: ActionListener, val itemWidth: Int) : RecyclerView.Adapter<RecentViewItemViewHolder>() {

    private var recentViewUiModels = ArrayList<RecentViewItemUiModel>()

    fun setData(recentViewItems: List<RecentView>) {
        for (recentView: RecentView in recentViewItems) {
            val recentViewUiModel = RecentViewItemUiModel()
            recentViewUiModel.recentView = recentView
            recentViewUiModels.add(recentViewUiModel)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(RecentViewItemViewHolder.LAYOUT, parent, false)
        return RecentViewItemViewHolder(view, listener, itemWidth)
    }

    override fun getItemCount(): Int {
        return recentViewUiModels.size
    }

    override fun onBindViewHolder(holder: RecentViewItemViewHolder, position: Int) {
        holder.bind(recentViewUiModels.get(position))
    }

}