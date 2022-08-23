package com.tokopedia.play.ui.productsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play_common.R as commonR

/**
 * Created by jegul on 03/03/20
 */
class ProductLineAdapterDelegate(
    listener: ProductLineViewHolder.Listener
) : TypedAdapterDelegate<PlayProductUiModel.Product, PlayProductUiModel, ProductLineViewHolder>(
    commonR.layout.view_play_empty
), ProductLineViewHolder.Listener by listener {

    override fun onBindViewHolder(item: PlayProductUiModel.Product, holder: ProductLineViewHolder) {
//        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductLineViewHolder {
//        return ProductLineViewHolder.create(parent, object : ProductBottomSheetCardView.Listener {
//            override fun onClicked(
//                view: ProductBottomSheetCardView,
//                product: PlayProductUiModel.Product
//            ) {
//
//            }
//
//            override fun onBuyProduct(
//                view: ProductBottomSheetCardView,
//                product: PlayProductUiModel.Product
//            ) {
//
//            }
//
//            override fun onAtcProduct(
//                view: ProductBottomSheetCardView,
//                product: PlayProductUiModel.Product
//            ) {
//
//            }
//        })
        return ProductLineViewHolder.create(parent, object : ProductLineViewHolder.Listener {
            override fun onProductImpressed(
                viewHolder: ProductLineViewHolder,
                product: PlayProductUiModel.Product,
                section: ProductSectionUiModel.Section
            ) {

            }

            override fun onProductClicked(
                viewHolder: ProductLineViewHolder,
                product: PlayProductUiModel.Product,
                section: ProductSectionUiModel.Section
            ) {

            }

            override fun onBuyProduct(
                viewHolder: ProductLineViewHolder,
                product: PlayProductUiModel.Product,
                section: ProductSectionUiModel.Section
            ) {

            }

            override fun onAtcProduct(
                viewHolder: ProductLineViewHolder,
                product: PlayProductUiModel.Product,
                section: ProductSectionUiModel.Section
            ) {

            }
        })
    }
}