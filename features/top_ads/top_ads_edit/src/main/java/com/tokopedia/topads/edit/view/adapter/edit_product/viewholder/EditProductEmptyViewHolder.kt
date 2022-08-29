package com.tokopedia.topads.edit.view.adapter.edit_product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductEmptyViewModel
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created by Pika on 8/4/20.
 */
class EditProductEmptyViewHolder(val view: View) : EditProductViewHolder<EditProductEmptyViewModel>(view) {

    private val imageView : ImageUnify = view.findViewById(R.id.imageView8)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_select_layout_product_list_item_no_product
    }

    override fun bind(item: EditProductEmptyViewModel) {
        imageView.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.no_products))
    }

}