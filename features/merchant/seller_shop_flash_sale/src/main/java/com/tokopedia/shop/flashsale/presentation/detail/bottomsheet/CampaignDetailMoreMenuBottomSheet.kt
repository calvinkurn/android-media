package com.tokopedia.shop.flashsale.presentation.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetCampaignDetailMoreMenuBinding
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.isOngoing
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignDetailMoreMenuBottomSheet : BottomSheetUnify() {

    companion object {
        private const val TAG = "CampaignDetailMoreMenuBottomSheet"
        private const val BUNDLE_KEY_CAMPAIGN_NAME = "campaign_name"
        private const val BUNDLE_KEY_CAMPAIGN_STATUS = "campaign_status"

        fun newInstance(campaignName: String, campaignStatus: CampaignStatus): CampaignDetailMoreMenuBottomSheet {
            return CampaignDetailMoreMenuBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_CAMPAIGN_NAME, campaignName)
                    putParcelable(BUNDLE_KEY_CAMPAIGN_STATUS, campaignStatus)
                }
            }
        }
    }

    private var binding by autoClearedNullable<SsfsBottomsheetCampaignDetailMoreMenuBinding>()
    private val campaignName by lazy { arguments?.getString(BUNDLE_KEY_CAMPAIGN_NAME).orEmpty() }
    private val campaignStatus by lazy { arguments?.getParcelable(BUNDLE_KEY_CAMPAIGN_STATUS) as? CampaignStatus  ?: CampaignStatus.CANCELLED }

    private var clickListener: CampaignDetailMoreMenuClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        this.clickListener = null
        super.onDestroyView()
    }

    private fun setupBottomSheet() {
        binding = SsfsBottomsheetCampaignDetailMoreMenuBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(campaignName)
    }

    private fun setupView() {
        val binding = binding ?: return
        binding.tgCancelCampaignLabel.text = if(campaignStatus.isOngoing()) {
            getString(R.string.sfs_stop)
        } else {
            getString(R.string.sfs_cancel)
        }
        binding.root.setOnClickListener {
            clickListener?.onMenuCancelCampaignClicked()
            dismiss()
        }
    }

    fun setClickListener(listener: CampaignDetailMoreMenuClickListener) {
        this.clickListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

}