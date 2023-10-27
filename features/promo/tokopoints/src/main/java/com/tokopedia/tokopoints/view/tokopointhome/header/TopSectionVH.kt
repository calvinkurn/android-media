package com.tokopedia.tokopoints.view.tokopointhome.header

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.notification.model.popupnotif.PopupNotif
import com.tokopedia.tokopoints.view.customview.*
import com.tokopedia.tokopoints.view.customview.DynamicItemActionView.Companion.BBO
import com.tokopedia.tokopoints.view.customview.DynamicItemActionView.Companion.KUPON
import com.tokopedia.tokopoints.view.customview.DynamicItemActionView.Companion.TOKOMEMBER
import com.tokopedia.tokopoints.view.customview.DynamicItemActionView.Companion.TOPQUEST
import com.tokopedia.tokopoints.view.model.homeresponse.TopSectionResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.DynamicActionListItem
import com.tokopedia.tokopoints.view.model.rewardtopsection.ProgressInfoList
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.rewrdsStatusMatching.MetadataItem
import com.tokopedia.tokopoints.view.model.rewrdsStatusMatching.TickerListItem
import com.tokopedia.tokopoints.view.model.usersaving.UserSaving
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.webview.KEY_TITLEBAR

class TopSectionVH(
    itemView: View,
    private val cardRuntimeHeightListener: CardRuntimeHeightListener,
    private val refreshOnTierUpgrade: RefreshOnTierUpgrade,
    private val toolbarItemList: Any?
) : RecyclerView.ViewHolder(itemView) {

    private var arrowIcon: AppCompatImageView? = null
    lateinit var cardTierInfo: ConstraintLayout
    private var dynamicAction: DynamicItemActionView? = null
    private var mTextMembershipLabel: TextView? = null
    private var mTargetText: TextView? = null
    private var mTextMembershipValue: TextView? = null
    private var mImgBackground: ImageView? = null
    private var mValueMembershipDescription: String? = null
    private var mImgEgg: ImageView? = null
    private var title: TextView? = null
    private var savingValue: TextView? = null
    private var savingDesc: TextView? = null
    private var cardContainer: ConstraintLayout? = null
    private var containerUserSaving: ConstraintLayout? = null
    private var containerStatusMatching: View? = null
    private var cardStatusMatching: CardUnify? = null
    private var confettiAnim: LottieAnimationView? = null
    private var timerTextView: Typography? = null
    private var statusMatchingTimer: TimerUnifySingle? = null
    private var parentStatusMatching: ConstraintLayout? = null
    private var ivStatusBackground: AppCompatImageView? = null
    private var progressBar: ProgressBarUnify? = null
    private var tierIconProgress: ImageUnify? = null
    private var currentTier: Typography? = null
    private val MEMBER_STATUS_BG_RADII = 16F
    private val ANIMATION_DURATION = 600L
    private val TP_CONFETTI_STATUS_MATCHING = "tp_confetti_entry_point.zip"
    private var digitContainer: LinearLayout? = null
    private var popupNotification: PopupNotif? = null
    private var topSectionData: TopSectionResponse? = null
    private var textTransaksi: Typography? = null
    private var textLagi: Typography? = null
    private var containerProgressBar: FrameLayout ? = null
    private var isNextTier = false
    val maxProgress = 100
    private var TIME_DELAY_PROGRESS = 1000L

    fun bind(model: TopSectionResponse) {
        cardTierInfo = itemView.findViewById(R.id.container_target)
        dynamicAction = itemView.findViewById(R.id.dynamic_widget)
        mTextMembershipValue = itemView.findViewById(R.id.text_membership_value)
        mTargetText = itemView.findViewById(R.id.tv_targetText)
        mImgEgg = itemView.findViewById(R.id.img_egg)
        mImgBackground = itemView.findViewById(R.id.img_bg_header)
        title = itemView.findViewById(R.id.tv_saving_title)
        savingValue = itemView.findViewById(R.id.tv_saving_value)
        savingDesc = itemView.findViewById(R.id.tv_saving_desc)
        cardContainer = itemView.findViewById(R.id.container_saving)
        containerUserSaving = itemView.findViewById(R.id.container_layout_saving)
        containerStatusMatching = itemView.findViewById(R.id.status_matching_container)
        cardStatusMatching = itemView.findViewById(R.id.cv_statusmatching)
        confettiAnim = itemView.findViewById(R.id.confetti_lottie)
        timerTextView = itemView.findViewById(R.id.timer_text_view)
        statusMatchingTimer = itemView.findViewById(R.id.countdown_status)
        ivStatusBackground = itemView.findViewById(R.id.iv_gojek)
        progressBar = itemView.findViewById(R.id.progressbar_membership)
        tierIconProgress = itemView.findViewById(R.id.tier_icon_progressbar)
        digitContainer = itemView.findViewById(R.id.digitContainer)
        currentTier = itemView.findViewById(R.id.text_current_tier)
        arrowIcon = itemView.findViewById(R.id.ic_arrow_icon)
        textTransaksi = itemView.findViewById(R.id.text_transaksi_desc)
        textLagi = itemView.findViewById(R.id.text_transaksi_lagi)
        containerProgressBar = itemView.findViewById(R.id.container_progressbar)

        renderToolbarWithHeader(model.tokopediaRewardTopSection)
        model.userSavingResponse?.userSaving?.let {
            containerUserSaving?.show()
            renderUserSaving(it)
        }
        model.rewardTickerResponse?.let {
            if (!it.rewardsTickerList?.tickerList.isNullOrEmpty()) {
                containerStatusMatching?.show()
                cardStatusMatching?.show()
                renderStatusMatchingView(it.rewardsTickerList?.tickerList)
            }
        }
        isNextTier = model.popupNotification?.popupNotif?.title?.isEmpty() == true
        popupNotification = model.popupNotification?.popupNotif
        topSectionData = model
    }

    private fun renderToolbarWithHeader(data: TokopediaRewardTopSection?) {
        cardTierInfo.doOnLayout {
            if (data?.progressInfoList.isNullOrEmpty()) {
                cardRuntimeHeightListener.setCardLayoutHeight(
                    it.height,
                    itemView.context.resources.getDimension(R.dimen.tp_home_top_bg_height_noprogressbar)
                )
            } else {
                cardRuntimeHeightListener.setCardLayoutHeight(
                    it.height,
                    itemView.context.resources.getDimension(R.dimen.tp_home_top_bg_height)
                )
            }
        }
        arrowIcon?.setOnClickListener {
            gotoMembershipPage()
        }
        mTextMembershipLabel?.text = data?.introductionText
        if (data?.progressInfoList?.isNotEmpty() == true) {
            currentTier?.text = data.progressInfoList.getOrNull(1)?.nextTierName ?: ""
            val diffAmount = data.progressInfoList.getOrNull(1)?.differenceAmountStr ?: ""
            val diffAmountCurrent = data.progressInfoList.getOrNull(1)?.nextAmountStr ?: ""
            tierIconProgress?.loadImage(data.progressInfoList[1].nextTierIconImageURL ?: "")

            if (digitContainer?.childCount == 0) {
                if (isContainsRp(diffAmount)) {
                    addRPToTransaction()
                    val digitTextView = DigitTextView(itemView.context)
                    digitTextView.setValue(diffAmountCurrent.removePrefix("Rp"), diffAmount.removePrefix("Rp"))
                    digitContainer?.addView(digitTextView)
                } else {
                    val digitTextView = DigitTextView(itemView.context)
                    digitTextView.setValue(diffAmountCurrent, diffAmount)
                    digitContainer?.addView(digitTextView)
                    addXToTransaction()
                }
            }
            progressBarAnimation(data.progressInfoList ?: listOf())
        } else {
            hideTierComponents()
        }

        data?.targetV2?.let {
            if (!it.textColor.isNullOrEmpty()) {
                mTargetText?.setTextColor(Color.parseColor("#" + it.textColor))
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mTargetText?.text = Html.fromHtml(it.text, Html.FROM_HTML_MODE_LEGACY)
            } else {
                mTargetText?.text = Html.fromHtml(it.text)
            }
            if (!it.backgroundColor.isNullOrEmpty()) {
                customBackground(cardTierInfo, Color.parseColor("#" + it.backgroundColor), MEMBER_STATUS_BG_RADII)
            }
            cardTierInfo.setOnClickListener {
                gotoMembershipPage()
            }
        }

        if (isDarkMode(itemView.context)) {
            mTextMembershipLabel?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
            mTextMembershipValue?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
        }

        mImgEgg.loadImageCircle(data?.tier?.imageURL)
        data?.backgroundImageURLMobileV2?.let { mImgBackground?.loadImage(it) }
        if (data?.tier != null) {
            mTextMembershipValue?.text = data.tier.nameDesc
        }

        renderDynamicActionList(data?.dynamicActionList)
    }

    private fun renderDynamicActionList(dataList: List<DynamicActionListItem?>?) {
        val mapOfIdtoPosition: HashMap<Int, Int> = HashMap()

        if (dataList != null && dataList.isNotEmpty()) {
            for (i in dataList.indices) {
                mapOfIdtoPosition.put(dataList[i]?.id ?: 0, i)
                dynamicAction?.setLayoutVisibility(View.VISIBLE, dataList[i]?.id ?: 0)
                dataList[i]?.let { dynamicAction?.setLayoutText(it.cta?.text ?: "", it.id ?: 0) }
                dataList[i]?.let { dynamicAction?.setLayoutIcon(it.iconImageURL ?: "", it.id ?: 0) }
                if (dataList[i]?.counter?.isShowCounter!! && dataList[i]?.counter?.counterStr != "0") {
                    dataList[i]?.let {
                        dynamicAction?.setLayoutNotification(
                            it.counter?.counterStr ?: "",
                            it.id ?: 0
                        )
                    }
                } else {
                    dynamicAction?.hideNotification(dataList[i]?.id ?: 0)
                    dataList[i]?.counter?.isShowCounter = false
                }
                if (dataList[i]?.counterTotal?.isShowCounter!!) {
                    dataList[i]?.let {
                        dynamicAction?.setLayoutLabel(
                            it.counterTotal?.counterStr ?: "",
                            it.id ?: 0
                        )
                    }
                }
            }
            dynamicAction?.findViewById<LinearLayout>(R.id.holder_tokomember)?.setOnClickListener {
                dataList[mapOfIdtoPosition?.get(TOKOMEMBER) ?: 0]?.cta?.let {
                    hideNotification(mapOfIdtoPosition?.get(TOKOMEMBER) ?: 0, dataList[mapOfIdtoPosition?.get(TOKOMEMBER) ?: 0])
                    dynamicAction?.setLayoutClicklistener(it.appLink, it.text, TOKOMEMBER)
                }
            }
            dynamicAction?.findViewById<LinearLayout>(R.id.holder_topquest)?.setOnClickListener {
                dataList[mapOfIdtoPosition?.get(TOPQUEST) ?: 0]?.cta?.let {
                    hideNotification(mapOfIdtoPosition?.get(TOPQUEST) ?: 0, dataList[mapOfIdtoPosition?.get(TOPQUEST) ?: 0])
                    dynamicAction?.setLayoutClicklistener(it.appLink, it.text, TOPQUEST)
                }
            }
            dynamicAction?.findViewById<LinearLayout>(R.id.holder_tokopoint)?.setOnClickListener {
                dataList[mapOfIdtoPosition?.get(KUPON) ?: 0]?.cta?.let {
                    hideNotification(mapOfIdtoPosition?.get(KUPON) ?: 0, dataList[mapOfIdtoPosition?.get(KUPON) ?: 0])
                    dynamicAction?.setLayoutClicklistener(it.appLink, it.text, KUPON)
                }
            }

            dynamicAction?.findViewById<LinearLayout>(R.id.holder_bbo)?.setOnClickListener {
                dataList[mapOfIdtoPosition?.get(BBO) ?: 0]?.cta?.let {
                    hideNotification(mapOfIdtoPosition?.get(BBO) ?: 0, dataList[mapOfIdtoPosition?.get(BBO) ?: 0])
                    dynamicAction?.setLayoutClicklistener(it.appLink, it.text, BBO)
                }
            }
        }
    }

    inline fun View.doOnLayout(crossinline action: (view: View) -> Unit) {
        if (ViewCompat.isLaidOut(this) && !isLayoutRequested) {
            action(this)
        } else {
            doOnNextLayout {
                action(it)
            }
        }
    }

    inline fun View.doOnNextLayout(crossinline action: (view: View) -> Unit) {
        addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                view: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                view.removeOnLayoutChangeListener(this)
                action(view)
            }
        })
    }

    private fun hideNotification(index: Int, dynamicActionListItem: DynamicActionListItem?) {
        toolbarItemList as ArrayList<NotificationUnify>
        toolbarItemList[index].hide()
        dynamicActionListItem?.counter?.isShowCounter = false
    }

    private fun customBackground(v: View, backgroundColor: Int, radii: Float) {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadii = floatArrayOf(radii, radii, radii, radii, radii, radii, radii, radii)
        shape.setColor(backgroundColor)
        v.background = shape
    }

    private fun renderUserSaving(userSavingInfo: UserSaving) {
        if (!userSavingInfo.title.isNullOrEmpty()) {
            (title as TextView).text = userSavingInfo.title
        } else {
            title?.hide()
        }
        if (!userSavingInfo.userTotalSavingStr.isNullOrEmpty()) {
            (savingValue as TextView).text = userSavingInfo.userTotalSavingStr
        } else {
            savingValue?.hide()
        }
        if (!userSavingInfo.descriptions.isNullOrEmpty()) {
            val savingPercent = userSavingInfo.descriptions[0]?.text
            val savingPercentStyle = userSavingInfo.descriptions[0]?.fontStyle
            var savingPercentColor = ""
            if (!savingPercentStyle.isNullOrEmpty()) {
                savingPercentColor = savingPercentStyle.replace(CommonConstant.USERSAVING_COLORSTR, "")
            }
            var savingDescription = ""
            var savingDescriptionStyle = ""
            var savingDescriptionColor = ""
            if (userSavingInfo.descriptions.size > 1) {
                savingDescription = userSavingInfo.descriptions[1]?.text.toString()
                savingDescriptionStyle = userSavingInfo.descriptions[1]?.fontStyle.toString()
                if (!savingDescriptionStyle.isNullOrEmpty()) {
                    savingDescriptionColor = savingDescriptionStyle.replace(CommonConstant.USERSAVING_COLORSTR, "")
                }
            }
            val spannable = SpannableString("$savingPercent $savingDescription")
            if (!savingPercent.isNullOrEmpty() && !savingPercentColor.isNullOrEmpty()) {
                savingPercent.length.let {
                    spannable.setSpan(
                        ForegroundColorSpan(Color.parseColor(savingPercentColor)),
                        0,
                        it,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    if (userSavingInfo.descriptions.size > 1) {
                        spannable.setSpan(StyleSpan(Typeface.BOLD), 0, it, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
            }
            if (!savingDescription.isNullOrEmpty() && !savingDescriptionColor.isNullOrEmpty()) {
                savingDescription.length.let {
                    spannable.setSpan(
                        ForegroundColorSpan(Color.parseColor(savingDescriptionColor)),
                        0,
                        it,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            (savingDesc as TextView).text = spannable
        }
        if (!userSavingInfo.backgroundImageURL.isNullOrEmpty()) {
            ImageHandler.loadBackgroundImage(cardContainer, userSavingInfo.backgroundImageURL)
        }
        cardContainer?.setOnClickListener {
            RouteManager.route(itemView.context, CommonConstant.WebLink.USERSAVING)
            AnalyticsTrackerUtil.sendEvent(
                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.CLICK_USERSAVING_ENTRYPOINT,
                "",
                AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
            )
        }
    }

    private fun renderStatusMatchingView(rewardTickerResponse: List<TickerListItem?>?) {
        if (itemView.context.isDarkMode()) {
            ivStatusBackground?.setImageResource(R.drawable.bg_statusmatching_dark)
        } else {
            ivStatusBackground?.setImageResource(R.drawable.bg_statusmatching_light)
        }
        containerStatusMatching?.setOnClickListener {
            rewardTickerResponse?.get(0)?.metadata?.get(0)?.link?.url?.let { url ->
                if (url.isNotEmpty()) {
                    val intent = RouteManager.getIntent(
                        itemView.context,
                        String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
                    )
                    intent.putExtra(KEY_TITLEBAR, false)
                    itemView.context.startActivity(intent)
                }
                AnalyticsTrackerUtil.sendEvent(
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_STATUSMATCHING_ON_REWARDS,
                    "",
                    AnalyticsTrackerUtil.EcommerceKeys.TOKOPOINT_BUSINESSUNIT,
                    AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
                )
            }
        }
        playAnimation()
        val metadata = rewardTickerResponse?.get(0)?.metadata?.get(0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            timerTextView?.text = Html.fromHtml(metadata?.text?.content, Html.FROM_HTML_MODE_LEGACY)
        } else {
            timerTextView?.text = Html.fromHtml(metadata?.text?.content)
        }
        if (metadata?.isShowTime == true) {
            setContainerWithTimer()
            setTimer(metadata)
        }
        AnalyticsTrackerUtil.sendEvent(
            AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT_IRIS,
            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
            AnalyticsTrackerUtil.ActionKeys.VIEW_STATUSMATCHING_ON_REWARDS,
            "",
            AnalyticsTrackerUtil.EcommerceKeys.TOKOPOINT_BUSINESSUNIT,
            AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
        )
    }

    private fun setTimer(metadata: MetadataItem?) {
        statusMatchingTimer?.timer?.cancel()
        statusMatchingTimer?.targetDate = convertSecondsToHrMmSs(metadata?.timeRemainingSeconds ?: 0L)
        statusMatchingTimer?.apply {
            timerTextWidth = TimerUnifySingle.TEXT_WRAP
            timerVariant = TimerUnifySingle.VARIANT_ALTERNATE
            onFinish = {
                hideStatusMatching()
            }
            onTick = {
                metadata?.timeRemainingSeconds = it / 1000
            }
        }
    }

    private fun setContainerWithTimer() {
        statusMatchingTimer?.show()
        parentStatusMatching = itemView.findViewById(R.id.container_statusmatching)
        val constraintSet = ConstraintSet()
        constraintSet.clone(parentStatusMatching)
        constraintSet.connect(
            R.id.timer_text_view,
            ConstraintSet.TOP,
            R.id.countdown_status,
            ConstraintSet.BOTTOM,
            0
        )
        constraintSet.applyTo(parentStatusMatching)

        val layoutParamsTextView = timerTextView?.layoutParams as ViewGroup.MarginLayoutParams
        layoutParamsTextView.topMargin = itemView.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
        timerTextView?.layoutParams = layoutParamsTextView

        val layoutParams = cardStatusMatching?.layoutParams
        layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        cardStatusMatching?.layoutParams = layoutParams
    }

    private fun playAnimation() {
        val lottieTask =
            LottieCompositionFactory.fromAsset(itemView.context, TP_CONFETTI_STATUS_MATCHING)
        lottieTask?.addListener { result: LottieComposition? ->
            result?.let {
                confettiAnim?.setComposition(result)
                confettiAnim?.repeatCount = ValueAnimator.INFINITE
                confettiAnim?.playAnimation()
            }
        }
    }

    private fun hideStatusMatching() {
        cardStatusMatching?.hide()
        containerStatusMatching?.hide()
    }

    private fun isContainsRp(upgradeValue: String?): Boolean {
        return upgradeValue?.startsWith("Rp") ?: false
    }

    private fun getProgress(progressInfoList: List<ProgressInfoList>): Pair<Int?, Int?> {
        val currentAmountNext = progressInfoList[1].currentAmount?.toFloat() ?: 0F
        val nextAmountNext = progressInfoList[1].nextAmount?.toFloat() ?: 1F
        val endPercentage = (currentAmountNext / nextAmountNext * 100).toInt()

        val currentAmountLast = progressInfoList[0].currentAmount?.toFloat() ?: 0F
        val nextAmountLast = progressInfoList[0].nextAmount?.toFloat() ?: 1F
        val startPercentage = (currentAmountLast / nextAmountLast * 100).toInt()

        return Pair(startPercentage, endPercentage)
    }

    private fun addRPToTransaction() {
        val rpView = Typography(itemView.context)
        rpView.apply {
            text = "Rp"
            setWeight(Typography.BOLD)
            setType(Typography.BODY_3)
            setTextColor(Color.WHITE)
        }
        digitContainer?.addView(rpView)
    }

    private fun addXToTransaction() {
        val dotView = Typography(itemView.context)
        dotView.apply {
            text = "x"
            setWeight(Typography.BOLD)
            setType(Typography.BODY_3)
            setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
        }
        digitContainer?.addView(dotView)
    }

    private fun progressBarAnimation(progressInfoList: List<ProgressInfoList>) {
        val container = progressBar?.progressBarContainer
        val progressValues = getProgress(progressInfoList)
        val progressLast = progressValues.first ?: -1
        val progressCurrent = progressValues.second ?: -1
        progressBar?.apply {
            progressBarHeight = ProgressBarUnify.SIZE_LARGE
            progressBarColorType = ProgressBarUnify.COLOR_GREEN
            clipChildren = false
            (progressBarWrapper.parent as ViewGroup).clipChildren = false
            (progressBarIndicatorWrapper).clipChildren = false
            (progressBarWrapper.parent as ViewGroup).clipToPadding = false
            gravity = Gravity.CENTER_VERTICAL
            if (progressLast > 0 && progressLast > progressCurrent) {
                setProgressIcon(
                    AppCompatResources.getDrawable(
                        itemView.context,
                        R.drawable.tp_tier_progress
                    )
                )
                setValue(maxProgress, true)
            } else {
                setProgressIcon(null)
                setValue(progressLast, true)
            }
        }
        progressBar?.postDelayed(
            {
                handleDelayedProgress(container, progressCurrent)
            },
            TIME_DELAY_PROGRESS
        )
    }

    private fun handleDelayedProgress(container: FrameLayout?, progressCurrent: Int) {
        try {
            // Check max progress for higher tier and open bottomsheet
            if (progressBar?.getValue() == maxProgress) {
                progressBarIconAnimation(container) {
                    progressBar?.setProgressIcon(null)
                    progressIconProgressCompletionHandle()
                }
            } else {
                progressBar?.setValue(progressCurrent, true)
                if (progressCurrent == 0) {
                    progressBar?.setProgressIcon(null)
                } else {
                    progressBar?.setProgressIcon(
                        AppCompatResources.getDrawable(
                            itemView.context,
                            R.drawable.tp_tier_progress
                        )
                    )
                }
                progressBarIconAnimation(container) {
                    progressBar?.setProgressIcon(null)
                    progressIconProgressCompletionHandle()
                }
            }
        } catch (e: Exception) {}
    }

    private fun progressIconProgressCompletionHandle() {
        if (!isNextTier) {
            topSectionData?.popupNotification = null
            refreshOnTierUpgrade.refreshReward(popupNotification)
        }
    }

    private fun progressBarIconAnimation(container: FrameLayout?, completion: (() -> Unit)? = null) {
        if (container?.childCount?.minus(1) ?: 0 >= 0) {
            val target = container?.getChildAt(container.childCount - 1)
            val animationScaleX =
                ObjectAnimator.ofFloat(target, View.SCALE_X, 1.2f)
                    .setDuration(ANIMATION_DURATION)
            val animationScaleY =
                ObjectAnimator.ofFloat(target, View.SCALE_Y, 1.2f)
                    .setDuration(ANIMATION_DURATION)

            val animationScaleXN =
                ObjectAnimator.ofFloat(target, View.SCALE_X, 1f)
                    .setDuration(ANIMATION_DURATION)
            val animationScaleYN =
                ObjectAnimator.ofFloat(target, View.SCALE_Y, 1f)
                    .setDuration(ANIMATION_DURATION)

            val animSetScaleUp = AnimatorSet()
            val animSetScaleDown = AnimatorSet()
            animSetScaleUp.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }
                override fun onAnimationEnd(p0: Animator) {
                    animSetScaleDown.playTogether(animationScaleXN, animationScaleYN)
                    animSetScaleDown.start()
                }
                override fun onAnimationCancel(p0: Animator) {
                }
                override fun onAnimationRepeat(p0: Animator) {
                }
            })
            animSetScaleUp.playTogether(animationScaleY, animationScaleX)
            animSetScaleUp.start()

            animSetScaleDown.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }
                override fun onAnimationEnd(p0: Animator) {
                    completion?.invoke()
                }
                override fun onAnimationCancel(p0: Animator) {
                }
                override fun onAnimationRepeat(p0: Animator) {
                }
            })
        }
    }

    private fun hideTierComponents() {
        textTransaksi?.hide()
        textLagi?.hide()
        digitContainer?.hide()
        currentTier?.hide()
        containerProgressBar?.hide()
    }

    private fun gotoMembershipPage() {
        RouteManager.route(
            itemView.context,
            ApplinkConstInternalGlobal.WEBVIEW_TITLE,
            itemView.context.resources.getString(R.string.tp_label_membership),
            CommonConstant.WebLink.MEMBERSHIP
        )

        AnalyticsTrackerUtil.sendEvent(
            itemView.context,
            AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
            AnalyticsTrackerUtil.ActionKeys.CLICK_MEMBERSHIP,
            mValueMembershipDescription
        )
    }

    interface CardRuntimeHeightListener {
        fun setCardLayoutHeight(height: Int, heightBackground: Float)
    }

    interface RefreshOnTierUpgrade {
        fun refreshReward(popupNotification: PopupNotif?)
    }
}
