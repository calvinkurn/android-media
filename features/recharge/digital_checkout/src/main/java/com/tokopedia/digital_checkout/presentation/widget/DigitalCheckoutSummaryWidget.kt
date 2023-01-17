package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.digital_checkout.data.PaymentSummary
import com.tokopedia.digital_checkout.databinding.ItemDigitalCheckoutSummaryViewBinding
import com.tokopedia.digital_checkout.presentation.adapter.DigitalCheckoutSummaryAdapter
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class DigitalCheckoutSummaryWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var mAdapter: DigitalCheckoutSummaryAdapter

    init {
        val binding = ItemDigitalCheckoutSummaryViewBinding.inflate(
            LayoutInflater.from(context),
            this, true
        )
        mAdapter = DigitalCheckoutSummaryAdapter()
        binding.rvCheckoutSummary.run {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun setSummaries(paymentSummary: PaymentSummary) {
        mAdapter.setSummaries(paymentSummary)
    }
}
