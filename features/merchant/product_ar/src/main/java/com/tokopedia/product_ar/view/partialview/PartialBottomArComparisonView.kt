package com.tokopedia.product_ar.view.partialview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.util.SELECTMODE
import com.tokopedia.product_ar.view.ProductArListener
import com.tokopedia.product_ar.view.adapter.VariantArAdapter
import com.tokopedia.unifycomponents.UnifyButton

class PartialBottomArComparisonView private constructor(val view: View, val listener: ProductArListener) {

    companion object {
        fun build(view: View, listener: ProductArListener) = PartialBottomArComparisonView(view, listener)
    }

    private val btnSaveImage = view.findViewById<UnifyButton>(R.id.btn_atc_ar_comparison)
    private val rvVariant = view.findViewById<RecyclerView>(R.id.rv_ar_comparison_variant)
    val adapter = VariantArAdapter(listener, SELECTMODE.MULTIPLE)

    init {
        rvVariant?.adapter = adapter
        btnSaveImage?.setOnClickListener {
            startButtonLoading()
            listener.onButtonClicked("")
        }
    }

    fun startButtonLoading() {
        btnSaveImage?.setOnClickListener {

        }
        btnSaveImage?.isLoading = true
    }

    fun stopButtonLoading() {
        btnSaveImage?.setOnClickListener {
            startButtonLoading()
            listener.onButtonClicked("")
        }
        btnSaveImage?.isLoading = false
    }

    fun renderView(data: List<ModifaceUiModel>) {
        adapter.updateList(data)
    }
}