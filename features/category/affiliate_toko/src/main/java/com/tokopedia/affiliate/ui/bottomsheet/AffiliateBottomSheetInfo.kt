package com.tokopedia.affiliate.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class AffiliateBottomSheetInfo : BottomSheetUnify() {
    private lateinit var contentView: View
    private var tickerData: AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data.TickerData? =
        null
    private var tickerId: Long = 0

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
                    putSerializable(TICKER_DATA, tickerData)
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
            tickerData =
                it.getSerializable(TICKER_DATA) as AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data.TickerData
        }
        contentView = View.inflate(context, R.layout.affiliate_bottom_sheet_info, null)
        setData()
        setChild(contentView)
    }

    private fun setData() {
        if (!tickerData?.illustrationURL.isNullOrEmpty()) {
            contentView.findViewById<ImageUnify>(R.id.info_image).apply {
                visible()
                loadImage(tickerData?.illustrationURL)
            }
        }
        if (!tickerData?.ctaTextSecondary.isNullOrEmpty()) {
            contentView.findViewById<UnifyButton>(R.id.info_button_secondary).apply {
                visible()
                text = tickerData?.ctaTextSecondary
                setOnClickListener {
                    RouteManager.route(
                        requireContext(),
                        tickerData?.ctaLinkSecondary?.androidURL
                    )
                }
            }
        }
        contentView.findViewById<Typography>(R.id.info_title).text = tickerData?.announcementTitle
        contentView.findViewById<Typography>(R.id.info_description).text =
            tickerData?.announcementDescription
        contentView.findViewById<UnifyButton>(R.id.info_button_primary).text = tickerData?.ctaText
        contentView.findViewById<UnifyButton>(R.id.info_button_primary).setOnClickListener {
            RouteManager.route(requireContext(), tickerData?.ctaLink?.androidURL)
        }

    }

}