package com.tokopedia.tokopoints.view.tokopointhome.header

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
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.customview.DynamicItemActionView
import com.tokopedia.tokopoints.view.model.rewardtopsection.DynamicActionListItem
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.rewrdsStatusMatching.MetadataItem
import com.tokopedia.tokopoints.view.model.rewrdsStatusMatching.TickerListItem
import com.tokopedia.tokopoints.view.model.usersaving.UserSaving
import com.tokopedia.tokopoints.view.tokopointhome.TopSectionResponse
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.convertSecondsToHrMmSs
import com.tokopedia.tokopoints.view.util.isDarkMode
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.webview.KEY_TITLEBAR


class TopSectionVH(itemView: View, private val cardRuntimeHeightListener: CardRuntimeHeightListener, private val toolbarItemList: Any?) : RecyclerView.ViewHolder(itemView) {

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
    private var parentStatusMatching : ConstraintLayout ? =null
    private var arrowIcon : AppCompatImageView?=null
    private val MEMBER_STATUS_BG_RADII = 16F

    fun bind(model: TopSectionResponse) {

        cardTierInfo = itemView.findViewById(R.id.container_target)
        dynamicAction = itemView.findViewById(R.id.dynamic_widget)
        mTextMembershipValue = itemView.findViewById(R.id.text_membership_value)
        mTargetText = itemView.findViewById(R.id.tv_targetText)
        mTextMembershipLabel = itemView.findViewById(R.id.text_membership_label)
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
        arrowIcon = itemView.findViewById(R.id.ic_arrow_icon_status)

        renderToolbarWithHeader(model.tokopediaRewardTopSection)
        model.userSavingResponse?.userSaving?.let {
            containerUserSaving?.show()
            renderUserSaving(it)
        }
        model.rewardTickerResponse?.let {
            if (!it.rewardsTickerList?.tickerList.isNullOrEmpty()) {
                cardStatusMatching?.show()
                renderStatusMatchingView(it.rewardsTickerList?.tickerList)
            }
        }
    }

    private fun renderToolbarWithHeader(data: TokopediaRewardTopSection?) {
        cardTierInfo.doOnLayout {
            cardRuntimeHeightListener.setCardLayoutHeight(it.height)
        }
        mTextMembershipLabel?.text = data?.introductionText

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

        if (dataList != null && dataList.isNotEmpty()) {
            for (i in dataList.indices) {
                dynamicAction?.setLayoutVisibility(View.VISIBLE, i)
                dataList[i]?.cta?.text?.let { dynamicAction?.setLayoutText(it, i) }
                dataList[i]?.cta?.text?.let { dynamicAction?.setLayoutText(it, i) }
                dataList[i]?.iconImageURL?.let { dynamicAction?.setLayoutIcon(it, i) }
                if (dataList[i]?.counter?.isShowCounter!! && dataList[i]?.counter?.counterStr != "0") {
                    dataList[i]?.counter?.counterStr?.let { dynamicAction?.setLayoutNotification(it, i) }
                } else {
                    dynamicAction?.hideNotification(i)
                    dataList[i]?.counter?.isShowCounter = false
                }

                if (dataList[i]?.counterTotal?.isShowCounter!!) {
                    dataList[i]?.counterTotal?.counterStr?.let { dynamicAction?.setLayoutLabel(it, i) }
                }
                dynamicAction?.findViewById<LinearLayout>(R.id.holder_tokopoint)?.setOnClickListener {
                    dataList[0]?.cta?.let {
                        hideNotification(0, dataList[0])
                        dynamicAction?.setLayoutClicklistener(it.appLink, it.text, 0)
                    }
                }
                dynamicAction?.findViewById<LinearLayout>(R.id.holder_coupon)?.setOnClickListener {
                    dataList[1]?.cta?.let {
                        hideNotification(1, dataList[1])
                        dynamicAction?.setLayoutClicklistener(it.appLink, it.text, 1)
                    }
                }
                dynamicAction?.findViewById<LinearLayout>(R.id.holder_tokomember)?.setOnClickListener {
                    dataList[2]?.cta?.let {
                        hideNotification(2, dataList[2])
                        dynamicAction?.setLayoutClicklistener(it.appLink, it.text, 2)
                    }
                }

                dynamicAction?.setVisibilityDivider(View.VISIBLE, i)
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

        AnalyticsTrackerUtil.sendEvent(
            AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT_IRIS,
            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
            AnalyticsTrackerUtil.ActionKeys.VIEW_STATUSMATCHING_ON_REWARDS, "",
            AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
            AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
        )
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
                    AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
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

     /*   val layoutParamsIcon = arrowIcon?.layoutParams
        layoutParamsIcon?.height = itemView.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
        layoutParamsIcon?.width = itemView.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
        arrowIcon?.layoutParams = layoutParamsIcon*/

        val layoutParams = cardStatusMatching?.layoutParams
        layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        cardStatusMatching?.layoutParams = layoutParams
    }

    private fun playAnimation(){
        confettiAnim?.repeatCount = ValueAnimator.INFINITE
        confettiAnim?.playAnimation()
    }

     private fun hideStatusMatching() {
         cardStatusMatching?.hide()
         containerStatusMatching?.hide()
    }

    interface CardRuntimeHeightListener {
        fun setCardLayoutHeight(height: Int)
    }
}