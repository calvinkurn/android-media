package com.tokopedia.tkpd.flashsale.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsBottomsheetProductCheckBinding
import com.tokopedia.tkpd.flashsale.domain.entity.ProductCheckingResult
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter.ProductCheckingResultAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ProductCheckBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<StfsBottomsheetProductCheckBinding>()
    private var productName: String = ""
    private var productCheckingResults: List<ProductCheckingResult> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        applyUnifyBackgroundColor()
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = StfsBottomsheetProductCheckBinding.inflate(inflater, container, false)
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(getString(R.string.commonbs_product_check_title))
        binding?.tfProductName?.text = productName
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding?.rvResult?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ProductCheckingResultAdapter().apply {
                setDataList(productCheckingResults)
            }
        }
    }

    fun setProductName(name: String) {
        this.productName = name
    }

    fun show(
        productCheckingResults: List<ProductCheckingResult>,
        manager: FragmentManager,
        tag: String?
    ) {
        this.productCheckingResults = productCheckingResults
        show(manager, tag)
    }
}
