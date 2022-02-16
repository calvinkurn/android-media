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
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.notification.model.PopupNotification
import com.tokopedia.tokopoints.view.customview.*
import com.tokopedia.tokopoints.view.customview.DynamicItemActionView.Companion.BBO
import com.tokopedia.tokopoints.view.customview.DynamicItemActionView.Companion.KUPON
import com.tokopedia.tokopoints.view.customview.DynamicItemActionView.Companion.TOKOMEMBER
import com.tokopedia.tokopoints.view.customview.DynamicItemActionView.Companion.TOPQUEST
import com.tokopedia.tokopoints.view.model.homeresponse.TopSectionResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.DynamicActionListItem
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
import kotlin.math.roundToInt

class TopSectionVH(
    itemView: View,
    private val cardRuntimeHeightListener: CardRuntimeHeightListener,
    private val refreshOnTierUpgrade: RefreshOnTierUpgrade,
    private val toolbarItemList: Any?) : RecyclerView.ViewHolder(itemView) {

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
    private val ANIMATION_DURATION = 800L
    private val TP_CONFETTI_STATUS_MATCHING = "tp_confetti_entry_point.zip"
    private var digitContainer: LinearLayout? = null
    private var popupNotification: PopupNotification? = null
    private var topSectionData : TopSectionResponse? =null
    private var isNextTier = false

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
        isNextTier = model.popupNotification?.tokoPoints?.popupNotif?.isEmpty == true
        popupNotification = model.popupNotification?.tokoPoints?.popupNotif
        topSectionData = model
    }

    private fun renderToolbarWithHeader(data: TokopediaRewardTopSection?) {
        cardTierInfo.doOnLayout {
            cardRuntimeHeightListener.setCardLayoutHeight(it.height)
        }
        mTextMembershipLabel?.text = data?.introductionText
        currentTier?.text = data?.progressInfoList?.get(0)?.nextTierName

        val diffAmount = data?.progressInfoList?.get(0)?.differenceAmountStr
        val diffAmountStr = diffAmount?.split(" ")?.get(1)

        tierIconProgress?.loadImage(data?.progressInfoList?.get(0)?.nextTierIconImageURL ?: "")
        var getDigitValue = diffAmountStr?.toCharArray()

        if (digitContainer?.childCount == 0) {
            getDigitValue = if (isContainsRp(diffAmountStr)) {
                addRPToTransaction()
                diffAmountStr?.removePrefix("Rp")?.toCharArray()
            } else {
                diffAmountStr?.removeSuffix("x")?.toCharArray()
            }
            if (getDigitValue != null) {
                setDigitContainerView(getDigitValue)
            }
            if (!isContainsRp(diffAmountStr)) {
                addXToTransaction()
            }
        }
        progressBarAnimation()

        progressBar?.apply {
            setValue(
                getProgress(
                    data?.progressInfoList?.get(0)?.currentAmount?.toFloat() ?: 0F,
                    data?.progressInfoList?.get(0)?.nextAmount?.toFloat() ?: 1F
                ), true
            )
            progressBarColorType = ProgressBarUnify.COLOR_GREEN
        }

        data?.target?.let {
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
                RouteManager.route(itemView.context, ApplinkConstInternalGlobal.WEBVIEW_TITLE, itemView.context.resources.getString(R.string.tp_label_membership), CommonConstant.WebLink.MEMBERSHIP)
                AnalyticsTrackerUtil.sendEvent(itemView.context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_MEMBERSHIP, mValueMembershipDescription)
            }
        }

        if (isDarkMode(itemView.context)){
            mTextMembershipLabel?.setTextColor(ContextCompat.getColor(itemView.context,com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
            mTextMembershipValue?.setTextColor(ContextCompat.getColor(itemView.context,com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
        }

        ImageHandler.loadImageCircle2(itemView.context, mImgEgg, data?.profilePicture)
        data?.backgroundImageURLMobileV2?.let { mImgBackground?.loadImage(it) }
        if (data?.tier != null) {
            mTextMembershipValue?.text = data.tier.nameDesc
        }

        renderDynamicActionList(data?.dynamicActionList)
    }

    private fun renderDynamicActionList(dataList: List<DynamicActionListItem?>?) {
        val mapOfIdtoPosition:HashMap<Int,Int> = HashMap()

        if (dataList != null && dataList.isNotEmpty()) {
            for (i in dataList.indices) {
                mapOfIdtoPosition.put(dataList[i]?.id ?: 0, i)
                dynamicAction?.setLayoutVisibility(View.VISIBLE, dataList[i]?.id ?: 0)
                dataList[i]?.let { dynamicAction?.setLayoutText(it.cta?.text ?: "", it.id ?: 0) }
                dataList[i]?.let { dynamicAction?.setLayoutIcon(it.iconImageURL ?: "", it.id ?: 0) }
                if (dataList[i]?.counter?.isShowCounter!! && dataList[i]?.counter?.counterStr != "0") {
                    dataList[i]?.let {
                        dynamicAction?.setLayoutNotification(
                            it.counter?.counterStr ?: "", it.id ?: 0)
                    }
                } else {
                    dynamicAction?.hideNotification(dataList[i]?.id ?: 0)
                    dataList[i]?.counter?.isShowCounter = false
                }
                if (dataList[i]?.counterTotal?.isShowCounter!!) {
                    dataList[i]?.let {
                        dynamicAction?.setLayoutLabel(
                            it.counterTotal?.counterStr ?: "", it.id ?: 0)
                    }
                }
            }
                dynamicAction?.findViewById<LinearLayout>(R.id.holder_tokomember)?.setOnClickListener {
                    dataList[mapOfIdtoPosition?.get(TOKOMEMBER)?:0]?.cta?.let {
                        hideNotification(mapOfIdtoPosition?.get(TOKOMEMBER)?:0, dataList[mapOfIdtoPosition?.get(TOKOMEMBER)?:0])
                        dynamicAction?.setLayoutClicklistener(it.appLink, it.text, TOKOMEMBER)
                    }
                }
                dynamicAction?.findViewById<LinearLayout>(R.id.holder_topquest)?.setOnClickListener {
                    dataList[mapOfIdtoPosition?.get(TOPQUEST)?:0]?.cta?.let {
                        hideNotification(mapOfIdtoPosition?.get(TOPQUEST)?:0, dataList[mapOfIdtoPosition?.get(TOPQUEST)?:0])
                        dynamicAction?.setLayoutClicklistener(it.appLink, it.text, TOPQUEST)
                    }
                }
                dynamicAction?.findViewById<LinearLayout>(R.id.holder_tokopoint)?.setOnClickListener {
                    dataList[mapOfIdtoPosition?.get(KUPON)?:0]?.cta?.let {
                        hideNotification(mapOfIdtoPosition?.get(KUPON)?:0, dataList[mapOfIdtoPosition?.get(KUPON)?:0])
                        dynamicAction?.setLayoutClicklistener(it.appLink, it.text, KUPON)
                    }
                }

                dynamicAction?.findViewById<LinearLayout>(R.id.holder_bbo)?.setOnClickListener {
                    dataList[mapOfIdtoPosition?.get(BBO)?:0]?.cta?.let {
                        hideNotification(mapOfIdtoPosition?.get(BBO)?:0, dataList[mapOfIdtoPosition?.get(BBO)?:0])
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
                savingPercentColor = savingPercentStyle?.replace(CommonConstant.USERSAVING_COLORSTR, "")
            }
            var savingDescription = ""
            var savingDescriptionStyle = ""
            var savingDescriptionColor = ""
            if (userSavingInfo.descriptions.size > 1) {
                savingDescription = userSavingInfo.descriptions[1]?.text.toString()
                savingDescriptionStyle = userSavingInfo.descriptions[1]?.fontStyle.toString()
                if (!savingDescriptionStyle.isNullOrEmpty()) {
                    savingDescriptionColor = savingDescriptionStyle?.replace(CommonConstant.USERSAVING_COLORSTR, "")
                }
            }
            val spannable = SpannableString("$savingPercent $savingDescription")
            if (!savingPercent.isNullOrEmpty() && !savingPercentColor.isNullOrEmpty()) {
                savingPercent.length.let {
                    spannable.setSpan(
                            ForegroundColorSpan(Color.parseColor(savingPercentColor)),
                            0, it,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    if (userSavingInfo.descriptions.size>1) {
                        spannable.setSpan(StyleSpan(Typeface.BOLD),0,it, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
            }
            if (!savingDescription.isNullOrEmpty() && !savingDescriptionColor.isNullOrEmpty()) {
                savingDescription.length.let {
                    spannable.setSpan(
                            ForegroundColorSpan(Color.parseColor(savingDescriptionColor)),
                            0, it,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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
                    AnalyticsTrackerUtil.ActionKeys.CLICK_USERSAVING_ENTRYPOINT, "",
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
                    AnalyticsTrackerUtil.ActionKeys.CLICK_STATUSMATCHING_ON_REWARDS, "",
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
        if(metadata?.isShowTime == true){
            setContainerWithTimer()
            setTimer(metadata)
        }
        AnalyticsTrackerUtil.sendEvent(
            AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT_IRIS,
            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
            AnalyticsTrackerUtil.ActionKeys.VIEW_STATUSMATCHING_ON_REWARDS, "",
            AnalyticsTrackerUtil.EcommerceKeys.TOKOPOINT_BUSINESSUNIT,
            AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
        )
    }

    private fun setTimer(metadata: MetadataItem?){
        statusMatchingTimer?.timer?.cancel()
        statusMatchingTimer?.targetDate = convertSecondsToHrMmSs(metadata?.timeRemainingSeconds?:0L)
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

    private fun setContainerWithTimer(){
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

    private fun isContainsRp(upgradeValue: String?) : Boolean {
        return upgradeValue?.startsWith("Rp")?:false
    }

    private fun getProgress(current:Float,next:Float) : Int{
        val progressPercentage = 100 - (((next-current)/next) * 100).roundToInt()
        return if (progressPercentage == 0){
            5
        } else progressPercentage
    }

    private fun setDigitContainerView(getDigitValue: CharArray) {
        for (i in getDigitValue){
            if(i.isDigit()){
                val digitTextView = DigitTextView(itemView.context)
                digitTextView.setValue(i.digitToInt())
                digitContainer?.addView(digitTextView)
            }
            else {
                val dotView =Typography(itemView.context)
                dotView.text="."
                dotView.setWeight(Typography.BOLD)
                digitContainer?.gravity = Gravity.BOTTOM
                dotView.setTextColor(Color.WHITE)
                digitContainer?.addView(dotView)
            }
        }
    }

    private fun addRPToTransaction(){
        val dotView =Typography(itemView.context)
        dotView.text="Rp"
        dotView.setWeight(Typography.BOLD)
        dotView.setType(Typography.BODY_3)
        dotView.gravity = Gravity.CENTER
        dotView.setTextColor(Color.WHITE)
        digitContainer?.addView(dotView)
    }

    private fun addXToTransaction(){
        val dotView =Typography(itemView.context)
        dotView.text="x"
        dotView.setWeight(Typography.BOLD)
        dotView.setType(Typography.BODY_3)
        dotView.setTextColor(Color.WHITE)
        digitContainer?.addView(dotView)
    }

    private fun progressBarAnimation(){

        val container = progressBar?.progressBarContainer
        progressBar?.progressBarHeight = ProgressBarUnify.SIZE_LARGE
        (progressBar?.progressBarWrapper?.parent as ViewGroup).clipChildren = false
        (progressBar?.progressBarWrapper)?.clipChildren = false
        (progressBar?.progressBarIndicatorWrapper)?.clipChildren = false
        (progressBar?.progressBarWrapper?.parent as ViewGroup).clipToPadding = false
        progressBar?.gravity = Gravity.CENTER_VERTICAL
        progressBar?.setProgressIcon(itemView.resources.getDrawable(R.drawable.tp_tier_progress))

        if(container?.childCount?.minus(1) ?:0  >= 0){
            val target = container?.getChildAt(container.childCount - 1)
            val moveXX = ObjectAnimator.ofFloat(target, View.SCALE_X, 2f).setDuration(ANIMATION_DURATION)
            val animatorContainerX =
                ObjectAnimator.ofFloat(target, View.SCALE_Y, 2f).setDuration(ANIMATION_DURATION)
            val animSet = AnimatorSet()

            animSet.addListener(object :Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    progressBar?.setProgressIcon(null)
                    if (!isNextTier) {
                        topSectionData?.popupNotification = null
                        refreshOnTierUpgrade.refreshReward(popupNotification)
                    }
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }

            })
            animSet.playTogether(animatorContainerX,moveXX)
            animSet.start()

        }
    }

    interface CardRuntimeHeightListener {
        fun setCardLayoutHeight(height: Int)
    }

    interface RefreshOnTierUpgrade {
        fun refreshReward(popupNotification: PopupNotification?)
    }

}