package com.tokopedia.tokopedianow.searchcategory.presentation.typefactory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.tokopedianow.searchcategory.presentation.model.*

interface BaseSearchCategoryTypeFactory: AdapterTypeFactory {

    fun type(chooseAddressDataView: ChooseAddressDataView): Int
    fun type(bannerDataView: BannerDataView): Int
    fun type(titleDataView: TitleDataView): Int
    fun type(categoryFilterDataView: CategoryFilterDataView): Int
    fun type(quickFilterDataView: QuickFilterDataView): Int
    fun type(productCountDataView: ProductCountDataView): Int
    fun type(productItemDataView: ProductItemDataView): Int
    fun type(progressBarDataView: ProgressBarDataView): Int
    fun type(switcherWidgetDataView: SwitcherWidgetDataView): Int
}