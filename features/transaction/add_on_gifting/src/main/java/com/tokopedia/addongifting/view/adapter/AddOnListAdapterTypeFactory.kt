package com.tokopedia.addongifting.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.addongifting.databinding.ItemAddOnBinding
import com.tokopedia.addongifting.databinding.ItemAddOnLoadingBinding
import com.tokopedia.addongifting.databinding.ItemProductBinding
import com.tokopedia.addongifting.view.AddOnActionListener
import com.tokopedia.addongifting.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.view.uimodel.ProductUiModel
import com.tokopedia.addongifting.view.viewholder.AddOnLoadingViewHolder
import com.tokopedia.addongifting.view.viewholder.AddOnViewHolder
import com.tokopedia.addongifting.view.viewholder.ProductViewHolder

class AddOnListAdapterTypeFactory(private val listener: AddOnActionListener)
    : BaseAdapterTypeFactory(), AddOnListTypeFactory {

    override fun type(uiModel: AddOnUiModel): Int {
        return AddOnViewHolder.LAYOUT
    }

    override fun type(uiModel: ProductUiModel): Int {
        return ProductViewHolder.LAYOUT
    }

    override fun type(uiModel: LoadingModel): Int {
        return AddOnLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            AddOnViewHolder.LAYOUT -> {
                val viewBinding = ItemAddOnBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                AddOnViewHolder(viewBinding, listener)
            }
            ProductViewHolder.LAYOUT -> {
                val viewBinding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                ProductViewHolder(viewBinding, listener)
            }
            AddOnLoadingViewHolder.LAYOUT -> {
                val viewBinding = ItemAddOnLoadingBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                AddOnLoadingViewHolder(viewBinding)
            }
            else -> super.createViewHolder(parent, viewType)
        }

    }
}