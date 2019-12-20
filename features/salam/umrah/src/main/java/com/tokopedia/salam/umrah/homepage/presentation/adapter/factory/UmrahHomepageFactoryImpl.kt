package com.tokopedia.salam.umrah.homepage.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryFeaturedEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageMyUmrahEntity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageCategoryFeaturedAdapter
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageChoosePacketAdapter
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageMyUmrahAdapter
import com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder.UmrahHomepageCategoryFeaturedViewHolder
import com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder.UmrahHomepageCategoryViewHolder
import com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder.UmrahHomepageMyUmrahViewHolder
import com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder.UmrahHomepageSpinnerLikeViewHolder
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener

/**
 * @author by firman on 23/10/19
 */
class UmrahHomepageFactoryImpl(private val onBindListener: onItemBindListener)
    : BaseAdapterTypeFactory(), UmrahHomepageFactory{


    override fun type(dataModel: UmrahSearchParameterEntity): Int {
        return UmrahHomepageSpinnerLikeViewHolder.LAYOUT
    }

    override fun type(dataModel: UmrahHomepageCategoryEntity): Int {
        return UmrahHomepageCategoryViewHolder.LAYOUT
    }

    override fun type(dataModel: UmrahHomepageCategoryFeaturedEntity): Int {
        return UmrahHomepageCategoryFeaturedViewHolder.LAYOUT
    }

    override fun type(dataModel: UmrahHomepageMyUmrahEntity):Int {
        return UmrahHomepageMyUmrahViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {

        val adapterFeaturedCategory = UmrahHomepageCategoryFeaturedAdapter(onBindListener).also {
            it.setHasStableIds(true)
        }
        val adapterChoosePacket = UmrahHomepageChoosePacketAdapter(onBindListener)
        val adapterMyUmrah = UmrahHomepageMyUmrahAdapter(onBindListener)

        return when(type){
            UmrahHomepageSpinnerLikeViewHolder.LAYOUT -> UmrahHomepageSpinnerLikeViewHolder(view, onBindListener)
            UmrahHomepageMyUmrahViewHolder.LAYOUT -> UmrahHomepageMyUmrahViewHolder(view, onBindListener, adapterMyUmrah)
            UmrahHomepageCategoryViewHolder.LAYOUT -> UmrahHomepageCategoryViewHolder(view, onBindListener, adapterChoosePacket)
            UmrahHomepageCategoryFeaturedViewHolder.LAYOUT -> UmrahHomepageCategoryFeaturedViewHolder(view, onBindListener, adapterFeaturedCategory)
            else -> super.createViewHolder(view, type)
        }
    }
}