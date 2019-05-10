package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.view.model.DigitalProductTelco

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalProductGridWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                         defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private val productList = mutableListOf<DigitalProductTelco>();
    private val adapter: com.tokopedia.topupbills.telco.view.adapter.DigitalProductGridAdapter

    init {
        val view = View.inflate(context, R.layout.view_digital_component_list, this)
        recyclerView = view.findViewById(R.id.recycler_view)

        adapter = com.tokopedia.topupbills.telco.view.adapter.DigitalProductGridAdapter(productList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)

    }

    fun renderGridProductList(productList: List<DigitalProductTelco>) {
        this.productList.addAll(productList)
        adapter.notifyDataSetChanged()
    }

}