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
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.constants.MpTopadsConst.AUTO_BID_CONST
import com.tokopedia.topads.constants.MpTopadsConst.CONST_2
import com.tokopedia.topads.constants.MpTopadsConst.IDR_CONST
import com.tokopedia.topads.constants.MpTopadsConst.PRODUCT_ID_PARAM
import com.tokopedia.topads.constants.MpTopadsConst.TRUE
import com.tokopedia.topads.create.R
import com.tokopedia.topads.common.R as topadscommonR
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
                    putString(PRODUCT_ID_PARAM, productId)
                }
            }
        }

        private const val IMPRESSION_VALUE = "impression"
        private const val CLICK_VALUE = "click"
        private const val CONVERSION_VALUE = "conversion"
        private const val SEARCH_DELAY_TIME = 500L
        private const val REQUEST_CODE = 99
        private const val successImageUrl = "https://images.tokopedia.net/img/android/topads/createads_success/mp_group_creation_success_dialog.png"
    }

    private var binding: MpAdGroupFragmentBinding? = null

    @JvmField @Inject
    var userSession: UserSessionInterface? = null

    private var productId = ""

    private val adGroupAdapter: AdGroupListAdapter by lazy {
        AdGroupListAdapter(getAdGroupTypeFactory())
    }
    private var linearLayoutManager: LinearLayoutManager? = null

    private var endlessScrollListener: EndlessRecyclerViewScrollListener? = null

    @JvmField @Inject
    var viewModelFactory: ViewModelFactory? = null

    private val adGroupViewModel: MpAdsGroupsViewModel? by lazy {
        if (viewModelFactory == null) {
            null
        } else {
            ViewModelProvider(this, viewModelFactory!!).get(MpAdsGroupsViewModel::class.java)
        }
    }

    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getString(PRODUCT_ID_PARAM).orEmpty()
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
        adGroupViewModel?.loadFirstPage()
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
                adGroupViewModel?.moveTopAdsGroup(productId)
                MpTracker.clickAdGroupSimpanCta()
            }
        }
    }

    private fun createEndlessRecyclerViewListener() {
        endlessScrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                adGroupViewModel?.loadMorePages()
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
        adGroupViewModel?.mainListLiveData?.observe(viewLifecycleOwner, ::submitListToAdapter)
        adGroupViewModel?.hasNextLiveData?.observe(viewLifecycleOwner, ::onMoreGroupsLoaded)
        adGroupViewModel?.topadsCreditLiveData?.observe(viewLifecycleOwner, ::onTopadsCreditCheck)
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

    private fun onTopadsCreditCheck(result: Result<Pair<FinalAdResponse, Deposit>>) {
        when (result) {
            is Success -> {
                val depositAmount = result.data.second.topadsDashboardDeposits.data.amount
                if (depositAmount > 0) {
                    val groupId = result.data.first.topadsManageGroupAds.groupResponse.data.id
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
            it.show(
                fragmentManager = childFragmentManager,
                filter = getFilerData(),
                callback = this,
                buttonApplyFilterDetailText = context?.getString(R.string.ad_group_filter_bottomsheet_cta),
                enableResetButton = false
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

    private fun isFilterOptionSelected(filterParam: String) = adGroupViewModel?.sortParam == filterParam

    override fun onApplyButtonClicked(optionList: List<Option>?) {
        optionList?.let { it1 ->
            adGroupViewModel?.sortParam = ""
            it1.forEach {
                if (it.inputState == TRUE) {
                    adGroupViewModel?.sortParam = it.value
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
        adGroupViewModel?.loadFirstPage()
    }

    override fun onAdStatClicked(bottomSheet: BottomSheetUnify) {
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onAdGroupClicked(index: Int, active: Boolean) {
        if (active) {
            binding?.adGroupCta?.isEnabled = true
            adGroupViewModel?.chooseAdGroup(index)
            MpTracker.clickExistingAdGroup()
        } else {
            binding?.adGroupCta?.isEnabled = false
            binding?.adGroupCta?.isLoading = false
            adGroupViewModel?.unChooseAdGroup(index)
        }
    }

    override fun getScreenName() = ""

    // Error Logic

    override fun onErrorActionClicked() {
        resetAdGroupList()
    }

    // Reload Logic
    override fun onReload() {
        adGroupViewModel?.loadMorePages()
    }

    // Search Logic
    private fun loadPageWithKeyword(query: String?) {
        adGroupViewModel?.searchKeyword = query.orEmpty()
        mHandler.removeCallbacksAndMessages(null)
        mHandler.postDelayed({
            adGroupViewModel?.loadFirstPage()
        }, SEARCH_DELAY_TIME)
    }

    private fun openSuccessDialog(groupId: String) {
        val dialog = DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ILLUSTRATION)
        dialog.setImageUrl(successImageUrl)
        dialog.setDescription(getString(topadscommonR.string.topads_common_create_group_success_dailog_desc))
        dialog.setTitle(getString(topadscommonR.string.topads_common_product_successfully_advertised))
        dialog.setPrimaryCTAText(getString(topadscommonR.string.topads_common_manage_ads_group))
        dialog.setSecondaryCTAText(getString(topadscommonR.string.topads_common_stay_here))
        dialog.setPrimaryCTAClickListener {
            MpTracker.clickAdGroupCreatedManageCta()
            val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS).apply {
                putExtra(TopAdsDashboardConstant.TAB_POSITION, CONST_2)
                putExtra(TopAdsDashboardConstant.GROUPID, groupId)
                putExtra(TopAdsDashboardConstant.GROUP_STRATEGY, AUTO_BID_CONST)
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
            adGroupViewModel?.unChooseAdGroup(adGroupViewModel?.getSelectedAdGroupPosition().orZero())
            binding?.adGroupCta?.isEnabled = false
        }
        dialog.show()
    }

    private fun openInsufficientCreditsDialog(dataAmount: Int) {
        val dialog = DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setDescription(getString(topadscommonR.string.topads_common_insufficient_credits_error_desc).replace(IDR_CONST, "Rp$dataAmount"))
        dialog.setTitle(getString(topadscommonR.string.topads_common_ads_created_successfully_but_cant_appear_yet))
        dialog.setPrimaryCTAText(getString(topadscommonR.string.topads_common_add_credit))
        dialog.setSecondaryCTAText(getString(topadscommonR.string.topads_common_later_keyword))
        dialog.setPrimaryCTAClickListener {
            MpTracker.clickAddCreditCta()
            val intent = Intent(activity, TopAdsAddCreditActivity::class.java)
            intent.putExtra(TopAdsAddCreditActivity.SHOW_FULL_SCREEN_BOTTOM_SHEET, true)
            startActivityForResult(intent, REQUEST_CODE)
        }
        dialog.setSecondaryCTAClickListener {
            MpTracker.clickAddCreditLaterStayCta()
            requireActivity().finish()
        }
        dialog.setOnDismissListener {
            binding?.adGroupCta?.isLoading = false
            adGroupViewModel?.unChooseAdGroup(adGroupViewModel?.getSelectedAdGroupPosition().orZero())
            binding?.adGroupCta?.isEnabled = false
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
