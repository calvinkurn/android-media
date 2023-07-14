package com.tokopedia.promocheckout.common.view.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.promocheckout.common.R

class PromoEntryPointRoundedTopWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : PromoEntryPointWidget(context, attrs, defStyleAttr) {

    override fun setupViewBackgrounds() {
        loadingView?.background = ResourcesCompat.getDrawable(resources, R.drawable.background_promo_checkout_teal_rounded, null)
        activeView?.background = ResourcesCompat.getDrawable(resources, R.drawable.background_promo_checkout_active_teal_rounded, null)
        val inActiveBackground = ResourcesCompat.getDrawable(
            resources,
            R.drawable.background_promo_checkout_teal_rounded,
            null
        )
        (inActiveBackground as? GradientDrawable)?.setColor(ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_NN50, null))
        inActiveView?.background = inActiveBackground
        val errorBackground = ResourcesCompat.getDrawable(
            resources,
            R.drawable.background_promo_checkout_teal_rounded,
            null
        )
        (errorBackground as? GradientDrawable)?.setColor(ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_YN50, null))
        errorView?.background = errorBackground
    }
}
