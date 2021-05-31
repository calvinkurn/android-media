package com.tokopedia.tokomart.searchcategory.presentation.typefactory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView

interface BaseSearchCategoryTypeFactory: AdapterTypeFactory {

    fun type(chooseAddressDataView: ChooseAddressDataView): Int
    fun type(bannerDataView: BannerDataView): Int
    fun type(titleDataView: TitleDataView): Int
    fun type(categoryFilterDataView: CategoryFilterDataView): Int
    fun type(quickFilterDataView: QuickFilterDataView): Int
    fun type(productCountDataView: ProductCountDataView): Int
    fun type(productItemDataView: ProductItemDataView): Int
}