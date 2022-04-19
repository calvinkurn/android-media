package com.tokopedia.shop.sort.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.sort.view.adapter.ShopProductSortAdapterTypeFactory

/**
 * Created by normansyahputa on 2/24/18.
 */
class ShopProductSortModel : Visitable<ShopProductSortAdapterTypeFactory?> {
    /**
     * @return The name
     */
    /**
     * @param name The name
     */
    var name: String? = null
    /**
     * @return The key
     */
    /**
     * @param key The key
     */
    var key: String? = null
    /**
     * @return The value
     */
    /**
     * @param value The value
     */
    var value: String? = null
    /**
     * @return The inputType
     */
    /**
     * @param inputType The input_type
     */
    var inputType: String? = null
    var isSelected = false
    override fun toString(): String {
        return name!!
    }

    override fun type(shopProductFilterAdapterTypeFactory: ShopProductSortAdapterTypeFactory?): Int {
        return shopProductFilterAdapterTypeFactory?.type(this).orZero()
    }
}