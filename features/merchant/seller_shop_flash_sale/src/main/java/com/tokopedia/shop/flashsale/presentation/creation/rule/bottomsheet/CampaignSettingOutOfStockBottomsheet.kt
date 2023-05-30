package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetCampaignSettingOosBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignSettingOutOfStockBottomsheet : BottomSheetUnify() {

    companion object {
        private const val TAG = "CampaignSettingOutOfStockBottomsheet"

        @JvmStatic
        fun createInstance(): CampaignSettingOutOfStockBottomsheet = CampaignSettingOutOfStockBottomsheet()
    }

    private var binding by autoClearedNullable<SsfsBottomsheetCampaignSettingOosBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SsfsBottomsheetCampaignSettingOosBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupView() {
        showCloseIcon = true
    }

}
