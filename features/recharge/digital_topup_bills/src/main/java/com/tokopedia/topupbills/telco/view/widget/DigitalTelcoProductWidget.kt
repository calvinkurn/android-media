package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.view.adapter.DigitalProductAdapter
import com.tokopedia.topupbills.telco.view.adapter.DigitalProductGridDecorator
import com.tokopedia.topupbills.telco.view.model.DigitalTrackProductTelco

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoProductWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                          defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private lateinit var adapter: DigitalProductAdapter
    private lateinit var listener: ActionListener
    private val digitalTrackTelcoPrev = mutableListOf<DigitalTrackProductTelco>()

    init {
        val view = View.inflate(context, R.layout.view_digital_product_list, this)
        recyclerView = view.findViewById(R.id.recycler_view)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun renderProductList(productType: Int, productList: List<TelcoProduct>,
                          selectedProductPos: Int) {
        adapter = DigitalProductAdapter(productList, productType)
        adapter.setListener(object : DigitalProductAdapter.ActionListener {
            override fun onClickItemProduct(itemProduct: TelcoProduct, position: Int) {
                listener.onClickProduct(itemProduct, position)
            }

            override fun onClickSeeMoreProduct(itemProduct: TelcoProduct) {
                listener.onSeeMoreProduct(itemProduct)
            }

            override fun notifyItemChanged(position: Int) {
                adapter.notifyItemChanged(position)
            }
        })
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
        if (productType == TelcoProductType.PRODUCT_GRID) {
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.addItemDecoration(DigitalProductGridDecorator(CELL_MARGIN_DP, resources))
        } else {
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        adapter.notifyDataSetChanged()
        recyclerView.layoutManager?.run {
            if (selectedProductPos > 0) {
                this.scrollToPosition(selectedProductPos)
            }
        }
        getVisibleProductItemsToUsersTracking(productList)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getVisibleProductItemsToUsersTracking(productList)
                }
            }
        })
    }

    fun getVisibleProductItemsToUsersTracking(productList: List<TelcoProduct>) {
        var firstPos = 0
        var lastPos = 0
        if (recyclerView.layoutManager is LinearLayoutManager) {
            firstPos = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            lastPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
        } else if (recyclerView.layoutManager is GridLayoutManager) {
            firstPos = (recyclerView.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
            lastPos = (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
        }


        val digitalTrackProductTelcoList = mutableListOf<DigitalTrackProductTelco>()
        for (i in firstPos..lastPos) {
            if (firstPos >= 0 && lastPos <= productList.size - 1) {
                digitalTrackProductTelcoList.add(DigitalTrackProductTelco(productList[i], i))
            }
        }
        if (digitalTrackProductTelcoList.size > 0 &&
                digitalTrackProductTelcoList.size != digitalTrackTelcoPrev.size &&
                digitalTrackProductTelcoList != digitalTrackTelcoPrev) {
            listener.onTrackImpressionProductsList(digitalTrackProductTelcoList)

            digitalTrackTelcoPrev.clear()
            digitalTrackTelcoPrev.addAll(digitalTrackProductTelcoList)
        }
    }

    fun selectProductItem(itemProduct: TelcoProduct) {
        adapter.selectItemProduct(itemProduct)
    }

    fun notifyProductItemChanges(productId: String) {
        if (::adapter.isInitialized) {
            adapter.resetProductListSelected(productId)
        }
    }

    interface ActionListener {
        fun onClickProduct(itemProduct: TelcoProduct, position: Int)
        fun onSeeMoreProduct(itemProduct: TelcoProduct)
        fun onTrackImpressionProductsList(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>)
    }

    companion object {
        const val CELL_MARGIN_DP: Int = 4
    }

}