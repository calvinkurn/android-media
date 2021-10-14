package com.tokopedia.attachproduct.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder.CheckableInteractionListener
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.attachproduct.databinding.ItemProductAttachBinding
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel
import com.tokopedia.attachproduct.view.viewholder.AttachProductEmptyResultViewHolder
import com.tokopedia.attachproduct.view.viewholder.AttachProductListItemViewHolder
import com.tokopedia.attachproduct.view.viewholder.CheckableInteractionListenerWithPreCheckedAction

class AttachProductListAdapterTypeFactory
    (private val checkableInteractionListener : CheckableInteractionListener)
    : BaseAdapterTypeFactory(), BaseListCheckableTypeFactory<AttachProductItemUiModel?> {

    private val listener = checkableInteractionListener as
            CheckableInteractionListenerWithPreCheckedAction

    override fun type(viewModel: AttachProductItemUiModel?): Int {
        return AttachProductListItemViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyResultViewModel): Int {
        return EmptyResultViewHolder.LAYOUT
    }

    override fun type(viewModel: ErrorNetworkModel): Int {
        return ErrorNetworkViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == AttachProductListItemViewHolder.LAYOUT) {
            AttachProductListItemViewHolder(parent, listener, checkableInteractionListener)
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            AttachProductEmptyResultViewHolder(parent)
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            ErrorNetworkViewHolder(parent)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}
