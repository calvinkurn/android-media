package com.tokopedia.topads.view.adapter.adgrouplist.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupShimmerUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.CreateAdGroupUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.ErrorUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.LoadingMoreUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.ReloadInfiniteUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.AdGroupShimmerViewHolder
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.AdGroupViewHolder
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.CreateAdGroupViewHolder
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.ErrorViewHolder
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.LoadingMoreViewHolder
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.ReloadInfiniteViewHolder

class AdGroupTypeFactoryImpl(
    private val adGroupListener: AdGroupViewHolder.AdGroupListener,
    private val errorListener: ErrorViewHolder.ErrorListener
) : AdGroupTypeFactory , BaseAdapterTypeFactory() {
    override fun type(model: AdGroupShimmerUiModel) = AdGroupShimmerViewHolder.LAYOUT

    override fun type(model: AdGroupUiModel) = AdGroupViewHolder.LAYOUT

    override fun type(model: CreateAdGroupUiModel) = CreateAdGroupViewHolder.LAYOUT

    override fun type(model: ErrorUiModel) = ErrorViewHolder.LAYOUT

    override fun type(model: ReloadInfiniteUiModel) = ReloadInfiniteViewHolder.LAYOUT

    override fun type(model: LoadingMoreUiModel) = LoadingMoreViewHolder.LAYOUT

    override fun createViewHolder(itemView: View, viewType:Int): AbstractViewHolder<*> {
        return when(viewType){
            AdGroupShimmerViewHolder.LAYOUT -> AdGroupShimmerViewHolder(itemView)
            AdGroupViewHolder.LAYOUT -> AdGroupViewHolder(itemView,adGroupListener)
            CreateAdGroupViewHolder.LAYOUT -> CreateAdGroupViewHolder(itemView)
            ErrorViewHolder.LAYOUT -> ErrorViewHolder(itemView,errorListener)
            ReloadInfiniteViewHolder.LAYOUT -> ReloadInfiniteViewHolder(itemView)
            LoadingMoreViewHolder.LAYOUT -> LoadingMoreViewHolder(itemView)
            else -> super.createViewHolder(itemView, viewType)
        }
    }
}
