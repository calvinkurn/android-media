package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.databinding.BottomSheetProductDescBinding
import com.tokopedia.digital_product_detail.presentation.adapter.ProductDescAdapter
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ProductDescBottomSheet(
    private val denomData: DenomData,
    private val listener: RechargeBuyWidgetListener
): BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
    }

    private var binding by autoClearedNullable<BottomSheetProductDescBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        binding = BottomSheetProductDescBinding.inflate(LayoutInflater.from(context))
        binding?.run {
            rvProductDesc.run {
                val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation).apply {
                    setUsePaddingLeft(false)
                }
                adapter = ProductDescAdapter(denomData.productDescriptions)
                layoutManager = linearLayoutManager
                addItemDecoration(dividerItemDecoration)
            }
            // [Misael] TODO: use custom attributes for ticker content
            tickerWidgetProductDesc.setText("Dummy")
            buyWidgetProductDesc.showBuyWidget(denomData, listener)
        }
        setTitle(getString(R.string.bottom_sheet_prod_desc_title))
        setChild(binding?.root)
    }
}