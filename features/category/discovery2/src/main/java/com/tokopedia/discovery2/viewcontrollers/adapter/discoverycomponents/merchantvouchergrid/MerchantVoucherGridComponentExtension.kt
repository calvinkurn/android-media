package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem

object MerchantVoucherGridComponentExtension {
    fun ArrayList<ComponentsItem>.addVoucherList(
        voucherList: List<ComponentsItem>
    ) {
        clear()
        addAll(voucherList)
    }

    fun ArrayList<ComponentsItem>.addShimmer() {
        val shimmerComponent = ComponentsItem(
            name = ComponentNames.Shimmer.componentName,
            shimmerHeight = MerchantVoucherGridViewModel.SHIMMER_COMPONENT_HEIGHT,
            shouldRefreshComponent = true
        )
        add(shimmerComponent)
        add(shimmerComponent)
    }
}
