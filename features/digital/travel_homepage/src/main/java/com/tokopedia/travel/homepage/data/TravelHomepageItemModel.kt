package com.tokopedia.travel.homepage.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.travel.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory

/**
 * @author by jessica on 2019-08-09
 */

abstract class TravelHomepageItemModel(var isLoaded: Boolean = false,
                                       var isSuccess: Boolean = false,
                                       var isLoadFromCloud: Boolean = true): Visitable<TravelHomepageAdapterTypeFactory> {
    abstract override fun type(typeFactory: TravelHomepageAdapterTypeFactory): Int
}