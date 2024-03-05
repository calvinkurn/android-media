package com.tokopedia.homenav.mainnav.view.interactor.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.buyagain.BuyAgainViewHolder
import com.tokopedia.homenav.mainnav.view.analytics.BuyAgainTracker
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.widget.BuyAgainModel
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.searchbar.navigation_component.NavSource
import java.util.HashMap

class BuyAgainCallback(
    private val context: Context,
    private val mainNavListener: MainNavListener,
    private val addToCart: (String, String) -> Unit
) : BuyAgainViewHolder.Listener {

    private var mDetail: PageDetail? = null

    fun setPageDetail(source: NavSource, path: String) = apply {
        mDetail = PageDetail(source, path)
    }

    override fun onProductCardClicked(model: BuyAgainModel, position: Int) {
        val intent = RouteManager
            .getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, model.productId)

        context.startActivity(intent)

        // tracker
        BuyAgainTracker.productClick(
            userId = mainNavListener.getUserId(),
            pageDetail = mDetail,
            position = position,
            model = model
        )
    }

    override fun onAtcButtonClicked(model: BuyAgainModel, position: Int) {
        if (model.hasVariant) {
            AtcVariantHelper.goToAtcVariant(
                context = context,
                productId = model.productId,
                pageSource = VariantPageSource.ME_PAGE_PAGESOURCE,
                shopId = model.shopId,
                startActivitResult = { intent, _ ->
                    context.startActivity(intent)
                }
            )
        } else {
            addToCart(model.productId, model.shopId)
        }

        // tracker
        BuyAgainTracker.productAtcClick(
            userId = mainNavListener.getUserId(),
            pageDetail = mDetail,
            position = position,
            model = model
        )
    }

    override fun onBuyAgainWidgetImpression(models: List<BuyAgainModel>, position: Int) {
        mainNavListener.putEEToTrackingQueue(
            BuyAgainTracker.impression(
                userId = mainNavListener.getUserId(),
                pageDetail = mDetail,
                position = position,
                models = models
            ) as HashMap<String, Any>
        )
    }

    data class PageDetail(
        val source: NavSource,
        val path: String
    )
}
