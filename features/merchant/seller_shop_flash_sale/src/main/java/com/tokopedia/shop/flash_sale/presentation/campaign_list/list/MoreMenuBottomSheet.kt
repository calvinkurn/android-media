package com.tokopedia.shop.flash_sale.presentation.campaign_list.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetMoreMenuBinding
import com.tokopedia.shop.flash_sale.common.extension.showToaster
import com.tokopedia.shop.flash_sale.domain.entity.CampaignListMoreMenu
import com.tokopedia.shop.flash_sale.domain.entity.enums.CampaignStatus
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class MoreMenuBottomSheet : BottomSheetUnify() {

    init {
        clearContentPadding = true
    }

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_NAME = "campaign_name"
        private const val BUNDLE_KEY_CAMPAIGN_STATUS = "campaign_status"

        fun newInstance(campaignName: String, campaignStatus: CampaignStatus): MoreMenuBottomSheet {
            val args = Bundle()
            args.putString(BUNDLE_KEY_CAMPAIGN_NAME, campaignName)
            args.putParcelable(BUNDLE_KEY_CAMPAIGN_STATUS, campaignStatus)

            val fragment = MoreMenuBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    private var binding by autoClearedNullable<SsfsBottomsheetMoreMenuBinding>()
    private val campaignName by lazy { arguments?.getString(BUNDLE_KEY_CAMPAIGN_NAME).orEmpty() }
    private val campaignStatus by lazy { arguments?.getParcelable(BUNDLE_KEY_CAMPAIGN_STATUS) as? CampaignStatus }
    private var onMenuClick: (CampaignListMoreMenu) -> Unit = {}

    private val availableCampaignMoreMenu = listOf(
        CampaignListMoreMenu(R.string.sfs_share, R.drawable.ic_sfs_share, {}),
        CampaignListMoreMenu(R.string.sfs_edit, R.drawable.ic_sfs_edit, {}),
        CampaignListMoreMenu(R.string.sfs_cancel, R.drawable.ic_sfs_cancel, {}),
        CampaignListMoreMenu(R.string.sfs_view_detail, R.drawable.ic_sfs_detail, {}),
    )

    private val upcomingCampaignMoreMenu = listOf(
        CampaignListMoreMenu(R.string.sfs_share, R.drawable.ic_sfs_share, {}),
        CampaignListMoreMenu(R.string.sfs_edit, R.drawable.ic_sfs_edit, {}),
        CampaignListMoreMenu(R.string.sfs_cancel, R.drawable.ic_sfs_cancel, {}),
        CampaignListMoreMenu(R.string.sfs_view_detail, R.drawable.ic_sfs_detail, {}),
    )

    private val ongoingCampaignMoreMenu = listOf(
        CampaignListMoreMenu(R.string.sfs_share, R.drawable.ic_sfs_share, {
            binding?.root showToaster getString(R.string.sfs_share)
        }),
        CampaignListMoreMenu(R.string.sfs_edit, R.drawable.ic_sfs_edit, {
            binding?.root showToaster getString(R.string.sfs_edit)
        }),
        CampaignListMoreMenu(R.string.sfs_stop, R.drawable.ic_sfs_cancel, {
            binding?.root showToaster getString(R.string.sfs_cancel)
        }),
        CampaignListMoreMenu(R.string.sfs_view_detail, R.drawable.ic_sfs_detail, {
            binding?.root showToaster getString(R.string.sfs_view_detail)
        }),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }


    private fun setupBottomSheet() {
        binding = SsfsBottomsheetMoreMenuBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(campaignName)
    }

    private fun setupView() {
        val recyclerViewAdapter = CampaignListMoreMenuAdapter()
        recyclerViewAdapter.setOnMenuClick { menu ->
            menu.onClick()
            onMenuClick(menu)
            dismiss()
        }

        binding?.recyclerView?.run {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.submit(getMenus())
    }

    private fun getMenus(): List<CampaignListMoreMenu> {
        return when (campaignStatus) {
            CampaignStatus.DRAFT -> emptyList()
            CampaignStatus.AVAILABLE -> availableCampaignMoreMenu
            CampaignStatus.UPCOMING -> upcomingCampaignMoreMenu
            CampaignStatus.ONGOING -> ongoingCampaignMoreMenu
            CampaignStatus.FINISHED -> emptyList()
            CampaignStatus.CANCELLED -> emptyList()
            null -> emptyList()
        }
    }

    fun setOnItemClickListener(onMenuClick: (CampaignListMoreMenu) -> Unit) {
        this.onMenuClick = onMenuClick
    }
}