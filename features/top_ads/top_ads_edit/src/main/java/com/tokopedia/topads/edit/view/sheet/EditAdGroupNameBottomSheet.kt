package com.tokopedia.topads.edit.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.topads.edit.databinding.TopadsEditSheetEditAdGroupNameBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class EditAdGroupNameBottomSheet: BottomSheetUnify()  {

    private var binding: TopadsEditSheetEditAdGroupNameBinding? = null
    private var groupName: String = ""
    private var groupId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val viewBinding = TopadsEditSheetEditAdGroupNameBinding.inflate(inflater, container, false)
        binding = viewBinding
        isHideable = true
        showCloseIcon = true
        setChild(viewBinding.root)
        setTitle(getString(com.tokopedia.topads.edit.R.string.top_ads_edit_ad_group_name_bottom_sheet_title))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(binding)
    }

    private fun initView(view: TopadsEditSheetEditAdGroupNameBinding?) {
        context?.let {
            binding?.textField?.editText?.setText(groupName)
        }
    }

    fun show(
        fragmentManager: FragmentManager,
    ) {
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "EDIT_AD_GROUP_NAME_BOTTOM_SHEET_TAG"
        fun newInstance(groupName: String, groupId: String): EditAdGroupNameBottomSheet =
            EditAdGroupNameBottomSheet().apply {
                this.groupName = groupName
                this.groupId = groupId
            }
    }
}