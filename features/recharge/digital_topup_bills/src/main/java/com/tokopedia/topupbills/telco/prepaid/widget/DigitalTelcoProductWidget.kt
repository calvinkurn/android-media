package com.tokopedia.topupbills.telco.prepaid.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoAttributesProduct
import com.tokopedia.topupbills.telco.data.TelcoCatalogDataCollection
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.prepaid.adapter.TelcoProductAdapter
import com.tokopedia.topupbills.telco.prepaid.adapter.TelcoProductAdapterFactory
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductMccmListViewHolder
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder
import com.tokopedia.topupbills.telco.prepaid.model.DigitalTrackProductTelco


/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoProductWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                          defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView

    lateinit var adapter: TelcoProductAdapter
    private lateinit var listener: ActionListener
    private var hasMccmProduct = false

    init {
        val view = View.inflate(context, R.layout.view_telco_product_list, this)
        recyclerView = view.findViewById(R.id.telco_product_rv)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun renderProductList(productType: Int, showTitle: Boolean, productList: List<TelcoCatalogDataCollection>,
                          isTabSelected: Boolean) {

        hasMccmProduct = false

        val dataCollection = mutableListOf<Visitable<*>>()
        dataCollection.addAll(productList.filter { it.isMccm() })
        if (dataCollection.isNotEmpty()) {
            hasMccmProduct = true
        }

        productList.map {
            if (!it.isMccm()) {
                if (showTitle) {
                    if (it.name.isNotEmpty()) {
                        dataCollection.add(TelcoCatalogDataCollection(it.name, listOf()))
                    } else {
                        if (productType == TelcoProductType.PRODUCT_GRID) {
                            dataCollection.add(TelcoCatalogDataCollection(context.getString(R.string.telco_choose_denom), listOf()))
                        } else {
                            dataCollection.add(TelcoCatalogDataCollection(context.getString(R.string.telco_other_recommendation), listOf()))
                        }
                    }
                }
                dataCollection.addAll(it.products)
            }
        }

        assignPositionToDataCollection(dataCollection)

        adapter = TelcoProductAdapter(context, TelcoProductAdapterFactory(productType, getProductListener(), getMccmListener()))
        recyclerView.adapter = adapter

        if (productType == TelcoProductType.PRODUCT_GRID) {
            val gridLayout = GridLayoutManager(context, PRODUCT_GRID_SPAN_LAYOUT, RecyclerView.VERTICAL, false)
            gridLayout.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val singleGridThreshold = if (hasMccmProduct) SINGLE_GRID_THRESHOLD_1 else SINGLE_GRID_THRESHOLD_0
                    return if (position <= singleGridThreshold) PRODUCT_GRID_SPAN_LAYOUT else SINGLE_PRODUCT_GRID_SPAN_LAYOUT
                }
            }
            recyclerView.layoutManager = gridLayout
        } else {
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = linearLayoutManager
        }

        adapter.renderList(dataCollection)

        trackFirstVisibleItemToUser(dataCollection, isTabSelected)
        recyclerView.clearOnScrollListeners()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    calculateProductItemVisibleItemTracking(dataCollection)
                }
            }
        })
    }

    private fun trackFirstVisibleItemToUser(productList: List<Visitable<*>>, isTabSelected: Boolean) {
        recyclerView.viewTreeObserver
                .addOnGlobalLayoutListener(
                        object : ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                // At this point the layout is complete and the
                                // dimensions of recyclerView and any child views
                                // are known.
                                if (isTabSelected) {
                                    calculateProductItemVisibleItemTracking(productList)
                                }
                                recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            }
                        })
    }

    fun trackFirstMccmVisibleItemToUser() {
        if (hasMccmProduct) {
            recyclerView.findViewHolderForAdapterPosition(MCCM_WIDGET_POSITION)?.let {
                (it as TelcoProductMccmListViewHolder).trackCurrentVisibleMccmProduct()
            }
        }
    }

    private fun assignPositionToDataCollection(dataCollection: List<Visitable<*>>) {
        var productCount = 0
        for (data in dataCollection) {
            if (data is TelcoProduct) {
                data.productPosition = productCount
                productCount++
            }
        }
    }

    private fun getProductListener() = object : TelcoProductViewHolder.ActionListener {
        override fun onClickItemProduct(element: TelcoProduct, position: Int) {
            val label = getLabelProductItem(element.id)
            listener.onClickProduct(element, position, label)
            adapter.selectedMccmPosition = RecyclerView.NO_POSITION
        }

        override fun onClickSeeMoreProduct(element: TelcoProduct, position: Int) {
            listener.onSeeMoreProduct(element, position)
        }

        override fun onTrackSpecialProductImpression(itemProduct: TelcoProduct, position: Int) {
            listener.onTrackImpressionSpecialProduct(itemProduct, position)
        }
    }

    private fun getMccmListener() = object : TelcoProductMccmListViewHolder.OnClickListener {
        override fun onTrackImpressionMccmProductsList(productList: List<DigitalTrackProductTelco>) {
            listener.onTrackImpressionMccmProductsList(productList)
        }

        override fun onClickMccm(element: TelcoProduct, position: Int) {
            listener.onClickMccmProduct(element, position)
            adapter.selectedMccmPosition = position
        }

        override fun onClickLihatDetail(element: TelcoProduct, position: Int) {
            listener.onSeeMoreMccmProduct(element, position)
        }
    }

    fun calculateProductItemVisibleItemTracking(productList: List<Visitable<*>>) {
        var firstPos = 0
        var lastPos = 0
        if (recyclerView.layoutManager is GridLayoutManager) {
            firstPos = (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            lastPos = (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
        } else if (recyclerView.layoutManager is LinearLayoutManager) {
            firstPos = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            lastPos = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }
        getVisibleProductItemsToUsersTracking(firstPos, lastPos, productList)
    }

    private fun getVisibleProductItemsToUsersTracking(firstPos: Int, lastPos: Int, productList: List<Visitable<*>>) {
        val digitalTrackProductTelcoList = mutableListOf<DigitalTrackProductTelco>()

        for (i in firstPos..lastPos) {
            if (firstPos >= 0 && lastPos < productList.size) {
                val product = productList[i]
                if (product is TelcoProduct) {
                    digitalTrackProductTelcoList.add(DigitalTrackProductTelco(product, product.productPosition))
                }

            }
        }
        if (digitalTrackProductTelcoList.isNotEmpty()) {
            listener.onTrackImpressionProductsList(digitalTrackProductTelcoList)
        }
    }

    fun selectProductItem(position: Int) {
        adapter.selectItemProduct(position)
        if (hasMccmProduct) {
            recyclerView.findViewHolderForAdapterPosition(MCCM_WIDGET_POSITION)?.let {
                (it as TelcoProductMccmListViewHolder).adapter.selectItemProduct(RecyclerView.NO_POSITION)
            }
        }
    }

    fun selectMccmProductItem(position: Int) {
        adapter.selectItemProduct(RecyclerView.NO_POSITION)
        recyclerView.findViewHolderForAdapterPosition(MCCM_WIDGET_POSITION)?.let {
            (it as TelcoProductMccmListViewHolder).adapter.selectItemProduct(position)
        }
    }

    fun resetSelectedProductItem() {
        if (::adapter.isInitialized) {
            adapter.selectItemProduct(TelcoProductAdapter.INIT_SELECTED_POSITION)
        }
    }

    fun removePaddingTop(remove: Boolean) {
        if (remove) {
            recyclerView.setPadding(0, 0, 0, recyclerView.paddingBottom)
        } else {
            recyclerView.setPadding(0, resources.getDimensionPixelSize(com.tokopedia.unifycomponents.R.dimen.unify_space_12), 0, recyclerView.paddingBottom)
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

    fun selectProductById(productId: String) {
        val label = getLabelProductItem(productId)
        var isSelected = false
        if (!::adapter.isInitialized) return
        for (i in adapter.data.indices) {
            if (adapter.data[i] is TelcoProduct) {
                val itemProduct = adapter.data[i] as TelcoProduct
                if (itemProduct.id == productId) {
                    listener.onClickProduct(itemProduct, i, label)
                    isSelected = true

                    scrollToPosition(i)
                    break
                }
            }
        }

        if (!isSelected) {
            listener.onFailedAutoSelectById()
        }
    }

    fun scrollToPosition(position: Int) {
        recyclerView.scrollToPosition(position)
    }

    interface ActionListener {
        fun onClickProduct(itemProduct: TelcoProduct, position: Int, labelList: String)
        fun onClickMccmProduct(itemProduct: TelcoProduct, position: Int)
        fun onSeeMoreProduct(itemProduct: TelcoProduct, position: Int)
        fun onSeeMoreMccmProduct(itemProduct: TelcoProduct, position: Int)
        fun onTrackImpressionProductsList(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>)
        fun onTrackImpressionMccmProductsList(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>)
        fun onScrollToPositionItem(position: Int)
        fun onTrackImpressionSpecialProduct(itemProduct: TelcoProduct, position: Int)
        fun onFailedAutoSelectById()
    }

    companion object {
        const val MCCM_WIDGET_POSITION = 0

        const val PRODUCT_GRID_SPAN_LAYOUT = 2
        const val SINGLE_PRODUCT_GRID_SPAN_LAYOUT = 1
        
        const val SINGLE_GRID_THRESHOLD_1 = 1
        const val SINGLE_GRID_THRESHOLD_0 = 0
    }

}