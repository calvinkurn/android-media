package com.tokopedia.affiliate.ui.bottomsheet

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateBottomSheetInfo : BottomSheetUnify() {
    private var contentView: View? = null
    private var tickerData: AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data.TickerData? =
        null
    private var tickerId: Long = 0

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    companion object {
        private const val TICKER_DATA = "tickerData"
        private const val TICKER_ID = "tickerId"
        fun newInstance(
            tickerId: Long,
            tickerData: AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data.TickerData?
        ): AffiliateBottomSheetInfo {
            return AffiliateBottomSheetInfo().apply {
                arguments = Bundle().apply {
                    putLong(TICKER_ID, tickerId)
                    putParcelable(TICKER_DATA, tickerData)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        arguments?.let {
            tickerId = it.getLong(TICKER_ID)
            tickerData = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(TICKER_DATA) as? AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data.TickerData
            } else {
                it.getParcelable(
                    TICKER_DATA,
                    AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data.TickerData::class.java
                )
            }
        }
        contentView = View.inflate(context, R.layout.affiliate_bottom_sheet_info, null)
        setData()
        setChild(contentView)
        setCloseClickListener {
            sendCloseClickEvent()
            dismiss()
        }
    }

    private fun sendCloseClickEvent() {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_CLOSE,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_BOTTOM_SHEET_COMMUNICATION,
            "$tickerId",
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun setData() {
        if (!tickerData?.illustrationURL.isNullOrEmpty()) {
            contentView?.findViewById<ImageUnify>(R.id.info_image)?.apply {
                visible()
                loadImage(tickerData?.illustrationURL)
            }
        }
        if (!tickerData?.ctaTextSecondary.isNullOrEmpty()) {
            contentView?.findViewById<UnifyButton>(R.id.info_button_secondary)?.apply {
                visible()
                text = tickerData?.ctaTextSecondary
                setOnClickListener {
                    sendSecondaryCtaEvent()
                    RouteManager.route(
                        requireContext(),
                        tickerData?.ctaLinkSecondary?.androidURL
                    )
                }
            }
        }
        contentView?.findViewById<Typography>(R.id.info_title)?.text = tickerData?.announcementTitle
        contentView?.findViewById<Typography>(R.id.info_description)?.text =
            tickerData?.announcementDescription
        contentView?.findViewById<UnifyButton>(R.id.info_button_primary)?.text = tickerData?.ctaText
        contentView?.findViewById<UnifyButton>(R.id.info_button_primary)?.setOnClickListener {
            sendPrimaryCtaEvent()
            RouteManager.route(requireContext(), tickerData?.ctaLink?.androidURL)
        }
    }

    private fun sendPrimaryCtaEvent() {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_PRIMARY_BUTTON,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_BOTTOM_SHEET_COMMUNICATION,
            "$tickerId",
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun sendSecondaryCtaEvent() {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_SECONDARY_BUTTON,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_BOTTOM_SHEET_COMMUNICATION,
            "$tickerId",
            userSessionInterface?.userId.orEmpty()
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initInject()
        return super.onCreateDialog(savedInstanceState)
    }

    private fun initInject() {
        getComponent().injectHomeBottomSheetCommunication(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
}
