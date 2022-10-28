package com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.seller_shop_flash_sale.R
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomSheetInformationCampaignTeaserBinding
import com.tokopedia.shop.flashsale.common.extension.toBulletSpan
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignTeaserInformationBottomSheet : BottomSheetUnify() {

    companion object {
        private const val TAG = "CampaignTeaserInformationBottomSheet"
    }

    private var binding by autoClearedNullable<SsfsBottomSheetInformationCampaignTeaserBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsBottomSheetInformationCampaignTeaserBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        setupContent()
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    @SuppressLint("ResourcePackage")
    private fun setupView() {
        setTitle(getString(R.string.campaign_teaser_info_title))
        showCloseIcon = true
    }

    private fun setupContent() {
        binding?.run {
            tgPoint1CampaignInfo.run {
                text = SpannableString(getString(R.string.campaign_teaser_info_point_1)).toBulletSpan()
            }
            tgPoint2CampaignInfo.run {
                text = SpannableString(getString(R.string.campaign_teaser_info_point_2)).toBulletSpan()
            }
        }
    }
}