package com.tokopedia.pdp.fintech.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.AttrRes
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter
import com.tokopedia.pdp.fintech.di.components.DaggerFintechWidgetComponent
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp.fintech.listner.WidgetClickListner
import com.tokopedia.pdp_fintech.R
import com.tokopedia.unifycomponents.BaseCustomView

class PdpFintechWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
) : BaseCustomView(context, attrs, defStyleAttr) {

    private lateinit var baseView: View
    private lateinit var fintechWidgetAdapter : FintechWidgetAdapter
    var i =0;
    private lateinit var instanceProductUpdateListner: ProductUpdateListner


    init {
        initInjector()
        initView()
        initRecycler()
    }

    private fun initInjector() {
        DaggerFintechWidgetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun initRecycler() {
        val recyclerView = baseView.findViewById<RecyclerView>(R.id.recycler_items)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        fintechWidgetAdapter = FintechWidgetAdapter(object :WidgetClickListner{
            override fun clickedWidget(position: Int) {
                if(position == 0)
                    instanceProductUpdateListner.showWebview()
                else
                    instanceProductUpdateListner.showBottomSheet(position)
            }



        })
        recyclerView.adapter = fintechWidgetAdapter
    }

    private fun initView() {
        baseView = inflate(context, R.layout.pdp_fintech_widget_layout, this)
    }

     fun updateProductId(
         productID: String,
         fintechWidgetViewHolder: ProductUpdateListner
     ) {
         this.instanceProductUpdateListner = fintechWidgetViewHolder
        if(i == 2)
        {
            fintechWidgetViewHolder.removeWidget()
            i++
        }
         else
        {
            fintechWidgetViewHolder.showWidget()
//            i++
            fintechWidgetAdapter.notifyDataSetChanged()
        }
    }

    fun sendLifeCycle(pdpLifeCycle:Lifecycle)
    {

    }




}