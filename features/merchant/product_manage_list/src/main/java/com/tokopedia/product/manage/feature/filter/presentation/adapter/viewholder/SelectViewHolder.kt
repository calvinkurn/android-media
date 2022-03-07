package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ItemSelectBinding
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.utils.view.binding.viewBinding

class SelectViewHolder(view: View, private val selectClickListener: SelectClickListener) : AbstractViewHolder<SelectUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_select
    }

    private val binding by viewBinding<ItemSelectBinding>()

    override fun bind(element: SelectUiModel) {
        binding?.selectWidget?.bind(element, selectClickListener)
    }
}