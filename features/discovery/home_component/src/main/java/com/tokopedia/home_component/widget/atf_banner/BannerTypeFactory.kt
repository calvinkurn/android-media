package com.tokopedia.home_component.widget.atf_banner

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.viewholders.adapter.BannerItemListener

/**
 * Created by frenzel
 */
interface BannerTypeFactory: AdapterTypeFactory {
    fun type(dataModel: BannerRevampItemModel): Int
    fun type(dataModel: BannerShimmerModel): Int

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<BannerVisitable>
}

/**
 * Created by frenzel
 */
class BannerTypeFactoryImpl(
    val listener: BannerItemListener
): BaseAdapterTypeFactory(), BannerTypeFactory {
    override fun type(dataModel: BannerRevampItemModel): Int {
        return BannerRevampItemViewHolder.LAYOUT
    }

    override fun type(dataModel: BannerShimmerModel): Int {
        return BannerShimmerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<BannerVisitable> {
        return when(viewType) {
            BannerRevampItemViewHolder.LAYOUT -> BannerRevampItemViewHolder(view, listener)
            BannerShimmerViewHolder.LAYOUT -> BannerShimmerViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        } as AbstractViewHolder<BannerVisitable>
    }
}
