package com.tokopedia.shop.score.performance.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.bottomsheet.BaseBottomSheetShopScore
import com.tokopedia.shop.score.databinding.BottomSheetProtectedParameterBinding

class BottomSheetProtectedParameter :
    BaseBottomSheetShopScore<BottomSheetProtectedParameterBinding>() {

    override fun bind(view: View) = BottomSheetProtectedParameterBinding.bind(view)

    override fun getLayoutResId(): Int = R.layout.bottom_sheet_protected_parameter

    override fun getTitleBottomSheet(): String =
        getString(R.string.title_relief_parameter_for_new_seller_bottom_sheet)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.setupView()
    }

    private fun BottomSheetProtectedParameterBinding?.setupView() = this?.run {
        val protectedParameterDesc = arguments?.getString(PROTECTED_PARAMETER_DESC_KEY).orEmpty()
        tvDescProtectedParameter.text = MethodChecker.fromHtml(protectedParameterDesc)
    }

    companion object {
        const val TAG = "BottomSheetProtectedParameter"
        private const val PROTECTED_PARAMETER_DESC_KEY = "PROTECTED_PARAMETER_DESC_KEY"

        fun createInstance(protectedParameterDesc: String): BottomSheetProtectedParameter {
            return BottomSheetProtectedParameter().apply {
                val bundle = Bundle()
                bundle.putString(PROTECTED_PARAMETER_DESC_KEY, protectedParameterDesc)
                arguments = bundle
            }
        }
    }
}