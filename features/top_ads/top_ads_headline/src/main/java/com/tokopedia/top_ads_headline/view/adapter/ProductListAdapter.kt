package com.tokopedia.top_ads_headline.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.top_ads_headline.R
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_layout_topads_category_shimmer.view.*

private const val DEFAULT_SHIMMER_COUNT = 5
private const val VIEW_SHIMMER = 0
private const val VIEW_CATEGORY = 1
private const val MAX_PRODUCT_SELECTION = 10
const val PRODUCT_ADDED = 1
const val PRODUCT_REMOVED = -1
const val PRODUCT_SAME = 0

class ProductListAdapter(var list: ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>,
                         private var selectedProductList: HashSet<ResponseProductList.Result.TopadsGetListProduct.Data>,
                         private val productListAdapterListener: ProductListAdapterListener? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var showShimmer = true
    private var isRecommendedCategory = false

    fun setProductList(list: ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>, isRecommendedCategory: Boolean) {
        showShimmer = false
        this.list = list
        this.isRecommendedCategory = isRecommendedCategory
        notifyDataSetChanged()
    }

    fun refreshList() {
        list.clear()
        showShimmer = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_CATEGORY -> {
                val v = LayoutInflater.from(parent.context).inflate(ProductListViewHolder.Layout, parent, false)
                ProductListViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(ShimmerViewHolder.Layout, parent, false)
                return ShimmerViewHolder(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ProductListViewHolder)?.let { viewHolder ->
            val product = list[position]
            viewHolder.productName.text = product.productName
            viewHolder.productPrice.text = product.productPrice
            viewHolder.productImage.loadImage(product.productImage)
            viewHolder.checkBox.isChecked = selectedProductList.contains(product)
            setProductRating(product.productRating, product.productReviewCount, viewHolder.ratingView)
            viewHolder.recommendationTag.showWithCondition(isRecommendedCategory)
            if (product.isRecommended) {
                if (selectedProductList.contains(product)) {
                    productListAdapterListener?.onRecommendedProductChange(product, true)
                }else{
                    productListAdapterListener?.onRecommendedProductChange(product, false)
                }
            }
            viewHolder.itemView.setOnClickListener {
                val checked = selectedProductList.contains(product)
                onItemSelection(checked, product)
                viewHolder.checkBox.isChecked = selectedProductList.contains(product)
            }
        }
    }

    private fun setProductRating(productRating: Int, reviewCount: Int, ratingView: ViewGroup) {
        if (productRating != 0) {
            showRating(productRating, ratingView)
            val ratingText = "($reviewCount)"
            ratingView.findViewById<Typography>(R.id.txt_rating_count).text = ratingText
            ratingView.findViewById<Typography>(R.id.txt_rating_count).show()
        } else {
            ratingView.hide()
            ratingView.findViewById<Typography>(R.id.txt_rating_count).hide()
        }
    }

    private fun onItemSelection(isAlreadyChecked: Boolean, product: ResponseProductList.Result.TopadsGetListProduct.Data) {
        var productConst = PRODUCT_SAME
        if (isAlreadyChecked) {
            selectedProductList.remove(product)
            productConst = PRODUCT_REMOVED
        } else {
            if (selectedProductList.size >= MAX_PRODUCT_SELECTION) {
                productListAdapterListener?.onProductOverSelect()
            } else {
                selectedProductList.add(product)
                productConst = PRODUCT_ADDED
            }
        }
        productListAdapterListener?.onProductClick(product, productConst)
    }

    private fun showRating(rating: Int, ratingView: ViewGroup) {
        for (i in 1..5) {
            showStar(i, ratingView, i <= rating)
        }
    }

    override fun getItemCount(): Int {
        return if (showShimmer) {
            DEFAULT_SHIMMER_COUNT
        } else {
            list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (showShimmer) {
            VIEW_SHIMMER
        } else {
            VIEW_CATEGORY
        }
    }

    class ProductListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val Layout = R.layout.item_layout_product_list
        }

        val productImage: ImageUnify = itemView.findViewById(R.id.product_image)
        val checkBox: CheckboxUnify = itemView.findViewById(R.id.checkBox)
        val productName: Typography = itemView.findViewById(R.id.product_name)
        val ratingView: ViewGroup = itemView.findViewById(R.id.rating_layout)
        val productPrice: Typography = itemView.findViewById(R.id.product_price)
        val recommendationTag: Typography = itemView.findViewById(R.id.recommendationTag)
    }

    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val Layout = R.layout.item_layout_topads_category_shimmer
        }

        init {
            itemView.topadsShimmer.run {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.topads_headline_item_product_card_height)
            }
        }
    }


    private fun showStar(i: Int, ratingView: ViewGroup, show: Boolean) {
        when (i) {
            1 -> {
                if (show)
                    ratingView.findViewById<ImageUnify>(R.id.imageViewRating1).setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    ratingView.findViewById<ImageUnify>(R.id.imageViewRating1).setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
            2 -> {
                if (show)
                    ratingView.findViewById<ImageUnify>(R.id.imageViewRating2).setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    ratingView.findViewById<ImageUnify>(R.id.imageViewRating2).setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
            3 -> {
                if (show)
                    ratingView.findViewById<ImageUnify>(R.id.imageViewRating3).setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    ratingView.findViewById<ImageUnify>(R.id.imageViewRating3).setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
            4 -> {
                if (show)
                    ratingView.findViewById<ImageUnify>(R.id.imageViewRating4).setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    ratingView.findViewById<ImageUnify>(R.id.imageViewRating4).setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
            5 -> {
                if (show)
                    ratingView.findViewById<ImageUnify>(R.id.imageViewRating5).setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    ratingView.findViewById<ImageUnify>(R.id.imageViewRating5).setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
        }
    }

    interface ProductListAdapterListener {
        fun onProductOverSelect()
        fun onProductClick(product: ResponseProductList.Result.TopadsGetListProduct.Data, added: Int)
        fun onRecommendedProductChange(product: ResponseProductList.Result.TopadsGetListProduct.Data, isAdded: Boolean)
    }
}