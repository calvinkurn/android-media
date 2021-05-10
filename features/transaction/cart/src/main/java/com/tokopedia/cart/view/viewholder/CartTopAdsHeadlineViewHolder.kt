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

    private fun fetchTopadsHeadlineAds(topAdsHeadlineView: TopAdsHeadlineView, data: CartTopAdsHeadlineData, userSession: UserSessionInterface) {
        topAdsHeadlineView.getHeadlineAds(getHeadlineAdsParam(data, userSession), this::onSuccessResponse, this::hideHeadlineView)
    }

    /**
     * device=android
     * &page=1
     * &ep=headline
     * &headline_product_count=3
     * &item=1
     * &src=fav_product
     * &template_id=3
     * &user_id=8828956"
     */

    /*
    * dep_id=
    * &device=desktop
    * &ep=cpm
    * &headline_product_count=3
    * &item=10
    * &q=
    * &src=cart
    * &st=product
    * &template_id=2%2C3%2C4
    * &user_id=148220336
    * &page=1
    *
    * */
    private fun getHeadlineAdsParam(data: CartTopAdsHeadlineData, userSession: UserSessionInterface): String {
        return UrlParamHelper.generateUrlParamString(mutableMapOf(
                PARAM_DEVICE to "android",
                PARAM_EP to "cpm",
                PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
                PARAM_ITEM to "10",
                PARAM_SRC to "fav_product",
                "st" to "product",
                PARAM_PAGE to 1,
                PARAM_TEMPLATE_ID to "2%2C3%2C4",
                PARAM_USER_ID to "148220336"
        ))
    }

    //2021-05-10 13:37:50.448 2123-6281/com.tokopedia.tkpd D/OkHttp: [
//2021-05-10 13:37:50.448 2123-6281/com.tokopedia.tkpd D/OkHttp:   {
//2021-05-10 13:37:50.448 2123-6281/com.tokopedia.tkpd D/OkHttp:     "md5": "7a2ff0e8f81cd215ba974371d292cd84",
//2021-05-10 13:37:50.448 2123-6281/com.tokopedia.tkpd D/OkHttp:     "operationName": null,
//2021-05-10 13:37:50.448 2123-6281/com.tokopedia.tkpd D/OkHttp:     "query": "query TopadsCPMHeadlineQuery($displayParams: String!) { displayAdsV3(displayParams: $displayParams) { status { error_code message } header { process_time total_data } data { ad_click_url ad_ref_key id redirect applinks headline { promoted_text name button_text layout position description image { full_url full_ecs } shop { id name tagline slogan location city domain is_followed merchant_vouchers product { id name applinks image { m_url } image_product { image_url image_click_url } price_format product_rating count_review_format } image_shop { xs_url } } badges { image_url title show } template_id } } } } ",
//2021-05-10 13:37:50.448 2123-6281/com.tokopedia.tkpd D/OkHttp:     "variables": {
//2021-05-10 13:37:50.448 2123-6281/com.tokopedia.tkpd D/OkHttp:       "displayParams": "dep_id=&device=desktop&ep=cpm&headline_product_count=3&item=10&q=&src=cart&st=product&page=1&template_id=2%252C3%252C4&user_id=148220336"
//2021-05-10 13:37:50.448 2123-6281/com.tokopedia.tkpd D/OkHttp:     }
//2021-05-10 13:37:50.448 2123-6281/com.tokopedia.tkpd D/OkHttp:   }
//2021-05-10 13:37:50.448 2123-6281/com.tokopedia.tkpd D/OkHttp: ]

//[
//    {
//        "md5": "7a2ff0e8f81cd215ba974371d292cd84",
//        "operationName": null,
//        "query": "query TopadsCPMHeadlineQuery($displayParams: String!) { displayAdsV3(displayParams: $displayParams) { status { error_code message } header { process_time total_data } data { ad_click_url ad_ref_key id redirect applinks headline { promoted_text name button_text layout position description image { full_url full_ecs } shop { id name tagline slogan location city domain is_followed merchant_vouchers product { id name applinks image { m_url } image_product { image_url image_click_url } price_format product_rating count_review_format } image_shop { xs_url } } badges { image_url title show } template_id } } } } ",
//              "variables": {
//                   "displayParams": "device=android&page=1&ep=headline&headline_product_count=3&item=1&src=fav_product&template_id=3&user_id=8828956"
//              }
//     }
//]


    // // 2021-05-10 13:37:54.489 2123-6281/com.tokopedia.tkpd D/OkHttp: [{"data":{"displayAdsV3":{"status":{"error_code":0,"message":"OK"},"header":{"process_time":0.00315168,"total_data":0},"data":[]}}}]

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