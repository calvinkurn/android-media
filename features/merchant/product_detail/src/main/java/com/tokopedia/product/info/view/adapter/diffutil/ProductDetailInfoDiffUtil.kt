package com.tokopedia.product.info.view.adapter.diffutil

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableImageDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableListDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoVisitable

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoDiffUtil : DiffUtil.ItemCallback<ProductDetailInfoVisitable>() {
    override fun areItemsTheSame(oldItem: ProductDetailInfoVisitable, newItem: ProductDetailInfoVisitable): Boolean {
        return oldItem.uniqueIdentifier() == newItem.uniqueIdentifier()
    }

    override fun areContentsTheSame(oldItem: ProductDetailInfoVisitable, newItem: ProductDetailInfoVisitable): Boolean {
        return oldItem.equalsWith(newItem) && oldItem.isExpand() == newItem.isExpand()
    }

    override fun getChangePayload(oldItem: ProductDetailInfoVisitable, newItem: ProductDetailInfoVisitable): Any? {
        val bundle = Bundle()
        if ((oldItem is ProductDetailInfoExpandableDataModel && newItem is ProductDetailInfoExpandableDataModel) ||
                (oldItem is ProductDetailInfoExpandableImageDataModel && newItem is ProductDetailInfoExpandableImageDataModel) ||
                (oldItem is ProductDetailInfoExpandableListDataModel && newItem is ProductDetailInfoExpandableListDataModel)) {
            if (oldItem.isExpand() != newItem.isExpand()) {
                bundle.putBoolean(DIFFUTIL_PAYLOAD_TOGGLE, newItem.isExpand())
            }
        }

        return bundle
    }

    companion object {
        const val DIFFUTIL_PAYLOAD_TOGGLE = "toggle"
    }
}