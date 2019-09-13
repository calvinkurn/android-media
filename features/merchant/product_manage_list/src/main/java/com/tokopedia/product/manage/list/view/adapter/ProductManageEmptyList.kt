package com.tokopedia.product.manage.list.view.adapter

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.manage.list.R


class ProductManageEmptyList(view: View) : AbstractViewHolder<EmptyModel>(view) {

    private val imgEmpty:ImageView = view.findViewById(R.id.img_product_empty)

    companion object {
        @JvmStatic
        @LayoutRes
        val LAYOUT = R.layout.empty_list_product_manage

        const val IMG_EMPTY_LIST = "https://ecs7.tokopedia.net/img/android/manage_product/manage_product_empty.png"
    }

    override fun bind(element: EmptyModel?) {
        ImageHandler.LoadImage(imgEmpty, IMG_EMPTY_LIST)
    }
}