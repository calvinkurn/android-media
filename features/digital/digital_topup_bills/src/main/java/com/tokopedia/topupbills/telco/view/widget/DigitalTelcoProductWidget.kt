package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoProductDataCollection
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.view.adapter.DigitalProductAdapter

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoProductWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                          defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private lateinit var adapter: DigitalProductAdapter
    private lateinit var listener: ActionListener

    init {
        val view = View.inflate(context, R.layout.view_digital_component_list, this)
        recyclerView = view.findViewById(R.id.recycler_view)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun renderGridProductList(productType: Int, productList: List<TelcoProductDataCollection>) {
        adapter = DigitalProductAdapter(productList, productType)
        adapter.setListener(object: DigitalProductAdapter.ActionListener {
            override fun onClickItemProduct(itemProduct: TelcoProductDataCollection) {
                listener.onClickProduct(itemProduct)
            }

            override fun onClickSeeMoreProduct(itemProduct: TelcoProductDataCollection) {

            }
        })
        recyclerView.adapter = adapter
        if (productType == TelcoProductType.PRODUCT_GRID) {
            recyclerView.layoutManager = GridLayoutManager(context, 2)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        adapter.notifyDataSetChanged()
    }

    fun notifyProductItemChanges(productId: String) {
        if (::adapter.isInitialized) {
            adapter.resetProductListSelected(productId)
        }
    }

    interface ActionListener {
        fun onClickProduct(itemProduct: TelcoProductDataCollection)
    }

}