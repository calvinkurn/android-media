package com.tokopedia.play.widget.analytic.impression

import com.tokopedia.play.widget.analytic.ImpressionableModel

/**
 * Created by jegul on 28/10/20
 */
class DefaultImpressionValidator : ImpressionValidator {

    private val impressionMap: MutableMap<ImpressionableModel, Boolean> = mutableMapOf()

    override fun isEligibleForImpression(model: ImpressionableModel): Boolean {
        return !model.impressHolder.isInvoke && impressionMap[model] != true
    }

    override fun onImpressed(model: ImpressionableModel) {
        model.impressHolder()
        impressionMap[model] = true
    }

    fun invalidate() {
        impressionMap.clear()
    }
}