package com.tokopedia.product.info.view.adapter

import androidx.recyclerview.widget.AsyncDifferConfig
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoVisitable

/**
 * Created by Yehezkiel on 13/10/20
 */
class BsProductDetailInfoAdapter(differ: AsyncDifferConfig<ProductDetailInfoVisitable>,
                                 adapterTypeFactory: ProductDetailInfoAdapterFactoryImpl) : BsProductDetailInfoBaseAdapter(differ, adapterTypeFactory) {


    fun closeAllExpanded(uniqueIdentifier: Int, toggle: Boolean, currentData: List<ProductDetailInfoVisitable>) {
        val data = currentData.map {
            if (it.uniqueIdentifier() == uniqueIdentifier) {
                val newInstance = it.newInstance()
                newInstance.setIsShowable(toggle)
                newInstance
            } else {
                if (it.isExpand()) {
                    val newInstance = it.newInstance()
                    newInstance.setIsShowable(false)
                    newInstance
                } else {
                    it
                }
            }
        }
        submitList(data)
    }
}