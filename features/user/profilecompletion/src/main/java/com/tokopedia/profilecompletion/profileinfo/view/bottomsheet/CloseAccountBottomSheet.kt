package com.tokopedia.profilecompletion.profileinfo.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.profilecompletion.databinding.LayoutBottomsheetCloseAccountBinding
import com.tokopedia.profilecompletion.profileinfo.data.Detail
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.profilecompletion.R

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

        setViewRequireImage()

        setViewRequireLabel1(detail.hasEgold || detail.hasMutualFund || detail.hasDepositBalance)
        setViewRequireLabel2(detail.hasLoan)
        setViewRequireLabel3(detail.hasOngoingTrx)

        bindingChild?.btnOke?.setOnClickListener { dismiss() }
    }

    private fun setViewRequireImage() {
        bindingChild?.apply {
            imgRequirement1.loadImage(getString(R.string.close_account_requirement_image_1))
            imgRequirement2.loadImage(getString(R.string.close_account_requirement_image_2))
            imgRequirement3.loadImage(getString(R.string.close_account_requirement_image_3))
        }
    }

    private fun setViewRequireLabel1(isShow: Boolean) {
        bindingChild?.apply {
            tgRequirementDesc1.showWithCondition(isShow)
            tgRequirementDesc1Center.showWithCondition(!isShow)
            labelRequirementCheck1.showWithCondition(isShow)
        }
    }

    private fun setViewRequireLabel2(isShow: Boolean) {
        bindingChild?.apply {
            tgRequirementDesc2.showWithCondition(isShow)
            tgRequirementDesc2Center.showWithCondition(!isShow)
            labelRequirementCheck2.showWithCondition(isShow)
        }
    }

    private fun setViewRequireLabel3(isShow: Boolean) {
        bindingChild?.apply {
            tgRequirementDesc3.showWithCondition(isShow)
            tgRequirementDesc3Center.showWithCondition(!isShow)
            labelRequirementCheck3.showWithCondition(isShow)
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