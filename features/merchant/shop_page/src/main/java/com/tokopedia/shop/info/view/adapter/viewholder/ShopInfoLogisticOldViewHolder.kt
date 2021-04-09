package com.tokopedia.shop.info.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.label.LabelView
import com.tokopedia.shop.R
import com.tokopedia.shop.info.view.model.ShopInfoLogisticUiModel

/**
 * @author by alvarisi on 12/12/17.
 */
@Deprecated("")
class ShopInfoLogisticOldViewHolder(itemView: View) : AbstractViewHolder<ShopInfoLogisticUiModel?>(itemView) {
    private var shopNoteLabelView: LabelView? = null
    private fun findViews(view: View) {
        shopNoteLabelView = view.findViewById(R.id.label_view)
    }

    override fun bind(element: ShopInfoLogisticUiModel?) {
        ImageHandler.loadImageRounded2(shopNoteLabelView!!.imageView.context, shopNoteLabelView!!.imageView, element?.shipmentImage)
        shopNoteLabelView?.setTitle(element?.shipmentName)
        shopNoteLabelView!!.setContent(element?.shipmentPackage)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_info_logistic
    }

    init {
        findViews(itemView)
    }
}