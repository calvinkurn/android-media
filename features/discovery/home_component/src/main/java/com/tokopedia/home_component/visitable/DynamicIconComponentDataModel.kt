package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.LoadableComponent
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.kotlin.model.ImpressHolder
import java.util.*

/**
 * Created by Lukas on 1/8/21.
 */
data class DynamicIconComponentDataModel(
        val id: String,
        val isCache: Boolean,
        val type: Int,
        val dynamicIconComponent: DynamicIconComponent,
        val currentFetch: Long = Calendar.getInstance().timeInMillis
) : HomeComponentVisitable, ImpressHolder(), LoadableComponent by BlocksLoadableComponent(
    { dynamicIconComponent.dynamicIcon.size > 3 },
    "HomeDynamicIcon"
) {
    override fun visitableId(): String {
        return id
    }

    override fun equalsWith(b: Any?): Boolean {
        return b is DynamicIconComponentDataModel &&
                b.isCache == isCache &&
                b.dynamicIconComponent.dynamicIcon.zip(dynamicIconComponent.dynamicIcon).all { (newIcon, oldIcon) ->
                    newIcon.id == oldIcon.id && newIcon.name == oldIcon.name && newIcon.imageUrl == oldIcon.imageUrl
                }
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}
