package com.tokopedia.affiliate.feature.dashboard.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.AffiliateDashboardItemTypeFactory

/**
 * Created by jegul on 2019-09-02.
 */
class AffiliateDashboardAdapter(adapterTypeFactory: AffiliateDashboardItemTypeFactory, visitables: List<Visitable<*>>)
    : BaseAdapter<AffiliateDashboardItemTypeFactory>(adapterTypeFactory, visitables)