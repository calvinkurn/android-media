package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartTopAdsHeadlineBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.utils.*
import com.tokopedia.topads.sdk.widget.TopAdsHeadlineView
import com.tokopedia.user.session.UserSessionInterface

class CartTopAdsHeadlineViewHolder(private val binding: ItemCartTopAdsHeadlineBinding,
                                   val listener: ActionListener?,
                                   val userSession: UserSessionInterface) : RecyclerView.ViewHolder(binding.root) {

    private var data: CartTopAdsHeadlineData? = null

    companion object {
        val LAYOUT = R.layout.item_cart_top_ads_headline
        const val VALUE_SRC_CART = "cart"
    }

    fun bind(data: CartTopAdsHeadlineData) {
        this.data = data
        bindTopAdsHeadlineView(data)
    }

    private fun fetchTopadsHeadlineAds(topAdsHeadlineView: TopAdsHeadlineView, userSession: UserSessionInterface) {
        topAdsHeadlineView.getHeadlineAds(getHeadlineAdsParam(userSession), this::onSuccessResponse, this::hideHeadlineView)
    }

    private fun getHeadlineAdsParam(userSession: UserSessionInterface): String {
        return UrlParamHelper.generateUrlParamString(mutableMapOf(
                PARAM_DEVICE to "android",
                PARAM_EP to "cpm",
                PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
                PARAM_ITEM to "10",
                PARAM_SRC to VALUE_SRC_CART,
                "st" to "product",
                PARAM_PAGE to 1,
                PARAM_TEMPLATE_ID to "2%2C3%2C4",
                PARAM_USER_ID to userSession.userId
        ))
    }

    private fun onSuccessResponse(cpmModel: CpmModel) {
        data?.apply {
            this.cpmModel = cpmModel
            showHeadlineView(cpmModel)
        }
    }

    private fun hideHeadlineView() {
        binding.cartTopadsHeadlineView.apply {
            hideShimmerView()
            hide()
        }
    }

    private fun bindTopAdsHeadlineView(data: CartTopAdsHeadlineData) {
        binding.cartTopadsHeadlineView.apply {
            hideHeadlineView()
            if (data.cpmModel != null) {
                showHeadlineView(data.cpmModel)
            } else {
                fetchTopadsHeadlineAds(this, userSession)
            }
        }
    }

    private fun showHeadlineView(cpmModel: CpmModel?) {
        binding.cartTopadsHeadlineView.apply {
            cpmModel?.let {
                hideShimmerView()
                show()
                displayAds(it)
            }

            topadsBannerView.setTopAdsBannerClickListener(TopAdsBannerClickListener { position: Int, applink: String?, data: CpmData? ->
                applink?.let {
                    RouteManager.route(itemView.context, applink)
                    if (it.contains("shop")) {
                        TopAdsGtmTracker.eventTopAdsHeadlineShopClick(position, "", data, userSession.userId)
                    } else {
                        TopAdsGtmTracker.eventTopAdsHeadlineProductClick(position, "", data, userSession.userId)
                    }
                }
            })

            topadsBannerView.setTopAdsImpressionListener(object : TopAdsItemImpressionListener() {
                override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData) {
                    TopAdsGtmTracker.eventTopAdsHeadlineShopView(position, data, "", userSession.userId)
                }
            })

        }
    }
}