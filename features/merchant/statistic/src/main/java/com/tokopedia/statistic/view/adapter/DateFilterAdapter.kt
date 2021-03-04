package com.tokopedia.statistic.view.adapter

import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.statistic.view.adapter.factory.DateFilterAdapterFactoryImpl
import com.tokopedia.statistic.view.adapter.listener.DateFilterListener

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateFilterAdapter(
        listener: DateFilterListener,
        fm: FragmentManager
) : BaseAdapter<DateFilterAdapterFactoryImpl>(DateFilterAdapterFactoryImpl(listener, fm))