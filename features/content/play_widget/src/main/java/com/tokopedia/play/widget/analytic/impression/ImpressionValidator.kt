package com.tokopedia.play.widget.analytic.impression

import com.tokopedia.play.widget.analytic.ImpressionableModel

/**
 * Created by jegul on 28/10/20
 */
interface ImpressionValidator {

    fun isEligibleForImpression(model: ImpressionableModel): Boolean

    fun onImpressed(model: ImpressionableModel)
}