package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.HeaderViewModel
import com.tokopedia.product.manage.oldlist.R

class HeaderViewHolder(view: View, onExpandListener: OnExpandListener) : AbstractViewHolder<HeaderViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_header
    }

    override fun bind(element: HeaderViewModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

interface OnExpandListener {
    fun onExpand()
}