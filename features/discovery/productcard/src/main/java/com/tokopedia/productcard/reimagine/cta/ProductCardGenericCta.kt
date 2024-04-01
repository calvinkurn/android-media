package com.tokopedia.productcard.reimagine.cta

import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.applyConstraintSet
import com.tokopedia.productcard.utils.getPixel

internal data class GenericCtaConstraints(
    @IdRes val start: Int,
    @IdRes val top: Int,
    @IdRes val end: Int,
    @IdRes val bottom: Int,
)

internal fun GenericCtaLayout(
    constraintLayout: ConstraintLayout,
    constraints: GenericCtaConstraints,
): LinearLayout? {
    val context = constraintLayout.context

    LayoutInflater
        .from(context)
        .inflate(R.layout.product_card_reimagine_cta_button, constraintLayout)
    constraintLayout.applyConstraintSet {
        val marginTop =
            context.getPixel(R.dimen.product_card_reimagine_button_atc_margin_top)
        connect(
            R.id.productCardGenericCta,
            ConstraintSet.TOP, constraints.top,
            ConstraintSet.BOTTOM, marginTop
        )
        connect(
            R.id.productCardGenericCta,
            ConstraintSet.BOTTOM, constraints.bottom,
            ConstraintSet.BOTTOM
        )
        connect(
            R.id.productCardGenericCta,
            ConstraintSet.START, constraints.start,
            ConstraintSet.START
        )
        connect(
            R.id.productCardGenericCta,
            ConstraintSet.END, constraints.end,
            ConstraintSet.END
        )
    }

    return constraintLayout.findViewById(R.id.productCardGenericCta)
}
