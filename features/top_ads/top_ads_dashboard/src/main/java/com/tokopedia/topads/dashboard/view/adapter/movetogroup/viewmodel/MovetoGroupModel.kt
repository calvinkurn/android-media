package com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.movetogroup.MovetoGroupAdapterTypeFactory

abstract class MovetoGroupModel {
    abstract fun type(typesFactory: MovetoGroupAdapterTypeFactory): Int
}