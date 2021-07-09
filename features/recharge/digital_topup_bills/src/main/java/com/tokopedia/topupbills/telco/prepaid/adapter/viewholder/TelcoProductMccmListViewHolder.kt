package com.tokopedia.topupbills.telco.prepaid.adapter.viewholder

import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoCatalogDataCollection
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType.PRODUCT_MCCM
import com.tokopedia.topupbills.telco.prepaid.adapter.TelcoProductAdapter
import com.tokopedia.topupbills.telco.prepaid.adapter.TelcoProductAdapterFactory
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
                adapter = TelcoProductAdapter(context, TelcoProductAdapterFactory(PRODUCT_MCCM, object : TelcoProductViewHolder.OnClickListener{
                    override fun onClickItemProduct(element: TelcoProduct, position: Int) {
                        listener?.onClickMccm(element, position)
                    }

                    override fun onClickSeeMoreProduct(element: TelcoProduct, position: Int) {
                        listener?.onClickLihatDetail(element, position)
                    }

                }, null))
                telco_mccm_rv.adapter = adapter

                adapter.selectedPosition = selectedMccmPosition
                adapter.renderList(element.products)
                show()

                maintainScrollPosition()
            }
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
        fun onClickMccm(element: TelcoProduct, position: Int)
        fun onClickLihatDetail(element: TelcoProduct, position: Int)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_telco_mccm_list_section_product
    }
}
