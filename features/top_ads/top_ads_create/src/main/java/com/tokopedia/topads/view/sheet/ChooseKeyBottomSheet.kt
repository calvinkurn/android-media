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
import kotlinx.android.synthetic.main.topads_choose_key_bs.*


class ChooseKeyBottomSheet : BottomSheetUnify() {
    var onSelect: ((type: String) -> Unit)? = null
    var selected: Int = 0


    private var contentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.topads_choose_key_bs, null)
        setChild(contentView)
        showCloseIcon = true
        setTitle(getString(R.string.topads_common_keyword_edit_info_sheet_sub_title))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        keySpecific?.setOnCheckedChangeListener(null)
        keyBroad?.setOnCheckedChangeListener(null)
        desc_1?.text = MethodChecker.fromHtml(getString(R.string.topads_common_choose_type_bs_desc1))
        desc_2?.text = MethodChecker.fromHtml(getString(R.string.topads_common_choose_type_bs_desc2))
        goToStaticSheet?.text = MethodChecker.fromHtml(getString(R.string.topads_common_choose_type_bs_extra))
        if (selected == BROAD_POSITIVE)
            keyBroad?.isChecked = true
        else
            keySpecific?.isChecked = true
        handleClick()
    }

    private fun handleClick() {
        desc_1?.setOnClickListener {
            keyBroad?.isChecked = true
        }

        desc_2?.setOnClickListener {
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
    fun show(
            fragmentManager: FragmentManager,
            currentSelected: Int) {
        selected = currentSelected
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "ACTION_FILTER_BOTTOM_SHEET_TAG"
        fun newInstance(): ChooseKeyBottomSheet = ChooseKeyBottomSheet()
    }

}