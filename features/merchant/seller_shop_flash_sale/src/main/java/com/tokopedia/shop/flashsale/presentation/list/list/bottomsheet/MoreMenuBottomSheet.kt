package com.tokopedia.shop.flashsale.presentation.list.list.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetMoreMenuBinding
import com.tokopedia.shop.flashsale.domain.entity.CampaignListMoreMenu
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.isAvailable
import com.tokopedia.shop.flashsale.domain.entity.enums.isOngoing
import com.tokopedia.shop.flashsale.domain.entity.enums.isUpcoming
import com.tokopedia.shop.flashsale.presentation.list.list.adapter.CampaignListMoreMenuAdapter
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
    private val campaignStatus by lazy { arguments?.getParcelable(BUNDLE_KEY_CAMPAIGN_STATUS) as? CampaignStatus  ?: CampaignStatus.CANCELLED }

    private val availableCampaignMoreMenu = listOf(
        CampaignListMoreMenu(R.string.sfs_share, R.drawable.ic_sfs_share) { onShareCampaignMenuSelected() },
        CampaignListMoreMenu(R.string.sfs_edit, R.drawable.ic_sfs_edit) { onEditCampaignMenuSelected() },
        CampaignListMoreMenu(R.string.sfs_cancel, R.drawable.ic_sfs_cancel) { onCancelMenuSelected() },
        CampaignListMoreMenu(R.string.sfs_view_detail, R.drawable.ic_sfs_detail) { onViewCampaignDetailMenuSelected() },
    )

    private val upcomingCampaignMoreMenu = listOf(
        CampaignListMoreMenu(R.string.sfs_share, R.drawable.ic_sfs_share) { onShareCampaignMenuSelected() },
        CampaignListMoreMenu(R.string.sfs_cancel, R.drawable.ic_sfs_cancel) { onCancelMenuSelected() },
        CampaignListMoreMenu(R.string.sfs_view_detail, R.drawable.ic_sfs_detail) { onViewCampaignDetailMenuSelected() },
    )

    private val ongoingCampaignMoreMenu = listOf(
        CampaignListMoreMenu(R.string.sfs_share, R.drawable.ic_sfs_share) { onShareCampaignMenuSelected() },
        CampaignListMoreMenu(R.string.sfs_stop, R.drawable.ic_sfs_cancel) { onStopMenuSelected() },
        CampaignListMoreMenu(R.string.sfs_view_detail, R.drawable.ic_sfs_detail) { onViewCampaignDetailMenuSelected() },
    )

    private var onViewCampaignDetailMenuSelected: () -> Unit = {}
    private var onEditCampaignMenuSelected: () -> Unit = {}
    private var onShareCampaignMenuSelected: () -> Unit = {}
    private var onCancelMenuSelected: () -> Unit = {}
    private var onStopMenuSelected: () -> Unit = {}

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
            dismiss()
        }

        binding?.recyclerView?.run {
            layoutManager = LinearLayoutManager(activity ?: return)
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.submit(getMenus())
    }

    private fun getMenus(): List<CampaignListMoreMenu> {
        return when {
            campaignStatus.isAvailable() -> availableCampaignMoreMenu
            campaignStatus.isUpcoming() -> upcomingCampaignMoreMenu
            campaignStatus.isOngoing() -> ongoingCampaignMoreMenu
            else -> emptyList()
        }
    }

    fun setOnShareCampaignMenuSelected(onShareCampaignMenuSelected: () -> Unit) {
        this.onShareCampaignMenuSelected = onShareCampaignMenuSelected
    }

    fun setOnEditCampaignMenuSelected(onEditCampaignMenuSelected: () -> Unit) {
        this.onEditCampaignMenuSelected = onEditCampaignMenuSelected
    }

    fun setOnCancelCampaignMenuSelected(onCancelMenuSelected: () -> Unit) {
        this.onCancelMenuSelected = onCancelMenuSelected
    }

    fun setOnStopCampaignMenuSelected(onStopMenuSelected: () -> Unit) {
        this.onStopMenuSelected = onStopMenuSelected
    }

    fun setOnViewCampaignMenuSelected(onViewCampaignDetailMenuSelected: () -> Unit) {
        this.onViewCampaignDetailMenuSelected = onViewCampaignDetailMenuSelected
    }
}