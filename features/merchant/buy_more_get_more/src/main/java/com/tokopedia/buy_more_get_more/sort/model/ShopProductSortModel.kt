package com.tokopedia.buy_more_get_more.sort.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buy_more_get_more.sort.adapter.ShopProductSortAdapterTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero


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

