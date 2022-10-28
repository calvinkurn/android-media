package com.tokopedia.topads.edit.view.adapter.edit_product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductItemViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 8/4/20.
 */
class EditProductItemViewHolder(val view: View, var actionChecked: ((pos: Int) -> Unit)?) : EditProductViewHolder<EditProductItemViewModel>(view) {

    private val productImage: ImageUnify = view.findViewById(R.id.product_image)
    private val productName: Typography = view.findViewById(R.id.product_name)
    private val productPrice: Typography = view.findViewById(R.id.product_price)
    private val delete: ImageUnify = view.findViewById(R.id.delete)
    private val imageViewRating1: ImageUnify = view.findViewById(R.id.imageViewRating1)
    private val imageViewRating2: ImageUnify = view.findViewById(R.id.imageViewRating2)
    private val imageViewRating3: ImageUnify = view.findViewById(R.id.imageViewRating3)
    private val imageViewRating4: ImageUnify = view.findViewById(R.id.imageViewRating4)
    private val imageViewRating5: ImageUnify = view.findViewById(R.id.imageViewRating5)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_layout_product_list_item_edit_product
    }

    override fun bind(item: EditProductItemViewModel) {
        item.let {
            productImage.setImageUrl(it.data.adDetailProduct.productImageURI)
            productName.text = it.data.adDetailProduct.productName
            delete.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_delete))
            imageViewRating1.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            imageViewRating2.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            imageViewRating3.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            imageViewRating4.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            imageViewRating5.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            productPrice.text = it.data.adPriceBidFmt
            delete.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    actionChecked?.invoke(adapterPosition)
            }
        }
    }

}