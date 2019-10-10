package com.tokopedia.affiliate.feature.dashboard.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.CuratedProductSortTypeFactory
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactory

/**
 * Created by jegul on 2019-09-09.
 */
class CuratedProductSortAdapter(adapterTypeFactory: CuratedProductSortTypeFactory, visitables: List<Visitable<*>>)
    : BaseAdapter<CuratedProductSortTypeFactory>(adapterTypeFactory, visitables)