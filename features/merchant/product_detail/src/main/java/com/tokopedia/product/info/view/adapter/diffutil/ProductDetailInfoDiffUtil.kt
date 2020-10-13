package com.tokopedia.product.info.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoVisitable

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoDiffUtil : DiffUtil.ItemCallback<ProductDetailInfoVisitable>() {
    override fun areItemsTheSame(oldItem: ProductDetailInfoVisitable, newItem: ProductDetailInfoVisitable): Boolean {
        return oldItem.uniqueIdentifier() == newItem.uniqueIdentifier()
    }

    override fun areContentsTheSame(oldItem: ProductDetailInfoVisitable, newItem: ProductDetailInfoVisitable): Boolean {
        return oldItem.equalsWith(newItem)
    }


}