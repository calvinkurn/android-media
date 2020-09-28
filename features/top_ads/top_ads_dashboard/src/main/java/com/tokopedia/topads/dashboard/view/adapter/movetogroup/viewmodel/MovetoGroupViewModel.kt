package com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.movetogroup.MovetoGroupAdapterTypeFactory

abstract class MovetoGroupViewModel {
    abstract fun type(typesFactory: MovetoGroupAdapterTypeFactory): Int
}