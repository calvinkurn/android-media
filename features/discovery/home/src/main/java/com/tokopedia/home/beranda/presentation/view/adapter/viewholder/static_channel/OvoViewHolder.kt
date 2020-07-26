package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.HexValidator
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.common_wallet.analytics.CommonWalletAnalytics
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.analytics.v2.OvoWidgetTracking
import com.tokopedia.home.beranda.data.model.SectionContentItem
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_OVO_VIEWHOLDER
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.util.ViewUtils
import com.tokopedia.kotlin.extensions.view.getResColor
import kotlin.math.roundToInt

/**
 * Created by Lukas on 2019-08-20
 */
class OvoViewHolder(itemView: View, val listener: HomeCategoryListener) : AbstractViewHolder<HeaderDataModel>(itemView) {
    private val context = itemView.context

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_item_widget_ovo_tokopoint_login
        @LayoutRes
        val NON_LOGIN_LAYOUT = R.layout.layout_item_widget_ovo_tokopoint_nonlogin

        private const val TITLE_HEADER_WEBSITE = "Tokopedia"
        private const val TITLE = "OVO"
        private const val WALLET_TYPE = "OVO"
        private const val CDN_URL = "https://ecs7.tokopedia.net/img/android/"
        private const val BG_CONTAINER_URL = CDN_URL + "bg_product_fintech_tokopoint_normal/" +
                "drawable-xhdpi/bg_product_fintech_tokopoint_normal.png"
    }

    private val walletAnalytics: CommonWalletAnalytics = CommonWalletAnalytics()

    override fun bind(element: HeaderDataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_OVO_VIEWHOLDER)
        if (element.isUserLogin) renderLogin(element)
        else renderNonLogin()
        BenchmarkHelper.endSystraceSection()
    }

    private fun renderNonLogin() {
        val scanHolder = itemView.findViewById<View>(R.id.container_action_scan)
        val container = itemView.findViewById<View>(R.id.container_nonlogin)
        val imgNonLogin = itemView.findViewById<AppCompatImageView>(R.id.bg_container_nonlogin)
        val containerOvo = itemView.findViewById<LinearLayout>(R.id.container_ovo)
        containerOvo.background = ViewUtils.generateBackgroundWithShadow(containerOvo, R.color.white, R.dimen.dp_8, R.color.shadow_6, R.dimen.dp_2, Gravity.CENTER)
        val radius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16f, itemView.resources.displayMetrics).roundToInt()
        Glide.with(itemView.context.applicationContext)
                .load(BG_CONTAINER_URL)
                .transform(RoundedCorners(radius))
                .into(imgNonLogin)

        container.setOnClickListener {
            HomePageTracking.eventTokopointNonLogin(itemView.context)
            listener.onTokopointCheckNowClicked(ApplinkConstInternalPromo.TOKOPOINTS_HOME)
        }
        scanHolder.setOnClickListener { goToScanner() }
    }

    private fun renderLogin(element: HeaderDataModel) {
        val containerOvo = itemView.findViewById<LinearLayout>(R.id.container_ovo)
        containerOvo.background = ViewUtils.generateBackgroundWithShadow(containerOvo, R.color.white, R.dimen.dp_8, R.color.shadow_6, R.dimen.dp_2, Gravity.CENTER)
        renderOvoLayout(element)
        renderTokoPoint(element)
    }

    private fun goToScanner() {
        HomePageTracking.eventQrCode(itemView.context)
        RouteManager.route(itemView.context, ApplinkConstInternalMarketplace.QR_SCANNEER)
    }

    @SuppressLint("SetTextI18n")
    private fun renderOvoLayout(element: HeaderDataModel) {
        val scanHolder = itemView.findViewById<View>(R.id.container_action_scan)
        val tokoCashHolder = itemView.findViewById<View>(R.id.container_tokocash)
        val tvActionTokocash = itemView.findViewById<TextView>(R.id.tv_btn_action_tokocash)
        val tvTitleTokocash = itemView.findViewById<TextView>(R.id.tv_title_tokocash)
        val tvBalanceTokocash = itemView.findViewById<TextView>(R.id.tv_balance_tokocash)
        val ivLogoTokocash = itemView.findViewById<ImageView>(R.id.iv_logo_tokocash)
        val tokocashProgressBar = itemView.findViewById<View>(R.id.progress_bar_tokocash)
        scanHolder.setOnClickListener { goToScanner() }
        tvBalanceTokocash.setTextColor(itemView.context.getResColor(R.color.font_black_disabled_38))

        if (element.homeHeaderWalletActionData == null && element.isWalletDataError) {
            tokoCashHolder.setOnClickListener {
                tokocashProgressBar.visibility = View.VISIBLE
                listener.onRefreshTokoCashButtonClicked()
            }
            tvTitleTokocash.setText(R.string.home_header_tokocash_unable_to_load_label)
            tvActionTokocash.setText(R.string.home_header_tokocash_refresh_label)
            tvActionTokocash.visibility = View.VISIBLE
            tvBalanceTokocash.visibility = View.GONE
            tokocashProgressBar.visibility = View.GONE
        } else if (element.homeHeaderWalletActionData == null && !element.isWalletDataError) {
            tvActionTokocash.visibility = View.GONE
            tvTitleTokocash.visibility = View.GONE
            tvBalanceTokocash.visibility = View.GONE
            tokoCashHolder.setOnClickListener(null)
            tokocashProgressBar.visibility = View.VISIBLE
        } else {
            val homeHeaderWalletAction = element.homeHeaderWalletActionData

            homeHeaderWalletAction?.let {homeHeaderWalletAction ->
                if (!TextUtils.isEmpty(homeHeaderWalletAction.walletType) && homeHeaderWalletAction.walletType == WALLET_TYPE) {
                    tokocashProgressBar.visibility = View.GONE
                    tvActionTokocash.text = homeHeaderWalletAction.labelActionButton
                    tvActionTokocash.setOnClickListener { goToOvoAppLink(homeHeaderWalletAction.isLinked, homeHeaderWalletAction.appLinkActionButton) }
                    tokoCashHolder.setOnClickListener { goToOvoAppLink(homeHeaderWalletAction.isLinked, homeHeaderWalletAction.appLinkBalance) }

                    if (homeHeaderWalletAction.isLinked) {

                        tvTitleTokocash.text = homeHeaderWalletAction.cashBalance
                        tvActionTokocash.visibility = View.VISIBLE
                        tvBalanceTokocash.visibility = View.VISIBLE
                        tvActionTokocash.visibility = if (homeHeaderWalletAction.isVisibleActionButton) View.VISIBLE else View.GONE
                        tvTitleTokocash.visibility = if (homeHeaderWalletAction.isVisibleActionButton) View.GONE else View.VISIBLE
                        if (homeHeaderWalletAction.isShowTopup) {
                            tvBalanceTokocash.setTextColor(itemView.context.getResColor(R.color.tkpd_main_green))
                            tvBalanceTokocash.text = itemView.resources.getString(R.string.home_header_topup_ovo)
                            tvBalanceTokocash.setTypeface(tvBalanceTokocash.getTypeface(), Typeface.BOLD)
                            tokoCashHolder.setOnClickListener { gotToTopupOvo(homeHeaderWalletAction.topupUrl) }
                        } else {
                            tvBalanceTokocash.text = itemView.resources.getString(R.string.home_header_fintech_points, homeHeaderWalletAction.pointBalance)
                        }
                    } else {
                        tvTitleTokocash.text = TITLE
                        tvActionTokocash.visibility = View.VISIBLE
                        tvBalanceTokocash.visibility = View.GONE
                        if (element.isPendingTokocashChecked && element.cashBackData != null) {
                            if (element.cashBackData?.amount?:0 > 0) {
                                tvTitleTokocash.text = "(+ ${element?.cashBackData?.amountText} )"
                            }
                        } else {
                            listener.onRequestPendingCashBack()
                        }
                        tvTitleTokocash.visibility = View.VISIBLE
                    }
                } else {
                    tokocashProgressBar.visibility = View.GONE
                    tvTitleTokocash.text = homeHeaderWalletAction.labelTitle
                    tvActionTokocash.text = homeHeaderWalletAction.labelActionButton
                    tvActionTokocash.setOnClickListener {
                        if (!homeHeaderWalletAction.appLinkActionButton.contains("webview") && !homeHeaderWalletAction.isLinked) {
                            HomePageTracking.eventTokoCashActivateClick(itemView.context)
                        }
                        listener.actionAppLinkWalletHeader(homeHeaderWalletAction.appLinkActionButton)
                    }
                    tokoCashHolder.setOnClickListener {
                        if (homeHeaderWalletAction.appLinkBalance != "" &&
                                !homeHeaderWalletAction.appLinkBalance.contains("webview") &&
                                homeHeaderWalletAction.isLinked) {
                            HomePageTracking.eventTokoCashCheckSaldoClick(itemView.context)
                        }

                        listener.actionAppLinkWalletHeader(homeHeaderWalletAction.appLinkBalance)
                    }
                    ivLogoTokocash.setImageResource(R.drawable.ic_tokocash)

                    if (homeHeaderWalletAction.isLinked) {
                        tvBalanceTokocash.visibility = View.VISIBLE
                        tvBalanceTokocash.text = homeHeaderWalletAction.balance

                        tvActionTokocash.visibility = if (homeHeaderWalletAction.isVisibleActionButton) View.VISIBLE else View.GONE
                        tvTitleTokocash.visibility = if (homeHeaderWalletAction.isVisibleActionButton) View.GONE else View.VISIBLE
                    } else {
                        tvBalanceTokocash.visibility = View.GONE
                        tvActionTokocash.visibility = View.VISIBLE
                        if (element.isPendingTokocashChecked && element.cashBackData != null) {
                            if (element.cashBackData?.amount?:0 > 0) {
                                tvActionTokocash.visibility = View.GONE
                                tvBalanceTokocash.visibility = View.VISIBLE
                                tvBalanceTokocash.text = element?.cashBackData?.amountText?:""
                                tvBalanceTokocash.setOnClickListener {
                                    element.cashBackData?.let {
                                        listener.actionInfoPendingCashBackTokocash(it, homeHeaderWalletAction.appLinkActionButton)
                                    }
                                }
                            }
                        } else {
                            listener.onRequestPendingCashBack()
                        }
                    }
                }
            }
        }
    }

    private fun renderTokoPoint(element: HeaderDataModel) {
        val tokoPointHolder = itemView.findViewById<View>(R.id.container_tokopoint)
        val tvBalanceTokoPoint = itemView.findViewById<TextView>(R.id.tv_balance_tokopoint)
        val tvActionTokopoint = itemView.findViewById<TextView>(R.id.tv_btn_action_tokopoint)
        val ivLogoTokoPoint = itemView.findViewById<ImageView>(R.id.iv_logo_tokopoint)
        val tokopointProgressBarLayout = itemView.findViewById<View>(R.id.progress_bar_tokopoint_layout)
        val tokopointActionContainer = itemView.findViewById<View>(R.id.container_action_tokopoint)
        val mTextCouponCount = itemView.findViewById<TextView>(R.id.text_coupon_count)
        ivLogoTokoPoint.setImageResource(R.drawable.ic_product_fintech_tokopoint_green_24)
        if (element.tokopointsDrawerHomeData == null && element.isTokoPointDataError) {
            tokoPointHolder.setOnClickListener {
                tokopointProgressBarLayout.visibility = View.VISIBLE
                listener.onRefreshTokoPointButtonClicked()
            }
            mTextCouponCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            mTextCouponCount.visibility = View.VISIBLE
            mTextCouponCount.setText(R.string.home_header_tokopoint_unable_to_load_label)
            mTextCouponCount.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_black_primary_70))
            tvActionTokopoint.setText(R.string.home_header_tokopoint_refresh_label)
            tvActionTokopoint.visibility = View.VISIBLE
            tokopointProgressBarLayout.visibility = View.GONE
            tokopointActionContainer.visibility = View.VISIBLE
            ivLogoTokoPoint.setImageResource(R.drawable.ic_product_fintech_tokopoint_normal_24)
            tvBalanceTokoPoint.visibility = View.GONE
        } else if (element.tokopointsDrawerHomeData == null && !element.isTokoPointDataError) {
            tokoPointHolder.setOnClickListener(null)
            tokopointProgressBarLayout.visibility = View.VISIBLE
            tokopointActionContainer.visibility = View.GONE
            tvBalanceTokoPoint.visibility = View.GONE
        } else {
            tokopointProgressBarLayout.visibility = View.GONE
            tokopointActionContainer.visibility = View.VISIBLE
            tvActionTokopoint.visibility = View.GONE
            tvBalanceTokoPoint.visibility = View.VISIBLE
            mTextCouponCount.visibility = View.VISIBLE

            ImageHandler.loadImageAndCache(ivLogoTokoPoint, element.tokopointsDrawerHomeData?.iconImageURL)
            mTextCouponCount.setTypeface(mTextCouponCount.typeface, Typeface.BOLD)
            element.tokopointsDrawerHomeData?.sectionContent?.let { sectionContent ->
                if (sectionContent.isNotEmpty()) {
                    setTokopointHeaderData(sectionContent[0], tvBalanceTokoPoint)
                    if (sectionContent.size >= 2) {
                        setTokopointHeaderData(sectionContent[1], mTextCouponCount)
                    }
                } else {
                    tvBalanceTokoPoint.setText(R.string.home_header_tokopoint_no_tokopoints)
                    mTextCouponCount.setText(R.string.home_header_tokopoint_no_coupons)
                    tvBalanceTokoPoint.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_black_primary_70))
                    mTextCouponCount.setTextColor(ContextCompat.getColor(itemView.context, R.color.tkpd_main_green))
                }
            }

            tokoPointHolder.setOnClickListener {
                if (element.tokopointsDrawerHomeData != null) {
                    HomePageTracking.eventUserProfileTokopoints(itemView.context)
                    element.tokopointsDrawerHomeData?.let {tokopointsDrawerHomeData->
                        listener.actionTokoPointClicked(
                                tokopointsDrawerHomeData.redirectAppLink,
                                tokopointsDrawerHomeData.redirectURL,
                                if (TextUtils.isEmpty(tokopointsDrawerHomeData.mainPageTitle))
                                    TITLE_HEADER_WEBSITE
                                else
                                    tokopointsDrawerHomeData.mainPageTitle
                        )

                        if (tokopointsDrawerHomeData.sectionContent.isNotEmpty() &&
                                tokopointsDrawerHomeData.sectionContent[0].tagAttributes?.text?.isNotEmpty() == true) {
                            HomePageTracking.sendClickOnTokopointsNewCouponTracker()
                        } else {
                            HomePageTracking.sendTokopointTrackerClick()
                        }
                    }
                }
            }
        }
    }

    private fun setTokopointHeaderData(sectionContentItem: SectionContentItem?, tokopointsTextView: TextView) {
        if (sectionContentItem != null) {

            //Initializing to default value to prevent stale data in case of onresume
            tokopointsTextView.background = null
            tokopointsTextView.text = null
            tokopointsTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(R.dimen.sp_12))

            if (sectionContentItem.tagAttributes != null && !TextUtils.isEmpty(sectionContentItem.tagAttributes.text)) {
                if (!TextUtils.isEmpty(sectionContentItem.tagAttributes.backgroundColour) && HexValidator.validate(sectionContentItem.tagAttributes.backgroundColour)) {
                    val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.bg_tokopoints_rounded)
                    if (drawable is GradientDrawable) {
                        val shapeDrawable = drawable as GradientDrawable?
                        shapeDrawable!!.setColorFilter(Color.parseColor(sectionContentItem.tagAttributes.backgroundColour), PorterDuff.Mode.SRC_ATOP)
                        tokopointsTextView.background = shapeDrawable
                        val horizontalPadding = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_2)
                        tokopointsTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(R.dimen.sp_8))
                        tokopointsTextView.setTypeface(null, Typeface.NORMAL)
                        tokopointsTextView.setPadding(horizontalPadding, 0, horizontalPadding, 0)
                    }
                    tokopointsTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                } else {
                    tokopointsTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_black_primary_70))
                }
                if (!TextUtils.isEmpty(sectionContentItem.tagAttributes.text)) {
                    tokopointsTextView.text = sectionContentItem.tagAttributes.text
                }
            } else if (sectionContentItem.textAttributes != null && !TextUtils.isEmpty(sectionContentItem.textAttributes.text)) {
                if (!TextUtils.isEmpty(sectionContentItem.textAttributes.colour) && HexValidator.validate(sectionContentItem.textAttributes.colour)) {
                    tokopointsTextView.setTextColor(Color.parseColor(sectionContentItem.textAttributes.colour))
                } else {
                    tokopointsTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_black_primary_70))
                }
                if (sectionContentItem.textAttributes.isBold) {
                    tokopointsTextView.setTypeface(null, Typeface.BOLD)
                } else {
                    tokopointsTextView.setTypeface(null, Typeface.NORMAL)
                }
                if (!TextUtils.isEmpty(sectionContentItem.textAttributes.text)) {
                    tokopointsTextView.text = sectionContentItem.textAttributes.text
                }

            }
        }
    }

    private fun goToOvoAppLink(linkedOvo: Boolean, applinkString: String) {
        if (RouteManager.isSupportApplink(context, applinkString)) {
            if (!linkedOvo) {
                if (context !is Activity && context is ContextWrapper) {
                    val context = context.baseContext
                    val activity = context as Activity
                    activity.overridePendingTransition(R.anim.anim_slide_up_in, R.anim.anim_page_stay)
                }
                walletAnalytics.eventClickActivationOvoHomepage()
            } else {
                OvoWidgetTracking.eventOvo(itemView.context)
            }
            val intentBalanceWallet = RouteManager.getIntent(context, applinkString)
            context.startActivity(intentBalanceWallet)
        }
    }
    private fun gotToTopupOvo(applinkString: String) {
        if (RouteManager.isSupportApplink(context, applinkString)) {
            OvoWidgetTracking.eventTopupOvo(listener.userId)
            val intentBalanceWallet = RouteManager.getIntent(context, applinkString)
            context.startActivity(intentBalanceWallet)
        }
    }
}
