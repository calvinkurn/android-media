package com.tokopedia.topads.edit.view.adapter.edit_product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductItemViewModel
import kotlinx.android.synthetic.main.topads_edit_layout_product_list_item_edit_product.view.*
import kotlinx.android.synthetic.main.topads_edit_layout_rating.view.*

/**
 * Created by Pika on 8/4/20.
 */
class EditProductItemViewHolder(val view: View, var actionChecked: ((pos: Int) -> Unit)?) : EditProductViewHolder<EditProductItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_layout_product_list_item_edit_product
    }

    override fun bind(item: EditProductItemViewModel) {
        item.let {
            view.product_image.setImageUrl(it.data.adDetailProduct.productImageURI)
            view.product_name.text = it.data.adDetailProduct.productName
            view.delete.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_delete))
            view.include.imageViewRating1.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            view.include.imageViewRating2.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            view.include.imageViewRating3.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            view.include.imageViewRating4.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            view.include.imageViewRating5.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_rating_active))
            view.product_price.text = it.data.adPriceBidFmt
            view.delete.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    actionChecked?.invoke(adapterPosition)
            }
        }
    }

}