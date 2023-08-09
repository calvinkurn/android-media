package com.tokopedia.minicart.bmgm.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.minicart.R
import com.tokopedia.minicart.databinding.BottomSheetBmgmMiniCartBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartBottomSheet : BottomSheetUnify() {

    companion object {

        const val TAG = "BmgmCartBottomSheet"

        fun getInstance(fm: FragmentManager): BmgmMiniCartBottomSheet {
            return (fm.findFragmentByTag(TAG) as? BmgmMiniCartBottomSheet)
                ?: BmgmMiniCartBottomSheet().apply {
                    showCloseIcon = false
                    showKnob = true
                    isDragable = true
                }
        }
    }

    private var binding: BottomSheetBmgmMiniCartBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetBmgmMiniCartBinding.inflate(inflater).apply {
            setChild(root)
            setTitle(getString(R.string.mini_cart_bmgm_bottom_sheet_title))
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}