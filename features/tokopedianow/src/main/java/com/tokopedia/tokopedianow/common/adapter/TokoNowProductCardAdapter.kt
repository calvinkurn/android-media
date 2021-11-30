package com.tokopedia.tokopedianow.common.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.common.adapter.TokoNowProductCardAdapter.*
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductCardViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductCardViewHolder.*

class TokoNowProductCardAdapter(
    typeFactory: TokoNowProductCardAdapterTypeFactory,
) : BaseTokopediaNowListAdapter<TokoNowProductCardUiModel, TokoNowProductCardAdapterTypeFactory>(typeFactory, TokoNowProductCardDiffer()) {

    class TokoNowProductCardAdapterTypeFactory(
        private val listener: TokoNowProductCardListener?
    ) : BaseAdapterTypeFactory(), TokoNowProductCardTypeFactory {

        override fun type(dataModel: TokoNowProductCardUiModel) = TokoNowProductCardViewHolder.LAYOUT

        override fun createViewHolder(
            parent: View,
            type: Int
        ): AbstractViewHolder<out Visitable<*>> {
            return when (type) {
                TokoNowProductCardViewHolder.LAYOUT -> TokoNowProductCardViewHolder(
                    parent,
                    listener
                )
                else -> super.createViewHolder(parent, type)
            }
        }
    }

    class TokoNowProductCardDiffer : BaseTokopediaNowDiffer() {
        private var oldList: List<Visitable<*>> = emptyList()
        private var newList: List<Visitable<*>> = emptyList()

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return if (oldItem is TokoNowProductCardUiModel && newItem is TokoNowProductCardUiModel) {
                oldItem.productId == newItem.productId
            } else {
                oldItem == newItem
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun create(
            oldList: List<Visitable<*>>,
            newList: List<Visitable<*>>
        ): BaseTokopediaNowDiffer {
            this.oldList = oldList
            this.newList = newList
            return this
        }
    }

    interface TokoNowProductCardTypeFactory {
        fun type(dataModel: TokoNowProductCardUiModel): Int
    }
}