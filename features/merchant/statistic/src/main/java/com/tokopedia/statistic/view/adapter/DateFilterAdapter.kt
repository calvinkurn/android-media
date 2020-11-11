package com.tokopedia.statistic.view.adapter

import android.app.Activity
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.statistic.view.adapter.factory.DateFilterAdapterFactoryImpl

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateFilterAdapter(
        activity: Activity?,
        listener: DateFilterAdapterFactoryImpl.Listener,
        fm: FragmentManager
) : BaseAdapter<DateFilterAdapterFactoryImpl>(DateFilterAdapterFactoryImpl(activity, listener, fm))