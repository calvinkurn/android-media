package com.tokopedia.topads.edit.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.topads.edit.databinding.TopadsEditSheetEditAdGroupSettingModeBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.topads.edit.R as topadseditR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class EditAdGroupSettingModeBottomSheet : BottomSheetUnify() {

    private var binding: TopadsEditSheetEditAdGroupSettingModeBinding? = null
    private var isAutomatic: Boolean = false
    private var isAutomaticChanged: Boolean = false
    private var clickListener: ((bidTypeAutomatic: Boolean) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val viewBinding =
            TopadsEditSheetEditAdGroupSettingModeBinding.inflate(inflater, container, false)
        binding = viewBinding
        isHideable = true
        showCloseIcon = false
        showKnob = true
        setChild(viewBinding.root)
        setTitle(getString(topadseditR.string.top_ads_edit_ad_group_setting_mode_bottom_sheet_title))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        updateState(isAutomatic)

        binding?.cardAutomatic?.setOnClickListener {
            isAutomaticChanged = true
            updateState(true)
        }

        binding?.cardManual?.setOnClickListener {
            isAutomaticChanged = false
            updateState(false)
        }

        binding?.editAdGroupNameCta?.setOnClickListener {
            clickListener?.invoke(isAutomaticChanged)
            dismiss()
        }
    }

    private fun updateState(isAutomatic: Boolean) {
        setCardState(binding?.cardAutomatic, isAutomatic)
        setCardState(binding?.cardManual, !isAutomatic)
        binding?.radiobtnOtomatis?.isChecked = isAutomatic
        binding?.radiobtnManual?.isChecked = !isAutomatic
        binding?.editAdGroupNameCta?.isEnabled = this.isAutomatic != isAutomaticChanged


    }

    private fun setCardState(card: CardUnify2?, isActive: Boolean) {
        val colorRes = if (isActive) unifyprinciplesR.color.Unify_GN50
        else unifyprinciplesR.color.Unify_NN0
        val cardType = if (isActive) CardUnify.TYPE_BORDER_ACTIVE
        else CardUnify.TYPE_CLEAR

        card?.setBackgroundResource(colorRes)
        card?.cardType = cardType
    }


    fun show(
        fragmentManager: FragmentManager,
        clickListener: (bidTypeAutomatic: Boolean) -> Unit
    ) {
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
        this.clickListener = clickListener
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG =
            "EDIT_AD_GROUP_SETTING_MODE_BOTTOM_SHEET_TAG"

        fun newInstance(isBidAutomatic: Boolean): EditAdGroupSettingModeBottomSheet =
            EditAdGroupSettingModeBottomSheet().apply {
                isAutomatic = isBidAutomatic
                isAutomaticChanged = isBidAutomatic
            }
    }
}
