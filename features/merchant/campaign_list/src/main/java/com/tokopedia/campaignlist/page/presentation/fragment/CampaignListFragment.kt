package com.tokopedia.campaignlist.page.presentation.fragment

import android.os.Bundle
import android.text.TextWatcher
import android.view.KeyEvent.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaignlist.R
import com.tokopedia.campaignlist.common.analytics.CampaignListTracker
import com.tokopedia.campaignlist.common.data.model.response.ShopData
import com.tokopedia.campaignlist.common.di.DaggerCampaignListComponent
import com.tokopedia.campaignlist.common.usecase.GetCampaignListUseCase
import com.tokopedia.campaignlist.common.util.onTextChanged
import com.tokopedia.campaignlist.databinding.FragmentCampaignListBinding
import com.tokopedia.campaignlist.page.presentation.adapter.ActiveCampaignListAdapter
import com.tokopedia.campaignlist.page.presentation.bottomsheet.CampaignStatusBottomSheet
import com.tokopedia.campaignlist.page.presentation.bottomsheet.CampaignTypeBottomSheet
import com.tokopedia.campaignlist.page.presentation.model.ActiveCampaign
import com.tokopedia.campaignlist.page.presentation.model.CampaignStatusSelection
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.campaignlist.page.presentation.viewholder.ActiveCampaignViewHolder
import com.tokopedia.campaignlist.page.presentation.viewmodel.CampaignListViewModel
import com.tokopedia.campaignlist.page.presentation.viewmodel.CampaignListViewModel.Companion.NPL_ICON_URL
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.net.UnknownHostException
import javax.inject.Inject

