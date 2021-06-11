package com.tokopedia.cart.view.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartTopAdsHeadlineBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.listener.TopAdsAddToCartClickListener
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.utils.*
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel
import com.tokopedia.topads.sdk.widget.TopAdsHeadlineView
import com.tokopedia.user.session.UserSessionInterface

class CartTopAdsHeadlineViewHolder(private val binding: ItemCartTopAdsHeadlineBinding,
                                   val listener: ActionListener?,
                                   val userSession: UserSessionInterface) : RecyclerView.ViewHolder(binding.root) {

    private var data: CartTopAdsHeadlineData? = null

    companion object {
        val LAYOUT = R.layout.item_cart_top_ads_headline
        const val VALUE_SRC_CART = "cart"
        const val PARAM_ST = "st"
        const val VALUE_CPM = "cpm"
        const val VALUE_PRODUCT = "product"
        const val VALUE_ITEM = "10"
        const val VALUE_TEMPLATE_ID = "2%2C3%2C4"
    }

    fun bind(data: CartTopAdsHeadlineData) {
        this.data = data
        bindTopAdsHeadlineView(data)
    }

    private fun fetchTopadsHeadlineAds(topAdsHeadlineView: TopAdsHeadlineView, userSession: UserSessionInterface) {
        topAdsHeadlineView.setHasAddToCartButton(true)
        topAdsHeadlineView.setShowCta(false)
        topAdsHeadlineView.setAddToCartClickListener(object : TopAdsAddToCartClickListener {
            override fun onAdToCartClicked(bannerShopProductViewModel: BannerShopProductViewModel) {
                listener?.onButtonAddToCartClicked(bannerShopProductViewModel)
            }
        })
        topAdsHeadlineView.getHeadlineAds(getHeadlineAdsParam(userSession), this::onSuccessResponse, this::hideHeadlineViewOnError)
    }

    private fun getHeadlineAdsParam(userSession: UserSessionInterface): String {
        return UrlParamHelper.generateUrlParamString(mutableMapOf(
                PARAM_DEVICE to VALUE_DEVICE,
                PARAM_EP to VALUE_CPM,
                PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
                PARAM_ITEM to VALUE_ITEM,
                PARAM_SRC to VALUE_SRC_CART,
                PARAM_ST to VALUE_PRODUCT,
                PARAM_PAGE to 1,
                PARAM_TEMPLATE_ID to VALUE_TEMPLATE_ID,
                PARAM_USER_ID to userSession.userId
        ))
    }

    private fun onSuccessResponse(cpmModel: CpmModel) {
        data?.apply {
            this.cpmModel = cpmModel
            showHeadlineView(cpmModel)
        }
    }

    private fun hideHeadlineViewOnFirstLoad() {
        binding.cartTopadsHeadlineView.apply {
            hideShimmerView()
            hide()
        }
    }

    private fun hideHeadlineViewOnError() {
        itemView.gone()
        val params = itemView.layoutParams
        params.height = 0
        params.width = 0
        itemView.layoutParams = params

        binding.cartTopadsHeadlineView.apply {
            hideShimmerView()
            hide()
        }
    }

    private fun bindTopAdsHeadlineView(data: CartTopAdsHeadlineData) {
        binding.cartTopadsHeadlineView.apply {
            hideHeadlineViewOnFirstLoad()
            if (data.cpmModel != null) {
                showHeadlineView(data.cpmModel)
            } else {
                fetchTopadsHeadlineAds(this, userSession)
            }
        }
    }

    private fun showHeadlineView(cpmModel: CpmModel?) {
        itemView.show()
        val params = itemView.layoutParams
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        itemView.layoutParams = params

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