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

    private val iuImageFulfill by lazy {
        view.findViewById<ImageUnify>(R.id.iu_image_fulfill)
    }
    private val tvFulfillDistrict by lazy {
        view.findViewById<Typography>(R.id.tv_fulfill_district)
    }
    private val separatorEstimatedTimeArrival by lazy {
        view.findViewById<View>(R.id.separator_estimated_time_arrival)
    }
    private val textEstimatedTimeArrival by lazy {
        view.findViewById<Typography>(R.id.text_estimated_time_arrival)
    }

    companion object {
        val LAYOUT = R.layout.item_mini_cart_shop
    }

    override fun bind(element: MiniCartShopUiModel) {
        renderShopBadge(element)
        renderShopLocation(element)
        renderEstimatedTimeArrival(element)
    }

    private fun renderEstimatedTimeArrival(element: MiniCartShopUiModel) {
        if (element.estimatedTimeArrival.isNotBlank()) {
            textEstimatedTimeArrival.text = element.estimatedTimeArrival
            separatorEstimatedTimeArrival.show()
        } else {
            separatorEstimatedTimeArrival.gone()
        }
    }

    private fun renderShopLocation(element: MiniCartShopUiModel) {
        tvFulfillDistrict.text = element.shopLocation
    }

    private fun renderShopBadge(element: MiniCartShopUiModel) {
        if (element.shopBadgeUrl.isNotBlank()) {
            ImageHandler.loadImageWithoutPlaceholder(iuImageFulfill, element.shopBadgeUrl)
            iuImageFulfill.show()
        } else {
            iuImageFulfill.gone()
        }
    }

}