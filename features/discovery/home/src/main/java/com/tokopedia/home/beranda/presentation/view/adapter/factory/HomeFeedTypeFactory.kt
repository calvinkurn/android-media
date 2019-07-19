package com.tokopedia.home.beranda.presentation.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeBannerFeedViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeFeedLoadingMoreViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeFeedViewHolder
import com.tokopedia.home.beranda.presentation.view.viewmodel.BannerFeedViewModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel

/**
 * Created by Lukas on 2019-07-16
 */
class HomeFeedTypeFactory(private val view: HomeFeedContract.View) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            HomeFeedViewHolder.LAYOUT -> HomeFeedViewHolder(parent, view)
            LoadingShimmeringGridViewHolder.LAYOUT -> LoadingShimmeringGridViewHolder(parent)
            HomeFeedLoadingMoreViewHolder.LAYOUT -> HomeFeedLoadingMoreViewHolder(parent)
            BannerFeedViewModel.LAYOUT -> HomeBannerFeedViewHolder(parent, view)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(viewModel: HomeFeedViewModel): Int {
        return HomeFeedViewHolder.LAYOUT
    }

    fun type(viewModel: BannerFeedViewModel): Int {
        return BannerFeedViewModel.LAYOUT.toInt()
    }


    override fun type(viewModel: LoadingModel): Int {
        return LoadingShimmeringGridViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingMoreModel): Int {
        return HomeFeedLoadingMoreViewHolder.LAYOUT
    }
}