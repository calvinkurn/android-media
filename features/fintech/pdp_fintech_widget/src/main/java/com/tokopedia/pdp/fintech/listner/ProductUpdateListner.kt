package com.tokopedia.pdp.fintech.listner

import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass

interface ProductUpdateListner {

    fun removeWidget()
    fun showWidget()
    fun fintechChipClicked(fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass, redirectionUrl: String)

    fun shouldImpression(): Boolean
}
