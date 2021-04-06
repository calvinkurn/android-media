package com.tokopedia.play.widget.analytic.impression

import android.view.View
import android.view.ViewTreeObserver
import com.tokopedia.play.widget.analytic.ImpressionableModel

/**
 * Created by jegul on 28/10/20
 */
class ImpressionHelper(
        private val validator: ImpressionValidator = DefaultImpressionValidator(),
        private val visibilityValidator: ImpressionVisibilityValidator = DefaultImpressionVisibilityValidator()
) {

    fun impress(view: View, model: ImpressionableModel, doOnImpressed: () -> Unit) {
        if (isEligible(view, model)) {
            view.viewTreeObserver.addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
                override fun onScrollChanged() {
                    if (visibilityValidator.isViewVisibleForImpression(view)) {
                        if (isEligible(view, model)) {
                            doOnImpressed()
                            validator.onImpressed(model)
                        }
                        view.viewTreeObserver.removeOnScrollChangedListener(this)
                    }
                }
            })
        }
    }

    private fun isEligible(view: View, model: ImpressionableModel): Boolean {
        return validator.isEligibleForImpression(model)
    }
}