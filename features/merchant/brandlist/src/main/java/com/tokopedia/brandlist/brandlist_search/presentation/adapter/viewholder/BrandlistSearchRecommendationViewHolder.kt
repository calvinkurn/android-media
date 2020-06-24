package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchRecommendationUiModel
import com.tokopedia.brandlist.common.listener.BrandlistSearchTrackingListener
import kotlinx.android.synthetic.main.item_search_recommendation.view.*


class BrandlistSearchRecommendationViewHolder(view: View): AbstractViewHolder<BrandlistSearchRecommendationUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_search_recommendation
    }

    private val context: Context = itemView.context
    private val imgBrandLogo = itemView.iv_brand_logo
    private val imgBrandImage = itemView.iv_brand_image
    private val txtBrandName = itemView.tv_brand_name

    override fun bind(element: BrandlistSearchRecommendationUiModel) {
        bindData(element.name, element.logoUrl, element.exclusiveLogoUrl,
                element.id, element.url, element.listener, element.position)
    }

    private fun bindData(name: String, brandLogoUrl: String, brandImageUrl: String, shopId: Int,
                         Applink: String, tracking: BrandlistSearchTrackingListener, position: String) {
        txtBrandName.text = name
        ImageHandler.loadImage(context, imgBrandLogo, brandLogoUrl, null)
        if (brandImageUrl.isNotBlank()) {
            ImageHandler.loadImage(context, imgBrandImage, brandImageUrl, null)
        } else {
            imgBrandImage.visibility = View.GONE
        }

        tracking.impressionBrand(shopId.toString(), position, name, brandImageUrl)
        itemView.setOnClickListener {
            tracking.clickBrand(shopId.toString(), position, name, brandImageUrl)
            RouteManager.route(context, Applink)
        }
    }
}