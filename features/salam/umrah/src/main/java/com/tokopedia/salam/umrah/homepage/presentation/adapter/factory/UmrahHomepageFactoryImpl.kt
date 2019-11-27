package com.tokopedia.salam.umrah.homepage.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.salam.umrah.common.analytics.TrackingUmrahUtil
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryFeaturedEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageMyUmrahEntity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder.UmrahHomepageCategoryFeaturedViewHolder
import com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder.UmrahHomepageCategoryViewHolder
import com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder.UmrahHomepageMyUmrahViewHolder
import com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder.UmrahHomepageSpinnerLikeViewHolder
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener

/**
 * @author by firman on 23/10/19
 */
class UmrahHomepageFactoryImpl(private val onBindListener: onItemBindListener,private val trackingUmrahUtil: TrackingUmrahUtil)
    : BaseAdapterTypeFactory(), UmrahHomepageFactory{
    override fun type(dataModel: UmrahSearchParameterEntity): Int {
       return UmrahSearchParameterEntity.LAYOUT
    }

    override fun type(dataModel: UmrahHomepageCategoryEntity): Int {
        return UmrahHomepageCategoryEntity.LAYOUT
    }

    override fun type(dataModel: UmrahHomepageCategoryFeaturedEntity): Int {
        return UmrahHomepageCategoryFeaturedEntity.LAYOUT
    }

    override fun type(dataModel: UmrahHomepageMyUmrahEntity):Int {
        return UmrahHomepageMyUmrahEntity.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when(type){
            UmrahSearchParameterEntity.LAYOUT -> UmrahHomepageSpinnerLikeViewHolder(view, onBindListener,trackingUmrahUtil)
            UmrahHomepageCategoryEntity.LAYOUT -> UmrahHomepageCategoryViewHolder(view, onBindListener, trackingUmrahUtil)
            UmrahHomepageCategoryFeaturedEntity.LAYOUT -> UmrahHomepageCategoryFeaturedViewHolder(view, onBindListener, trackingUmrahUtil)
            UmrahHomepageMyUmrahEntity.LAYOUT -> UmrahHomepageMyUmrahViewHolder(view, onBindListener, trackingUmrahUtil)
            else -> super.createViewHolder(view, type)
        }
    }
}