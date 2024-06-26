package com.tokopedia.tokopedianow.searchcategory.presentation.typefactory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.tokopedianow.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProgressBarDataView

interface BaseSearchCategoryTypeFactory: AdapterTypeFactory {

    fun type(chooseAddressDataView: ChooseAddressDataView): Int
    fun type(bannerDataView: BannerDataView): Int
    fun type(titleDataView: TitleDataView): Int
    fun type(categoryFilterDataView: CategoryFilterDataView): Int
    fun type(quickFilterDataView: QuickFilterDataView): Int
    fun type(productCountDataView: ProductCountDataView): Int
    fun type(progressBarDataView: ProgressBarDataView): Int
}
