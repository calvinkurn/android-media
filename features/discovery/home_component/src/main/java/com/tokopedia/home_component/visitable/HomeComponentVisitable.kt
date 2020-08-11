package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.HomeComponentTypeFactory

interface HomeComponentVisitable: Visitable<HomeComponentTypeFactory> {
    fun visitableId(): String?
    fun equalsWith(b: Any?): Boolean
    fun getChangePayloadFrom(b: Any?): Bundle?
}