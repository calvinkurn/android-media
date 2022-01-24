package com.tokopedia.campaignlist.page.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.campaignlist.R
import com.tokopedia.campaignlist.databinding.BottomsheetCampaignTypeLayoutBinding
import com.tokopedia.campaignlist.page.presentation.adapter.CampaignTypeListAdapter
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.unifycomponents.BottomSheetUnify

class CampaignTypeBottomSheet : BottomSheetUnify() {

    interface OnApplyButtonClickListener {
        fun onApplyCampaignTypeFilter(selectedCampaignType: CampaignTypeSelection)
    }

    companion object {

        private const val CAMPAIGN_TYPE_SELECTIONS = "CAMPAIGN_TYPE_SELECTIONS"

        @JvmStatic
        fun createInstance(campaignTypeSelections: List<CampaignTypeSelection>,
                           clickListener: OnApplyButtonClickListener): CampaignTypeBottomSheet {
            return CampaignTypeBottomSheet().apply {
                this.clickListener = clickListener
                arguments = Bundle().apply {
                    putParcelableArrayList(CAMPAIGN_TYPE_SELECTIONS, ArrayList(campaignTypeSelections))
                }
            }
        }
    }

    private var adapter: CampaignTypeListAdapter? = CampaignTypeListAdapter()

    private var binding: BottomsheetCampaignTypeLayoutBinding? = null

    private var clickListener: OnApplyButtonClickListener? = null

    private val campaignTypeSelections: ArrayList<CampaignTypeSelection> by lazy {
        arguments?.getParcelableArrayList(CAMPAIGN_TYPE_SELECTIONS) ?: arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetCampaignTypeLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setTitle(getString(R.string.campaign_type))
        setChild(viewBinding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        adapter?.setCampaignTypeSelections(campaignTypeSelections)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetCampaignTypeLayoutBinding?) {
        binding?.rvCampaignTypeList?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding?.tpgApplyButton?.setOnClickListener {
            val selectedCampaignType = adapter?.getSelectedCampaignType() ?: CampaignTypeSelection()
            clickListener?.onApplyCampaignTypeFilter(selectedCampaignType)
            dismiss()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }
}