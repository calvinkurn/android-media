package com.tokopedia.promocheckout.common.view.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ViewSwitcher
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.get
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.view.uimodel.PromoEntryPointSummaryItem
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class PromoEntryPointWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private fun getLayout(): Int {
        return R.layout.layout_widget_promo_checkout_switcher
    }

    private var loadingView: View? = null
    private var switcherView: ViewSwitcher? = null

    private var activeView: View? = null
    private var activeViewRightIcon: IconUnify? = null
    private var activeViewLeftImage: ImageUnify? = null
    private var activeViewWording: TextFlipper? = null
    private var activeViewTitleWording: Typography? = null
    private var activeViewDescWording: Typography? = null
    private var activeViewDivider: DividerUnify? = null
    private var activeViewSummaryLayout: LinearLayout? = null
    private var activeViewConfettiFrame: ImageUnify? = null

    private var inActiveView: View? = null
    private var inActiveViewLeftImage: ImageUnify? = null
    private var inActiveViewWording: TextFlipper? = null
    private var inActiveViewRightIcon: IconUnify? = null
    private var errorView: View? = null

    init {
        inflate(context, getLayout(), this)
        setupViews(attrs)
    }

    private fun setupViews(attrs: AttributeSet?) {
        loadingView = findViewById(R.id.loader_promo_checkout)
        errorView = findViewById(R.id.error_promo_checkout)
        switcherView = findViewById(R.id.switcher_promo_checkout)

        activeView = switcherView?.get(0)
        activeViewLeftImage = activeView?.findViewById(R.id.iv_promo_checkout_left)
        activeViewWording = activeView?.findViewById(R.id.tv_promo_checkout_text)
        activeViewTitleWording = activeView?.findViewById(R.id.tv_promo_checkout_title_wording)
        activeViewDescWording = activeView?.findViewById(R.id.tv_promo_checkout_desc_wording)
        activeViewRightIcon = activeView?.findViewById(R.id.ic_promo_checkout_right)
        activeViewDivider = activeView?.findViewById(R.id.divider_promo_checkout)
        activeViewSummaryLayout = activeView?.findViewById(R.id.ll_promo_checkout_summary)
        activeViewConfettiFrame = activeView?.findViewById(R.id.frame_promo_checkout_header)

        inActiveView = switcherView?.get(1)
        inActiveViewLeftImage = inActiveView?.findViewById(R.id.iv_promo_checkout_left)
        inActiveViewWording = inActiveView?.findViewById(R.id.tv_promo_checkout_text)
        inActiveViewRightIcon = inActiveView?.findViewById(R.id.ic_promo_checkout_right)
        inActiveView?.findViewById<Typography>(R.id.tv_promo_checkout_title_wording)?.visibility =
            View.GONE
        inActiveView?.findViewById<Typography>(R.id.tv_promo_checkout_desc_wording)?.visibility =
            View.GONE
        inActiveView?.findViewById<DividerUnify>(R.id.divider_promo_checkout)?.visibility =
            View.GONE
        inActiveView?.findViewById<LinearLayout>(R.id.ll_promo_checkout_summary)?.visibility =
            View.GONE
        inActiveView?.findViewById<ImageUnify>(R.id.frame_promo_checkout_header)?.visibility =
            View.GONE

        setupViewBackgrounds(attrs)

        // workaround issue wrap content
        activeViewSummaryLayout?.visibility = View.GONE
        activeViewDivider?.visibility = View.GONE
        activeViewTitleWording?.visibility = View.GONE
        activeViewDescWording?.visibility = View.GONE
    }

    private fun setupViewBackgrounds(attrs: AttributeSet?) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.PromoEntryPointWidget)
        try {
            if (styledAttributes.getBoolean(R.styleable.PromoEntryPointWidget_rounded, false)) {
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
            } else {
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
        } finally {
            styledAttributes.recycle()
        }
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
        wording: String,
        onClickListener: () -> Unit = {}
    ) {
        switcherView?.reset()
        inActiveViewLeftImage?.setImageUrl("https://images.tokopedia.net/img/ios/promo_widget/disabled_product.png")
        inActiveViewWording?.setCurrentText(HtmlLinkHelper(context, wording).spannedString)
        inActiveViewRightIcon?.visibility = View.GONE
        switcherView?.displayedChild = 1
        switcherView?.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
        loadingView?.visibility = View.GONE
        inActiveView?.setOnClickListener {
            onClickListener.invoke()
        }
    }

    fun showInactiveNew(
        leftImageUrl: String,
        wording: String,
        animate: Boolean = false,
        onClickListener: () -> Unit = {}
    ) {
        if (animate && switcherView?.visibility == View.VISIBLE) {
            switcherView?.visibility = View.VISIBLE
            inActiveViewLeftImage?.setImageUrl(leftImageUrl)
            inActiveViewWording?.setCurrentText(HtmlLinkHelper(context, wording).spannedString)
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
            inActiveViewWording?.setCurrentText(HtmlLinkHelper(context, wording).spannedString)
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

    fun showActiveNew(
        leftImageUrl: String,
        wording: String,
        rightIcon: Int,
        animate: Boolean = false,
        animateWording: Boolean = false,
        onClickListener: () -> Unit = {}
    ) {
        activeViewConfettiFrame?.visibility = View.GONE
        activeViewSummaryLayout?.visibility = View.GONE
        activeViewDivider?.visibility = View.GONE
        activeViewTitleWording?.visibility = View.GONE
        activeViewDescWording?.visibility = View.GONE
        activeViewWording?.visibility = View.VISIBLE
        if (animate && switcherView?.visibility == View.VISIBLE) {
            activeViewLeftImage?.setImageUrl(leftImageUrl)
            activeViewRightIcon?.setImage(rightIcon)
            if (switcherView?.displayedChild != 0) {
                // only trigger view switch animation if currently showing different view
                activeViewWording?.setCurrentText(HtmlLinkHelper(context, wording).spannedString)
                switcherView?.displayedChild = 0
            } else if (animateWording) {
                activeViewWording?.setText(HtmlLinkHelper(context, wording).spannedString)
            }
            errorView?.visibility = View.GONE
            loadingView?.visibility = View.GONE
        } else {
            switcherView?.reset()
            activeViewLeftImage?.setImageUrl(leftImageUrl)
            activeViewWording?.setCurrentText(HtmlLinkHelper(context, wording).spannedString)
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

    // cart makin hemat pakai promo
    fun showActive(
        wording: String,
        rightIcon: Int,
        onClickListener: () -> Unit = {}
    ) {
        activeViewConfettiFrame?.visibility = View.GONE
        activeViewSummaryLayout?.visibility = View.GONE
        activeViewDivider?.visibility = View.GONE
        activeViewTitleWording?.visibility = View.GONE
        activeViewDescWording?.visibility = View.GONE
        activeViewWording?.visibility = View.VISIBLE
        switcherView?.reset()
        activeViewLeftImage?.setImageUrl("https://images.tokopedia.net/img/ios/promo_widget/promo_coupon.png")
        activeViewWording?.setCurrentText(HtmlLinkHelper(context, wording).spannedString)
        activeViewRightIcon?.setImage(rightIcon)
        switcherView?.displayedChild = 0
        switcherView?.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
        loadingView?.visibility = View.GONE
        activeView?.setOnClickListener {
            onClickListener.invoke()
        }
    }

    private fun setFlippingDuration(durationInMs: Long) {
        activeViewWording?.setFlippingDuration(durationInMs)
        inActiveViewWording?.setFlippingDuration(durationInMs)
    }

    private fun setMaximumFlippingCount(count: Int) {
        activeViewWording?.setMaximumFlippingCount(count)
        inActiveViewWording?.setMaximumFlippingCount(count)
    }

    fun showActiveFlipping(
        leftImageUrl: String,
        wordings: List<String>,
        rightIcon: Int,
        flippingDurationInMs: Long,
        maximumFlippingCount: Int,
        onClickListener: () -> Unit = {}
    ) {
        if (wordings.isEmpty()) {
            return
        }
        activeViewConfettiFrame?.visibility = View.GONE
        activeViewSummaryLayout?.visibility = View.GONE
        activeViewDivider?.visibility = View.GONE
        activeViewWording?.visibility = View.VISIBLE
        activeViewTitleWording?.visibility = View.GONE
        activeViewDescWording?.visibility = View.GONE
        switcherView?.reset()
        activeViewLeftImage?.setImageUrl(leftImageUrl)
        activeViewWording?.setCurrentText(HtmlLinkHelper(context, wordings.first()).spannedString)
        setFlippingDuration(flippingDurationInMs)
        setMaximumFlippingCount(maximumFlippingCount)
        if (wordings.size > 1) {
            activeViewWording?.startFlipping(wordings)
        }
        activeViewRightIcon?.setImage(rightIcon)
        switcherView?.displayedChild = 0
        switcherView?.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
        loadingView?.visibility = View.GONE
        activeView?.setOnClickListener {
            onClickListener.invoke()
        }
    }

    // for old promo in cart & checkout, when has promo applied
    fun showApplied(
        title: String,
        desc: String,
        rightIcon: Int?,
        summaries: List<PromoEntryPointSummaryItem>,
        showConfetti: Boolean = false,
        onClickListener: () -> Unit = {}
    ) {
        switcherView?.reset()
        activeViewConfettiFrame?.visibility = if (showConfetti) View.VISIBLE else View.GONE
        activeViewLeftImage?.setImageUrl("https://images.tokopedia.net/img/ios/promo_widget/checklist.png")
        activeViewTitleWording?.text = HtmlLinkHelper(context, title).spannedString
        activeViewDescWording?.text = HtmlLinkHelper(context, desc).spannedString
        activeViewWording?.visibility = View.GONE
        activeViewTitleWording?.visibility = View.VISIBLE
        activeViewDescWording?.visibility = View.VISIBLE
        if (rightIcon == null) {
            activeViewRightIcon?.visibility = View.INVISIBLE
        } else {
            activeViewRightIcon?.visibility = View.VISIBLE
            activeViewRightIcon?.setImage(rightIcon)
        }
        switcherView?.displayedChild = 0
        switcherView?.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
        loadingView?.visibility = View.GONE
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
                    if (it.subValue.isNotEmpty()) {
                        summaryView.findViewById<Typography>(R.id.tv_promo_checkout_summary_subvalue).apply {
                            text = it.value
                            visibility = View.VISIBLE
                        }
                    } else {
                        summaryView.findViewById<Typography>(R.id.tv_promo_checkout_summary_subvalue).visibility = View.GONE
                    }
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
