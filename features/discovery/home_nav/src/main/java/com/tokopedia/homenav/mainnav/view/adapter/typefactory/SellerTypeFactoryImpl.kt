package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileAffiliateDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileSellerDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderNavVisitable

/**
 * Created by dhaba
 */
class SellerTypeFactoryImpl : BaseAdapterTypeFactory(), SellerTypeFactory {
    override fun type(profileSellerDataModel: ProfileSellerDataModel): Int {
        return 0
    }

    override fun type(profileAffiliateDataModel: ProfileAffiliateDataModel): Int {
        return 0
    }

    override fun createViewHolder(view: View?, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {

            else -> super.createViewHolder(view, viewType)
        } as AbstractViewHolder<OrderNavVisitable>
    }
}