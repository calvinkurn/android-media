package com.tokopedia.salam.umrah.homepage.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageBannerEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryFeaturedEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageMyUmrahEntity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageCategoryFeaturedAdapter
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageChoosePacketAdapter
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageMyUmrahAdapter
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepagePartnerTravelAdapter
import com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder.*
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by firman on 23/10/19
 */
class UmrahHomepageFactoryImpl(private val onBindListener: onItemBindListener, val userSession: UserSessionInterface)
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

    override fun type(dataModel: UmrahHomepageBannerEntity): Int{
        return UmrahHomepageBannerViewHolder.LAYOUT
    }

    override fun type(dataModel: UmrahTravelAgentsEntity): Int {
        return UmrahHomepagePartnerTravelsViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            UmrahHomepageSpinnerLikeViewHolder.LAYOUT -> UmrahHomepageSpinnerLikeViewHolder(view, onBindListener)
            UmrahHomepageBannerViewHolder.LAYOUT-> UmrahHomepageBannerViewHolder(view, onBindListener)
            UmrahHomepageMyUmrahViewHolder.LAYOUT -> {
                val adapterMyUmrah = UmrahHomepageMyUmrahAdapter(onBindListener)
                UmrahHomepageMyUmrahViewHolder(view, onBindListener, adapterMyUmrah, userSession)
            }
            UmrahHomepageCategoryViewHolder.LAYOUT -> {
                val adapterChoosePacket = UmrahHomepageChoosePacketAdapter(onBindListener)
                UmrahHomepageCategoryViewHolder(view, onBindListener, adapterChoosePacket)
            }
            UmrahHomepageCategoryFeaturedViewHolder.LAYOUT -> {
                val adapterFeaturedCategory = UmrahHomepageCategoryFeaturedAdapter(onBindListener).also {
                    it.setHasStableIds(true)
                }
                UmrahHomepageCategoryFeaturedViewHolder(view, onBindListener, adapterFeaturedCategory)
            }
            UmrahHomepagePartnerTravelsViewHolder.LAYOUT ->{
                val adapter = UmrahHomepagePartnerTravelAdapter(onBindListener)
                UmrahHomepagePartnerTravelsViewHolder(view,onBindListener, adapter)
            }
            else -> super.createViewHolder(view, type)
        }
    }
}