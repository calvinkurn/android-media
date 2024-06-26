package com.tokopedia.content.product.picker.ugc.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.R as contentcommonR
import com.tokopedia.content.product.picker.ugc.view.adapter.MyShopProductAdapter
import com.tokopedia.content.product.picker.ugc.view.adapter.viewholder.LoadingViewHolder
import com.tokopedia.content.product.picker.ugc.view.adapter.viewholder.MyShopProductViewHolder
import com.tokopedia.content.product.picker.ugc.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on May 09, 2022
 */
internal class MyShopProductAdapterDelegate private constructor() {

    internal class Product(
        private val onSelected: (ProductUiModel, Int) -> Unit,
    ) : TypedAdapterDelegate<
        MyShopProductAdapter.Model.Product,
        MyShopProductAdapter.Model,
        MyShopProductViewHolder.Product>(
        contentcommonR.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: MyShopProductAdapter.Model.Product,
            holder: MyShopProductViewHolder.Product
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): MyShopProductViewHolder.Product {
            return MyShopProductViewHolder.Product.create(
                parent, onSelected
            )
        }
    }

    internal class ProductWithCheckbox(
        private val onSelected: (ProductUiModel, Int) -> Unit,
    ) : TypedAdapterDelegate<
        MyShopProductAdapter.Model.ProductWithCheckbox,
        MyShopProductAdapter.Model,
        MyShopProductViewHolder.ProductWithCheckbox>
    (contentcommonR.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: MyShopProductAdapter.Model.ProductWithCheckbox,
            holder: MyShopProductViewHolder.ProductWithCheckbox
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): MyShopProductViewHolder.ProductWithCheckbox {
            return MyShopProductViewHolder.ProductWithCheckbox.create(
                parent, onSelected
            )
        }
    }

    internal class Loading : TypedAdapterDelegate<
        MyShopProductAdapter.Model.Loading,
        MyShopProductAdapter.Model,
        LoadingViewHolder>
    (contentcommonR.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: MyShopProductAdapter.Model.Loading,
            holder: LoadingViewHolder
        ) {
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): LoadingViewHolder {
            return LoadingViewHolder.create(parent)
        }
    }
}
