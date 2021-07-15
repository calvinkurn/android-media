package com.tokopedia.topupbills.telco.prepaid.adapter.viewholder

import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoCatalogDataCollection
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType.PRODUCT_MCCM
import com.tokopedia.topupbills.telco.prepaid.adapter.TelcoProductAdapter
import com.tokopedia.topupbills.telco.prepaid.adapter.TelcoProductAdapterFactory
import com.tokopedia.topupbills.telco.prepaid.model.DigitalTrackProductTelco
import com.tokopedia.topupbills.telco.prepaid.widget.GravitySnapHelper
import kotlinx.android.synthetic.main.item_telco_mccm_list_section_product.view.*


class TelcoProductMccmListViewHolder(itemView: View, val listener: OnClickListener?) : AbstractViewHolder<TelcoCatalogDataCollection>(itemView) {

    var selectedMccmPosition = -1
    lateinit var adapter: TelcoProductAdapter

    override fun bind(element: TelcoCatalogDataCollection) {
        with(itemView) {
            if (element.products.isEmpty()) hide()
            else {
                if (element.name.isEmpty()) {
                    telco_mccm_title_product.hide()
                } else {
                    telco_mccm_title_product.text = element.name
                    telco_mccm_title_product.show()
                }

                telco_mccm_rv.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = TelcoProductAdapter(context, TelcoProductAdapterFactory(PRODUCT_MCCM, object : TelcoProductViewHolder.OnClickListener {
                    override fun onClickItemProduct(element: TelcoProduct, position: Int) {
                        listener?.onClickMccm(element, position)
                    }

                    override fun onClickSeeMoreProduct(element: TelcoProduct, position: Int) {
                        listener?.onClickLihatDetail(element, position)
                    }

                }, null))
                telco_mccm_rv.adapter = adapter

                telco_mccm_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            trackCurrentVisibleMccmProduct()
                        }
                    }
                })

                adapter.selectedPosition = selectedMccmPosition
                adapter.renderList(element.products)
                show()

                trackFirstTimeVisibleProduct()

                maintainScrollPosition()
            }
        }
    }

    fun trackCurrentVisibleMccmProduct() {
        with(itemView) {
            val firstPos = (telco_mccm_rv.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            val lastPos = (telco_mccm_rv.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            getVisibleProductItemsToUsersTracking(firstPos, lastPos, adapter.data)
        }
    }

    private fun trackFirstTimeVisibleProduct() {
        with(itemView) {
            telco_mccm_rv.viewTreeObserver
                    .addOnGlobalLayoutListener(
                            object : OnGlobalLayoutListener {
                                override fun onGlobalLayout() {
                                    // At this point the layout is complete and the
                                    // dimensions of recyclerView and any child views
                                    // are known.
                                    trackCurrentVisibleMccmProduct()
                                    telco_mccm_rv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                }
                            })
        }
    }

    private fun getVisibleProductItemsToUsersTracking(firstPos: Int, lastPos: Int, productList: List<Visitable<*>>) {
        val digitalTrackProductTelcoList = mutableListOf<DigitalTrackProductTelco>()

        for (i in firstPos..lastPos) {
            if (firstPos >= 0 && lastPos <= productList.size - 1) {
                if (productList[i] is TelcoProduct) {
                    digitalTrackProductTelcoList.add(DigitalTrackProductTelco(productList[i] as TelcoProduct, i))
                }
            }
        }
        if (digitalTrackProductTelcoList.size > 0) {
            listener?.onTrackImpressionMccmProductsList(digitalTrackProductTelcoList)
        }
    }

    private fun maintainScrollPosition() {
        with(itemView) {
            val snapHelper = GravitySnapHelper(Gravity.CENTER)
            snapHelper.attachToRecyclerView(telco_mccm_rv)
            snapHelper.smoothScrollToPosition(selectedMccmPosition)
        }
    }

    interface OnClickListener {
        fun onTrackImpressionMccmProductsList(productList: List<DigitalTrackProductTelco>)
        fun onClickMccm(element: TelcoProduct, position: Int)
        fun onClickLihatDetail(element: TelcoProduct, position: Int)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_telco_mccm_list_section_product
    }
}
