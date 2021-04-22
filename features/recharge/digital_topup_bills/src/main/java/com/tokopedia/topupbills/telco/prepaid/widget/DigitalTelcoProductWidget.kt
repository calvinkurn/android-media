package com.tokopedia.topupbills.telco.prepaid.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoCatalogDataCollection
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.prepaid.adapter.DigitalProductGridDecorator
import com.tokopedia.topupbills.telco.prepaid.adapter.TelcoProductAdapter
import com.tokopedia.topupbills.telco.prepaid.adapter.TelcoProductAdapterFactory
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder
import com.tokopedia.topupbills.telco.prepaid.model.DigitalTrackProductTelco


/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoProductWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                          defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView

    private lateinit var adapter: TelcoProductAdapter
    private lateinit var listener: ActionListener

    init {
        val view = View.inflate(context, R.layout.view_telco_product_list, this)
        recyclerView = view.findViewById(R.id.telco_product_rv)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun renderProductList(productType: Int, showTitle: Boolean, productList: List<TelcoCatalogDataCollection>) {
        val dataCollection = mutableListOf<Visitable<*>>()
        val dataTracking = mutableListOf<TelcoProduct>()
        productList.map {
            if (showTitle) {
                if (it.name.isNotEmpty()) {
                    dataCollection.add(TelcoCatalogDataCollection(it.name, listOf()))
                } else {
                    dataCollection.add(TelcoCatalogDataCollection(context.getString(R.string.telco_other_recommendation), listOf()))
                }
            }
            dataCollection.addAll(it.products)
            dataTracking.addAll(it.products)
        }

        adapter = TelcoProductAdapter(context, TelcoProductAdapterFactory(productType, object : TelcoProductViewHolder.OnClickListener {
            override fun onClickItemProduct(element: TelcoProduct, position: Int) {
                val label = getLabelProductItem(element.id)
                listener.onClickProduct(element, position, label)
            }

            override fun onClickSeeMoreProduct(element: TelcoProduct, position: Int) {
                listener.onSeeMoreProduct(element, position)
            }
        }))
        recyclerView.adapter = adapter
        if (productType == TelcoProductType.PRODUCT_GRID) {
            recyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
            if (recyclerView.itemDecorationCount == 0) {
                recyclerView.addItemDecoration(DigitalProductGridDecorator(CELL_MARGIN_DP, resources))
            }
        } else {
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = linearLayoutManager
        }
        adapter.renderList(dataCollection)

        getVisibleProductItemsToUsersTracking(DEFAULT_POS_FIRST_ITEM, DEFAULT_POS_LAST_ITEM, dataTracking)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    calculateProductItemVisibleItemTracking(dataTracking)
                }
            }
        })
    }

    fun calculateProductItemVisibleItemTracking(productList: List<TelcoProduct>) {
        var firstPos = 0
        var lastPos = 0
        if (recyclerView.layoutManager is LinearLayoutManager) {
            firstPos = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            lastPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
        } else if (recyclerView.layoutManager is GridLayoutManager) {
            firstPos = (recyclerView.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
            lastPos = (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
        }
        getVisibleProductItemsToUsersTracking(firstPos, lastPos, productList)
    }

    private fun getVisibleProductItemsToUsersTracking(firstPos: Int, lastPos: Int, productList: List<TelcoProduct>) {
        val digitalTrackProductTelcoList = mutableListOf<DigitalTrackProductTelco>()
        for (i in firstPos..lastPos) {
            if (firstPos >= 0 && lastPos <= productList.size - 1) {
                digitalTrackProductTelcoList.add(DigitalTrackProductTelco(productList[i], i))
            }
        }
        if (digitalTrackProductTelcoList.size > 0) {
            listener.onTrackImpressionProductsList(digitalTrackProductTelcoList)
        }
    }

    fun selectProductItem(position: Int) {
        adapter.selectItemProduct(position)
    }

    fun resetSelectedProductItem() {
        if (::adapter.isInitialized) {
            adapter.selectItemProduct(TelcoProductAdapter.INIT_SELECTED_POSITION)
        }
    }

    fun getLabelProductItem(productId: String): String {
        if (!::adapter.isInitialized) return ""

        var label = ""
        var itemTitle = ""

        for (i in adapter.data.indices) {
            if (adapter.data[i] is TelcoCatalogDataCollection) {
                itemTitle = (adapter.data[i] as TelcoCatalogDataCollection).name
            }

            if (adapter.data[i] is TelcoProduct) {
                val itemProduct = adapter.data[i] as TelcoProduct
                if (itemProduct.id == productId) {
                    label = itemTitle
                    break
                }
            }
        }
        return label
    }

    fun selectProductFromFavNumber(productId: String) {
        val label = getLabelProductItem(productId)
        if (!::adapter.isInitialized) return
        for (i in adapter.data.indices) {
            if (adapter.data[i] is TelcoProduct) {
                val itemProduct = adapter.data[i] as TelcoProduct
                if (itemProduct.id == productId) {
                    listener.onClickProduct(itemProduct, i, label)

                    scrollToPosition(i)
                    break
                }
            }
        }
    }

    fun scrollToPosition(position: Int) {
        recyclerView.scrollToPosition(position)
    }

    interface ActionListener {
        fun onClickProduct(itemProduct: TelcoProduct, position: Int, labelList: String)
        fun onSeeMoreProduct(itemProduct: TelcoProduct, position: Int)
        fun onTrackImpressionProductsList(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>)
        fun onScrollToPositionItem(position: Int)
    }

    companion object {
        const val CELL_MARGIN_DP: Int = 2

        //add default pos first and last to fire tracking visible item for first time
        const val DEFAULT_POS_FIRST_ITEM = 0
        const val DEFAULT_POS_LAST_ITEM = 3
    }

}