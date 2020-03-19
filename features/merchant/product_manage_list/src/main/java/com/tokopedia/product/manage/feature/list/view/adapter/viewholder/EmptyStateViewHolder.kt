package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.constant.ProductManageUrl.PRODUCT_MANAGE_EMPTY_STATE
import kotlinx.android.synthetic.main.product_manage_empty_state.view.*

class EmptyStateViewHolder(itemView: View): AbstractViewHolder<EmptyModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.product_manage_empty_state
    }

    override fun bind(data: EmptyModel) {
        ImageHandler.LoadImage(itemView.image, PRODUCT_MANAGE_EMPTY_STATE)
    }
}