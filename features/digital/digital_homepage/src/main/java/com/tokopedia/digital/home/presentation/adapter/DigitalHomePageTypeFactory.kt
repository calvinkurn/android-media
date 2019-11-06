package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.presentation.adapter.viewholder.*
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener

class DigitalHomePageTypeFactory(val onItemBindListener: OnItemBindListener,
                                 val transactionListener: DigitalHomePageTransactionViewHolder.TransactionListener?)
    : BaseAdapterTypeFactory() {

    fun type(digitalHomePageBannerModel: DigitalHomePageBannerModel): Int {
        return DigitalHomePageBannerViewHolder.LAYOUT
    }

    fun type(digitalHomePageCategoryModel: DigitalHomePageCategoryModel): Int {
        return DigitalHomePageCategoryViewHolder.LAYOUT
    }

    fun type(digitalHomePagePromoModel: DigitalHomePagePromoModel): Int {
        return DigitalHomePagePromoViewHolder.LAYOUT
    }

    fun type(digitalHomePageTransactionModel: DigitalHomePageTransactionModel): Int {
        return DigitalHomePageTransactionViewHolder.LAYOUT
    }

    fun type(digitalHomePageFavoritesModel: DigitalHomePageFavoritesModel): Int {
        return DigitalHomePageFavoriteViewHolder.LAYOUT
    }

//    fun type(digitalHomePageTrustMarkModel: DigitalHomePageTrustMarkModel): Int {
//        return DigitalHomePageTrustMarkViewHolder.LAYOUT
//    }
//
//    fun type(digitalHomePageNewUserZoneModel: DigitalHomePageNewUserZoneModel): Int {
//        return DigitalHomePageNewUserZoneViewHolder.LAYOUT
//    }
//
//    fun type(digitalHomePageSpotlightModel: DigitalHomePageSpotlightModel): Int {
//        return DigitalHomePageSpotlightViewHolder.LAYOUT
//    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            DigitalHomePageBannerViewHolder.LAYOUT -> return DigitalHomePageBannerViewHolder(parent, onItemBindListener)
            DigitalHomePageCategoryViewHolder.LAYOUT -> return DigitalHomePageCategoryViewHolder(parent, onItemBindListener)
            DigitalHomePagePromoViewHolder.LAYOUT -> return DigitalHomePagePromoViewHolder(parent, onItemBindListener)
            DigitalHomePageTransactionViewHolder.LAYOUT -> return DigitalHomePageTransactionViewHolder(parent, transactionListener)
            DigitalHomePageFavoriteViewHolder.LAYOUT -> return DigitalHomePageFavoriteViewHolder(parent, onItemBindListener)
        }
        return super.createViewHolder(parent, type)
    }

}
