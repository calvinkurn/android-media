package com.tokopedia.shop.info.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopInfoLogisticBinding
import com.tokopedia.shop.info.view.model.ShopInfoLogisticUiModel
import com.tokopedia.shop_widget.common.customview.ShopPageLabelView
import com.tokopedia.utils.view.binding.viewBinding

class ShopInfoLogisticViewHolder(val view: View) :
    AbstractViewHolder<ShopInfoLogisticUiModel>(view) {

    companion object {
        @JvmStatic val LAYOUT = R.layout.item_shop_info_logistic
    }

    private val viewBinding: ItemShopInfoLogisticBinding? by viewBinding()
    private var logisticLabelView: ShopPageLabelView? = viewBinding?.logisticLabelView

    override fun bind(element: ShopInfoLogisticUiModel) {
        logisticLabelView?.run {
            title = element.shipmentName
            setSubTitle(element.shipmentPackage)
            imageView?.adjustViewBounds = true
            imageView?.loadImageRounded(element.shipmentImage)
        }
    }
}
