package com.tokopedia.createpost.producttag.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.producttag.view.adapter.MyShopProductAdapter
import com.tokopedia.createpost.producttag.view.adapter.viewholder.MyShopProductViewHolder
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

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
        R.layout.view_cc_empty) {

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

    internal class Loading : TypedAdapterDelegate<
            MyShopProductAdapter.Model.Loading,
            MyShopProductAdapter.Model,
            MyShopProductViewHolder.Loading>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: MyShopProductAdapter.Model.Loading,
            holder: MyShopProductViewHolder.Loading
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): MyShopProductViewHolder.Loading {
            return MyShopProductViewHolder.Loading.create(parent)
        }
    }
}