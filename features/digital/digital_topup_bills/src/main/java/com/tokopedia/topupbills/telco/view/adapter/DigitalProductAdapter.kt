package com.tokopedia.topupbills.telco.view.adapter

import android.content.Context
import android.graphics.Paint
import android.support.v7.content.res.AppCompatResources
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


/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalProductAdapter(val productList: List<TelcoProductDataCollection>, val productType: Int)
    : RecyclerView.Adapter<DigitalProductAdapter.BaseProductViewHolder>() {

    private lateinit var listener: ActionListener
    private lateinit var context: Context

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun resetProductListSelected(productId: String) {
        for (i in 0 until productList.size) {
            if (productList[i].product.attributes.selected && productList[i].key != productId) {
                productList[i].product.attributes.selected = false
                notifyItemChanged(i)
                break
            }
        }
    }

    override fun onBindViewHolder(holder: BaseProductViewHolder, position: Int) {
        when (holder) {
            is ProductGridViewHolder -> holder.bindView()
            is ProductListViewHolder -> holder.bindView()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseProductViewHolder {
        context = parent.context
        return if (productType == TelcoProductType.PRODUCT_GRID) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_digital_product_grid, parent, false)
            ProductGridViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_digital_product_list, parent, false)
            ProductListViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    abstract inner class BaseProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        protected val titleProduct: TextView = view.findViewById(R.id.title_product)
        protected val descProduct: TextView = view.findViewById(R.id.desc_product)
        protected val productPromoPrice: TextView = view.findViewById(R.id.product_promo_price)
        protected val productPrice: TextView = view.findViewById(R.id.product_price)
        protected lateinit var productItem: TelcoProductDataCollection

        open fun bindView() {
            productItem = productList.get(adapterPosition)
            titleProduct.text = productItem.product.attributes.desc
            descProduct.text = productItem.product.attributes.detail
            productPrice.text = productItem.product.attributes.price

            productPromoPrice.visibility = View.INVISIBLE
            productItem.product.attributes.productPromo?.run {
                if (this.newPrice.isNotEmpty()) {
                    productPrice.text = this.newPrice
                    productPromoPrice.text = productItem.product.attributes.price
                    productPromoPrice.paintFlags = productPromoPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    productPromoPrice.visibility = View.VISIBLE
                }
            }
        }

        protected fun onClickProductItem() {
            for (i in 0 until productList.size) {
                if (productList[i].product.attributes.selected) {
                    productList[i].product.attributes.selected = false
                    notifyItemChanged(i)
                    break
                }
            }

            productItem.product.attributes.selected = true
            notifyItemChanged(adapterPosition)
        }

        protected fun setItemSelected(viewGrup: ViewGroup) {
            var drawable = AppCompatResources.getDrawable(context, R.drawable.digital_bg_transparent_round)
            if (productItem.product.attributes.selected) {
                listener.onClickItemProduct(productItem, adapterPosition)
                drawable = AppCompatResources.getDrawable(context, R.drawable.digital_bg_green_light_rounded)
            }
            viewGrup.background = drawable
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
            setItemSelected(layoutProduct)
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
            setItemSelected(layoutProduct)
        }
    }

    interface ActionListener {
        fun onClickItemProduct(itemProduct: TelcoProductDataCollection, position: Int)
        fun onClickSeeMoreProduct(itemProduct: TelcoProductDataCollection)
    }

}
