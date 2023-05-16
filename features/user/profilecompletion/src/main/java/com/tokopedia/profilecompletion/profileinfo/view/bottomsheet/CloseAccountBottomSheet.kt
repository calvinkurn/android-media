package com.tokopedia.profilecompletion.profileinfo.view.bottomsheet

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.databinding.LayoutBottomsheetCloseAccountBinding
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule
import com.tokopedia.profilecompletion.profileinfo.data.Detail
import com.tokopedia.profilecompletion.profileinfo.tracker.CloseAccountTracker
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class CloseAccountBottomSheet(private val detail: Detail) : BottomSheetUnify() {

    @Inject
    lateinit var closeAccountTracker: CloseAccountTracker

    private var _bindingChild: LayoutBottomsheetCloseAccountBinding? = null
    private val bindingChild get() = _bindingChild

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.application?.let { getComponent(it).inject(this) }
    }

    private fun getComponent(application: Application): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .profileCompletionSettingModule(ProfileCompletionSettingModule(requireActivity()))
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingChild =
            LayoutBottomsheetCloseAccountBinding.inflate(layoutInflater, container, false)
        setChild(bindingChild?.root)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewRequireImage()

        val arrayReasonTracking = arrayListOf<String>()

        val require1 = detail.hasEgold || detail.hasMutualFund || detail.hasDepositBalance
        val require2 = detail.hasLoan
        val require3 = detail.hasOngoingTrx

        setViewRequireLabel1(require1)
        setViewRequireLabel2(require2)
        setViewRequireLabel3(require3)

        bindingChild?.btnOke?.setOnClickListener { dismiss() }

        if (require1) arrayReasonTracking.add(CloseAccountTracker.REASON_1)
        if (require2) arrayReasonTracking.add(CloseAccountTracker.REASON_2)
        if (require3) arrayReasonTracking.add(CloseAccountTracker.REASON_3)
        val reasonTracking = arrayReasonTracking.joinToString()
        closeAccountTracker.trackShowBottomSheet(reasonTracking)
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

    override fun dismiss() {
        closeAccountTracker.trackDismissBottomSheet()
        super.dismiss()
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
