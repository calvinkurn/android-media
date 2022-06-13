package com.tokopedia.tokopedianow.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

class ChooseAddressDataView(val chooseAddressData: LocalCacheModel? = null): Visitable<BaseSearchCategoryTypeFactory> {

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0
}