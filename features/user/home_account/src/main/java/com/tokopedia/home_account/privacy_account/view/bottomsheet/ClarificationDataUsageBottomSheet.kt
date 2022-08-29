package com.tokopedia.home_account.privacy_account.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.LayoutBottomSheetPrivacyAccountClarificationDataUsageBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ClarificationDataUsageBottomSheet : BottomSheetUnify() {

    private var _binding: LayoutBottomSheetPrivacyAccountClarificationDataUsageBinding? by autoClearedNullable()
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutBottomSheetPrivacyAccountClarificationDataUsageBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(R.string.opt_bottom_sheet_header_clarification_data_usage))
        setViewBanner()
    }

    private fun setViewBanner() {
        binding?.carouselClarificationDataUsage?.slideToShow = CAROUSEL_SLIDE_TO_SHOW

        val itemParam = { item: View, data: Any ->
            val img = item.findViewById<ImageUnify>(R.id.image_banner_clarification_data_usage)
            img.loadImage(data.toString())
        }
        val listImageBanner = arrayListOf<Any>(
            getString(R.string.privacy_account_bottom_sheet_clarification_image_banner_1),
            getString(R.string.privacy_account_bottom_sheet_clarification_image_banner_2),
            getString(R.string.privacy_account_bottom_sheet_clarification_image_banner_3)
        )
        binding?.carouselClarificationDataUsage?.addItems(R.layout.item_custom_image_banner, listImageBanner, itemParam)
    }

    companion object {
        private const val CAROUSEL_SLIDE_TO_SHOW = 1f
    }

}