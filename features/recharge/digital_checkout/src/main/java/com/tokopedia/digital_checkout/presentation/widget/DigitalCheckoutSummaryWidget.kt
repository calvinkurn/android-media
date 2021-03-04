package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.presentation.adapter.DigitalCheckoutSummaryAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class DigitalCheckoutSummaryWidget @JvmOverloads constructor(@NotNull context: Context,
                                                         attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rvCheckoutSummary: RecyclerView
    private var mAdapter: DigitalCheckoutSummaryAdapter

    var actionListener: ActionListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.item_digital_checkout_summary_view, this, true)
        mAdapter = DigitalCheckoutSummaryAdapter()
        rvCheckoutSummary = findViewById(R.id.rvCheckoutSummary)
        rvCheckoutSummary.run {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        addItem()
        removeItem()
    }

    fun addItem() {
//         TODO : change function name and add functionality
        mAdapter.addItem(DigitalCheckoutSummaryAdapter.Payment("A", 12000), 0)
        mAdapter.addItem(DigitalCheckoutSummaryAdapter.Payment("B", 12000), 1)
        mAdapter.addItem(DigitalCheckoutSummaryAdapter.Payment("C", 12000), 2)
        mAdapter.addItem(DigitalCheckoutSummaryAdapter.Payment("D", 12000), 5)
        actionListener?.updateTotalPayment(calculateTotalPayment())
    }

    fun removeItem() {
//         TODO : change function name and add functionality
        mAdapter.removeItem("A")
        actionListener?.updateTotalPayment(calculateTotalPayment())
    }

    private fun calculateTotalPayment(): Int {
        val payments = mAdapter.getAllItems()
        var totalPayment = 0
        payments.forEach { totalPayment += it.priceAmount }
        return totalPayment
    }

    interface ActionListener {
        fun updateTotalPayment(totalPayment: Int)
    }
}
