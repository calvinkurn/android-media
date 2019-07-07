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
import com.tokopedia.topupbills.telco.view.adapter.DigitalProductGridDecorator

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
        val view = View.inflate(context, R.layout.view_digital_product_list, this)
        recyclerView = view.findViewById(R.id.recycler_view)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun renderProductList(productType: Int, productList: List<TelcoProductDataCollection>,
                          selectedProductPos: Int) {
        adapter = DigitalProductAdapter(productList, productType)
        adapter.setListener(object : DigitalProductAdapter.ActionListener {
            override fun onClickItemProduct(itemProduct: TelcoProductDataCollection) {
                listener.onClickProduct(itemProduct)
            }

            override fun onClickSeeMoreProduct(itemProduct: TelcoProductDataCollection) {
                listener.onSeeMoreProduct(itemProduct)

            }
        })
        recyclerView.adapter = adapter
        if (productType == TelcoProductType.PRODUCT_GRID) {
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.addItemDecoration(DigitalProductGridDecorator(CELL_MARGIN, resources))
        } else {
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        adapter.notifyDataSetChanged()
        recyclerView.layoutManager?.run {
            this.scrollToPosition(selectedProductPos)
        }
    }

    fun notifyProductItemChanges(productId: String) {
        if (::adapter.isInitialized) {
            adapter.resetProductListSelected(productId)
        }
    }

    interface ActionListener {
        fun onClickProduct(itemProduct: TelcoProductDataCollection)
        fun onSeeMoreProduct(itemProduct: TelcoProductDataCollection)
    }

    companion object {
        const val CELL_MARGIN: Int = 8
    }

}