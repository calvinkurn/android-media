package com.tokopedia.statistic.presentation.view.adapter

import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.statistic.presentation.view.adapter.factory.DateFilterAdapterFactoryImpl

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateFilterAdapter(
        listener: DateFilterAdapterFactoryImpl.Listener,
        fm: FragmentManager
) : BaseAdapter<DateFilterAdapterFactoryImpl>(DateFilterAdapterFactoryImpl(listener, fm))