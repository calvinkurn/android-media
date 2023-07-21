package com.tokopedia.promocheckout.common.view.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextSwitcher
import android.widget.ViewSwitcher
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.get
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.view.uimodel.PromoEntryPointSummaryItem
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

open class PromoEntryPointWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private fun getLayout(): Int {
        return R.layout.layout_widget_promo_checkout_switcher
    }

    internal var loadingView: View? = null
    internal var switcherView: ViewSwitcher? = null

    internal var activeView: View? = null
    internal var activeViewRightIcon: IconUnify? = null
    internal var activeViewLeftImage: ImageUnify? = null
    internal var activeViewWording: TextSwitcher? = null
    internal var activeViewDivider: DividerUnify? = null
    internal var activeViewSummaryLayout: LinearLayout? = null

    internal var inActiveView: View? = null
    internal var inActiveViewLeftImage: ImageUnify? = null
    internal var inActiveViewWording: TextSwitcher? = null
    internal var inActiveViewRightIcon: IconUnify? = null
    internal var errorView: View? = null

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
        activeViewDivider = activeView?.findViewById(R.id.divider_promo_checkout)
        activeViewSummaryLayout = activeView?.findViewById(R.id.ll_promo_checkout_summary)

        inActiveView = switcherView?.get(1)
        inActiveViewLeftImage = inActiveView?.findViewById(R.id.iv_promo_checkout_left)
        inActiveViewWording = inActiveView?.findViewById(R.id.tv_promo_checkout_title)
        inActiveViewRightIcon = inActiveView?.findViewById(R.id.ic_promo_checkout_right)
        inActiveView?.findViewById<DividerUnify>(R.id.divider_promo_checkout)?.visibility =
            View.GONE
        inActiveView?.findViewById<LinearLayout>(R.id.ll_promo_checkout_summary)?.visibility =
            View.GONE

        setupViewBackgrounds()
    }

    internal open fun setupViewBackgrounds() {
        val loadingBackground = ResourcesCompat.getDrawable(
            resources,
            R.drawable.background_promo_checkout_teal_gradient,
            null
        )
        val n0Color = ResourcesCompat.getColor(
            resources,
            com.tokopedia.unifyprinciples.R.color.Unify_NN0,
            null
        )
        val t100Color = ResourcesCompat.getColor(
            resources,
            com.tokopedia.unifyprinciples.R.color.Unify_TN100,
            null
        )
        val t100ColorAlpha = ColorUtils.setAlphaComponent(t100Color, 56)
        val loadingDrawable = loadingBackground as? GradientDrawable
        loadingDrawable?.mutate()
        loadingDrawable?.colors = intArrayOf(t100ColorAlpha, n0Color)
        loadingView?.background = loadingBackground
        val inActiveBackground = ResourcesCompat.getDrawable(
            resources,
            R.drawable.background_promo_checkout_teal_gradient,
            null
        )
        val gradientDrawable = inActiveBackground as? GradientDrawable
        gradientDrawable?.mutate()
        val nn50Color = ResourcesCompat.getColor(
            resources,
            com.tokopedia.unifyprinciples.R.color.Unify_NN50,
            null
        )
        val nn50ColorAlpha = ColorUtils.setAlphaComponent(nn50Color, 163)
        gradientDrawable?.colors = intArrayOf(
            nn50ColorAlpha,
            n0Color
        )
        inActiveView?.background = inActiveBackground
        activeView?.background = loadingBackground
        val errorBackground = ResourcesCompat.getDrawable(
            resources,
            R.drawable.background_promo_checkout_teal,
            null
        )
        (errorBackground as? GradientDrawable)?.setColor(
            ResourcesCompat.getColor(
                resources,
                com.tokopedia.unifyprinciples.R.color.Unify_YN50,
                null
            )
        )
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

    fun showInactive(
        leftImageUrl: String,
        wording: String,
        animate: Boolean = false,
        onClickListener: () -> Unit = {}
    ) {
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

    fun showActive(
        leftImageUrl: String,
        wording: String,
        rightIcon: Int,
        animate: Boolean = false,
        animateWording: Boolean = false,
        onClickListener: () -> Unit = {}
    ) {
        activeViewSummaryLayout?.visibility = View.GONE
        activeViewDivider?.visibility = View.GONE
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

    fun showApplied(
        leftImageUrl: String,
        wording: String,
        rightIcon: Int,
        summaries: List<PromoEntryPointSummaryItem>,
        animate: Boolean = false,
        animateWording: Boolean = false,
        onClickListener: () -> Unit = {}
    ) {
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
        if (summaries.isNotEmpty()) {
            activeViewSummaryLayout?.visibility = View.VISIBLE
            activeViewDivider?.visibility = View.VISIBLE

            activeViewSummaryLayout?.apply {
                removeAllViews()
                summaries.forEach {
                    val summaryView = LayoutInflater.from(this.context)
                        .inflate(R.layout.layout_item_promo_checkout_summary, this, false)
                    summaryView.findViewById<Typography>(R.id.tv_promo_checkout_summary_title).text =
                        it.title
                    summaryView.findViewById<Typography>(R.id.tv_promo_checkout_summary_value).text =
                        it.value
                    this.addView(summaryView)
                }
            }
        } else {
            activeViewSummaryLayout?.visibility = View.GONE
            activeViewDivider?.visibility = View.GONE
        }
        activeView?.setOnClickListener {
            onClickListener.invoke()
        }
    }
}
