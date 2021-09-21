package com.tokopedia.smartbills.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.smartbills.data.Section

interface SmartBillsFactory : AdapterTypeFactory {
    fun type(dataModel: Section): Int
}