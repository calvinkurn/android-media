package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.LIGHT_BLUE
import com.tokopedia.discovery2.LIGHT_GREEN
import com.tokopedia.discovery2.LIGHT_GREY
import com.tokopedia.discovery2.LIGHT_RED
import com.tokopedia.discovery2.LIGHT_ORANGE
import com.tokopedia.discovery2.DARK_GREY
import com.tokopedia.discovery2.DARK_BLUE
import com.tokopedia.discovery2.DARK_GREEN
import com.tokopedia.discovery2.DARK_RED
import com.tokopedia.discovery2.DARK_ORANGE
import com.tokopedia.discovery2.TRANSPARENT_BLACK
import com.tokopedia.discovery2.data.productcarditem.LabelsGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.Label


internal fun Label.initLabelGroup(labelGroup : LabelsGroup?) {
    if (labelGroup == null) hide()
    else showLabel(labelGroup)
}

private fun Label.showLabel(labelGroup: LabelsGroup) {
    shouldShowWithAction(labelGroup.title.isNotEmpty()) {
        it.text = MethodChecker.fromHtml(labelGroup.title)
        it.determineLabelType(labelGroup.type)
    }
}

private fun Label.determineLabelType(labelGroupType: String) {
    val unifyLabelType = labelGroupType.toUnifyLabelType()

    if (unifyLabelType != -1) setLabelType(unifyLabelType)
    else setCustomLabelType(labelGroupType)
}

private fun String?.toUnifyLabelType(): Int {
    return when (this) {
        LIGHT_GREY -> Label.GENERAL_LIGHT_GREY
        LIGHT_BLUE -> Label.GENERAL_LIGHT_BLUE
        LIGHT_GREEN -> Label.GENERAL_LIGHT_GREEN
        LIGHT_RED -> Label.GENERAL_LIGHT_RED
        LIGHT_ORANGE -> Label.GENERAL_LIGHT_ORANGE
        DARK_GREY -> Label.GENERAL_DARK_GREY
        DARK_BLUE -> Label.GENERAL_DARK_BLUE
        DARK_GREEN -> Label.GENERAL_DARK_GREEN
        DARK_RED -> Label.GENERAL_DARK_RED
        DARK_ORANGE -> Label.GENERAL_DARK_ORANGE
        else -> -1
    }
}

private fun Label.setCustomLabelType(labelGroupType: String) {
    try {
        trySetCustomLabelType(labelGroupType)
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    }
}

private fun Label.trySetCustomLabelType(labelGroupType: String) {
    unlockFeature = true

    val colorRes = labelGroupType.toUnifyLabelColor()
    val colorHexInt = ContextCompat.getColor(context, colorRes)
    val colorHexString = "#${Integer.toHexString(colorHexInt)}"
    setLabelType(colorHexString)
}

@ColorRes
private fun String?.toUnifyLabelColor(): Int {
    return when (this) {
        TRANSPARENT_BLACK -> com.tokopedia.unifyprinciples.R.color.Unify_N700_68
        else -> com.tokopedia.unifyprinciples.R.color.Unify_N700_68
    }

}

internal fun <T: View> T?.shouldShowWithAction(shouldShow: Boolean, action: (T) -> Unit) {
    if (this == null) return
    if (shouldShow) {
        this.visibility = View.VISIBLE
        action(this)
    } else {
        this.visibility = View.GONE
    }
}

