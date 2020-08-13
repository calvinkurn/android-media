package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.TopAdsImageDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class ProductTopAdsImageViewHolder(private val view: View, val listener: DynamicProductDetailListener) : AbstractViewHolder<TopAdsImageDataModel>(view) {

    private val topAdsImageView: TopAdsImageView = view.findViewById(R.id.adsTopAdsImageView)

    companion object {
        val LAYOUT = R.layout.item_top_ads_image_view
    }

    override fun bind(element: TopAdsImageDataModel) {
        if (!element.data.isNullOrEmpty()) {
            val bannerId = element.data?.get(0)?.bannerId ?: ""
            val bannerName = element.data?.get(0)?.bannerName ?: ""

            topAdsImageView.loadImage(element.data?.get(0) ?: TopAdsImageViewModel()) {
                topAdsImageView.hide()
            }
            topAdsImageView.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
                override fun onTopAdsImageViewClicked(applink: String?) {
                    listener.onTopAdsImageViewClicked(element, applink, bannerId, bannerName)
                }
            })

            topAdsImageView.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
                override fun onTopAdsImageViewImpression(viewUrl: String) {
                    ImpresionTask(this@ProductTopAdsImageViewHolder.javaClass.canonicalName).execute(viewUrl)
                    listener.onTopAdsImageViewImpression(element, bannerId, bannerName)
                }
            })
        }

    }


}