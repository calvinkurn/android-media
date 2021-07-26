package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.data.PaymentSummary
import com.tokopedia.digital_checkout.presentation.adapter.DigitalCheckoutSummaryAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class DigitalCheckoutSummaryWidget @JvmOverloads constructor(@NotNull context: Context,
                                                         attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rvCheckoutSummary: RecyclerView
    private var mAdapter: DigitalCheckoutSummaryAdapter

    init {
        LayoutInflater.from(context).inflate(R.layout.item_digital_checkout_summary_view, this, true)
        mAdapter = DigitalCheckoutSummaryAdapter()
        rvCheckoutSummary = findViewById(R.id.rvCheckoutSummary)
        rvCheckoutSummary.run {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun setSummaries(paymentSummary: PaymentSummary) {
        mAdapter.setSummaries(paymentSummary)
    }
}
