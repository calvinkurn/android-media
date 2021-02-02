package com.tokopedia.topads.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemViewModel
import kotlinx.android.synthetic.main.topads_create_layout_product_list_item_product.view.*
import kotlinx.android.synthetic.main.topads_create_layout_rating.view.*

/**
 * Author errysuprayogi on 11,November,2019
 */
class ProductItemViewHolder(val view: View, var actionChecked: (() -> Unit)?) : ProductViewHolder<ProductItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_product_list_item_product
    }

    init {
        view?.setOnClickListener {
            it.checkBox.isChecked = !it.checkBox.isChecked
            actionChecked?.invoke()
        }
    }

    override fun bind(item: ProductItemViewModel) {
        item?.let {
            view.include.imageViewRating1.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            view.include.imageViewRating2.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            view.include.imageViewRating3.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            view.include.imageViewRating4.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            view.include.imageViewRating5.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            view.product_name.text = it.data.productName
            view.product_price.text = it.data.productPrice
            view.checkBox.setOnCheckedChangeListener(null)
            view.checkBox.isChecked = item.isChecked
            view.product_image.setImageUrl(it.data.productImage)
            view.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChecked = isChecked
                actionChecked?.invoke()
            }
        }
    }

}