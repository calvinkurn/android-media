package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.manage.R
import kotlinx.android.synthetic.main.product_manage_empty_state.view.*

class EmptyStateViewHolder(itemView: View): AbstractViewHolder<EmptyModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.product_manage_empty_state
    }

    override fun bind(data: EmptyModel) {
        setupEmptyStateContainer(data)
        itemView.title.text = getString(data.contentRes)
        ImageHandler.LoadImage(itemView.image, data.urlRes)
    }

    private fun setupEmptyStateContainer(data: EmptyModel) {
        val layoutParams = if(data.contentRes == R.string.product_manage_list_empty_product) {
            LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        } else {
            LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }
        itemView.container.layoutParams = layoutParams
    }
}