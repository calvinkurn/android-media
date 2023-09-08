package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import androidx.fragment.app.FragmentManager
import com.tokopedia.topads.common.databinding.TopadsEditSheetEditAdGroupNameBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.topads.common.R as topadscommonR

class CreateEditAdGroupNameBottomSheet() : BottomSheetUnify() {

    private var binding: TopadsEditSheetEditAdGroupNameBinding? = null
    private var groupName: String = ""
    private var groupId: String = ""
    lateinit var clickListener: (groupName: String) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val viewBinding = TopadsEditSheetEditAdGroupNameBinding.inflate(inflater, container, false)
        binding = viewBinding
        isHideable = true
        showCloseIcon = true
        setChild(viewBinding.root)
        setTitle(getString(topadscommonR.string.top_ads_create_edit_ad_group_name_bottom_sheet_title))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(binding)
    }

    private fun initView(view: TopadsEditSheetEditAdGroupNameBinding?) {
        context?.let {
            binding?.textField?.editText?.setText(groupName)
            binding?.editAdGroupNameCta?.setOnClickListener {
                clickListener.invoke(binding?.textField?.editText?.text.toString())
                dismiss()
            }
        }
    }

    fun show(
        fragmentManager: FragmentManager,
        clickListener: (groupName: String) -> Unit
    ) {
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
        this.clickListener = clickListener
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "EDIT_AD_GROUP_NAME_BOTTOM_SHEET_TAG"
        fun newInstance(groupName: String, groupId: String): CreateEditAdGroupNameBottomSheet =
            CreateEditAdGroupNameBottomSheet().apply {
                this.groupName = groupName
                this.groupId = groupId
            }
    }
}
