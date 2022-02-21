package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchRecommendationUiModel
import com.tokopedia.brandlist.common.listener.BrandlistSearchTrackingListener
import com.tokopedia.brandlist.databinding.ItemSearchRecommendationBinding
import com.tokopedia.utils.view.binding.viewBinding


class BrandlistSearchRecommendationViewHolder(view: View): AbstractViewHolder<BrandlistSearchRecommendationUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_search_recommendation
    }
    private var binding: ItemSearchRecommendationBinding? by viewBinding()
    private val context: Context = itemView.context
    private val imgBrandLogo = binding?.ivBrandLogo
    private val imgBrandImage = binding?.ivBrandImage
    private val txtBrandName = binding?.tvBrandName

    override fun bind(element: BrandlistSearchRecommendationUiModel) {
        bindData(element.name, element.logoUrl, element.exclusiveLogoUrl,
                element.id, element.url, element.listener, element.position)
    }

    private fun bindData(name: String, brandLogoUrl: String, brandImageUrl: String, shopId: Int,
                         Applink: String, tracking: BrandlistSearchTrackingListener, position: String) {
        txtBrandName?.text = name
        ImageHandler.loadImage(context, imgBrandLogo, brandLogoUrl, null)
        if (brandImageUrl.isNotBlank()) {
            ImageHandler.loadImage(context, imgBrandImage, brandImageUrl, null)
        } else {
            imgBrandImage?.visibility = View.GONE
        }

        tracking.impressionBrand(shopId.toString(), position, name, brandImageUrl)
        itemView.setOnClickListener {
            tracking.clickBrand(shopId.toString(), position, name, brandImageUrl)
            RouteManager.route(context, Applink)
        }
    }
}