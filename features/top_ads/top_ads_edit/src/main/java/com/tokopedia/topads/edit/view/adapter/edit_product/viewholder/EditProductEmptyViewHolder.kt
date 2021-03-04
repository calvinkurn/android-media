package com.tokopedia.topads.edit.view.adapter.edit_product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductEmptyViewModel
import kotlinx.android.synthetic.main.topads_edit_select_layout_product_list_item_no_product.view.*

/**
 * Created by Pika on 8/4/20.
 */
class EditProductEmptyViewHolder(val view: View) : EditProductViewHolder<EditProductEmptyViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_select_layout_product_list_item_no_product
    }

    override fun bind(item: EditProductEmptyViewModel) {
        view.imageView8.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.ill_no_product))
    }

}