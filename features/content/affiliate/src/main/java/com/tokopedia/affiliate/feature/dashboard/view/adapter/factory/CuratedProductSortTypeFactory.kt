package com.tokopedia.affiliate.feature.dashboard.view.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CuratedProductSortViewModel

/**
 * Created by jegul on 2019-09-09.
 */
interface CuratedProductSortTypeFactory : AdapterTypeFactory {

    fun type(curatedProductSortViewModel: CuratedProductSortViewModel): Int

}