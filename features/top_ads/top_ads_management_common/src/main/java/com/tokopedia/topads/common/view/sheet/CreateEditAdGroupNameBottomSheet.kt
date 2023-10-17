package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.databinding.TopadsEditSheetEditAdGroupNameBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
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
        setListeners()
        initView(binding)
    }

    private fun setListeners() {
        binding?.textField?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim() == groupName) {
                    binding?.editAdGroupNameCta?.isEnabled = false
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding?.editAdGroupNameCta?.isEnabled = p0.toString().trim() != groupName && (p0?.length
                    ?: Int.ZERO) < 71
                if ((p0?.length ?: Int.ZERO) > 70) {
                    dialog?.window?.decorView?.let {
                        Toaster.build(
                            it,
                            "Grup iklan maksimal 70 karakter",
                            Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding?.editAdGroupNameCta?.setOnClickListener {
            clickListener.invoke(binding?.textField?.editText?.text.toString())
            dismiss()
        }
    }

    private fun initView(view: TopadsEditSheetEditAdGroupNameBinding?) {
        context?.let {
            binding?.textField?.editText?.setText(groupName)
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
