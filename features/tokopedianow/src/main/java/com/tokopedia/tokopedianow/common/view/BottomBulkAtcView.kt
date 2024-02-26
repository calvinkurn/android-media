package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowBottomBulkAtcViewBinding

class BottomBulkAtcView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var binding: LayoutTokopedianowBottomBulkAtcViewBinding = LayoutTokopedianowBottomBulkAtcViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        bind(
            10,
            1000.0
        ) {

        }
    }

    fun bind(
        counter: Int,
        price: Double,
        onAtcClickListener: OnClickListener
    ) {
        binding.ubAtc.setOnClickListener(onAtcClickListener)
        binding.tpPrice.text = price.getCurrencyFormatted()
        binding.tpCounter.text = "$counter produk terpilih"
    }
}
