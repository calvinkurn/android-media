package com.tokopedia.tokopoints.view.tokopointhome

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.text.Html
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
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.unifycomponents.NotificationUnify

class TopSectionVH(itemView: View, val cardRuntimeHeightListener: CardRuntimeHeightListener, val toolbarItemList: Any?, val adapter: SectionAdapter?) : RecyclerView.ViewHolder(itemView) {

    lateinit var cardTierInfo: ConstraintLayout
    private var dynamicAction: DynamicItemActionView? = null
    private var mTextMembershipLabel: TextView? = null
    private var mTargetText: TextView? = null
    private var mTextMembershipValueBottom: TextView? = null
    private var mTextMembershipValue: TextView? = null
    private var mImgBackground: ImageView? = null
    private var mValueMembershipDescription: String? = null
    private var mImgEgg: ImageView? = null

    fun bind(model: TokopediaRewardTopSection) {

        cardTierInfo = itemView.findViewById(R.id.container_target)
        dynamicAction = itemView.findViewById(R.id.dynamic_widget)
        mTextMembershipValue = itemView.findViewById(R.id.text_membership_value)
        mTargetText = itemView.findViewById(R.id.tv_targetText)
        mTextMembershipLabel = itemView.findViewById(R.id.text_membership_label)
        mImgEgg = itemView.findViewById(R.id.img_egg)
        mImgBackground = itemView.findViewById(R.id.img_bg_header)

        renderToolbarWithHeader(model)
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
            cardTierInfo.background.setColorFilter(Color.parseColor("#" + it.backgroundColor), PorterDuff.Mode.SRC_OVER)
            cardTierInfo.setOnClickListener {
                RouteManager.route(itemView.context, ApplinkConstInternalGlobal.WEBVIEW_TITLE, itemView.context.resources.getString(R.string.tp_label_membership), CommonConstant.WebLink.MEMBERSHIP)
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
            dynamicAction?.setFirstLayoutVisibility(View.VISIBLE)
            dataList[0]?.cta?.text?.let { dynamicAction?.setFirstLayoutText(it) }
            dataList[0]?.iconImageURL?.let { dynamicAction?.setFirstLayoutIcon(it) }
            if (dataList[0]?.counter?.isShowCounter!! && dataList[0]?.counter?.counterStr != "0") {
                dataList[0]?.counter?.counterStr?.let { dynamicAction?.setFirstLayoutNotification(it) }
            }
            else{
                dynamicAction?.notifFirstLayout?.hide()
            }
            dynamicAction?.findViewById<LinearLayout>(R.id.holder_tokopoint)?.setOnClickListener {
                dataList[0]?.cta?.let {
                    hideNotification(0)
                    dynamicAction?.setLayoutClickListener(it.appLink, it.text)
                }
            }

            if (dataList.size > 1) {
                dynamicAction?.setCenterLayoutVisibility(View.VISIBLE)
                dataList[1]?.cta?.text?.let { dynamicAction?.setCenterLayoutText(it) }
                dataList[1]?.iconImageURL?.let { dynamicAction?.setCenterLayoutIcon(it) }
                if (dataList[1]?.counter?.isShowCounter!! && dataList[1]?.counter?.counterStr != "0") {
                    dataList[1]?.counter?.counterStr?.let { dynamicAction?.setCenterLayoutNotification(it) }
                }
                else{
                    dynamicAction?.notifCenterLayout?.hide()
                }
                dynamicAction?.findViewById<LinearLayout>(R.id.holder_coupon)?.setOnClickListener {
                    dataList[1]?.cta?.let {
                        hideNotification(1)
                        dynamicAction?.setCenterLayoutClickListener(it.appLink, it.text)
                    }
                }
                dynamicAction?.setVisibilityDividerOne(View.VISIBLE)
            }
            if (dataList.size > 2) {
                dynamicAction?.setRightLayoutVisibility(View.VISIBLE)
                dataList[2]?.cta?.text?.let { dynamicAction?.setRightLayoutText(it) }
                dataList[2]?.iconImageURL?.let { dynamicAction?.setRightLayoutIcon(it) }
                if (dataList[2]?.counter?.isShowCounter!! && dataList[2]?.counter?.counterStr != "0") {
                    dataList[2]?.counter?.counterStr?.let { dynamicAction?.setRightLayoutNotification(it) }
                }
                else{
                    dynamicAction?.notifRightLayout?.hide()
                }
                dynamicAction?.findViewById<LinearLayout>(R.id.holder_tokomember)?.setOnClickListener {
                    dataList[2]?.cta?.let {
                        hideNotification(2)
                        dynamicAction?.setRightLayoutClickListener(it.appLink, it.text)
                    }
                }
                dynamicAction?.setVisibilityDividerTwo(View.VISIBLE)
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

    fun hideNotification(index: Int) {
        toolbarItemList as ArrayList<NotificationUnify>
        toolbarItemList[index].hide()
    }

    interface CardRuntimeHeightListener {
        fun setCardLayoutHeight(height: Int)
    }
}