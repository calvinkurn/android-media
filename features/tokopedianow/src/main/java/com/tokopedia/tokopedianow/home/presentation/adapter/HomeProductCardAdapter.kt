package com.tokopedia.tokopedianow.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeProductCardAdapter.*
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProductCardViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProductCardViewHolder.*

class HomeProductCardAdapter(
    typeFactory: HomeProductCardAdapterTypeFactory,
) : BaseTokopediaNowListAdapter<HomeProductCardUiModel, HomeProductCardAdapterTypeFactory>(typeFactory, HomeProductCardDiffer()) {

    class HomeProductCardAdapterTypeFactory(
        private val tokoNowView: TokoNowView?,
        private val listener: HomeProductCardListener?
        ) : BaseAdapterTypeFactory(), HomeProductCardTypeFactory {
        override fun type(dataModel: HomeProductCardUiModel) = HomeProductCardViewHolder.LAYOUT

        override fun createViewHolder(
            parent: View,
            type: Int
        ): AbstractViewHolder<out Visitable<*>> {
            return when (type) {
                HomeProductCardViewHolder.LAYOUT -> HomeProductCardViewHolder(parent, tokoNowView, listener)
                else -> super.createViewHolder(parent, type)
            }
        }
    }

    class HomeProductCardDiffer : BaseTokopediaNowDiffer() {
        private var oldList: List<Visitable<*>> = emptyList()
        private var newList: List<Visitable<*>> = emptyList()

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return if (oldItem is HomeProductCardUiModel && newItem is HomeProductCardUiModel) {
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

    interface HomeProductCardTypeFactory {
        fun type(dataModel: HomeProductCardUiModel): Int
    }
}