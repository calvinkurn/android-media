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
import com.tokopedia.attachproduct.view.uimodel.NewAttachProductItemUiModel
import com.tokopedia.attachproduct.view.viewholder.NewAttachProductEmptyResultViewHolder
import com.tokopedia.attachproduct.view.viewholder.NewAttachProductListItemViewHolder
import com.tokopedia.attachproduct.view.viewholder.NewCheckableInteractionListenerWithPreCheckedAction

class NewAttachProductListAdapterTypeFactory
    (private val checkableInteractionListener : CheckableInteractionListener)
    : BaseAdapterTypeFactory(), BaseListCheckableTypeFactory<NewAttachProductItemUiModel?> {
    override fun type(viewModelNew: NewAttachProductItemUiModel?): Int {
        return NewAttachProductListItemViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyResultViewModel): Int {
        return EmptyResultViewHolder.LAYOUT
    }

    override fun type(viewModel: ErrorNetworkModel): Int {
        return ErrorNetworkViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == NewAttachProductListItemViewHolder.LAYOUT) {
            val listener = checkableInteractionListener as NewCheckableInteractionListenerWithPreCheckedAction
            NewAttachProductListItemViewHolder(parent, listener, checkableInteractionListener)
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            NewAttachProductEmptyResultViewHolder(parent)
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            ErrorNetworkViewHolder(parent)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}
