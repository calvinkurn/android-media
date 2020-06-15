package com.tokopedia.statistic.presentation.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.statistic.presentation.view.adapter.factory.DateRangeAdapterFactoryImpl

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateRangeAdapter(listener: DateRangeAdapterFactoryImpl.Listener) : BaseAdapter<DateRangeAdapterFactoryImpl>(DateRangeAdapterFactoryImpl(listener))