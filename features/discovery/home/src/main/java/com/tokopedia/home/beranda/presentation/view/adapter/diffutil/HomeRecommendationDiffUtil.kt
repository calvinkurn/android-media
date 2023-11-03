package com.tokopedia.home.beranda.presentation.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

class HomeRecommendationDiffUtil : DiffUtil.ItemCallback<Visitable<HomeRecommendationTypeFactoryImpl>>() {


    override fun getChangePayload(
        oldItem: Visitable<HomeRecommendationTypeFactoryImpl>,
        newItem: Visitable<HomeRecommendationTypeFactoryImpl>
    ): Any? {
        return Pair(oldItem, newItem)
    }

    override fun areItemsTheSame(
        oldItem: Visitable<HomeRecommendationTypeFactoryImpl>,
        newItem: Visitable<HomeRecommendationTypeFactoryImpl>
    ): Boolean {
        return oldItem.hashCode() == newItem.getUniqueIdentity()
    }

    override fun areContentsTheSame(
        oldItem: Visitable<HomeRecommendationTypeFactoryImpl>,
        newItem: Visitable<HomeRecommendationTypeFactoryImpl>
    ): Boolean {
        return oldItem.equalsDataModel(newItem)
    }
}
