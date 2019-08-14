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
    private val productShop: Typography = v.findViewById(R.id.productShop)
    private val commission: Typography = v.findViewById(R.id.commission)
    private val clickNumber: Typography = v.findViewById(R.id.clickNumber)
    private val buyNumber: Typography = v.findViewById(R.id.buyNumber)
    private val commissionTotal: Typography = v.findViewById(R.id.commissionTotal)

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_commission_header
    }

    override fun bind(element: CommissionDetailHeaderViewModel?) {
        productImage.loadImage("https://avatars2.githubusercontent.com/u/11229830?s=88&v=4")
        productName.text = "The Unbranded Brand - UB 16oz SL bla bla and bla and bla vbla bla and bla and bla bla bla and bla and bla"
        productPrice.text = "The Unbranded Brand - UB 16oz SL bla bla and bla and bla vbla bla and bla and bla bla bla and bla and bla "
        productShop.text = "Dijual oleh Denim House"
        commission.text = "Rp10.000"
        clickNumber.text = "123"
        buyNumber.text = "696"
        commissionTotal.text = "Rp100.000.100.000"
    }
}