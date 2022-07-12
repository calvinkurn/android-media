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

class ClarificationDataUsageBottomSheet : BottomSheetUnify() {

    private var _binding: LayoutBottomSheetPrivacyAccountClarificationDataUsageBinding? = null
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
        binding?.carousel?.slideToShow = 1f

        val itemParam = { item: View, data: Any ->
            val img = item.findViewById<ImageUnify>(R.id.image_banner_clarification_data_usage)
            img.loadImage(data.toString())
        }
        binding?.carousel?.addItems(R.layout.item_custom_image_banner, LIST_IMAGE_BANNER, itemParam)
    }

    companion object {
        private const val BANNER_IMAGE_1 = "https://images.tokopedia.net/img/android/user/optinout/img_privacy_account_banner_1.png"
        private const val BANNER_IMAGE_2 = "https://images.tokopedia.net/img/android/user/optinout/img_privacy_account_banner_2.png"
        private const val BANNER_IMAGE_3 = "https://images.tokopedia.net/img/android/user/optinout/img_privacy_account_banner_3.png"
        val LIST_IMAGE_BANNER = arrayListOf<Any>(BANNER_IMAGE_1, BANNER_IMAGE_2, BANNER_IMAGE_3)
    }

}