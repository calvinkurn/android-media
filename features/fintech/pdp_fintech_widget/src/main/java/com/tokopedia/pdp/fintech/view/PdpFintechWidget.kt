package com.tokopedia.pdp.fintech.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp_fintech.R
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.unifycomponents.BaseCustomView

class PdpFintechWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
   val  variantInterface:AtcVariantListener,
) : BaseCustomView(context, attrs, defStyleAttr), ProductUpdateListner {

    private lateinit var baseView: View


    init {
        initView()
        initRecycler()
    }

    private fun initRecycler() {
        val recyclerView = baseView.findViewById<RecyclerView>(R.id.recycler_items)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = FintechWidgetAdapter()
    }

    private fun initView() {
        baseView = inflate(context, R.layout.pdp_fintech_widget_layout, this)
    }

    override fun updateProduct(productID: String) {

    }


}