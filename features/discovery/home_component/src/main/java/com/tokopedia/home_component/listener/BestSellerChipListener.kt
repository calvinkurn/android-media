package com.tokopedia.home_component.listener

import com.tokopedia.home_component.visitable.BestSellerChipDataModel

internal interface BestSellerChipListener {

    fun onChipImpressed(chip: BestSellerChipDataModel, chipPosition: Int)

    fun onChipClicked(chip: BestSellerChipDataModel, chipPosition: Int)
}
