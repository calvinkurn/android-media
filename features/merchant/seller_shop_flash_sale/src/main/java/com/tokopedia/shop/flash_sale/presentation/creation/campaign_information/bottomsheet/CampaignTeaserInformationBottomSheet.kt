package com.tokopedia.shop.flash_sale.presentation.creation.campaign_information.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import com.tokopedia.seller_shop_flash_sale.R
import androidx.fragment.app.FragmentManager
import com.tokopedia.shop.flash_sale.common.extension.setBulletSpan

import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class CampaignTeaserInformationBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context): CampaignTeaserInformationBottomSheet =
            CampaignTeaserInformationBottomSheet().apply {
                val view = View.inflate(
                    context,
                    R.layout.ssfs_bottom_sheet_informasi_campaign_teaser,
                    null
                )
                setChild(view)
            }

        private const val TAG = "CampaignTeaserInformationBottomSheet"
    }

    private var campaignTeaserInfoPoint1: Typography? = null
    private var campaignTeaserInfoPoint2: Typography? = null

    @SuppressLint("ResourcePackage")
    private val campaignTeaserInfoPoint1Text =
        SpannableString(getString(R.string.campaign_teaser_info_point_1))
    @SuppressLint("ResourcePackage")
    private val campaignTeaserInfoPoint2text =
        SpannableString(getString(R.string.campaign_teaser_info_point_2))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
        setupContent()
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    @SuppressLint("ResourcePackage")
    private fun setupView(view: View) {
        setTitle(getString(R.string.campaign_teaser_info_title))

        campaignTeaserInfoPoint1 = view.findViewById(R.id.tg_point_1_campaign_info)
        campaignTeaserInfoPoint2 = view.findViewById(R.id.tg_point_2_campaign_info)
        showCloseIcon = true
    }

    private fun setupContent() {
        campaignTeaserInfoPoint1Text.setBulletSpan()
        campaignTeaserInfoPoint2text.setBulletSpan()

        campaignTeaserInfoPoint1?.run {
            text = campaignTeaserInfoPoint1Text
        }
        campaignTeaserInfoPoint2?.run {
            text = campaignTeaserInfoPoint2text
        }
    }
}