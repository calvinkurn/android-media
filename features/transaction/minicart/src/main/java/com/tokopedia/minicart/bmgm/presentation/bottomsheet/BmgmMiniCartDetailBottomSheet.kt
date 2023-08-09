package com.tokopedia.minicart.bmgm.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.minicart.R
import com.tokopedia.minicart.databinding.BottomSheetBmgmMiniCartDetailBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartDetailBottomSheet : BottomSheetUnify() {

    companion object {

        const val TAG = "BmgmCartBottomSheet"

        fun getInstance(fm: FragmentManager): BmgmMiniCartDetailBottomSheet {
            return (fm.findFragmentByTag(TAG) as? BmgmMiniCartDetailBottomSheet)
                ?: BmgmMiniCartDetailBottomSheet().apply {
                    showCloseIcon = false
                    showKnob = true
                    isDragable = true
                    clearContentPadding = true
                }
        }
    }

    private var binding: BottomSheetBmgmMiniCartDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetBmgmMiniCartDetailBinding.inflate(inflater).apply {
            setChild(root)
            setTitle(getString(R.string.mini_cart_bmgm_bottom_sheet_title))
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}