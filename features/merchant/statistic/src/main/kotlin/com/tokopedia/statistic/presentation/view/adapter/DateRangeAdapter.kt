package com.tokopedia.statistic.presentation.view.adapter

import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.statistic.presentation.view.adapter.factory.DateRangeAdapterFactoryImpl

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateRangeAdapter(
        listener: DateRangeAdapterFactoryImpl.Listener,
        fm: FragmentManager
) : BaseAdapter<DateRangeAdapterFactoryImpl>(DateRangeAdapterFactoryImpl(listener, fm))