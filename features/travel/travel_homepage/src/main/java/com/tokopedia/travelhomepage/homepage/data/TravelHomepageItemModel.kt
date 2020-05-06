package com.tokopedia.travelhomepage.homepage.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory

/**
 * @author by jessica on 2019-08-09
 */

abstract class TravelHomepageItemModel(var isLoaded: Boolean = false,
                                       var isSuccess: Boolean = false,
                                       var isLoadFromCloud: Boolean = true,
                                       var layoutData: TravelLayoutSubhomepage.Data = TravelLayoutSubhomepage.Data() ): Visitable<TravelHomepageAdapterTypeFactory> {
    abstract override fun type(typeFactory: TravelHomepageAdapterTypeFactory): Int
}