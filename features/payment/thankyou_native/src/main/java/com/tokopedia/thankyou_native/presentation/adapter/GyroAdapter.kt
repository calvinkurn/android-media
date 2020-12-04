package com.tokopedia.thankyou_native.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.factory.GyroRecommendationFactory
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendationListItem

class GyroAdapter(private val visitableList: ArrayList<Visitable<*>>,
                  private val typeFactory: GyroRecommendationFactory) :
        BaseAdapter<GyroRecommendationFactory>(typeFactory, visitableList) {

    fun addItems(data: ArrayList<Visitable<*>>) {
        visitableList.clear()
        visitableList.addAll(data)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.tag?.let {
            if (it is GyroRecommendationListItem && !it.isVisited) {
                it.isVisited = true
                typeFactory.listener.onItemDisplayed(it, holder.adapterPosition)
            }
        }
    }

}

interface GyroAdapterListener {
    fun onItemDisplayed(gyroRecommendationListItem: GyroRecommendationListItem, position: Int)
    fun onItemClicked(gyroRecommendationListItem: GyroRecommendationListItem, position: Int)
    fun openAppLink(appLink: String)
    fun openWebUrl(url: String)
}