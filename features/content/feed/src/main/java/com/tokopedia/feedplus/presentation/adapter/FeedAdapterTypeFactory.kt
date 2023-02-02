package com.tokopedia.feedplus.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.databinding.ItemDummyBinding
import com.tokopedia.feedplus.presentation.adapter.viewholder.DummyViewHolder
import com.tokopedia.feedplus.presentation.model.DummyModel

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(model: DummyModel) = DummyViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
        when (type) {
            DummyViewHolder.LAYOUT -> DummyViewHolder(
                ItemDummyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
            )
            else -> super.createViewHolder(parent, type)
        }

}
