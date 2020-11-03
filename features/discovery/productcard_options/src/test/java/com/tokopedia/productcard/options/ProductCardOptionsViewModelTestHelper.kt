package com.tokopedia.productcard.options

import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel

internal fun ProductCardOptionsViewModel.getOption(optionTitle: String): ProductCardOptionsItemModel {
    return this.getOptionsListLiveData().value?.single {
        it is ProductCardOptionsItemModel && it.title == optionTitle
    } as ProductCardOptionsItemModel
}