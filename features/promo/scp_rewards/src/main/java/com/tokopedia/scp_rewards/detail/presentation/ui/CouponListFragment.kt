package com.tokopedia.scp_rewards.detail.presentation.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.scp_rewards.R
import com.tokopedia.scp_rewards.databinding.FragmentCouponListBinding
import com.tokopedia.scp_rewards.detail.di.MedalDetailComponent
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.CouponListViewModel
import com.tokopedia.scp_rewards_common.utils.launchLink
import com.tokopedia.scp_rewards_widgets.constants.CouponStatus
import com.tokopedia.scp_rewards_widgets.coupon_list.CouponListViewTypeFactory
import com.tokopedia.scp_rewards_widgets.model.CouponListActiveEmptyModel
import com.tokopedia.scp_rewards_widgets.model.CouponListHistoryEmptyModel
import com.tokopedia.scp_rewards_widgets.model.CouponListHistoryErrorModel
import com.tokopedia.scp_rewards_widgets.model.FilterModel
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject
import com.tokopedia.scp_rewards_widgets.R as scp_rewards_widgetsR

class CouponListFragment : BaseDaggerFragment() {

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(MedalDetailComponent::class.java).inject(this)
    }

    @Inject
    @JvmField
    var viewModelFactory: ViewModelFactory? = null

    private val couponListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory!!)[CouponListViewModel::class.java]
    }

    private var _binding: FragmentCouponListBinding? = null
    private val binding get() = _binding!!

    private var couponListCallBack: OnCouponListCallBack? = null

    private val listAdapter by lazy {
        BaseAdapter(
            CouponListViewTypeFactory({ data, position ->
                if (data.cta?.isAutoApply == true) {
                    couponListViewModel.applyCoupon(
                        benefitModel = data,
                        shopId = null,
                        couponCode = data.cta?.couponCode.orEmpty(),
                        position = position
                    )
                } else {
                    context?.launchLink(data.appLink, data.url)
                }
            }, { data ->
                if (couponListViewModel.couponPageStatus != CouponStatus.EXPIRED) {
                    context?.launchLink(data.appLink, data.url)
                }
            })
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractData()
        observeViewModel()
        inflateUi()
    }

    private fun observeViewModel() {
        observeCouponAutoApply()
        observeCouponList()
    }

    private fun observeCouponList() {
        couponListViewModel.couponListLiveData.observe(viewLifecycleOwner) {
            it?.let { safeState ->
                when (safeState) {
                    is CouponListViewModel.CouponState.Error -> {
                        listAdapter.setVisitables(
                            listOf(
                                CouponListHistoryErrorModel(
                                    context?.let { context ->
                                        ContextCompat.getDrawable(
                                            context,
                                            scp_rewards_widgetsR.drawable.ic_coupon_error
                                        )
                                    }
                                )
                            )
                        )
                    }
                    CouponListViewModel.CouponState.Loading -> {}
                    is CouponListViewModel.CouponState.Success -> {
                        couponListCallBack?.onReceiveCoupons(
                            couponListViewModel.couponPageStatus.orEmpty(),
                            couponListViewModel.totalItemsCount
                        )
                        listAdapter.setVisitables(safeState.list)
                        handleCouponState()
                    }
                    is CouponListViewModel.CouponState.ActiveTabEmpty -> {
                        couponListCallBack?.onReceiveCoupons(
                            couponListViewModel.couponPageStatus.orEmpty(),
                            0
                        )
                        listAdapter.setVisitables(
                            listOf(
                                CouponListActiveEmptyModel(
                                    getString(R.string.coupon_list_active_empty_title),
                                    getString(R.string.coupon_list_active_empty_subtitle),
                                    context?.let { context ->
                                        ContextCompat.getDrawable(
                                            context,
                                            scp_rewards_widgetsR.drawable.ic_bonus_active_empty
                                        )
                                    }
                                )
                            )
                        )
                    }
                    is CouponListViewModel.CouponState.HistoryTabEmpty -> {
                        listAdapter.setVisitables(
                            listOf(
                                CouponListHistoryEmptyModel(
                                    getString(R.string.coupon_list_history_empty_title),
                                    getString(R.string.coupon_list_history_empty_subtitle)
                                ) { couponListCallBack?.goToTab(CouponStatus.ACTIVE) }
                            )
                        )
                    }
                }
            }
        }
    }

    private fun handleCouponState() {
        when (couponListViewModel.couponPageStatus) {
            CouponStatus.INACTIVE -> {
                binding.lockedWarning.show()
                binding.filterList.hide()
            }

            CouponStatus.ACTIVE -> {
                binding.lockedWarning.hide()
                binding.filterList.show()
            }

            else -> {
                binding.filterList.hide()
                binding.lockedWarning.hide()
            }
        }
    }

    private fun observeCouponAutoApply() {
        couponListViewModel.autoApplyCoupon.observe(viewLifecycleOwner) {
            it?.let { safeResult ->
                when (safeResult) {
                    is CouponListViewModel.AutoApplyState.Error -> {
                        showHideLoading(safeResult.position, safeResult.benefit, false)
                        showToastAndNavigateToLink(
                            safeResult.throwable.localizedMessage,
                            safeResult.benefit.cta?.appLink,
                            safeResult.benefit.cta?.url
                        )
                    }

                    is CouponListViewModel.AutoApplyState.Loading -> {
                        showHideLoading(safeResult.position, safeResult.benefit, true)
                    }

                    is CouponListViewModel.AutoApplyState.SuccessCouponApplied -> {
                        showHideLoading(safeResult.position, safeResult.benefit, false)
                        showToastAndNavigateToLink(
                            safeResult.data?.couponAutoApply?.infoMessage?.title,
                            safeResult.benefit.cta?.appLink,
                            safeResult.benefit.cta?.url
                        )
                    }

                    is CouponListViewModel.AutoApplyState.SuccessCouponFailed -> {
                        showHideLoading(safeResult.position, safeResult.benefit, false)
                        showToastAndNavigateToLink(
                            safeResult.data?.couponAutoApply?.infoMessage?.title,
                            safeResult.benefit.cta?.appLink,
                            safeResult.benefit.cta?.url
                        )
                    }
                }
            }
        }
    }

    private fun showHideLoading(position: Int, benefit: MedalBenefitModel, toShow: Boolean) {
        listAdapter.setElement(position, benefit.apply { isLoading = toShow })
    }

    private fun showToastAndNavigateToLink(message: String?, appLink: String?, url: String?) {
        Toaster.apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                toasterCustomBottomHeight = getNavigationBarHeight()
            }
        }
            .build(binding.root, message.orEmpty())
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    context?.launchLink(appLink, url)
                }
            }).show()
    }

    private fun getNavigationBarHeight(): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val imm =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
        val resources = context?.resources
        val resourceId: Int =
            resources?.getIdentifier("navigation_bar_height", "dimen", "android").toZeroIfNull()
        return if (resourceId > 0) {
            resources?.getDimensionPixelSize(resourceId) ?: 0
        } else {
            0
        }
    }

    @SuppressLint("DeprecatedMethod")
    private fun extractData() {
        val list = arguments?.getParcelableArrayList<MedalBenefitModel?>(COUPON_LIST)
        val filterList = arguments?.getParcelableArrayList<FilterModel?>(FILTER_LIST)
        couponListViewModel.setFilters(filterList)
        couponListViewModel.setPageStatus(arguments?.getString(PAGE_STATUS))
        if (list != null) {
            couponListViewModel.setCouponsList(list)
        } else {
            couponListViewModel.getCouponList(
                medaliSlug = arguments?.getString(MEDALI_SLUG).orEmpty(),
                sourceName = ""
            )
        }
    }

    private fun inflateUi() {
        binding.rvCoupons.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }
        initialiseFilters()
    }

    private fun initialiseFilters() {
        couponListViewModel.filtersList?.let { filters ->
            val list = filters.map { filter ->
                SortFilterItem(
                    title = filter.text.orEmpty(),
                    iconUrl = filter.iconImageURL.orEmpty(),
                    listener = {
                        couponListViewModel.getFilteredData(filter)
                    }
                ).apply {
                    type = if (filter.isSelected) {
                        ChipsUnify.TYPE_SELECTED
                    } else {
                        ChipsUnify.TYPE_NORMAL
                    }
                }
            }
            binding.filterList.parentListener = {
                binding.filterList.resetAllFilters()
                couponListViewModel.getFilteredData(FilterModel())
            }
            binding.filterList.addItem(list as ArrayList<SortFilterItem>)
            // Explicitly hiding textview so that only close icon is visible
            binding.filterList.textView?.hide()
            context?.let {
                binding.filterList.filterIcon?.background =
                    getIconUnifyDrawable(it, IconUnify.CLOSE)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val COUPON_LIST = "couponlist"
        private const val FILTER_LIST = "filterlist"
        private const val PAGE_STATUS = "pageStatus"
        private const val MEDALI_SLUG = "medaliSlug"
        fun newInstance(
            pageStatus: String,
            medaliSlug: String,
            list: List<MedalBenefitModel>? = null,
            filters: List<FilterModel>? = null,
            onCouponListCallBack: OnCouponListCallBack
        ) = CouponListFragment().apply {
            arguments = bundleOf(
                PAGE_STATUS to pageStatus,
                MEDALI_SLUG to medaliSlug,
                COUPON_LIST to list,
                FILTER_LIST to filters
            )

            this.couponListCallBack = onCouponListCallBack
        }
    }

    interface OnCouponListCallBack {
        fun onReceiveCoupons(couponStatus: String, count: Int)
        fun goToTab(couponStatus: String)
    }
}
