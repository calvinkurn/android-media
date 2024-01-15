package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory

class OrigamiSDUIDataModel(val origamiData: String, val visitableID : String) : HomeComponentVisitable {
    override fun visitableId(): String? {
        return visitableID
    }

    override fun equalsWith(b: Any?): Boolean {
        return b == this
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}
