package com.tokopedia.minicart.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartShopUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class MiniCartShopViewHolder(private val view: View,
                             private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartShopUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_shop
    }

    private val iuImageFulfill: ImageUnify? by lazy {
        view.findViewById(R.id.iu_image_fulfill)
    }
    private val tvFulfillDistrict: Typography? by lazy {
        view.findViewById(R.id.tv_fulfill_district)
    }
    private val separatorEstimatedTimeArrival: View? by lazy {
        view.findViewById(R.id.separator_estimated_time_arrival)
    }
    private val textEstimatedTimeArrival: Typography? by lazy {
        view.findViewById(R.id.text_estimated_time_arrival)
    }

    override fun bind(element: MiniCartShopUiModel) {
        renderShopBadge(element)
        renderShopLocation(element)
        renderEstimatedTimeArrival(element)
    }

    private fun renderEstimatedTimeArrival(element: MiniCartShopUiModel) {
        if (element.estimatedTimeArrival.isNotBlank()) {
            textEstimatedTimeArrival?.text = element.estimatedTimeArrival
            separatorEstimatedTimeArrival?.show()
        } else {
            separatorEstimatedTimeArrival?.gone()
        }
    }

    private fun renderShopLocation(element: MiniCartShopUiModel) {
        tvFulfillDistrict?.text = element.shopLocation
    }

    private fun renderShopBadge(element: MiniCartShopUiModel) {
        iuImageFulfill?.let {
            if (element.shopBadgeUrl.isNotBlank()) {
                ImageHandler.loadImageWithoutPlaceholder(it, element.shopBadgeUrl)
                it.show()
            } else {
                it.gone()
            }
        }
    }

}