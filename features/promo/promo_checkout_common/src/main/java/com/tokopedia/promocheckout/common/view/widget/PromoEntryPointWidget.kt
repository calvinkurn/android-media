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
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.updateLayoutParams
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.view.uimodel.PromoEntryPointSummaryItem
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class PromoEntryPointWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private fun getLayout(): Int {
        return R.layout.layout_widget_promo_checkout_switcher
    }

    private var loadingView: View? = null
    private var loaderStart: LoaderUnify? = null
    private var loaderCenter: LoaderUnify? = null
    private var loaderEnd: LoaderUnify? = null

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
    private var activeViewFrame: View? = null

    private var inActiveView: View? = null
    private var inActiveViewFrame: View? = null
    private var inActiveViewLeftImage: ImageUnify? = null
    private var inActiveViewWording: TextFlipper? = null
    private var inActiveViewRightIcon: IconUnify? = null
    private var errorView: View? = null

    private var isRounded: Boolean = false

    init {
        inflate(context, getLayout(), this)
        setupViews(attrs)
    }

    private fun setupViews(attrs: AttributeSet?) {
        loadingView = findViewById(R.id.loader_promo_checkout)
        loaderStart = findViewById(R.id.loader_promo_start)
        loaderCenter = findViewById(R.id.loader_promo_center)
        loaderEnd = findViewById(R.id.loader_promo_end)

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
        activeViewConfettiFrame =
            activeView?.findViewById(R.id.frame_promo_checkout_header_confetti)
        activeViewFrame = activeView?.findViewById(R.id.frame_promo_checkout_header)

        inActiveView = switcherView?.get(1)
        inActiveViewLeftImage = inActiveView?.findViewById(R.id.iv_promo_checkout_left)
        inActiveViewWording = inActiveView?.findViewById(R.id.tv_promo_checkout_text)
        inActiveViewRightIcon = inActiveView?.findViewById(R.id.ic_promo_checkout_right)
        inActiveViewFrame = inActiveView?.findViewById(R.id.frame_promo_checkout_header)
        inActiveView?.findViewById<Typography>(R.id.tv_promo_checkout_title_wording)?.visibility =
            View.GONE
        inActiveView?.findViewById<Typography>(R.id.tv_promo_checkout_desc_wording)?.visibility =
            View.GONE
        inActiveView?.findViewById<DividerUnify>(R.id.divider_promo_checkout)?.visibility =
            View.GONE
        inActiveView?.findViewById<LinearLayout>(R.id.ll_promo_checkout_summary)?.visibility =
            View.GONE
        inActiveView?.findViewById<ImageUnify>(R.id.frame_promo_checkout_header_confetti)?.visibility =
            View.GONE

        setupViewBackgrounds(attrs)

        // workaround issue wrap content
        activeViewSummaryLayout?.visibility = View.GONE
        activeViewDivider?.visibility = View.GONE
        activeViewTitleWording?.visibility = View.GONE
        activeViewDescWording?.visibility = View.GONE
    }

    private fun setupViewBackgrounds(attrs: AttributeSet?) {
        val styledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.PromoEntryPointWidget)
        try {
            isRounded =
                styledAttributes.getBoolean(R.styleable.PromoEntryPointWidget_rounded, false)
            if (isRounded) {
                loadingView?.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.background_promo_checkout_teal_rounded,
                    null
                )
                activeViewFrame?.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.background_promo_checkout_active_teal_rounded,
                    null
                )
                val inActiveBackground = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.background_promo_checkout_inactive_grey_rounded,
                    null
                )
                inActiveViewFrame?.background = inActiveBackground
                errorView?.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.background_promo_checkout_error_yellow_rounded,
                    null
                )

                activeViewRightIcon?.updateLayoutParams<MarginLayoutParams> {
                    marginEnd = 16.dpToPx(context.resources.displayMetrics)
                }
                findViewById<View>(R.id.ic_error_promo_checkout)?.updateLayoutParams<MarginLayoutParams> {
                    marginEnd = 0
                }
            } else {
                val loadingBackground = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.background_promo_checkout_teal_gradient,
                    null
                )
                val n0Color = ResourcesCompat.getColor(
                    resources,
                    unifyprinciplesR.color.Unify_NN0,
                    null
                )
                val t100Color = ResourcesCompat.getColor(
                    resources,
                    unifyprinciplesR.color.Unify_TN100,
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
                    unifyprinciplesR.color.Unify_NN50,
                    null
                )
                val nn50ColorAlpha = ColorUtils.setAlphaComponent(nn50Color, 163)
                gradientDrawable?.colors = intArrayOf(
                    nn50ColorAlpha,
                    n0Color
                )
                inActiveViewFrame?.background = inActiveBackground
                activeViewFrame?.background = loadingBackground
                val errorBackground = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.background_promo_checkout_teal,
                    null
                )
                (errorBackground as? GradientDrawable)?.setColor(
                    ResourcesCompat.getColor(
                        resources,
                        unifyprinciplesR.color.Unify_YN50,
                        null
                    )
                )
                errorView?.background = errorBackground

                activeViewRightIcon?.updateLayoutParams<MarginLayoutParams> {
                    marginEnd = 28.dpToPx(context.resources.displayMetrics)
                }
                findViewById<View>(R.id.ic_error_promo_checkout)?.updateLayoutParams<MarginLayoutParams> {
                    marginEnd = 12.dpToPx(context.resources.displayMetrics)
                }
            }
        } finally {
            styledAttributes.recycle()
        }
    }

    fun showLoading() {
        loaderStart?.type = LoaderUnify.TYPE_CIRCLE
        loaderCenter?.type = LoaderUnify.TYPE_LINE
        loaderEnd?.type = LoaderUnify.TYPE_LINE
        loadingView?.visibility = View.VISIBLE
        switcherView?.visibility = View.GONE
        errorView?.visibility = View.GONE
        activeViewWording?.stopFlipping()
    }

    fun showError(onReloadClickListener: () -> Unit) {
        errorView?.visibility = View.VISIBLE
        loadingView?.visibility = View.GONE
        switcherView?.visibility = View.GONE
        errorView?.setOnClickListener {
            onReloadClickListener.invoke()
        }
        activeViewWording?.stopFlipping()
    }

    /**
     * show inactive state for cart page without promo revamp (when user have not select any product)
     */
    fun showInactive(
        wording: String,
        onClickListener: () -> Unit = {}
    ) {
        switcherView?.reset()
        inActiveViewLeftImage?.setImageUrl(DISABLED_PRODUCTS_ICON)
        inActiveViewWording?.setCurrentText(HtmlLinkHelper(context, wording).spannedString)
        inActiveViewWording?.children?.forEach {
            if (it is Typography) {
                it.setTextColorCompat(unifyprinciplesR.color.Unify_NN600)
            }
        }
        inActiveViewRightIcon?.visibility = View.GONE
        switcherView?.displayedChild = 1
        switcherView?.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
        loadingView?.visibility = View.GONE
        inActiveView?.setOnClickListener {
            onClickListener.invoke()
        }
        activeViewWording?.stopFlipping()
    }

    /**
     * show inactive state for cart page with promo revamp (when user have not select any product, when user blacklisted, etc)
     */
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
            inActiveViewWording?.children?.forEach {
                if (it is Typography) {
                    it.setTextColorCompat(unifyprinciplesR.color.Unify_NN600)
                }
            }
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
            inActiveViewWording?.children?.forEach {
                if (it is Typography) {
                    it.setTextColorCompat(unifyprinciplesR.color.Unify_NN600)
                }
            }
            inActiveViewRightIcon?.visibility = View.GONE
            switcherView?.displayedChild = 1
            switcherView?.visibility = View.VISIBLE
            errorView?.visibility = View.GONE
            loadingView?.visibility = View.GONE
        }
        inActiveView?.setOnClickListener {
            onClickListener.invoke()
        }
        activeViewWording?.stopFlipping()
    }

    /**
     * show active state for cart page with promo revamp without expandable & confetti (when user have not use any promo, when applied promo in cart)
     */
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
        activeViewRightIcon?.setOnClickListener {
            /* no-op */
        }
        activeView?.setOnClickListener {
            onClickListener.invoke()
        }
        activeViewWording?.stopFlipping()
    }

    /**
     * show active state for checkout page with promo revamp with expandable & confetti
     */
    fun showActiveNewExpandable(
        leftImageUrl: String,
        wording: String,
        firstLevelSummary: List<PromoEntryPointSummaryItem>,
        groupedSummary: List<PromoEntryPointSummaryItem>,
        secondaryText: String,
        isSecondaryTextEnabled: Boolean = false,
        isExpanded: Boolean = false,
        animateWording: Boolean = false,
        onClickListener: () -> Unit = {}
    ) {
        activeViewConfettiFrame?.visibility = View.VISIBLE
        activeViewSummaryLayout?.visibility = View.GONE
        activeViewDivider?.visibility = View.GONE
        activeViewTitleWording?.visibility = View.GONE
        activeViewDescWording?.visibility = View.GONE
        activeViewWording?.visibility = View.VISIBLE
        if (switcherView?.visibility == View.VISIBLE) {
            activeViewLeftImage?.setImageUrl(leftImageUrl)
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
            switcherView?.displayedChild = 0
            switcherView?.visibility = View.VISIBLE
            errorView?.visibility = View.GONE
            loadingView?.visibility = View.GONE
        }
        if (firstLevelSummary.isNotEmpty() || secondaryText.isNotEmpty() || groupedSummary.isNotEmpty()) {
            activeViewSummaryLayout?.apply {
                removeAllViews()
                firstLevelSummary.forEach {
                    val summaryView = LayoutInflater.from(this.context)
                        .inflate(R.layout.layout_item_promo_checkout_summary, this, false)
                    summaryView.findViewById<Typography>(R.id.tv_promo_checkout_summary_title).text =
                        it.title
                    summaryView.findViewById<Typography>(R.id.tv_promo_checkout_summary_value).text =
                        it.value
                    if (it.subValue.isNotEmpty()) {
                        summaryView.findViewById<Typography>(R.id.tv_promo_checkout_summary_subvalue)
                            .apply {
                                text = it.value
                                visibility = View.VISIBLE
                            }
                    } else {
                        summaryView.findViewById<Typography>(R.id.tv_promo_checkout_summary_subvalue).visibility =
                            View.GONE
                    }
                    this.addView(summaryView)
                }
                if (secondaryText.isNotEmpty()) {
                    val summaryView = LayoutInflater.from(this.context)
                        .inflate(R.layout.layout_promo_checkout_summary_grouping, this, false)
                    val secondaryTv =
                        summaryView.findViewById<Typography>(R.id.tv_promo_checkout_summary_button)
                    val secondaryIc =
                        summaryView.findViewById<IconUnify>(R.id.ic_promo_checkout_summary_button)
                    secondaryTv.text = HtmlLinkHelper(context, secondaryText).spannedString
                    if (isSecondaryTextEnabled) {
                        secondaryTv.setTextColorCompat(unifyprinciplesR.color.Unify_NN950)
                        secondaryIc.visibility = View.VISIBLE
                    } else {
                        secondaryTv.setTextColorCompat(unifyprinciplesR.color.Unify_NN400)
                        secondaryIc.visibility = View.GONE
                    }
                    summaryView.findViewById<View>(R.id.group_promo_checkout_summary).visibility =
                        View.GONE
                    summaryView.setOnClickListener {
                        onClickListener.invoke()
                    }
                    this.addView(summaryView)
                } else if (groupedSummary.isNotEmpty()) {
                    val summaryView = LayoutInflater.from(this.context)
                        .inflate(R.layout.layout_promo_checkout_summary_grouping, this, false)
                    summaryView.findViewById<Typography>(R.id.tv_promo_checkout_summary_button).visibility =
                        View.GONE
                    summaryView.findViewById<IconUnify>(R.id.ic_promo_checkout_summary_button).visibility =
                        View.GONE
                    val groupContainer =
                        summaryView.findViewById<View>(R.id.group_promo_checkout_summary)
                    val groupLayout =
                        summaryView.findViewById<LinearLayout>(R.id.ll_promo_checkout_summary_group)
                    this.addView(summaryView)
                    groupedSummary.forEach {
                        val summaryGroupItemView = LayoutInflater.from(this.context)
                            .inflate(
                                R.layout.layout_item_promo_checkout_summary,
                                groupLayout,
                                false
                            )
                        summaryGroupItemView.findViewById<Typography>(R.id.tv_promo_checkout_summary_title).text =
                            it.title
                        summaryGroupItemView.findViewById<Typography>(R.id.tv_promo_checkout_summary_value).text =
                            it.value
                        if (it.subValue.isNotEmpty()) {
                            summaryGroupItemView.findViewById<Typography>(R.id.tv_promo_checkout_summary_subvalue)
                                .apply {
                                    text = it.value
                                    visibility = View.VISIBLE
                                }
                        } else {
                            summaryGroupItemView.findViewById<Typography>(R.id.tv_promo_checkout_summary_subvalue).visibility =
                                View.GONE
                        }
                        groupLayout.addView(summaryGroupItemView)
                    }
                    groupContainer.setOnClickListener {
                        onClickListener.invoke()
                    }
                }
            }
            if (isExpanded) {
                activeViewRightIcon?.setImage(IconUnify.CHEVRON_UP)
                activeViewSummaryLayout?.visibility = View.VISIBLE
                activeViewDivider?.visibility = View.VISIBLE
            } else {
                activeViewRightIcon?.setImage(IconUnify.CHEVRON_DOWN)
                activeViewSummaryLayout?.visibility = View.GONE
                activeViewDivider?.visibility = View.GONE
            }
            activeViewRightIcon?.setOnClickListener {
                if (activeViewSummaryLayout?.visibility == View.VISIBLE) {
                    activeViewRightIcon?.setImage(IconUnify.CHEVRON_DOWN)
                    activeViewSummaryLayout?.visibility = View.GONE
                    activeViewDivider?.visibility = View.GONE
                } else {
                    activeViewRightIcon?.setImage(IconUnify.CHEVRON_UP)
                    activeViewSummaryLayout?.visibility = View.VISIBLE
                    activeViewDivider?.visibility = View.VISIBLE
                }
            }
        } else {
            activeViewSummaryLayout?.visibility = View.GONE
            activeViewDivider?.visibility = View.GONE
            activeViewRightIcon?.setOnClickListener {
                /* no-op */
            }
        }
        activeViewWording?.stopFlipping()
    }

    // cart makin hemat pakai promo
    /**
     * show active state for cart-checkout page without promo revamp
     */
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
        activeViewLeftImage?.setImageUrl(PROMO_COUPON_ICON)
        activeViewWording?.setCurrentText(HtmlLinkHelper(context, wording).spannedString)
        activeViewRightIcon?.setImage(rightIcon)
        switcherView?.displayedChild = 0
        switcherView?.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
        loadingView?.visibility = View.GONE
        activeView?.setOnClickListener {
            onClickListener.invoke()
        }
        activeViewWording?.stopFlipping()
    }

    private fun setFlippingDuration(durationInMs: Long) {
        activeViewWording?.setFlippingDuration(durationInMs)
        inActiveViewWording?.setFlippingDuration(durationInMs)
    }

    private fun setMaximumFlippingCount(count: Int) {
        activeViewWording?.setMaximumFlippingCount(count)
        inActiveViewWording?.setMaximumFlippingCount(count)
    }

    /**
     * show active state for cart page with promo revamp and flipping animation
     */
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
        activeViewWording?.stopFlipping()
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
    /**
     * show active state for cart-checkout page without promo revamp with promo applied
     */
    fun showApplied(
        title: String,
        desc: String,
        rightIcon: Int,
        summaries: List<PromoEntryPointSummaryItem>,
        showConfetti: Boolean = false,
        onClickListener: () -> Unit = {}
    ) {
        switcherView?.reset()
        activeViewConfettiFrame?.visibility = if (showConfetti) View.VISIBLE else View.GONE
        activeViewLeftImage?.setImageUrl(CHECKMARK_APPLIED_ICON)
        activeViewTitleWording?.text = HtmlLinkHelper(context, title).spannedString
        activeViewDescWording?.text = HtmlLinkHelper(context, desc).spannedString
        activeViewWording?.visibility = View.GONE
        activeViewTitleWording?.visibility = View.VISIBLE
        activeViewDescWording?.visibility = View.VISIBLE
        activeViewRightIcon?.visibility = View.VISIBLE
        activeViewRightIcon?.setImage(rightIcon)
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
                        summaryView.findViewById<Typography>(R.id.tv_promo_checkout_summary_subvalue)
                            .apply {
                                text = it.subValue
                                visibility = View.VISIBLE
                            }
                    } else {
                        summaryView.findViewById<Typography>(R.id.tv_promo_checkout_summary_subvalue).visibility =
                            View.GONE
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
        activeViewWording?.stopFlipping()
    }

    companion object {
        private const val CHECKMARK_APPLIED_ICON =
            "https://images.tokopedia.net/img/ios/promo_widget/checklist.png"
        private const val PROMO_COUPON_ICON =
            "https://images.tokopedia.net/img/ios/promo_widget/promo_coupon.png"
        private const val DISABLED_PRODUCTS_ICON =
            "https://images.tokopedia.net/img/ios/promo_widget/disabled_product.png"
    }
}
