package com.tokopedia.shop_showcase.shop_showcase_add.presentation.adapter

import com.tokopedia.shop_showcase.shop_showcase_add.presentation.model.AddShowcaseHeader

/**
 * @author by Rafli Syam on 2020-03-04
 */
interface TypeFactoryViewHolder {
    fun type(addShowcaseHeader: AddShowcaseHeader): Int
}