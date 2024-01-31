package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemBannerImageBinding
import com.tokopedia.catalogcommon.databinding.WidgetItemSellerOfferingBinding
import com.tokopedia.catalogcommon.uimodel.SellerOfferingUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.abstraction.R as abstractionR


class SellerOfferingViewHolder(itemView: View) : AbstractViewHolder<SellerOfferingUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_seller_offering
    }

    private val binding by viewBinding<WidgetItemSellerOfferingBinding>()

    override fun bind(element: SellerOfferingUiModel) {
       binding?.apply {
           ivBadge.loadImage(element.shopBadge)
           tvShopName.text = element.shopName
           tvShopResposiveChat.text = element.chatResponseTime
           tvShopResponsiveOrder.text = element.orderProcessTime
           ivProduct.loadImage(element.productImage)
           tvProductName.text = element.productName
           tvPrice.text = element.productPrice
           tvSlashPrice.text = element.productSlashPrice
           tvSlashPrice.showWithCondition(element.productSlashPrice.isNotEmpty())
           flPromo.showWithCondition(element.labelPromo.isNotEmpty())
           tvLabelPromo.text = element.labelPromo
           tvLabelDisc.text = element.labelTotalDisc
           tvSalesRatingFloat.text = element.shopRating
           tvTotalSold.text = element.totalSold
           ivFreeOngkir.loadImage(element.freeOngkir)
           ivFreeOngkir.showWithCondition(element.freeOngkir.isNotEmpty())
           tvGuarantee.showWithCondition(element.isShopGuarantee)
           tvInstallment.text = element.installment
           progressProduct.setProgressIcon(
               icon = ContextCompat.getDrawable(
                   itemView.context,
                   R.drawable.catalog_ic_stockbar_progress_top
               ),
               width = 16.toPx(),
               height = 15.toPx()
           )
       }
    }

}
