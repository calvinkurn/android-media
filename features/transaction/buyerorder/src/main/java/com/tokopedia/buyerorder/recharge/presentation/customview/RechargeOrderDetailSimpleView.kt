package com.tokopedia.buyerorder.recharge.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.buyerorder.databinding.LayoutOrderDetailRechargeProductDetailItemBinding
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailSimpleModel
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeSimpleAlignment
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by furqan on 24/10/2021
 */
class RechargeOrderDetailSimpleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: LayoutOrderDetailRechargeProductDetailItemBinding =
            LayoutOrderDetailRechargeProductDetailItemBinding.inflate(
                    LayoutInflater.from(context),
                    this,
                    true
            )

    fun setData(data: RechargeOrderDetailSimpleModel) {
        with(binding) {
            tgRechargeOrderDetailProductLabel.text = data.label
            tgRechargeOrderDetailProduct.text = data.detail
            if (data.isTitleBold) {
                tgRechargeOrderDetailProductLabel.weightType = Typography.BOLD
            } else {
                tgRechargeOrderDetailProductLabel.weightType = Typography.REGULAR
            }
            if (data.isDetailBold) {
                tgRechargeOrderDetailProduct.weightType = Typography.BOLD
            } else {
                tgRechargeOrderDetailProduct.weightType = Typography.REGULAR
            }
            when (data.alignment) {
                RechargeSimpleAlignment.LEFT -> {
                    tgRechargeOrderDetailProduct.gravity = Gravity.START
                }
                RechargeSimpleAlignment.RIGHT -> {
                    tgRechargeOrderDetailProduct.gravity = Gravity.END
                }
            }
            if (data.isCopyable) {
                icRechargeSimpleCopy.visibility = View.VISIBLE
            } else {
                icRechargeSimpleCopy.visibility = View.GONE
            }
            root.visibility = View.VISIBLE
        }
    }

    fun setCopyListener(action: () -> Unit) {
        binding.icRechargeSimpleCopy.setOnClickListener {
            action()
        }
    }

}