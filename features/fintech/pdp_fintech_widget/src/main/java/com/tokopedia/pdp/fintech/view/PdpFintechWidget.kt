package com.tokopedia.pdp.fintech.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp_fintech.R
import com.tokopedia.unifycomponents.BaseCustomView

class PdpFintechWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
) : BaseCustomView(context, attrs, defStyleAttr) {

    private lateinit var baseView: View
    private  var fintechWidgetAdapter = FintechWidgetAdapter()
    var i =0;


    init {
        initView()
        initRecycler()
    }

    private fun initRecycler() {
        val recyclerView = baseView.findViewById<RecyclerView>(R.id.recycler_items)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = fintechWidgetAdapter
    }

    private fun initView() {
        baseView = inflate(context, R.layout.pdp_fintech_widget_layout, this)
    }

     fun updateProductId(
         productID: String,
         fintechWidgetViewHolder: ProductUpdateListner
     ) {
        if(i == 2)
        {
            fintechWidgetViewHolder.removeWidget()
            i++
        }
         else
        {
            fintechWidgetViewHolder.showWidget()
            i++
            fintechWidgetAdapter.notifyDataSetChanged()
        }
    }

    fun sendLifeCycle(pdpLifeCycle:Lifecycle)
    {

    }




}