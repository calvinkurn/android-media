package com.tokopedia.topupbills.telco.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoProductDataCollection
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.view.model.DigitalProductTelcoItem

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalProductAdapter(val productList: List<TelcoProductDataCollection>, val productType: Int)
    : RecyclerView.Adapter<DigitalProductAdapter.BaseProductViewHolder>() {

    private lateinit var listener: ActionListener

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: BaseProductViewHolder, position: Int) {
        when (holder) {
            is ProductGridViewHolder -> holder.bindView()
            is ProductListViewHolder -> holder.bindView()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseProductViewHolder {
        if (productType == TelcoProductType.PRODUCT_GRID) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_digital_product_grid, parent, false)
            return ProductGridViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_digital_product_list, parent, false)
            return ProductListViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return productList.size;
    }

    inner open abstract class BaseProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        protected val titleProduct: TextView = view.findViewById(R.id.title_product)
        protected val descProduct: TextView = view.findViewById(R.id.desc_product)
        protected val originalPrice: TextView = view.findViewById(R.id.product_original_price)
        protected val productPrice: TextView = view.findViewById(R.id.product_price)
        protected lateinit var productItem: TelcoProductDataCollection

        init {

        }

        open fun bindView() {
            productItem = productList.get(adapterPosition)
            titleProduct.setText(productItem.product.attributes.desc)
            descProduct.setText(productItem.product.attributes.detail)
            productPrice.setText(productItem.product.attributes.price)
        }

        open fun onClickProductItem() {
            for (i in 0..productList.size-1) {
                if (productList.get(i).product.attributes.selected) {
                    productList.get(i).product.attributes.selected = false
                    notifyItemChanged(i)
                    break
                }
            }

            productItem.product.attributes.selected = true
            notifyItemChanged(adapterPosition)
            listener.onClickItemProduct(productItem)
        }
    }

    inner class ProductGridViewHolder(view: View) : BaseProductViewHolder(view) {

        protected val layoutProduct: LinearLayout = view.findViewById(R.id.layout_product)

        init {
            layoutProduct.setOnClickListener {
                onClickProductItem()
            }
        }

        override fun bindView() {
            super.bindView()
            if (productItem.product.attributes.selected) {
                layoutProduct.setBackgroundResource(R.drawable.digital_bg_transparent_border_green)
            } else {
                layoutProduct.setBackgroundResource(R.drawable.digital_bg_transparent_round)
            }
        }

    }

    inner class ProductListViewHolder(view: View) : BaseProductViewHolder(view) {

        private val seeMoreBtn: TextView = view.findViewById(R.id.see_more_btn)
        protected val layoutProduct: RelativeLayout = view.findViewById(R.id.layout_product)

        init {
            seeMoreBtn.setOnClickListener {
                listener.onClickSeeMoreProduct(productItem)
            }

            layoutProduct.setOnClickListener {
                onClickProductItem()
            }
        }

        override fun bindView() {
            super.bindView()
            if (productItem.product.attributes.selected) {
                layoutProduct.setBackgroundResource(R.drawable.digital_bg_transparent_border_green)
            } else {
                layoutProduct.setBackgroundResource(R.drawable.digital_bg_transparent_round)
            }
        }
    }

    interface ActionListener {
        fun onClickItemProduct(itemProduct: TelcoProductDataCollection)
        fun onClickSeeMoreProduct(itemProduct: TelcoProductDataCollection)
    }

}
