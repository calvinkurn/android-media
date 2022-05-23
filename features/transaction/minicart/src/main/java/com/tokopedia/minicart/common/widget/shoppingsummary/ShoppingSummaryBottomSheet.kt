package com.tokopedia.minicart.common.widget.shoppingsummary

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.widget.shoppingsummary.adapter.ShoppingSummaryAdapter
import com.tokopedia.minicart.common.widget.shoppingsummary.adapter.ShoppingSummaryAdapterTypeFactory
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryHeaderUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryProductUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummarySeparatorUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryTotalTransactionUiModel
import com.tokopedia.minicart.databinding.LayoutBottomsheetShoppingSummaryBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class ShoppingSummaryBottomSheet @Inject constructor() {

    companion object {
        private const val FRAGMENT_GENERAL_SUMMARY_TRANSACTION_TAG = "Mini Cart Summary Transaction"
    }

    private var adapter: ShoppingSummaryAdapter? = null
    private var bottomSheet: BottomSheetUnify? = null

    fun show(fragmentManager: FragmentManager, context: Context) {
        bottomSheet = BottomSheetUnify().apply {
            showCloseIcon = true
            showHeader = true
            clearContentPadding = true
            setTitle(context.getString(R.string.mini_cart_title_summary_transaction))
        }

        val viewBinding =
            LayoutBottomsheetShoppingSummaryBinding.inflate(LayoutInflater.from(context))
        initializeRecyclerView(viewBinding, getShoppingSummaryList())

        bottomSheet?.setChild(viewBinding.root)
        bottomSheet?.show(fragmentManager, FRAGMENT_GENERAL_SUMMARY_TRANSACTION_TAG)
    }

    private fun initializeRecyclerView(viewBinding: LayoutBottomsheetShoppingSummaryBinding, data: ArrayList<Visitable<*>>) {
        with(viewBinding) {
            val adapterTypeFactory = ShoppingSummaryAdapterTypeFactory()
            adapter = ShoppingSummaryAdapter(data, adapterTypeFactory)
            rvShoppingSummaryList.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            rvShoppingSummaryList.adapter = adapter
        }
    }

    private fun getShoppingSummaryList(): ArrayList<Visitable<*>> {
        val dummyItems = arrayListOf<Visitable<*>>()
        dummyItems.add(ShoppingSummaryHeaderUiModel("", "Shop Store", "Jakarta Selatan"))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Sepatu Basket Nike Air Jordan", "Rp5.500.000.000"))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Topi Baseball Snapback - Hitam - XL", "Rp500.000"))
        dummyItems.add(ShoppingSummarySeparatorUiModel())

        dummyItems.add(ShoppingSummaryHeaderUiModel("", "Shop Store", "Kab. Bandung"))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Sepatu Basket Nike Air Jordan", "Rp5.500.000.000"))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Topi Baseball Snapback - Hitam - XL", "Rp500.000"))
        dummyItems.add(ShoppingSummarySeparatorUiModel())

        dummyItems.add(ShoppingSummaryHeaderUiModel("", "Shop Store", ""))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Sepatu Basket Nike Air Jordan", "Rp5.500.000.000"))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Topi Baseball Snapback - Hitam - XL - Testing tesitjsiejisfdjaifd adfsmasdf", "Rp500.000"))
        dummyItems.add(ShoppingSummarySeparatorUiModel())

        dummyItems.add(ShoppingSummaryHeaderUiModel("", "Shop Store", ""))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Sepatu Basket Nike Air Jordan", "Rp5.500.000.000"))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Topi Baseball Snapback - Hitam - XL - Testing tesitjsiejisfdjaifd adfsmasdf", "Rp500.000"))
        dummyItems.add(ShoppingSummarySeparatorUiModel())

        dummyItems.add(ShoppingSummaryHeaderUiModel("", "Shop Store", ""))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Sepatu Basket Nike Air Jordan", "Rp5.500.000.000"))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Topi Baseball Snapback - Hitam - XL - Testing tesitjsiejisfdjaifd adfsmasdf", "Rp500.000"))
        dummyItems.add(ShoppingSummarySeparatorUiModel())

        dummyItems.add(ShoppingSummaryHeaderUiModel("", "Shop Store", ""))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Sepatu Basket Nike Air Jordan", "Rp5.500.000.000"))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Topi Baseball Snapback - Hitam - XL - Testing tesitjsiejisfdjaifd adfsmasdf", "Rp500.000"))
        dummyItems.add(ShoppingSummarySeparatorUiModel())

        dummyItems.add(ShoppingSummaryHeaderUiModel("", "Shop Store", ""))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Sepatu Basket Nike Air Jordan", "Rp5.500.000.000"))
        dummyItems.add(ShoppingSummaryProductUiModel("1 x Topi Baseball Snapback - Hitam - XL - Testing tesitjsiejisfdjaifd adfsmasdf", "Rp500.000"))
        dummyItems.add(ShoppingSummarySeparatorUiModel())

        dummyItems.add(ShoppingSummaryTotalTransactionUiModel("Total Harga (12 Barang)", "<b>Rp5.500.000.000</b>"))

        return dummyItems
    }
}