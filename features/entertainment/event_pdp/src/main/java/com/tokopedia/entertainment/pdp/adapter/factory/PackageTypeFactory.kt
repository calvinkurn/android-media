package com.tokopedia.entertainment.pdp.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.entertainment.pdp.data.Package

interface PackageTypeFactory: AdapterTypeFactory {

    fun type(dataModel: Package): Int
}