package com.tokopedia.promocheckout.common.view.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ViewSwitcher
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import com.tokopedia.promocheckout.common.R
import com.tokopedia.unifycomponents.BaseCustomView

open class PromoEntryPointWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr)  {

    private fun getLayout(): Int {
        return R.layout.layout_widget_promo_checkout_switcher
    }

    private var loadingView: View? = null
    private var switcherView: ViewSwitcher? = null
    private var activeView: View? = null
    private var inActiveView: View? = null
    private var errorView: View? = null

    init {
        inflate(context, getLayout(), this)
        setupViews()
    }

    private fun setupViews() {
        loadingView = findViewById(R.id.loader_promo_checkout)
        errorView = findViewById(R.id.error_promo_checkout)
        switcherView = findViewById(R.id.switcher_promo_checkout)
        activeView = switcherView?.get(0)
        inActiveView = switcherView?.get(1)

        val loadingBackground = ResourcesCompat.getDrawable(resources, R.drawable.background_promo_checkout_teal_gradient, null)
//        val n0Color = ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_NN1000, null)
//        val t100Color = ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_TN100, null)
//        val t100ColorAlpha = ColorUtils.setAlphaComponent(t100Color, 56)
//        (loadingBackground as? GradientDrawable)?.colors = intArrayOf(t100ColorAlpha, n0Color)
        loadingView?.background = loadingBackground
        activeView?.background = ResourcesCompat.getDrawable(resources, R.drawable.background_promo_checkout_teal_gradient, null)
        val inActiveBackground = ResourcesCompat.getDrawable(
            resources,
            R.drawable.background_promo_checkout_teal,
            null
        )
        (inActiveBackground as? GradientDrawable)?.setColor(ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_NN50, null))
        inActiveView?.background = inActiveBackground
        val errorBackground = ResourcesCompat.getDrawable(
            resources,
            R.drawable.background_promo_checkout_teal,
            null
        )
        (errorBackground as? GradientDrawable)?.setColor(ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_YN50, null))
        errorView?.background = errorBackground
    }

    fun showLoading() {
        loadingView?.visibility = View.VISIBLE
        switcherView?.visibility = View.GONE
        errorView?.visibility = View.GONE
    }

    fun showError() {
        errorView?.visibility = View.VISIBLE
        loadingView?.visibility = View.GONE
        switcherView?.visibility = View.GONE
    }


}
