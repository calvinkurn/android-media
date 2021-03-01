package com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.movetogroup.MovetoGroupAdapterTypeFactory

/**
 * Created by Pika on 7/6/20.
 */
class MovetoGroupEmptyModel : MovetoGroupModel() {
    override fun type(typesFactory: MovetoGroupAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}