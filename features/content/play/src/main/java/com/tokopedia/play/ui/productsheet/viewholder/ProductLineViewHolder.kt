package com.tokopedia.play.ui.productsheet.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.databinding.ItemProductLineBinding
import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.play.view.custom.ProductBottomSheetCardView
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel

/**
 * Created by jegul on 03/03/20
 */
class ProductLineViewHolder(
    private val binding: ItemProductLineBinding,
    private val listener: Listener,
    private val source: DataSource,
) : BaseViewHolder(binding.root) {

    init {
        binding.root.setListener(object : ProductBottomSheetCardView.Listener {
            override fun onClicked(
                view: ProductBottomSheetCardView,
                product: PlayProductUiModel.Product,
                section: ProductSectionUiModel.Section
            ) {
                listener.onProductClicked(
                    this@ProductLineViewHolder,
                    product,
                    section,
                )
            }

            override fun onButtonTransactionProduct(
                view: ProductBottomSheetCardView,
                product: PlayProductUiModel.Product,
                section: ProductSectionUiModel.Section,
                action: ProductAction
            ) {
                listener.onButtonTransactionProduct(
                    this@ProductLineViewHolder,
                    product,
                    section,
                    action,
                )
            }
        })
    }

    fun bind(item: ProductSheetAdapter.Item.Product) {
        binding.root.setItem(item.product, item.section, source.getChannelType())
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener,
            source: DataSource,
        ) = ProductLineViewHolder(
            ItemProductLineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            listener,
            source,
        )
    }

    interface Listener {
        fun onProductClicked(
            viewHolder: ProductLineViewHolder,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section,
        )
        fun onButtonTransactionProduct(
            viewHolder: ProductLineViewHolder,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section,
            action: ProductAction,
        )
    }

    interface DataSource {
        fun getChannelType() : PlayChannelType
    }
}
