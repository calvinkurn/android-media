package com.tokopedia.topupbills.telco.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.view.adapter.viewholder.BaseTelcoProductViewHolder
import com.tokopedia.topupbills.telco.view.adapter.viewholder.TelcoProductGridViewHolder
import com.tokopedia.topupbills.telco.view.adapter.viewholder.TelcoProductListViewHolder
import com.tokopedia.topupbills.telco.view.adapter.viewholder.TelcoTitleProductViewHolder


/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalProductAdapter(private val productList: List<TelcoProduct>, private val productType: Int)
    : RecyclerView.Adapter<BaseTelcoProductViewHolder>() {

    private lateinit var listener: ActionListener

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun resetProductListSelected(productId: String) {
        for (i in productList.indices) {
            if (productList[i].attributes.selected && productList[i].id != productId) {
                productList[i].attributes.selected = false
                notifyItemChanged(i)
                break
            }
        }
    }

    fun selectItemProduct(itemProduct: TelcoProduct) {
        if (itemProduct.attributes.status != BaseTelcoProductViewHolder.PRODUCT_STATUS_OUT_OF_STOCK) {
            for (i in productList.indices) {
                if (productList[i].attributes.selected) {
                    productList[i].attributes.selected = false
                    listener.notifyItemChanged(i)
                }

                //handle if product item is already selected still should selected
                if (productList[i].id == itemProduct.id) {
                    productList[i].attributes.selected = true
                    listener.notifyItemChanged(i)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BaseTelcoProductViewHolder, position: Int) {
        when (holder) {
            is TelcoProductGridViewHolder -> holder.bindView(productList, productList[position])
            is TelcoProductListViewHolder -> holder.bindView(productList, productList[position])
            is TelcoTitleProductViewHolder -> holder.bindView(productList, productList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTelcoProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            TelcoTitleProductViewHolder.LAYOUT -> TelcoTitleProductViewHolder(view, listener)
            TelcoProductGridViewHolder.LAYOUT -> TelcoProductGridViewHolder(view, listener)
            TelcoProductListViewHolder.LAYOUT -> TelcoProductListViewHolder(view, listener)
            else -> {
                TelcoTitleProductViewHolder(view, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (productList[position].isTitle) {
            TelcoTitleProductViewHolder.LAYOUT
        } else {
            if (productType == TelcoProductType.PRODUCT_GRID)
                TelcoProductGridViewHolder.LAYOUT
            else
                TelcoProductListViewHolder.LAYOUT
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    interface ActionListener {
        fun onClickItemProduct(itemProduct: TelcoProduct, position: Int, labelList: String)
        fun onClickSeeMoreProduct(itemProduct: TelcoProduct)
        fun notifyItemChanged(position: Int)
    }

}
