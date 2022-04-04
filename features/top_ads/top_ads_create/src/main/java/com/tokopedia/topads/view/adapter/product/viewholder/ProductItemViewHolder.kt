package com.tokopedia.topads.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
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

    private val checkBox : CheckboxUnify? = view.findViewById(R.id.checkBox_create_product)
    private val productName : Typography? = view.findViewById(R.id.product_name)
    private val productPrice : Typography? = view.findViewById(R.id.product_price)
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
        item?.let {
            imageViewRating1?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            imageViewRating2?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            imageViewRating3?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            imageViewRating4?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            imageViewRating5?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
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

}