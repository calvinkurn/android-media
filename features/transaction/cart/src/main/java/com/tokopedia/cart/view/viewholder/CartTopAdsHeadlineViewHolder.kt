package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartTopAdsHeadlineBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.utils.*
import com.tokopedia.topads.sdk.widget.TopAdsHeadlineView
import com.tokopedia.user.session.UserSessionInterface

class CartTopAdsHeadlineViewHolder(private val binding: ItemCartTopAdsHeadlineBinding,
                                   val listener: ActionListener?,
                                   val userSession: UserSessionInterface) : RecyclerView.ViewHolder(binding.root) {

    private val data: CartTopAdsHeadlineData? = null

    companion object {
        val LAYOUT = R.layout.item_cart_top_ads_headline
        const val VALUE_SRC_CART = "cart"
    }

    fun bind(data: CartTopAdsHeadlineData) {
        bindTopAdsHeadlineView(data)
    }

    private fun fetchTopadsHeadlineAds(topAdsHeadlineView: TopAdsHeadlineView, data: CartTopAdsHeadlineData, userSession: UserSessionInterface) {
        topAdsHeadlineView.getHeadlineAds(getHeadlineAdsParam(data, userSession), this::onSuccessResponse, this::hideHeadlineView)
    }

    private fun getHeadlineAdsParam(data: CartTopAdsHeadlineData, userSession: UserSessionInterface): String {
        return UrlParamHelper.generateUrlParamString(mutableMapOf(
                PARAM_DEVICE to VALUE_DEVICE,
                PARAM_PAGE to 1,
                PARAM_EP to VALUE_EP,
                PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
                PARAM_ITEM to VALUE_ITEM,
                PARAM_SRC to VALUE_SRC_CART,
                PARAM_TEMPLATE_ID to VALUE_TEMPLATE_ID,
                PARAM_USER_ID to userSession.userId,
                PARAM_PRODUCT_ID to data.productIds.joinToString(",")
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
                fetchTopadsHeadlineAds(this, data, userSession)
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
        }
    }
}