package com.tokopedia.tokopedianow.common.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowRepurchaseProductAdapter.TokoNowRepurchaseProductAdapterTypeFactory
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseProductUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRepurchaseProductViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRepurchaseProductViewHolder.TokoNowRepurchaseProductListener

class TokoNowRepurchaseProductAdapter(
    typeFactory: TokoNowRepurchaseProductAdapterTypeFactory,
) : BaseTokopediaNowListAdapter<TokoNowRepurchaseProductUiModel, TokoNowRepurchaseProductAdapterTypeFactory>(
    typeFactory,
    TokoNowRepurchaseProductDiffer()
) {

    class TokoNowRepurchaseProductAdapterTypeFactory(
        private val listener: TokoNowRepurchaseProductListener?
    ) : BaseAdapterTypeFactory(), TokoNowRepurchaseProductTypeFactory {

        override fun type(dataModel: TokoNowRepurchaseProductUiModel) =
            TokoNowRepurchaseProductViewHolder.LAYOUT

        override fun createViewHolder(
            parent: View,
            type: Int
        ): AbstractViewHolder<out Visitable<*>> {
            return when (type) {
                TokoNowRepurchaseProductViewHolder.LAYOUT -> TokoNowRepurchaseProductViewHolder(
                    parent,
                    listener
                )
                else -> super.createViewHolder(parent, type)
            }
        }
    }

    class TokoNowRepurchaseProductDiffer : BaseTokopediaNowDiffer() {
        private var oldList: List<Visitable<*>> = emptyList()
        private var newList: List<Visitable<*>> = emptyList()

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return if (oldItem is TokoNowRepurchaseProductUiModel && newItem is TokoNowRepurchaseProductUiModel) {
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

    interface TokoNowRepurchaseProductTypeFactory {
        fun type(dataModel: TokoNowRepurchaseProductUiModel): Int
    }
}
