package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemDummyBinding
import com.tokopedia.feedplus.presentation.model.DummyModel

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class DummyViewHolder(private val binding: ItemDummyBinding) :
    AbstractViewHolder<DummyModel>(binding.root) {

    override fun bind(element: DummyModel) {
        binding.tvFeed.text = element.text
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dummy
    }
}
