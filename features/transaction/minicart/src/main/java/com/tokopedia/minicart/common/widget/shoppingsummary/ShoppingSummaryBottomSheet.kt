package com.tokopedia.minicart.common.widget.shoppingsummary

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.domain.data.ShoppingSummaryBottomSheetData
import com.tokopedia.minicart.common.widget.shoppingsummary.adapter.ShoppingSummaryAdapter
import com.tokopedia.minicart.common.widget.shoppingsummary.adapter.ShoppingSummaryAdapterTypeFactory
import com.tokopedia.minicart.databinding.LayoutBottomsheetShoppingSummaryBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class ShoppingSummaryBottomSheet @Inject constructor() {

    companion object {
        private const val FRAGMENT_GENERAL_SUMMARY_TRANSACTION_TAG = "Mini Cart Summary Transaction"
    }

    private var adapter: ShoppingSummaryAdapter? = null
    private var bottomSheet: BottomSheetUnify? = null

    fun show(data: ShoppingSummaryBottomSheetData, fragmentManager: FragmentManager, context: Context?) {
        context?.let {
            bottomSheet = BottomSheetUnify().apply {
                showCloseIcon = true
                showHeader = true
                clearContentPadding = true
                setTitle(
                    data.title.ifBlank {
                        context.getString(R.string.mini_cart_widget_label_purchase_summary)
                    }
                )
            }

            val viewBinding =
                LayoutBottomsheetShoppingSummaryBinding.inflate(LayoutInflater.from(context))
            initializeRecyclerView(viewBinding, data.items)

            bottomSheet?.setChild(viewBinding.root)
            bottomSheet?.show(fragmentManager, FRAGMENT_GENERAL_SUMMARY_TRANSACTION_TAG)
        }
    }

    private fun initializeRecyclerView(viewBinding: LayoutBottomsheetShoppingSummaryBinding, data: List<Visitable<*>>) {
        with(viewBinding) {
            val adapterTypeFactory = ShoppingSummaryAdapterTypeFactory()
            adapter = ShoppingSummaryAdapter(data, adapterTypeFactory)
            rvShoppingSummaryList.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            rvShoppingSummaryList.adapter = adapter
        }
    }

    fun dismiss() {
        bottomSheet?.dismiss()
    }
}
