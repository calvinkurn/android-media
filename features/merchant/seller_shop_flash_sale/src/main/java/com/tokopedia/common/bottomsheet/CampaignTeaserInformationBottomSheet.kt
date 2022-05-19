package com.tokopedia.common.bottomsheet

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BulletSpan
import android.view.View
import com.tokopedia.seller_shop_flash_sale.R
import androidx.fragment.app.FragmentManager

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
    private val campaignTeaserInfoPoint1Text =
        SpannableString("Calon pembeli dapat melihat preview produk campaign mendatang dan mengaktifkan pengingat campaign.")
    private val campaignTeaserInfoPoint2text =
        SpannableString("Pembelian di luar jadwal campaign akan dikenakan harga normal.")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
        setupContent()
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupView(view: View) {
        setTitle("Informasi Teaser Campaign")

        campaignTeaserInfoPoint1 = view.findViewById(R.id.tg_point_1_campaign_info)
        campaignTeaserInfoPoint2 = view.findViewById(R.id.tg_point_2_campaign_info)
        showCloseIcon = true
    }

    private fun setupContent() {
        campaignTeaserInfoPoint1Text.setSpan(
            BulletSpan(16, Color.BLACK),
            0,
            0,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        campaignTeaserInfoPoint2text.setSpan(
            BulletSpan(16, Color.BLACK),
            0,
            0,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        campaignTeaserInfoPoint1?.run {
            text = campaignTeaserInfoPoint1Text
        }
        campaignTeaserInfoPoint2?.run {
            text = campaignTeaserInfoPoint2text
        }
    }
}