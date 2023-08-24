package com.tokopedia.topads.view.adapter.product.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Author errysuprayogi on 11,November,2019
 */
class ProductItemViewHolder(val view: View, var actionChecked: (() -> Unit)?) : ProductViewHolder<ProductItemViewModel>(view) {

    private val checkBox : CheckboxUnify? = view.findViewById(R.id.checkBox)
    private val productName : Typography? = view.findViewById(R.id.product_name)
    private val productPrice : Typography? = view.findViewById(R.id.product_price)
    private val ratingCount : Typography? = view.findViewById(R.id.txt_rating_count)
    private val productImage : ImageUnify? = view.findViewById(R.id.product_image)
    private val imageViewRating1 : ImageUnify? = view.findViewById(R.id.imageViewRating1)
    private val imageViewRating2 : ImageUnify? = view.findViewById(R.id.imageViewRating2)
    private val imageViewRating3 : ImageUnify? = view.findViewById(R.id.imageViewRating3)
    private val imageViewRating4 : ImageUnify? = view.findViewById(R.id.imageViewRating4)
    private val imageViewRating5 : ImageUnify? = view.findViewById(R.id.imageViewRating5)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_product_list_item_product
    }

    init {
        checkBox?.let {
            it.isChecked = !it.isChecked
            actionChecked?.invoke()
        }
    }

    override fun bind(item: ProductItemViewModel) {
        item.let {
            manageRating(it.data.productRating)
            ratingCount?.text = String.format("(%s)", it.data.productReviewCount)
            productName?.text = it.data.productName
            productPrice?.text = it.data.productPrice
            checkBox?.setOnCheckedChangeListener(null)
            checkBox?.isChecked = item.isChecked
            productImage?.setImageUrl(it.data.productImage)
            checkBox?.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChecked = isChecked
                actionChecked?.invoke()
            }
        }
    }

    private fun manageRating(productRating: Int) {
        for (i in 1..5) {
            showStar(i, i <= productRating)
        }
    }

    private fun showStar(i: Int, show: Boolean) {
        when (i) {
            1 -> {
                if (show)
                    imageViewRating1?.setImageDrawable(AppCompatResources.getDrawable(view.context,
                            com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    imageViewRating1?.setImageDrawable(AppCompatResources.getDrawable(view.context,
                            com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
            2 -> {
                if (show)
                    imageViewRating2?.setImageDrawable(AppCompatResources.getDrawable(view.context,
                            com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    imageViewRating2?.setImageDrawable(AppCompatResources.getDrawable(view.context,
                            com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
            3 -> {
                if (show)
                    imageViewRating3?.setImageDrawable(AppCompatResources.getDrawable(view.context,
                            com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    imageViewRating3?.setImageDrawable(AppCompatResources.getDrawable(view.context,
                            com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
            4 -> {
                if (show)
                    imageViewRating4?.setImageDrawable(AppCompatResources.getDrawable(view.context,
                        com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    imageViewRating4?.setImageDrawable(AppCompatResources.getDrawable(view.context,
                        com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
            5 -> {
                if (show)
                    imageViewRating5?.setImageDrawable(AppCompatResources.getDrawable(view.context,
                        com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
                else
                    imageViewRating5?.setImageDrawable(AppCompatResources.getDrawable(view.context,
                        com.tokopedia.topads.common.R.drawable.topads_ic_rating_default))
            }
        }
    }

}
