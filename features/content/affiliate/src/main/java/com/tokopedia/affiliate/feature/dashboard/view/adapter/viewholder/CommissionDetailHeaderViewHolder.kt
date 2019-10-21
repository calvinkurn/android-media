package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailHeaderViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by milhamj on 2019-08-14.
 */
class CommissionDetailHeaderViewHolder(v: View) : AbstractViewHolder<CommissionDetailHeaderViewModel>(v) {

    private val productImage: ImageView = v.findViewById(R.id.productImage)
    private val productName: Typography = v.findViewById(R.id.productName)
    private val productPrice: Typography = v.findViewById(R.id.productPrice)
    private val productShop: Typography = v.findViewById(R.id.productShop)
    private val commission: Typography = v.findViewById(R.id.commission)
    private val clickNumber: Typography = v.findViewById(R.id.clickNumber)
    private val buyNumber: Typography = v.findViewById(R.id.buyNumber)
    private val commissionTotal: Typography = v.findViewById(R.id.commissionTotal)
    private val statusTv: Typography = v.findViewById(R.id.statusTv)
    private val statusLayout: FrameLayout = v.findViewById(R.id.statusLayout)

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_commission_header
    }

    override fun bind(element: CommissionDetailHeaderViewModel) {
        productImage.loadImage(element.productImg)
        productName.text = element.productName
        val priceText = getString(R.string.af_price_title) + " " +  element.priceFmt
        productPrice.text = priceText
        productShop.text = element.shopName
        commission.text = element.commissionFmt
        clickNumber.text = element.totalClick.toString()
        buyNumber.text = element.totalSold.toString()
        commissionTotal.text = element.totalCOmmissionFmt
        statusTv.setTextColor(MethodChecker.getColor(
                statusTv.context,
                if (element.isActive)
                    R.color.color_ongoing_text
                else
                    R.color.font_black_secondary_54))
        statusTv.setText(statusTv.getContext().getString(
                if (element.isActive)
                    R.string.text_af_ongoing
                else
                    R.string.text_af_finished))
        statusLayout.setBackground(MethodChecker.getDrawable(
                statusLayout.context,
                if (element.isActive)
                    R.drawable.bg_af_ongoing
                else
                    R.drawable.bg_af_finished))
    }
}