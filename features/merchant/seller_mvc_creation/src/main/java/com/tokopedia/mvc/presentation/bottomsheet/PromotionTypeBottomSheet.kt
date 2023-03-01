package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.MvcBottomsheetPromotionTypeBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PromotionTypeBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<MvcBottomsheetPromotionTypeBinding>()

    companion object {
        @JvmStatic
        fun newInstance(): PromotionTypeBottomSheet {
            return PromotionTypeBottomSheet()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBinding = MvcBottomsheetPromotionTypeBinding.inflate(inflater, container, false)
        binding = viewBinding
        setTitle(getString(R.string.mvc_bottomsheet_promotion_type_title))
        setChild(viewBinding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }

}
