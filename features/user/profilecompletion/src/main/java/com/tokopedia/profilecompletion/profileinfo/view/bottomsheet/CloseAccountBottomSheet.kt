package com.tokopedia.profilecompletion.profileinfo.view.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.profilecompletion.databinding.LayoutBottomsheetCloseAccountBinding
import com.tokopedia.profilecompletion.profileinfo.data.Detail
import com.tokopedia.unifycomponents.BottomSheetUnify

class CloseAccountBottomSheet(private val detail: Detail) : BottomSheetUnify() {

    private var _bindingChild: LayoutBottomsheetCloseAccountBinding? = null
    private val bindingChild get() = _bindingChild

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingChild = LayoutBottomsheetCloseAccountBinding.inflate(layoutInflater, container, false)
        setChild(bindingChild?.root)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewRequireLabel1(detail.hasEgold || detail.hasMutualFund || detail.hasDepositBalance)
        setViewRequireLabel2(detail.hasLoan)
        setViewRequireLabel3(detail.hasOngoingTrx)

        bindingChild?.btnOke?.setOnClickListener { dismiss() }
    }

    private fun setViewRequireLabel1(isShow: Boolean) {
        bindingChild?.apply {
            if (isShow) {
                tgRequirementDesc1.show()
                tgRequirementDesc1Center.hide()
                labelRequirementCheck1.show()
            } else {
                tgRequirementDesc1.hide()
                tgRequirementDesc1Center.show()
                labelRequirementCheck1.hide()
            }
        }
    }

    private fun setViewRequireLabel2(isShow: Boolean) {
        bindingChild?.apply {
            if (isShow) {
                tgRequirementDesc2.show()
                tgRequirementDesc2Center.hide()
                labelRequirementCheck2.show()
            } else {
                tgRequirementDesc2.hide()
                tgRequirementDesc2Center.show()
                labelRequirementCheck2.hide()
            }
        }
    }

    private fun setViewRequireLabel3(isShow: Boolean) {
        bindingChild?.apply {
            if (isShow) {
                tgRequirementDesc3.show()
                tgRequirementDesc3Center.hide()
                labelRequirementCheck3.show()
            } else {
                tgRequirementDesc3.hide()
                tgRequirementDesc3Center.show()
                labelRequirementCheck3.hide()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingChild = null
    }

}