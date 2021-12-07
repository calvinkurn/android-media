package com.tokopedia.campaignlist.page.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.campaignlist.R
import com.tokopedia.campaignlist.databinding.BottomsheetCampaignStatusLayoutBinding
import com.tokopedia.campaignlist.page.presentation.adapter.CampaignStatusListAdapter
import com.tokopedia.campaignlist.page.presentation.model.CampaignStatusSelection
import com.tokopedia.unifycomponents.BottomSheetUnify

class CampaignStatusBottomSheet : BottomSheetUnify() {

    interface OnApplyButtonClickListener {
        fun onApplyCampaignStatusFilter(selectedCampaignStatus: CampaignStatusSelection)
        fun onNoCampaignStatusSelected()
    }

    companion object {

        private const val CAMPAIGN_STATUS_SELECTIONS = "CAMPAIGN_STATUS_SELECTIONS"

        @JvmStatic
        fun createInstance(campaignStatusSelections: List<CampaignStatusSelection>,
                           clickListener: OnApplyButtonClickListener): CampaignStatusBottomSheet {
            return CampaignStatusBottomSheet().apply {
                this.clickListener = clickListener
                arguments = Bundle().apply {
                    putParcelableArrayList(CAMPAIGN_STATUS_SELECTIONS, ArrayList(campaignStatusSelections))
                }
            }
        }
    }

    private var adapter: CampaignStatusListAdapter? = CampaignStatusListAdapter()

    private var binding: BottomsheetCampaignStatusLayoutBinding? = null

    private var clickListener: OnApplyButtonClickListener? = null

    private val campaignStatusSelections: ArrayList<CampaignStatusSelection> by lazy {
        arguments?.getParcelableArrayList(CAMPAIGN_STATUS_SELECTIONS) ?: arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetCampaignStatusLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setTitle(getString(R.string.campaign_status))
        setChild(viewBinding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        adapter?.setCampaignStatusSelections(campaignStatusSelections)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetCampaignStatusLayoutBinding?) {
        binding?.rvCampaignStatusList?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding?.tpgApplyButton?.setOnClickListener {
            val campaignStatus = adapter?.getCampaignStatusSelection() ?: return@setOnClickListener
            val selectedCampaigns = campaignStatus.filter { it.isSelected }
            if (selectedCampaigns.isEmpty()) {
                clickListener?.onNoCampaignStatusSelected()
            } else {
                val selectedCampaign = campaignStatus.find { it.isSelected }
                clickListener?.onApplyCampaignStatusFilter(selectedCampaign ?: CampaignStatusSelection())
            }
            dismiss()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }
}