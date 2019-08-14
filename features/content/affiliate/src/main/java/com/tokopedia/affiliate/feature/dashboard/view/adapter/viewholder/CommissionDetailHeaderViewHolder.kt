package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
    private val commission: Typography = v.findViewById(R.id.productShop)
    private val clickNumber: Typography = v.findViewById(R.id.productShop)
    private val buyNumber: Typography = v.findViewById(R.id.productShop)
    private val commissionTotal: Typography = v.findViewById(R.id.productShop)

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_commission_header
    }

    override fun bind(element: CommissionDetailHeaderViewModel?) {
        productImage.loadImage("")
        productName.text = ""
        productPrice.text = ""
        commission.text = ""
        clickNumber.text = ""
        buyNumber.text = ""
        commissionTotal.text = ""
    }
}