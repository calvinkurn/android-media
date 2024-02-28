package com.tokopedia.homenav.mainnav.view.interactor.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.buyagain.BuyAgainView

class BuyAgainCallback constructor(
    private val context: Context
) : BuyAgainView.Listener {

    override fun onProductCardClicked(id: String) {
        val intent = RouteManager
            .getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, id)

        context.startActivity(intent)
    }

    override fun onAtcButtonClicked(id: String) {

    }
}
