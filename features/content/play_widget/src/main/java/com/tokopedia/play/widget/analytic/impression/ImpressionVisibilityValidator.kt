package com.tokopedia.play.widget.analytic.impression

import android.view.View

/**
 * Created by jegul on 28/10/20
 */
interface ImpressionVisibilityValidator {

    fun isViewVisibleForImpression(view: View): Boolean
}