package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.performanceTracing.components.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.util.DynamicIconBigUtil
import com.tokopedia.home_component.util.DynamicIconSmallUtil
import com.tokopedia.home_component.util.DynamicIconUtil
import com.tokopedia.kotlin.model.ImpressHolder
import java.util.*

/**
 * Created by Lukas on 1/8/21.
 */
data class DynamicIconComponentDataModel(
    val id: String,
    val isCache: Boolean,
    val dynamicIconComponent: DynamicIconComponent,
    val currentFetch: Long = Calendar.getInstance().timeInMillis,
    val numOfRows: Int = 1,
    val type: Type = Type.BIG,
) : HomeComponentVisitable, ImpressHolder(), LoadableComponent by BlocksLoadableComponent(
    { dynamicIconComponent.dynamicIcon.size > 3 },
    "HomeDynamicIcon"
) {
    enum class Type(val scrollableItemThreshold: Int) {
        BIG(3),
        SMALL(0);

        fun isBigIcons() = this == BIG
        fun isSmallIcons() = this == SMALL
    }

    val dynamicIconUtil: DynamicIconUtil = when(type) {
        Type.SMALL -> DynamicIconSmallUtil()
        else -> DynamicIconBigUtil()
    }

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
