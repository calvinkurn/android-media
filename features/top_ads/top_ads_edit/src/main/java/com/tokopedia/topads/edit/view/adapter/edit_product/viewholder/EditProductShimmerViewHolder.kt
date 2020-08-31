package com.tokopedia.topads.edit.view.adapter.edit_product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductShimmerViewModel

/**
 * Created by Pika on 8/4/20.
 */
class EditProductShimmerViewHolder(val view: View) : EditProductViewHolder<EditProductShimmerViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_select_layout_product_list_item_shimmering
    }
}