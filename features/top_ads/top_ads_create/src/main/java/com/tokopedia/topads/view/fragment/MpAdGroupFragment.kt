package com.tokopedia.topads.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.TopadsManageGroupAdsResponse
import com.tokopedia.topads.create.R
import com.tokopedia.topads.create.databinding.MpAdGroupFragmentBinding
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.trackers.MpTracker
import com.tokopedia.topads.view.activity.RoutingCallback
import com.tokopedia.topads.view.adapter.adgrouplist.AdGroupListAdapter
import com.tokopedia.topads.view.adapter.adgrouplist.model.ErrorUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.typefactory.AdGroupTypeFactory
import com.tokopedia.topads.view.adapter.adgrouplist.typefactory.AdGroupTypeFactoryImpl
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.AdGroupViewHolder
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.CreateAdGroupViewHolder
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.ErrorViewHolder
import com.tokopedia.topads.view.adapter.adgrouplist.viewholder.ReloadInfiniteViewHolder
import com.tokopedia.topads.view.model.MpAdsGroupsViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class MpAdGroupFragment :
    BaseDaggerFragment(),
    AdGroupViewHolder.AdGroupListener,
    FilterGeneralDetailBottomSheet.Callback,
    ErrorViewHolder.ErrorListener,
    ReloadInfiniteViewHolder.ReloadInfiniteScrollListener,
    CreateAdGroupViewHolder.CreateAdsCallback {

    companion object {
        fun newInstance(productId: String = ""): MpAdGroupFragment {
            return MpAdGroupFragment().apply {
                arguments = Bundle().apply {
                    putString(PRODUCT_ID_KEY, productId)
                }
            }
        }

        private const val IMPRESSION_VALUE = "impression"
        private const val CLICK_VALUE = "click"
        private const val CONVERSION_VALUE = "conversion"
        private const val PRODUCT_ID_KEY = "product_id"
        private const val SEARCH_DELAY_TIME = 500L
        private const val successImageUrl = "https://images.tokopedia.net/img/android/topads/createads_success/mp_group_creation_success_dialog.png"
    }

    private var binding: MpAdGroupFragmentBinding? = null
    var userSession: UserSessionInterface? = null
        @Inject set

    private var productId = ""

    private val adGroupAdapter: AdGroupListAdapter by lazy {
        AdGroupListAdapter(getAdGroupTypeFactory())
    }
    private var linearLayoutManager: LinearLayoutManager? = null

    private var endlessScrollListener: EndlessRecyclerViewScrollListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val adGroupViewModel: MpAdsGroupsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MpAdsGroupsViewModel::class.java)
    }

    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getString(PRODUCT_ID_KEY).orEmpty()
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MpAdGroupFragmentBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        setupRecyclerView()
        setupCta()
        attachFilterClickListener()
        setupAdSearchBar()
        observeViewModel()
        adGroupViewModel.loadFirstPage()
    }

    private fun setupHeader() {
        binding?.adGroupHeader?.apply {
            setNavigationOnClickListener {
                MpTracker.clickCloseAdGroupModal()
                activity?.onBackPressed()
            }
        }
    }

    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context)
        createEndlessRecyclerViewListener()
        binding?.adGroupRv?.apply {
            layoutManager = linearLayoutManager
            adapter = adGroupAdapter
            addRecyclerViewScrollListeners()
        }
    }

    private fun setupCta() {
        binding?.adGroupCta?.apply {
            isEnabled = false
            setOnClickListener {
                isLoading = true
                adGroupViewModel.moveTopAdsGroup(productId)
                MpTracker.clickAdGroupSimpanCta()
            }
        }
    }

    private fun createEndlessRecyclerViewListener() {
        endlessScrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                adGroupViewModel.loadMorePages()
            }
        }
    }

    private fun setupAdSearchBar() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        binding?.adGroupSearch?.searchBarTextField?.setOnFocusChangeListener { _, focus ->
            if (focus) {
                MpTracker.clickAdSearchBar()
            }
            if (inputMethodManager.isAcceptingText) {
                MpTracker.clickSearchEnterKeyboard()
            }
        }
        binding?.adGroupSearch?.searchBarTextField?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                loadPageWithKeyword(query?.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun observeViewModel() {
        adGroupViewModel.mainListLiveData.observe(viewLifecycleOwner, ::submitListToAdapter)
        adGroupViewModel.hasNextLiveData.observe(viewLifecycleOwner, ::onMoreGroupsLoaded)
        adGroupViewModel.topadsCreditLiveData.observe(viewLifecycleOwner, ::onTopadsCreditCheck)
    }

    private fun attachFilterClickListener() {
        binding?.adGroupFilterBtn?.setOnClickListener {
            MpTracker.clickAdGroupSortCta()
            showFilterBottomSheet()
        }
    }

    private fun getAdGroupTypeFactory(): AdGroupTypeFactory = AdGroupTypeFactoryImpl(
        this,
        this,
        this,
        this
    )

    @Suppress("UNCHECKED_CAST")
    private fun submitListToAdapter(list: List<Visitable<*>>) {
        adGroupAdapter.submitList(list as List<Visitable<Any>>)
        if (list.filterIsInstance(ErrorUiModel::class.java).isNotEmpty()) {
            hideCTA()
        } else {
            showCTA()
        }
    }

    private fun onMoreGroupsLoaded(hasNext: Boolean) {
        if (!hasNext) {
            removeRecyclerViewScrollListeners()
            return
        }
        endlessScrollListener?.updateStateAfterGetData()
        endlessScrollListener?.setHasNextPage(hasNext)
    }

    private fun onTopadsCreditCheck(result: Result<Pair<TopadsManageGroupAdsResponse, Deposit>>) {
        when (result) {
            is Success -> {
                val depositAmount = result.data.second.topadsDashboardDeposits.data.amount
                if (depositAmount > 0) {
                    val groupId = result.data.first.topAdsManageGroupAds.groupResponse.data.id
                    openSuccessDialog(groupId.toString())
                } else {
                    openInsufficientCreditsDialog(depositAmount)
                }
            }
            is Fail -> {}
        }
    }

    private fun removeRecyclerViewScrollListeners() {
        endlessScrollListener?.let {
            binding?.adGroupRv?.removeOnScrollListener(it)
        }
    }

    private fun addRecyclerViewScrollListeners() {
        endlessScrollListener?.let {
            binding?.adGroupRv?.addOnScrollListener(it)
        }
    }

    private fun hideCTA() {
        binding?.adGroupCta?.visibility = View.GONE
    }

    private fun showCTA() {
        binding?.adGroupCta?.visibility = View.VISIBLE
    }

    // Filter Logic
    private fun showFilterBottomSheet() {
        FilterGeneralDetailBottomSheet().also {
            it.enableResetButton = false
            it.show(
                fragmentManager = childFragmentManager,
                filter = getFilerData(),
                callback = this,
                buttonApplyFilterDetailText = context?.getString(R.string.ad_group_filter_bottomsheet_cta)
            )
        }
    }

    private fun getFilerData() = Filter(
        title = context?.getString(R.string.ad_group_filter_bottomsheet_title).orEmpty(),
        options = getFilterOptions()
    )

    private fun getFilterOptions(): List<Option> {
        val impressionText = context?.getString(R.string.ad_group_impression).orEmpty()
        val clickText = context?.getString(R.string.ad_group_click).orEmpty()
        val conversionText = context?.getString(R.string.ad_group_conversion).orEmpty()
        return listOf(
            Option(
                name = impressionText,
                key = impressionText,
                value = IMPRESSION_VALUE,
                inputType = Option.INPUT_TYPE_RADIO,
                inputState = "${isFilterOptionSelected(IMPRESSION_VALUE)}"
            ),
            Option(
                name = clickText,
                key = clickText,
                value = CLICK_VALUE,
                inputType = Option.INPUT_TYPE_RADIO,
                inputState = "${isFilterOptionSelected(CLICK_VALUE)}"
            ),
            Option(
                name = conversionText,
                key = conversionText,
                value = CONVERSION_VALUE,
                inputType = Option.INPUT_TYPE_RADIO,
                inputState = "${isFilterOptionSelected(CONVERSION_VALUE)}"
            )
        )
    }

    private fun isFilterOptionSelected(filterParam: String) = adGroupViewModel.sortParam == filterParam

    override fun onApplyButtonClicked(optionList: List<Option>?) {
        optionList?.let { it1 ->
            adGroupViewModel.sortParam = ""
            it1.forEach {
                if (it.inputState == "true") {
                    adGroupViewModel.sortParam = it.value
                }
            }
            resetAdGroupList()
        }
    }

    override fun onOptionClick(option: Option, isChecked: Boolean, position: Int) {
        if (isChecked) {
            MpTracker.apply {
                when (option.value) {
                    IMPRESSION_VALUE -> clickAdGroupSortByImpression()
                    CLICK_VALUE -> clickAdGroupSortByClick()
                    CONVERSION_VALUE -> clickAdGroupSortByOrder()
                }
            }
        }
    }
    // Filter Logic End

    // Call this method to reset the ad group list
    private fun resetAdGroupList() {
        endlessScrollListener?.resetState()
        adGroupViewModel.loadFirstPage()
    }

    override fun onAdStatClicked(bottomSheet: BottomSheetUnify) {
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onAdGroupClicked(index: Int, active: Boolean) {
        if (active) {
            binding?.adGroupCta?.isEnabled = true
            adGroupViewModel.chooseAdGroup(index)
            MpTracker.clickExistingAdGroup()
        } else {
            binding?.adGroupCta?.isEnabled = false
            binding?.adGroupCta?.isLoading = false
            adGroupViewModel.unChooseAdGroup(index)
        }
    }

    override fun getScreenName() = ""

    // Error Logic

    override fun onErrorActionClicked() {
        resetAdGroupList()
    }

    // Reload Logic
    override fun onReload() {
        adGroupViewModel.loadMorePages()
    }

    // Search Logic
    private fun loadPageWithKeyword(query: String?) {
        adGroupViewModel.searchKeyword = query.orEmpty()
        mHandler.removeCallbacksAndMessages(null)
        mHandler.postDelayed({
            adGroupViewModel.loadFirstPage()
        }, SEARCH_DELAY_TIME)
    }

    private fun openSuccessDialog(groupId: String) {
        val dialog = DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ILLUSTRATION)
        dialog.setImageUrl(successImageUrl)
        dialog.setDescription(getString(R.string.success_dailog_description))
        dialog.setTitle(getString(R.string.product_successfully_advertised))
        dialog.setPrimaryCTAText(getString(R.string.manage_ads_group))
        dialog.setSecondaryCTAText(getString(R.string.stay_here))
        dialog.setPrimaryCTAClickListener {
            MpTracker.clickAdGroupCreatedManageCta()
            val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS).apply {
                putExtra(TopAdsDashboardConstant.TAB_POSITION, 2)
                putExtra(TopAdsDashboardConstant.GROUPID, groupId)
                putExtra(TopAdsDashboardConstant.GROUP_STRATEGY, "auto_bid")
            }
            startActivity(intent)
            dialog.dismiss()
            requireActivity().finish()
        }
        dialog.setSecondaryCTAClickListener {
            MpTracker.clickAdGroupCreatedStayHereCta()
            requireActivity().finish()
        }
        dialog.setOnDismissListener {
            binding?.adGroupCta?.isLoading = false
            adGroupViewModel.unChooseAdGroup(adGroupViewModel.getSelectedAdGroupPosition())
        }
        dialog.show()
    }

    private fun openInsufficientCreditsDialog(dataAmount: Int) {
        val dialog = DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setDescription(getString(R.string.success_group_creation_insufficient_credits_text).replace("Rpx.xxx", "Rp$dataAmount"))
        dialog.setTitle(getString(R.string.ads_created_successfully_but_cant_appear_yet))
        dialog.setPrimaryCTAText(getString(R.string.add_credit))
        dialog.setSecondaryCTAText(getString(R.string.later))
        dialog.setPrimaryCTAClickListener {
            MpTracker.clickAddCreditCta()
            val intent = Intent(activity, TopAdsAddCreditActivity::class.java)
            intent.putExtra(TopAdsAddCreditActivity.SHOW_FULL_SCREEN_BOTTOM_SHEET, true)
            startActivityForResult(intent, 99)
        }
        dialog.setSecondaryCTAClickListener {
            MpTracker.clickAddCreditLaterStayCta()
            requireActivity().finish()
        }
        dialog.setOnDismissListener {
            binding?.adGroupCta?.isLoading = false
            adGroupViewModel.unChooseAdGroup(adGroupViewModel.getSelectedAdGroupPosition())
        }
        dialog.show()
    }

    override fun onCreateAdsClicked() {
        MpTracker.clickAdCreateAdGroup()
        activity?.let {
            if (it is RoutingCallback) {
                it.addCreateAds()
            }
        }
    }
}
