package com.tokopedia.sellerhomecommon.presentation.view.adapter

import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.sellerhomecommon.presentation.view.adapter.factory.DateFilterAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.view.adapter.listener.DateFilterListener

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

class DateFilterAdapter(
    listener: DateFilterListener,
    fm: FragmentManager
) : BaseAdapter<DateFilterAdapterFactoryImpl>(DateFilterAdapterFactoryImpl(listener, fm))