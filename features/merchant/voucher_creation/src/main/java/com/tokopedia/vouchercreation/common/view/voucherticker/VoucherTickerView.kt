package com.tokopedia.vouchercreation.common.view.voucherticker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.vouchercreation.databinding.ViewMvcVoucherTickerBinding

/**
 * Created By @ilhamsuaib on 10/05/20
 */

class VoucherTickerView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    val binding: ViewMvcVoucherTickerBinding = ViewMvcVoucherTickerBinding.inflate(LayoutInflater.from(context), this, true)

    var title: String = ""
        set(value) {
            binding.tvMvcTickerTitle.text = value
            field = value
        }

    var description: String = ""
        set(value) {
            binding.tvMvcTickerDescription.text = value
            field = value
        }

    var nominal: String = ""
        set(value) {
            binding.tvMvcExpense.text = value
            field = value
        }

    fun setOnTooltipClick(callback: () -> Unit) {
        binding.mvcImgInfo.visible()
        binding.mvcImgInfo.setOnClickListener {
            callback()
        }
    }
}