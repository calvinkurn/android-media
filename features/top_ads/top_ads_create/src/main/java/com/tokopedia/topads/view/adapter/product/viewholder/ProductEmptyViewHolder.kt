package com.tokopedia.topads.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductEmptyViewModel
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Author errysuprayogi on 11,November,2019
 */
class ProductEmptyViewHolder(val view: View): ProductViewHolder<ProductEmptyViewModel>(view) {

    private val emptyStateProductList : GlobalError? = view.findViewById(R.id.emptyStateProductList)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_product_list_item_no_product
    }

    override fun bind(item: ProductEmptyViewModel) {
        emptyStateProductList?.apply{
            errorAction.hide()
            errorTitle.text = "Tidak ada produk yang Belum Diiklankan"
            errorDescription.text = "Kamu belum punya produk yang Belum Diiklankan. Coba cari dengan filter lain, ya."
        }
    }

}
