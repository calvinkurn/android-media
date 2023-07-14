package com.tokopedia.promocheckout.common.view.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.TextSwitcher
import android.widget.ViewSwitcher
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.promocheckout.common.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify

open class PromoEntryPointWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr)  {

    private fun getLayout(): Int {
        return R.layout.layout_widget_promo_checkout_switcher
    }

    private var loadingView: View? = null
    private var switcherView: ViewSwitcher? = null
    private var activeView: View? = null
    private var activeViewRightIcon: IconUnify? = null
    private var activeViewLeftImage: ImageUnify? = null
    private var activeViewWording: TextSwitcher? = null
    private var inActiveView: View? = null
    private var inActiveViewLeftImage: ImageUnify? = null
    private var inActiveViewWording: TextSwitcher? = null
    private var inActiveViewRightIcon: IconUnify? = null
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
        activeViewLeftImage = activeView?.findViewById(R.id.iv_promo_checkout_left)
        activeViewWording = activeView?.findViewById(R.id.tv_promo_checkout_title)
        activeViewRightIcon = activeView?.findViewById(R.id.ic_promo_checkout_right)
        inActiveView = switcherView?.get(1)
        inActiveViewLeftImage = inActiveView?.findViewById(R.id.iv_promo_checkout_left)
        inActiveViewWording = inActiveView?.findViewById(R.id.tv_promo_checkout_title)
        inActiveViewRightIcon = inActiveView?.findViewById(R.id.ic_promo_checkout_right)

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

    fun showError(onReloadClickListener: () -> Unit) {
        errorView?.visibility = View.VISIBLE
        loadingView?.visibility = View.GONE
        switcherView?.visibility = View.GONE
        errorView?.setOnClickListener {
            onReloadClickListener.invoke()
        }
    }

    fun showInactive(leftImageUrl: String, wording: String, animate: Boolean = false, onClickListener: () -> Unit = {}) {
        if (animate && switcherView?.visibility == View.VISIBLE) {
            switcherView?.visibility = View.VISIBLE
            inActiveViewLeftImage?.setImageUrl(leftImageUrl)
            inActiveViewWording?.setCurrentText(MethodChecker.fromHtml(wording))
            inActiveViewRightIcon?.visibility = View.GONE
            if (switcherView?.displayedChild != 1) {
                // only trigger animation if currently showing different view
                switcherView?.displayedChild = 1
            }
            errorView?.visibility = View.GONE
            loadingView?.visibility = View.GONE
        } else {
            switcherView?.reset()
            inActiveViewLeftImage?.setImageUrl(leftImageUrl)
            inActiveViewWording?.setCurrentText(MethodChecker.fromHtml(wording))
            inActiveViewRightIcon?.visibility = View.GONE
            switcherView?.displayedChild = 1
            switcherView?.visibility = View.VISIBLE
            errorView?.visibility = View.GONE
            loadingView?.visibility = View.GONE
        }
        inActiveView?.setOnClickListener {
            onClickListener.invoke()
        }
    }

    fun showActive(leftImageUrl: String, wording: String, rightIcon: Int, animate: Boolean = false, animateWording: Boolean = false, onClickListener: () -> Unit = {}) {
        if (animate && switcherView?.visibility == View.VISIBLE) {
            switcherView?.visibility = View.VISIBLE
            activeViewLeftImage?.setImageUrl(leftImageUrl)
            activeViewRightIcon?.setImage(rightIcon)
            if (switcherView?.displayedChild != 0) {
                // only trigger view switch animation if currently showing different view
                activeViewWording?.setCurrentText(MethodChecker.fromHtml(wording))
                switcherView?.displayedChild = 0
            } else if (animateWording) {
                activeViewWording?.setText(MethodChecker.fromHtml(wording))
            }
            errorView?.visibility = View.GONE
            loadingView?.visibility = View.GONE
        } else {
            switcherView?.reset()
            activeViewLeftImage?.setImageUrl(leftImageUrl)
            activeViewWording?.setCurrentText(MethodChecker.fromHtml(wording))
            activeViewRightIcon?.setImage(rightIcon)
            switcherView?.displayedChild = 0
            switcherView?.visibility = View.VISIBLE
            errorView?.visibility = View.GONE
            loadingView?.visibility = View.GONE
        }
        activeView?.setOnClickListener {
            onClickListener.invoke()
        }
    }
}
