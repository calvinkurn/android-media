package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.AffiliateViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.SellerViewHolder
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileAffiliateDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileSellerDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderNavVisitable
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by dhaba
 */
class SellerTypeFactoryImpl(private val mainNavListener: MainNavListener,
                            private val userSession: UserSessionInterface
) : BaseAdapterTypeFactory(), SellerTypeFactory {
    override fun type(profileSellerDataModel: ProfileSellerDataModel): Int {
        return SellerViewHolder.LAYOUT
    }

    override fun type(profileAffiliateDataModel: ProfileAffiliateDataModel): Int {
        return AffiliateViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            SellerViewHolder.LAYOUT -> {
                SellerViewHolder(view, mainNavListener, userSession)
            }
            AffiliateViewHolder.LAYOUT -> {
                AffiliateViewHolder(view, mainNavListener, userSession)
            }
            else -> super.createViewHolder(view, viewType)
        } as AbstractViewHolder<OrderNavVisitable>
    }
}