package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.DynamicIconComponent

/**
 * Created by Lukas on 1/8/21.
 */
class DynamicIconComponentDataModel(
        val id: String,
        val dynamicIconComponent: DynamicIconComponent
) : HomeComponentVisitable{
    override fun visitableId(): String {
        return id
    }

    override fun equalsWith(b: Any?): Boolean {
        return b is DynamicIconComponentDataModel && b.dynamicIconComponent.dynamicIcon == dynamicIconComponent.dynamicIcon
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}