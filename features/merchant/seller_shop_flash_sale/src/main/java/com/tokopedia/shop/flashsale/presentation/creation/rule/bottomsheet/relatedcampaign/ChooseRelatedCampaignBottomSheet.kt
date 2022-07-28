package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomSheetChooseRelatedCampaignBinding
import com.tokopedia.shop.flashsale.common.extension.disable
import com.tokopedia.shop.flashsale.common.extension.enable
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.adapter.ChooseRelatedCampaignAdapter
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.adapter.ChooseRelatedCampaignListener
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.adapter.RelatedCampaignItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ChooseRelatedCampaignBottomSheet : BottomSheetUnify(),
    ChooseRelatedCampaignListener {
    companion object {
        private const val DEFAULT_CAMPAIGN_ID = -1L
        private const val TAG = "RelatedCampaignBottomSheet"
        private const val KEY_RELATED_CAMPAIGNS_SELECTED = "KEY_RELATED_CAMPAIGNS_SELECTED"
        private const val KEY_CAMPAIGN_ID = "KEY_CAMPAIGN_ID"

        @JvmStatic
        fun createInstance(
            campaignId: Long,
            relatedCampaigns: List<RelatedCampaign>,
        ): ChooseRelatedCampaignBottomSheet {
            return ChooseRelatedCampaignBottomSheet().apply {
                arguments = Bundle().apply {
                    val relatedCampaignIds = relatedCampaigns.map {
                        RelatedCampaignItem(it.id, it.name, true)
                    }.toTypedArray()
                    putLong(KEY_CAMPAIGN_ID, campaignId)
                    putParcelableArray(KEY_RELATED_CAMPAIGNS_SELECTED, relatedCampaignIds)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ChooseRelatedCampaignViewModel::class.java) }

    private var binding by autoClearedNullable<SsfsBottomSheetChooseRelatedCampaignBinding>()
    private var listener: ChooseRelatedCampaignBottomSheetListener? = null

    private var adapter: ChooseRelatedCampaignAdapter? = null

    private val selectedRelatedCampaigns: List<RelatedCampaignItem> by lazy {
        (arguments?.getParcelableArray(KEY_RELATED_CAMPAIGNS_SELECTED) as? Array<RelatedCampaignItem>)?.toList()
            ?: emptyList()
    }

    private val campaignId by lazy {
        arguments?.getLong(KEY_CAMPAIGN_ID) ?: DEFAULT_CAMPAIGN_ID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupDependencyInjection() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsBottomSheetChooseRelatedCampaignBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(R.string.choose_related_campaign_title))
        setUpRecyclerView()
        setUpClickListeners()
        viewModel.setCampaignId(campaignId)
        viewModel.setSelectedCampaigns(selectedRelatedCampaigns)
        getInitialRelatedCampaigns()
        observePreviousCampaigns()
        observeAddCampaignButtonActiveState()
    }

    private fun getInitialRelatedCampaigns() {
        showLoading()
        viewModel.getPreviousCampaign()
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding?.rvPreviousCampaign ?: return
        adapter = ChooseRelatedCampaignAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setUpClickListeners() {
        val binding = binding ?: return
        binding.btnAddCampaign.setOnClickListener {
            listener?.onRelatedCampaignsAddButtonClicked(viewModel.getRelatedCampaignsSelected())
            dismiss()
        }
        binding.searchbar.searchBarTextField.afterTextChanged { viewModel.getPreviousCampaign(it) }
        binding.searchbar.clearListener = ::onSearchBarClearClicked
    }

    private fun observePreviousCampaigns() {
        viewModel.relatedCampaignsResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ChooseRelatedCampaignResult.Loading -> renderLoading()
                is ChooseRelatedCampaignResult.SearchEmptyResult -> renderEmptyResult()
                is ChooseRelatedCampaignResult.Fail -> renderFailResult(result)
                is ChooseRelatedCampaignResult.Success -> renderSuccessResult(result)
            }
        }
    }

    private fun renderLoading() {
        showLoading()
        hideEmptyResultMessage()
        showEmptyResult()
    }

    private fun renderEmptyResult() {
        hideLoading()
        showEmptyResultMessage()
    }

    private fun renderSuccessResult(result: ChooseRelatedCampaignResult.Success) {
        hideLoading()
        showCampaignList(result.relatedCampaigns)
    }

    private fun renderFailResult(result: ChooseRelatedCampaignResult.Fail) {
        hideLoading()
        showErrorMessage(result.error)
    }

    private fun showCampaignList(campaigns: List<RelatedCampaignItem>) {
        adapter?.submitList(campaigns)
    }

    private fun showEmptyResult() {
        adapter?.submitList(emptyList())
    }

    private fun observeAddCampaignButtonActiveState() {
        viewModel.isAddRelatedCampaignButtonActive.observe(viewLifecycleOwner) { isActive ->
            if (isActive) enableAddCampaignButton() else disableAddCampaignButton()
        }
    }

    override fun onDestroyView() {
        listener = null
        super.onDestroyView()
    }

    fun setChooseRelatedCampaignListener(listener: ChooseRelatedCampaignBottomSheetListener) {
        this.listener = listener
    }

    override fun onRelatedCampaignClicked(previousCampaign: RelatedCampaignItem) {
        viewModel.onCampaignClicked(previousCampaign)
    }

    private fun showLoading() {
        val binding = binding ?: return
        binding.loader.show()
    }

    private fun hideLoading() {
        val binding = binding ?: return
        binding.loader.hide()
    }

    private fun enableAddCampaignButton() {
        val binding = binding ?: return
        binding.btnAddCampaign.enable()
    }

    private fun disableAddCampaignButton() {
        val binding = binding ?: return
        binding.btnAddCampaign.disable()
    }

    private fun showEmptyResultMessage() {
        val binding = binding ?: return
        binding.tgSearchEmptyResultMessage.visible()
    }

    private fun showErrorMessage(t: Throwable) {
        binding?.btnAddCampaign showError t
    }

    private fun hideEmptyResultMessage() {
        val binding = binding ?: return
        binding.tgSearchEmptyResultMessage.hide()
    }

    private fun onSearchBarClearClicked() {
        viewModel.getPreviousCampaign()
    }
}