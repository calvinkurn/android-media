package com.tokopedia.shop.score.performance.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.BaseBottomSheetShopScore
import com.tokopedia.unifyprinciples.Typography

class BottomSheetProtectedParameter : BaseBottomSheetShopScore() {

    private var tvDescProtectedParameter: Typography? = null

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
        view.setupView()
    }

    private fun View.setupView() = this.run {
        val protectedParameterDesc = arguments?.getString(PROTECTED_PARAMETER_DESC_KEY).orEmpty()
        tvDescProtectedParameter = findViewById(R.id.tvDescProtectedParameter)
        tvDescProtectedParameter?.text =
            MethodChecker.fromHtml(
                getString(
                    R.string.desc_relief_parameter_for_new_seller_bottom_sheet,
                    protectedParameterDesc
                )
            )
    }

    companion object {
        const val TAG = "BottomSheetProtectedParameter"
        private const val PROTECTED_PARAMETER_DESC_KEY = "PROTECTED_PARAMETER_DESC_KEY"

        fun createInstance(protectedParameterDate: String): BottomSheetProtectedParameter {
            return BottomSheetProtectedParameter().apply {
                val bundle = Bundle()
                bundle.putString(PROTECTED_PARAMETER_DESC_KEY, protectedParameterDate)
                arguments = bundle
            }
        }
    }
}