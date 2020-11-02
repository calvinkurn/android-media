package com.tokopedia.top_ads_headline.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.top_ads_headline.R
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.item_layout_topads_category_shimmer.view.*
import kotlinx.android.synthetic.main.topads_common_layout_rating.view.*

private const val DEFAULT_SHIMMER_COUNT = 5
private const val VIEW_SHIMMER = 0
private const val VIEW_CATEGORY = 1

class ProductListAdapter(private var list: ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>,
                         private val productItemClickListener: ProductItemClickListener? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setProductList(list: ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>) {
        this.list = list
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
        (holder as? ProductListViewHolder)?.let {
            val product = list[position]
            it.productName.text = product.productName
            it.productPrice.text = product.productPrice
            ImageUtils.loadImage(it.productImage, product.productImage)
            it.checkBox.setOnCheckedChangeListener { _, _ ->
                productItemClickListener?.onProductItemClick()
            }
            if (product.productRating != 0) {
                showRating(product.productRating, it.ratingView)
                val ratingText = "($product.productReviewCount)"
                it.ratingView.txt_rating_count.text = ratingText
                it.ratingView.txt_rating_count.show()
            } else {
                it.ratingView.hide()
                it.ratingView.txt_rating_count.hide()
            }
            it.recommendationTag.visibility = if(product.productIsPromoted){
                View.VISIBLE
            }else{
                View.GONE
            }
        }
    }

    private fun showRating(rating: Int, ratingView: ViewGroup) {
        for (i in 1..5) {
            showStar(i, ratingView, i <= rating)
        }
    }

    override fun getItemCount(): Int {
        return if (list.isEmpty()) {
            DEFAULT_SHIMMER_COUNT
        } else {
            list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.size <= 0) {
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
                layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.item_product_card_height)
            }
        }
    }


    private fun showStar(i: Int, ratingView: ViewGroup, show: Boolean) {
        when (i) {
            1 -> {
                if (show)
                    ratingView.imageViewRating1.setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    ratingView.imageViewRating1.setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
            2 -> {
                if (show)
                    ratingView.imageViewRating2.setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    ratingView.imageViewRating2.setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
            3 -> {
                if (show)
                    ratingView.imageViewRating3.setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    ratingView.imageViewRating3.setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
            4 -> {
                if (show)
                    ratingView.imageViewRating4.setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    ratingView.imageViewRating4.setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
            5 -> {
                if (show)
                    ratingView.imageViewRating5.setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    ratingView.imageViewRating5.setImageDrawable(AppCompatResources.getDrawable(ratingView.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
        }
    }

    interface ProductItemClickListener {
        fun onProductItemClick()
    }

}