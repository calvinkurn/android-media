package com.tokopedia.tokopoints.view.tokopointhome.header

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.customview.DynamicItemActionView
import com.tokopedia.tokopoints.view.model.rewardtopsection.DynamicActionListItem
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.usersaving.UserSaving
import com.tokopedia.tokopoints.view.model.usersaving.UserSavingResponse
import com.tokopedia.tokopoints.view.tokopointhome.TokoPointsHomeFragmentNew
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.unifycomponents.NotificationUnify

class TopSectionVH(itemView: View, val cardRuntimeHeightListener: CardRuntimeHeightListener, val toolbarItemList: Any?) : RecyclerView.ViewHolder(itemView) {

    lateinit var cardTierInfo: ConstraintLayout
    private var dynamicAction: DynamicItemActionView? = null
    private var mTextMembershipLabel: TextView? = null
    private var mTargetText: TextView? = null
    private var mTextMembershipValueBottom: TextView? = null
    private var mTextMembershipValue: TextView? = null
    private var mImgBackground: ImageView? = null
    private var mValueMembershipDescription: String? = null
    private var mImgEgg: ImageView? = null
    private var title: TextView? = null
    private var savingValue: TextView? = null
    private var savingDesc: TextView? = null
    private var cardContainer: ConstraintLayout? = null


    fun bind(model: TokoPointsHomeFragmentNew.TopSectionResponse) {

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

        renderToolbarWithHeader(model.tokopediaRewardTopSection)
        model.userSavingResponse?.tokopointsUserSaving?.userSaving?.let { renderUserSaving(it) }
    }

    fun renderToolbarWithHeader(data: TokopediaRewardTopSection?) {
        cardTierInfo.doOnLayout {
            cardRuntimeHeightListener.setCardLayoutHeight(it.height)
        }
        mTextMembershipLabel?.text = data?.introductionText

        data?.target?.let {
            mTargetText?.setTextColor(Color.parseColor("#" + it.textColor))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mTargetText?.text = Html.fromHtml(it.text, Html.FROM_HTML_MODE_LEGACY)
            } else {
                mTargetText?.text = Html.fromHtml(it.text)
            }
            cardTierInfo.setBackgroundColor(Color.parseColor("#" + it.backgroundColor))
            cardTierInfo.setOnClickListener {
                RouteManager.route(itemView.context, ApplinkConstInternalGlobal.WEBVIEW_TITLE, itemView.context.resources.getString(R.string.tp_label_membership), CommonConstant.WebLink.MEMBERSHIP)
                AnalyticsTrackerUtil.sendEvent(itemView.context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_MEMBERSHIP, mValueMembershipDescription)
            }
        }

        ImageHandler.loadImageCircle2(itemView.context, mImgEgg, data?.profilePicture)
        mTextMembershipValueBottom?.text = mValueMembershipDescription
        data?.backgroundImageURLMobileV2?.let { mImgBackground?.loadImage(it) }
        if (data?.tier != null) {
            mTextMembershipValue?.text = data.tier.nameDesc
        }

        renderDynamicActionList(data?.dynamicActionList)
    }

    fun renderDynamicActionList(dataList: List<DynamicActionListItem?>?) {

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

    fun hideNotification(index: Int, dynamicActionListItem: DynamicActionListItem?) {
        toolbarItemList as ArrayList<NotificationUnify>
        toolbarItemList[index].hide()
        dynamicActionListItem?.counter?.isShowCounter = false
    }

    fun renderUserSaving(userSavingInfo: UserSaving) {
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
            var savingDescription=""
            var savingDescriptionStyle=""
            if (userSavingInfo.descriptions.size>1) {
                savingDescription = userSavingInfo.descriptions[1]?.text.toString()
                savingDescriptionStyle=userSavingInfo.descriptions[1]?.fontStyle.toString()
            }
            val spannable = SpannableString("$savingPercent $savingDescription")
            savingPercent?.length?.let {
                spannable.setSpan(
                        ForegroundColorSpan(Color.GREEN),
                        0, it,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            (savingDesc as TextView).text = spannable
        }

        if (!userSavingInfo.backgroundImageURL.isNullOrEmpty()) {
            ImageHandler.loadBackgroundImage(cardContainer, userSavingInfo.backgroundImageURL)
        }
    }

    interface CardRuntimeHeightListener {
        fun setCardLayoutHeight(height: Int)
    }
}