class CampaignListFragment : BaseDaggerFragment(),
        CampaignStatusBottomSheet.OnApplyButtonClickListener,
        CampaignTypeBottomSheet.OnApplyButtonClickListener,
        ActiveCampaignViewHolder.OnShareButtonClickListener, ShareBottomsheetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var tracker: CampaignListTracker

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(CampaignListViewModel::class.java)
    }

    private var campaignTypeBottomSheet: CampaignTypeBottomSheet? = null
    private var campaignStatusBottomSheet: CampaignStatusBottomSheet? = null
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var adapter: ActiveCampaignListAdapter? = null
    private var binding: FragmentCampaignListBinding? = null
    private var campaignTypeFilter: SortFilterItem? = null
    private var campaignStatusFilter: SortFilterItem? = null

    companion object {
        @JvmStatic
        fun createInstance() = CampaignListFragment()
        private const val SHARE = "Share"
        private const val EMPTY_SEARCH_KEYWORD = ""
    }

    override fun getScreenName(): String {
        return getString(R.string.active_campaign_list)
    }

    override fun initInjector() {
        DaggerCampaignListComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val viewBinding = FragmentCampaignListBinding.inflate(inflater, container, false)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        observeLiveData()
        viewModel.getSellerMetaData()
        viewModel.getCampaignList()
    }

    override fun onShareButtonClicked(activeCampaign: ActiveCampaign) {
        viewModel.setSelectedActiveCampaign(activeCampaign)
        try { viewModel.getSellerBanner(activeCampaign.campaignId.toInt()) }
        catch (e: Exception) {
            // TODO log invalid campaign id
        }
        tracker.sendShareButtonClickEvent(
                activeCampaign.campaignType,
                activeCampaign.campaignId,
                userSession.shopId,
                userSession.userId,
        )
    }

    override fun onApplyCampaignTypeFilter(selectedCampaignType: CampaignTypeSelection) {
        campaignTypeFilter?.title = selectedCampaignType.campaignTypeName
        var campaignTypeId = 0
        try {
            campaignTypeId = selectedCampaignType.campaignTypeId.toInt()
        } catch (e: java.lang.Exception) {
            // TODO handle exception
        }
        viewModel.setCampaignTypeId(campaignTypeId)
        viewModel.getCampaignList(campaignTypeId = campaignTypeId)
        tracker.sendSelectCampaignTypeFilterClickEvent(selectedCampaignType.campaignTypeName, userSession.shopId)
    }

    override fun onApplyCampaignStatusFilter(selectedCampaignStatus: CampaignStatusSelection) {
        campaignStatusFilter?.title = selectedCampaignStatus.statusText
        viewModel.setCampaignStatusId(listOf(selectedCampaignStatus.statusId))
        viewModel.getCampaignList(statusId = listOf(selectedCampaignStatus.statusId))
        tracker.sendSelectCampaignStatusClickEvent(selectedCampaignStatus.statusId, userSession.shopId)
    }

    override fun onNoCampaignStatusSelected() {
        viewModel.getCampaignList(
            EMPTY_SEARCH_KEYWORD,
            viewModel.getCampaignTypeId(),
            GetCampaignListUseCase.NPL_LIST_TYPE,
            GetCampaignListUseCase.statusId
        )
    }

    private fun setupView(binding: FragmentCampaignListBinding?) {
        setupSearchBar(binding)
        setupActiveCampaignListView(binding)
    }

    private fun setupSearchBar(binding: FragmentCampaignListBinding?) {
        binding?.sbuCampaignList?.clearListener = { getCampaignListWithCurrentlySelectedFilter() }
        binding?.sbuCampaignList?.searchBarTextField?.onTextChanged { searchKeyword ->
            if (searchKeyword.isEmpty()) {
                getCampaignListWithCurrentlySelectedFilter()
            }
        }
        binding?.sbuCampaignList?.searchBarTextField?.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == IME_ACTION_SEARCH || event.keyCode == KEYCODE_ENTER) {
                val query = textView.text.toString()
                viewModel.setCampaignName(query)
                viewModel.getCampaignList(campaignName = query)
                return@setOnEditorActionListener true
            } else return@setOnEditorActionListener false
        }
    }

    private fun setupCampaignListFilter(binding: FragmentCampaignListBinding?) {
        binding?.sfCampaignList?.apply {
            // setup campaign status filter
            val campaignStatusFilterTitle = getString(R.string.campaign_list_label_status)
            campaignStatusFilter = SortFilterItem(campaignStatusFilterTitle)
            campaignStatusFilter?.listener = {
                campaignStatusFilter?.type = if (campaignStatusFilter?.type == ChipsUnify.TYPE_NORMAL) {
                    ChipsUnify.TYPE_SELECTED
                } else {
                    ChipsUnify.TYPE_NORMAL
                }
                campaignStatusBottomSheet?.show(childFragmentManager)
                tracker.sendOpenCampaignStatusFilterClickEvent(userSession.shopId)
            }

            // setup campaign type filter
            val campaignTypeFilterTitle = viewModel.getSelectedCampaignTypeSelection()?.campaignTypeName ?: ""
            campaignTypeFilter = SortFilterItem(campaignTypeFilterTitle)
            campaignTypeFilter?.type = ChipsUnify.TYPE_SELECTED
            campaignTypeFilter?.listener = {
                campaignStatusFilter?.type = if (campaignStatusFilter?.type == ChipsUnify.TYPE_NORMAL) {
                    ChipsUnify.TYPE_SELECTED
                } else {
                    ChipsUnify.TYPE_NORMAL
                }
                campaignTypeBottomSheet?.show(childFragmentManager)
            }
            val sortFilterItemList = ArrayList<SortFilterItem>()
            campaignStatusFilter?.run { sortFilterItemList.add(this) }
            campaignTypeFilter?.run { sortFilterItemList.add(this) }
            addItem(sortFilterItemList)
            campaignStatusFilter?.refChipUnify?.setChevronClickListener {
                campaignStatusFilter?.listener?.invoke()
            }
            campaignTypeFilter?.refChipUnify?.setChevronClickListener {
                campaignTypeFilter?.listener?.invoke()
            }
        }
    }

    private fun setupActiveCampaignListView(binding: FragmentCampaignListBinding?) {
        adapter = ActiveCampaignListAdapter(this)
        adapter?.setCampaignImpression { campaign ->
            tracker.sendCampaignImpressionEvent(campaign.campaignId, userSession.shopId)
        }
        binding?.rvCampaignList?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupCampaignTypeBottomSheet(campaignTypeSelections: List<CampaignTypeSelection>) {
        campaignTypeBottomSheet = CampaignTypeBottomSheet.createInstance(campaignTypeSelections, this)
    }

    private fun setupCampaignStatusBottomSheet(campaignStatusSelections: List<CampaignStatusSelection>) {
        campaignStatusBottomSheet = CampaignStatusBottomSheet.createInstance(campaignStatusSelections, this)
    }

    private fun setupUniversalShareBottomSheet(shopData: ShopData) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@CampaignListFragment)
            setMetaData(
                    tnTitle = viewModel.getShareBottomSheetTitle(shopData.name),
                    tnImage = NPL_ICON_URL
            )
            setUtmCampaignData(
                    pageName = this@CampaignListFragment.getString(R.string.active_campaign_list),
                    userId = userSession.userId,
                    pageId = userSession.shopId,
                    feature = SHARE
            )
        }
    }

    private fun observeLiveData() {
        viewModel.getCampaignListResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val activeCampaignList = viewModel.mapCampaignListDataToActiveCampaignList(result.data.getCampaignListV2.campaignList)
                    adapter?.setActiveCampaignList(activeCampaignList)
                }
                is Fail -> {
                    if (result.throwable is UnknownHostException) {
                        displayGetCampaignListError(R.string.no_internet_error_message)
                        return@observe
                    }
                    displayGetCampaignListError(R.string.campaign_error_fetch_campaign_list)
                }
            }
        })

        viewModel.getMerchantBannerResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    if (UniversalShareBottomSheet.isCustomSharingEnabled(context)) {
                        // show share bottom sheet
                        val merchantBannerData = result.data.getMerchantCampaignBannerGeneratorData
                        viewModel.setMerchantBannerData(merchantBannerData)
                        val shopData = merchantBannerData.shopData
                        setupUniversalShareBottomSheet(shopData)
                        universalShareBottomSheet?.show(childFragmentManager, this)
                        val selectedActiveCampaign = viewModel.getSelectedActiveCampaign()
                        selectedActiveCampaign?.run {
                            tracker.sendShareBottomSheetDisplayedEvent(
                                    viewModel.getCampaignTypeId(),
                                    this.campaignId,
                                    userSession.userId,
                                    userSession.shopId
                            )
                        }
                    }
                }
                is Fail -> {
                    displayGetDataError()
                }
            }
        })

        viewModel.getSellerMetaDataResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    // setup campaign type bottom sheet
                    val campaignTypeData = result.data.getSellerCampaignSellerAppMeta.campaignTypeData
                    val campaignTypeSelections = viewModel.mapCampaignTypeDataToCampaignTypeSelections(campaignTypeData)
                    setupCampaignTypeBottomSheet(campaignTypeSelections)
                    // setup campaign status bottom sheet
                    val campaignStatus = result.data.getSellerCampaignSellerAppMeta.campaignStatus
                    val campaignStatusSelections = viewModel.mapCampaignStatusToCampaignStatusSelections(campaignStatus)
                    setupCampaignStatusBottomSheet(campaignStatusSelections)
                    // set default selection for campaign type
                    viewModel.setDefaultCampaignTypeSelection(campaignTypeSelections)
                    setupCampaignListFilter(binding)
                }
                is Fail -> {
                    displayGetDataError()
                }
            }
        })
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        val merchantBannerData = viewModel.getMerchantBannerData()
        merchantBannerData?.run {
            val shopData = merchantBannerData.shopData
            val campaignData = merchantBannerData.campaign
            val campaignStatusId = viewModel.getSelectedActiveCampaign()?.campaignStatus
            val linkerShareData = viewModel.generateLinkerShareData(shopData, campaignData, shareModel, campaignStatusId)
            LinkerManager.getInstance().executeShareRequest(
                    LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult?) {
                            val shareWording = viewModel.getShareDescriptionWording(shopData, campaignData, linkerShareData?.shareUri, campaignStatusId)
                            SharingUtil.executeShareIntent(shareModel, linkerShareData, activity, view, shareWording)
                            universalShareBottomSheet?.dismiss()
                        }

                        override fun onError(linkerError: LinkerError?) {}
                    })
            )
        }
        val selectedActiveCampaign = viewModel.getSelectedActiveCampaign()
        selectedActiveCampaign?.run {
            tracker.sendSelectShareChannelClickEvent(
                    shareModel.channel.orEmpty(),
                    viewModel.getCampaignTypeId(),
                    this.campaignId,
                    userSession.userId,
                    userSession.shopId
            )

        }
    }

    override fun onCloseOptionClicked() {
        val selectedActiveCampaign = viewModel.getSelectedActiveCampaign()
        selectedActiveCampaign?.run {
            tracker.sendShareBottomSheetDismissClickEvent(
                    viewModel.getCampaignTypeId(),
                    this.campaignId,
                    userSession.userId,
                    userSession.shopId
            )
        }
    }

    private fun displayGetCampaignListError(@StringRes stringResourceId: Int) {
        Toaster.build(
                view = binding?.root ?: return,
                text = getString(stringResourceId),
                actionText = getString(R.string.retry),
                duration = Toaster.LENGTH_LONG,
                type = Toaster.TYPE_ERROR,
                clickListener = {
                    //Retry to get campaign list data with currently selected filter and search bar query
                    viewModel.getCampaignList(
                            campaignName = viewModel.getCampaignName(),
                            campaignTypeId = viewModel.getCampaignTypeId(),
                            statusId = viewModel.getCampaignStatusId()
                    )
                }
        ).show()
    }

    private fun displayGetDataError() {
        Toaster.build(
                view = binding?.root ?: return,
                text = getString(R.string.campaign_error_fetch_metadata),
                actionText = getString(R.string.ok),
                duration = Toaster.LENGTH_LONG,
                type = Toaster.TYPE_ERROR
        ).show()
    }

    private fun getCampaignListWithCurrentlySelectedFilter() {
        viewModel.getCampaignList(
            EMPTY_SEARCH_KEYWORD,
            viewModel.getCampaignTypeId(),
            GetCampaignListUseCase.NPL_LIST_TYPE,
            viewModel.getCampaignStatusId()
        )
    }
}