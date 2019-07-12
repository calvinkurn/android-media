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
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
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

    fun renderProductList(productType: Int, productList: List<TelcoProductDataCollection>,
                          selectedProductPos: Int) {
        adapter = DigitalProductAdapter(productList, productType)
        adapter.setListener(object : DigitalProductAdapter.ActionListener {
            override fun onClickItemProduct(itemProduct: TelcoProductDataCollection, position: Int) {
                listener.onClickProduct(itemProduct, position, getCategoryName(itemProduct.product.attributes.categoryId))
            }

            override fun onClickSeeMoreProduct(itemProduct: TelcoProductDataCollection) {
                listener.onSeeMoreProduct(itemProduct)
            }
        })
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

    fun getVisibleProductItemsToUsersTracking(productList: List<TelcoProductDataCollection>) {
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
        var categoryName = ""
        for (i in firstPos..lastPos) {
            if (firstPos >= 0 && lastPos <= productList.size - 1) {
                categoryName = getCategoryName(productList[i].product.attributes.categoryId)
                digitalTrackProductTelcoList.add(DigitalTrackProductTelco(productList[i], i))
            }
        }
        if (digitalTrackProductTelcoList.size > 0 &&
                digitalTrackProductTelcoList.size != digitalTrackTelcoPrev.size &&
                digitalTrackProductTelcoList != digitalTrackTelcoPrev) {
            listener.onTrackImpressionProductsList(digitalTrackProductTelcoList, categoryName)

            digitalTrackTelcoPrev.clear()
            digitalTrackTelcoPrev.addAll(digitalTrackProductTelcoList)
        }
    }

    fun getCategoryName(categoryId: Int): String {
        return when (categoryId) {
            TelcoCategoryType.CATEGORY_PULSA -> TelcoComponentName.PRODUCT_PULSA
            TelcoCategoryType.CATEGORY_PAKET_DATA -> TelcoComponentName.PRODUCT_PAKET_DATA
            TelcoCategoryType.CATEGORY_ROAMING -> TelcoComponentName.PRODUCT_ROAMING
            else -> TelcoComponentName.PRODUCT_PULSA
        }
    }

    fun notifyProductItemChanges(productId: String) {
        if (::adapter.isInitialized) {
            adapter.resetProductListSelected(productId)
        }
    }

    interface ActionListener {
        fun onClickProduct(itemProduct: TelcoProductDataCollection, position: Int, categoryName: String)
        fun onSeeMoreProduct(itemProduct: TelcoProductDataCollection)
        fun onTrackImpressionProductsList(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>,
                                          categoryName: String)
    }

    companion object {
        const val CELL_MARGIN_DP: Int = 8
    }

}