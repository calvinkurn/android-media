package com.tokopedia.topads.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_POSITIVE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_TYPE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.SPECIFIC_TYPE
import com.tokopedia.topads.create.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class ChooseKeyBottomSheet : BottomSheetUnify() {

    private var keySpecific: RadioButtonUnify? = null
    private var keyBroad: RadioButtonUnify? = null
    private var desc1: Typography? = null
    private var desc2: Typography? = null
    private var goToStaticSheet: Typography? = null

    var onSelect: ((type: String) -> Unit)? = null
    private var selected: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView = View.inflate(context, R.layout.topads_choose_key_bs, null)
        setChild(contentView)
        keySpecific = contentView.findViewById(R.id.keySpecific)
        keyBroad = contentView.findViewById(R.id.keyBroad)
        desc1 = contentView.findViewById(R.id.desc_1)
        desc2 = contentView.findViewById(R.id.desc_2)
        goToStaticSheet = contentView.findViewById(R.id.goToStaticSheet)
        showCloseIcon = true
        setTitle(getString(com.tokopedia.topads.common.R.string.topads_common_keyword_edit_info_sheet_sub_title))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        keySpecific?.setOnCheckedChangeListener(null)
        keyBroad?.setOnCheckedChangeListener(null)
        desc1?.text =
            MethodChecker.fromHtml(getString(com.tokopedia.topads.common.R.string.topads_common_choose_type_bs_desc1))
        desc2?.text =
            MethodChecker.fromHtml(getString(com.tokopedia.topads.common.R.string.topads_common_choose_type_bs_desc2))
        goToStaticSheet?.text =
            MethodChecker.fromHtml(getString(com.tokopedia.topads.common.R.string.topads_common_choose_type_bs_extra))
        if (selected == BROAD_POSITIVE)
            keyBroad?.isChecked = true
        else
            keySpecific?.isChecked = true
        handleClick()
    }

    private fun handleClick() {
        desc1?.setOnClickListener {
            keyBroad?.isChecked = true
        }

        desc2?.setOnClickListener {
            keySpecific?.isChecked = true
        }

        keySpecific?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onSelect?.invoke(SPECIFIC_TYPE)
                dismiss()
            }
        }
        keyBroad?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onSelect?.invoke(BROAD_TYPE)
                dismiss()
            }
        }
        goToStaticSheet?.setOnClickListener {
            val sheet = StaticInfoBottomSheet.newInstance()
            sheet.show(childFragmentManager)
        }
    }

    fun show(fragmentManager: FragmentManager, currentSelected: Int) {
        selected = currentSelected
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "ACTION_FILTER_BOTTOM_SHEET_TAG"
        fun newInstance(): ChooseKeyBottomSheet = ChooseKeyBottomSheet()
    }

}