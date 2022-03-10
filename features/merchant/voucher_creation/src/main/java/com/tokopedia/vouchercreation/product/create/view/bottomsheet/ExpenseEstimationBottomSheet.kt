package com.tokopedia.vouchercreation.product.create.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.FragmentExpenseEstimationBottomSheetBinding


class ExpenseEstimationBottomSheet : BottomSheetUnify() {

    private var binding: FragmentExpenseEstimationBottomSheetBinding? = null

    companion object {
        @JvmStatic
        fun newInstance(): ExpenseEstimationBottomSheet {
            return ExpenseEstimationBottomSheet()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = FragmentExpenseEstimationBottomSheetBinding.inflate(inflater, container, false)
        binding = viewBinding
        setTitle(getString(R.string.mvc_create_promo_type_bottomsheet_title_promo_expenses))
        setChild(viewBinding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }

}