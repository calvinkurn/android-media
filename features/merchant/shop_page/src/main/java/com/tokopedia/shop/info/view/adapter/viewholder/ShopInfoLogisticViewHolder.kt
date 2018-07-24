package com.tokopedia.shop.info.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.info.view.model.ShopInfoLogisticViewModel
import kotlinx.android.synthetic.main.item_shop_info_logistic.view.*

class ShopInfoLogisticViewHolder(val view: View):
        AbstractViewHolder<ShopInfoLogisticViewModel>(view) {

    companion object {
        @JvmStatic val LAYOUT = R.layout.item_shop_info_logistic
    }
    override fun bind(element: ShopInfoLogisticViewModel) {
        itemView.logisticLabelView.run {
            title = element.shipmentName
            setSubTitle(element.shipmentPackage)
            imageView.adjustViewBounds = true
            ImageHandler.loadImageRounded2(context, imageView, element.shipmentImage)
        }
    }
}