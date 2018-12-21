package com.tokopedia.common_digital.product.presentation.adapter

import android.graphics.Paint
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common_digital.R
import com.tokopedia.common_digital.product.presentation.model.Product
import java.lang.RuntimeException

import java.util.ArrayList

/**
 * Created by Rizky on 12/09/18.
 */
class ProductChooserAdapter(private val hostFragment: Fragment,
                            productList: List<Product>?,
                            private val actionListener: ActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val productList: List<Product>

    interface ActionListener {
        fun onProductItemSelected(product: Product?)
    }

    init {
        this.productList = productList ?: ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(
                hostFragment.activity).inflate(viewType, parent, false)
        if (viewType == TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM) {
            return ItemDescAndPriceHolder(view)
        } else if (viewType == TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC) {
            return ItemPriceAdmin(view)
        } else if (viewType == TYPE_HOLDER_PRODUCT_PROMO) {
            return ItemHolderPromoProduct(view)
        }
        throw RuntimeException("View holder not supported")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        val product = productList[position]
        if (type == TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM) {
            (holder as ItemDescAndPriceHolder).bind(product)
        } else if (type == TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC) {
            (holder as ItemPriceAdmin).bind(product)
        } else if (type == TYPE_HOLDER_PRODUCT_PROMO) {
            (holder as ItemHolderPromoProduct).bind(product)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (productList[position].promo != null)
            TYPE_HOLDER_PRODUCT_PROMO
        else if (!TextUtils.isEmpty(productList[position].detailCompact))
            TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC
        else
            TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    internal inner class ItemDescAndPriceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitlePrice: TextView
        private val tvPrice: TextView
        private val emptyStockNotification: TextView

        private var product: Product? = null

        init {

            tvTitlePrice = itemView.findViewById(R.id.title_price)
            tvPrice = itemView.findViewById(R.id.tv_price)
            emptyStockNotification = itemView.findViewById(R.id.empty_stock_notification)

            itemView.setOnClickListener {
                if (product!!.status != Product.STATUS_OUT_OF_STOCK) {
                    actionListener.onProductItemSelected(product)
                }
            }
        }

        fun bind(product: Product) {
            this.product = product
            setViewPriceDescription(product)
            setProductAvailability(product)
        }

        private fun setViewPriceDescription(product: Product) {
            if (TextUtils.isEmpty(product.desc)) {
                tvTitlePrice.visibility = View.GONE
            } else {
                tvTitlePrice.visibility = View.VISIBLE
                tvTitlePrice.text = product.desc
            }
            if (TextUtils.isEmpty(product.price)) {
                tvPrice.visibility = View.GONE
            } else {
                tvPrice.visibility = View.VISIBLE
                tvPrice.text = product.price
            }
        }

        private fun setProductAvailability(product: Product) {
            if (product.status == Product.STATUS_OUT_OF_STOCK) {
                disableView(itemView)
                emptyStockNotification.visibility = View.VISIBLE
                emptyStockNotification.setTextColor(hostFragment
                        .resources.getColor(R.color.white))
            } else {
                enableView()
                emptyStockNotification.visibility = View.GONE
            }
        }

        private fun enableView() {
            tvTitlePrice.setTextColor(hostFragment.resources.getColor(R.color.black_70))
            tvPrice.setTextColor(hostFragment.resources.getColor(R.color.black_70))
        }
    }

    internal inner class ItemPriceAdmin(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductPrice: TextView
        private val tvProductDescription: TextView
        private val tvProductTotalPrice: TextView
        private val emptyStockNotification: TextView

        private var product: Product? = null

        init {

            tvProductPrice = itemView.findViewById(R.id.product_price_no_addition)
            tvProductDescription = itemView.findViewById(R.id.product_plain_description)
            tvProductTotalPrice = itemView.findViewById(R.id.product_total_price)
            emptyStockNotification = itemView.findViewById(R.id.empty_stock_notification)

            itemView.setOnClickListener {
                if (product!!.status != Product.STATUS_OUT_OF_STOCK) {
                    actionListener.onProductItemSelected(product)
                }
            }
        }

        fun bind(product: Product) {
            this.product = product
            setViewPriceAdditionalFee(product)
            setProductAvailability(product)
        }

        private fun setViewPriceAdditionalFee(product: Product) {
            if (TextUtils.isEmpty(product.desc)) {
                tvProductPrice.visibility = View.GONE
            } else {
                tvProductPrice.visibility = View.VISIBLE
                tvProductPrice.text = product.desc
            }

            if (TextUtils.isEmpty(product.price)) {
                tvProductTotalPrice.visibility = View.GONE
            } else {
                tvProductTotalPrice.visibility = View.VISIBLE
                tvProductTotalPrice.text = product.price
            }

            if (TextUtils.isEmpty(product.detailCompact)) {
                tvProductDescription.visibility = View.GONE
            } else {
                tvProductDescription.visibility = View.VISIBLE
                tvProductDescription.text = MethodChecker.fromHtml(product.detailCompact)
            }
        }

        private fun setProductAvailability(product: Product) {
            if (product.status == Product.STATUS_OUT_OF_STOCK) {
                disableView(itemView)
                emptyStockNotification.visibility = View.VISIBLE
                emptyStockNotification.setTextColor(hostFragment
                        .resources.getColor(R.color.white))
            } else {
                enableView()
                emptyStockNotification.visibility = View.GONE
            }
        }

        private fun enableView() {
            tvProductPrice.setTextColor(hostFragment.resources
                    .getColor(R.color.black_70))
            tvProductDescription.setTextColor(hostFragment.resources
                    .getColor(R.color.grey_500))
            tvProductTotalPrice.setTextColor(hostFragment.resources
                    .getColor(R.color.black_70))
        }
    }

    internal inner class ItemHolderPromoProduct(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductPromoTitle: TextView
        private val tvProductPromoTag: TextView
        private val tvProductPromoDescription: TextView
        private val tvPromoProductPrice: TextView
        private val tvProductPromoOldPrice: TextView
        private val emptyStockNotification: TextView

        private var product: Product? = null

        init {

            tvProductPromoTitle = itemView.findViewById(R.id.product_promo_title)
            tvProductPromoTag = itemView.findViewById(R.id.product_promo_tag)
            tvProductPromoDescription = itemView.findViewById(R.id.product_promo_description)
            tvPromoProductPrice = itemView.findViewById(R.id.product_promo_price)
            tvProductPromoOldPrice = itemView.findViewById(R.id.product_promo_old_price)
            emptyStockNotification = itemView.findViewById(R.id.empty_stock_notification)

            itemView.setOnClickListener {
                if (product!!.status != Product.STATUS_OUT_OF_STOCK) {
                    actionListener.onProductItemSelected(product)
                }
            }
        }

        fun bind(product: Product) {
            this.product = product
            setViewPromo(product)
            setProductAvailability(product)
        }

        private fun setViewPromo(product: Product) {
            tvProductPromoTitle.text = product.desc
            if (TextUtils.isEmpty(product.detailCompact)) {
                tvProductPromoDescription.visibility = View.GONE
            } else {
                tvProductPromoDescription.visibility = View.VISIBLE
                tvProductPromoDescription.text = MethodChecker.fromHtml(product.detailCompact)
            }
            if (TextUtils.isEmpty(product.promo!!.tag)) {
                tvProductPromoTag.visibility = View.GONE
            } else {
                tvProductPromoTag.visibility = View.VISIBLE
                tvProductPromoTag.text = product.promo!!.tag
            }
            tvPromoProductPrice.text = product.promo!!.newPrice
            tvProductPromoOldPrice.text = product.price
            tvProductPromoOldPrice.paintFlags = tvProductPromoOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        private fun setProductAvailability(product: Product) {
            if (product.status == Product.STATUS_OUT_OF_STOCK) {
                disableView(itemView)
                emptyStockNotification.visibility = View.VISIBLE
                emptyStockNotification.setTextColor(hostFragment
                        .resources.getColor(R.color.white))
            } else {
                enableView()
                emptyStockNotification.visibility = View.GONE
            }
        }

        private fun enableView() {
            tvProductPromoTitle.setTextColor(hostFragment.resources
                    .getColor(R.color.black_70))
            tvProductPromoTag.setTextColor(hostFragment.resources
                    .getColor(R.color.deep_orange_500))
            tvProductPromoDescription.setTextColor(hostFragment.resources
                    .getColor(R.color.grey_500))
            tvProductPromoOldPrice.setTextColor(hostFragment.resources
                    .getColor(R.color.black_70))
            tvPromoProductPrice.setTextColor(hostFragment.resources
                    .getColor(R.color.deep_orange_500))
        }
    }

    private fun disableView(itemView: View) {
        for (i in 0 until (itemView as ViewGroup).childCount) {
            val adapterElement = itemView.getChildAt(i)
            adapterElement.isEnabled = false
            if (adapterElement is TextView) {
                disableTextView(adapterElement)
            } else if (adapterElement is ViewGroup) {
                for (j in 0 until adapterElement.childCount) {
                    if (adapterElement.getChildAt(j) is TextView) {
                        disableTextView(adapterElement
                                .getChildAt(j) as TextView)
                    }
                }
            }
        }
    }

    private fun disableTextView(textViewToDisable: TextView) {
        textViewToDisable.setTextColor(hostFragment.resources.getColor(R.color.grey_400))
    }

    companion object {

        private val TYPE_HOLDER_PRODUCT_DESC_AND_PRICE_ITEM = R.layout.view_holder_item_product_desc_and_price_digital_module
        private val TYPE_HOLDER_PRODUCT_PRICE_PLUS_ADMIN_AND_DESC = R.layout.view_holder_price_plus_admin_and_desc
        private val TYPE_HOLDER_PRODUCT_PROMO = R.layout.view_holder_product_promo
    }

}
