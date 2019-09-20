package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePagePromoModel
import com.tokopedia.digital.home.model.DigitalHomePageTransactionModel
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageBannerViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageCategoryViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePagePromoViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageTransactionViewHolder
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

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            DigitalHomePageBannerViewHolder.LAYOUT -> return DigitalHomePageBannerViewHolder(parent, onItemBindListener)
            DigitalHomePageCategoryViewHolder.LAYOUT -> return DigitalHomePageCategoryViewHolder(parent, onItemBindListener)
            DigitalHomePagePromoViewHolder.LAYOUT -> return DigitalHomePagePromoViewHolder(parent, onItemBindListener)
            DigitalHomePageTransactionViewHolder.LAYOUT -> return DigitalHomePageTransactionViewHolder(parent, transactionListener)
        }
        return super.createViewHolder(parent, type)
    }

}
