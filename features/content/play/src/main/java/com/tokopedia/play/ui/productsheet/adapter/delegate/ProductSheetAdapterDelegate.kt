package com.tokopedia.play.ui.productsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.ui.productsheet.viewholder.ProductSheetSectionViewHolder
import com.tokopedia.play_common.R as commonR

/**
 * Created by kenny.hadisaputra on 18/08/22
 */
class ProductSheetAdapterDelegate private constructor() {

    class Section(
        private val listener: ProductSheetSectionViewHolder.Listener,
    ) : TypedAdapterDelegate<
            ProductSheetAdapter.Item.Section,
            ProductSheetAdapter.Item,
            ProductSheetSectionViewHolder>(
        commonR.layout.view_play_empty
    ) {
        override fun onBindViewHolder(
            item: ProductSheetAdapter.Item.Section,
            holder: ProductSheetSectionViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductSheetSectionViewHolder {
            return ProductSheetSectionViewHolder.create(parent, listener)
        }
    }

    class Product(
        private val listener: ProductLineViewHolder.Listener,
    ) : TypedAdapterDelegate<
            ProductSheetAdapter.Item.Product,
            ProductSheetAdapter.Item,
            ProductLineViewHolder>(
        commonR.layout.view_play_empty
    ) {
        override fun onBindViewHolder(
            item: ProductSheetAdapter.Item.Product,
            holder: ProductLineViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductLineViewHolder {
            return ProductLineViewHolder.create(parent, listener)
        }
    }

    class Loading : TypedAdapterDelegate<
            ProductSheetAdapter.Item.Loading,
            ProductSheetAdapter.Item,
            RecyclerView.ViewHolder>(
        R.layout.item_play_product_placeholder
    ) {
        override fun onBindViewHolder(
            item: ProductSheetAdapter.Item.Loading,
            holder: RecyclerView.ViewHolder
        ) {}

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): RecyclerView.ViewHolder {
            return BaseViewHolder(basicView)
        }
    }
}