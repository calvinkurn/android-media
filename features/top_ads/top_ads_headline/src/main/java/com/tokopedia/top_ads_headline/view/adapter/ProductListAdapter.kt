package com.tokopedia.top_ads_headline.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.Category
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_layout_topads_category_shimmer.view.*

private const val DEFAULT_SHIMMER_COUNT = 5
private const val VIEW_SHIMMER = 0
private const val VIEW_CATEGORY = 1
private const val MAX_DEPARTMENT_NAME_LENGTH = 20
const val MAX_PRODUCT_SELECTION = 10
const val SINGLE_SELECTION = 1

class ProductListAdapter(var list: ArrayList<TopAdsProductModel>,
                         private var selectedProductMap: HashMap<Category, ArrayList<TopAdsProductModel>>,
                         private val productListAdapterListener: ProductListAdapterListener? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var showShimmer = true
    private val departmentLengthRange = 0..17

    fun setProductList(list: ArrayList<TopAdsProductModel>) {
        showShimmer = false
        this.list = list
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
            val category = Category(product.departmentID.toString(), product.departmentName)
            viewHolder.productName.text = product.productName
            viewHolder.productPrice.text = product.productPrice
            viewHolder.productImage.loadImage(product.productImage)
            viewHolder.checkBox.isChecked = selectedProductMap[category]?.contains(product) ?: false
            setProductRating(product.productRating, product.productReviewCount, viewHolder.ratingView)
            viewHolder.recommendationTag.text = getDepartmentName(holder.itemView.context, product)
            viewHolder.itemView.setOnClickListener {
                val checked = selectedProductMap[category]?.contains(product) ?: false
                onItemSelection(checked, category, product)
                viewHolder.checkBox.isChecked = selectedProductMap[category]?.contains(product)
                        ?: false
            }
            setMarginBottomIfLast(holder, position)
            product.isSingleSelect = checkIfSingleSelect(category, product)
            if (product.isSingleSelect) {
                holder.productCard.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.Red_R100))
            } else {
                holder.productCard.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            }
            product.positionInRv = position
        }
    }

    private fun checkIfSingleSelect(category: Category, product: TopAdsProductModel): Boolean {
        selectedProductMap[category]?.let {
            if (it.size == SINGLE_SELECTION && it.contains(product)) {
                return true
            }
        }
        return false
    }

    private fun setMarginBottomIfLast(holder: ProductListViewHolder, position: Int) {
        if (list.size - 1 == position) {
            holder.itemView.setMargin(0, 0, 0, holder.itemView.context.resources.getDimensionPixelSize(R.dimen.dp_72))
        } else {
            holder.itemView.setMargin(0, 0, 0, 0)
        }
    }

    private fun getDepartmentName(context: Context, product: TopAdsProductModel): CharSequence? {
        return if (product.isRecommended) {
            context.getString(R.string.topads_headline_recommendasi)
        } else {
            if (product.departmentName.length > MAX_DEPARTMENT_NAME_LENGTH) {
                product.departmentName.substring(departmentLengthRange) + "..."
            } else {
                product.departmentName
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

    private fun onItemSelection(isAlreadyChecked: Boolean, category: Category, product: TopAdsProductModel) {
        if (isAlreadyChecked) {
            selectedProductMap[category]?.remove(product)
        } else {
            var totalProducts = 0
            selectedProductMap.values.forEach {
                totalProducts += it.size
            }
            if (totalProducts >= MAX_PRODUCT_SELECTION) {
                productListAdapterListener?.onProductOverSelect()
            } else {
                if (selectedProductMap[category] != null) {
                    selectedProductMap[category]?.add(product)
                } else {
                    val value = ArrayList<TopAdsProductModel>()
                    value.add(product)
                    selectedProductMap[category] = value
                }
            }
        }
        productListAdapterListener?.onProductClick(product)
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

        val productCard: ConstraintLayout = itemView.findViewById(R.id.card)
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
        fun onProductClick(product: TopAdsProductModel)
    }
}