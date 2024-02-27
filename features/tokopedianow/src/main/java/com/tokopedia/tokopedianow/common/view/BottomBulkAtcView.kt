package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.tokopedianow.R
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

    fun bind(
        counter: Int,
        priceInt: Double
    ) {
        binding.apply {
            tpCounter.text = context.getString(R.string.tokopedianow_shopping_list_bulk_atc_counter_text, counter)
            tpPrice.text = if (counter.isZero()) context.getString(R.string.tokopedianow_shopping_list_bulk_atc_empty_price_text) else priceInt.getCurrencyFormatted()
        }
    }

    fun onAtcClickListener(onClickListener: OnClickListener) {
        binding.ubAtc.setOnClickListener(onClickListener)
    }
}
