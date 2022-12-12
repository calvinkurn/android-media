package com.tokopedia.mvc.presentation.list.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.utils.extension.showToaster
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.campaign.utils.extension.slideDown
import com.tokopedia.campaign.utils.extension.slideUp
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.attachOnScrollListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.R
import com.tokopedia.mvc.common.util.PaginationConstant.INITIAL_PAGE
import com.tokopedia.mvc.common.util.PaginationConstant.PAGE_SIZE
import com.tokopedia.mvc.databinding.SmvcFragmentMvcListBinding
import com.tokopedia.mvc.databinding.SmvcFragmentMvcListFooterBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.presentation.bottomsheet.EduCenterBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.FilterVoucherBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.FilterVoucherStatusBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.OtherPeriodBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.displayvoucher.DisplayVoucherBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.editperiod.VoucherEditPeriodBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.moremenu.MoreMenuBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.voucherperiod.DateStartEndData
import com.tokopedia.mvc.presentation.bottomsheet.voucherperiod.VoucherPeriodBottomSheet
import com.tokopedia.mvc.presentation.detail.VoucherDetailActivity
import com.tokopedia.mvc.presentation.list.adapter.VoucherAdapterListener
import com.tokopedia.mvc.presentation.list.adapter.VouchersAdapter
import com.tokopedia.mvc.presentation.list.constant.PageState
import com.tokopedia.mvc.presentation.list.helper.MvcListPageStateHelper
import com.tokopedia.mvc.presentation.list.model.FilterModel
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel
import com.tokopedia.mvc.presentation.list.viewmodel.MvcListViewModel
import com.tokopedia.mvc.presentation.product.add.AddProductActivity
import com.tokopedia.mvc.util.SharingUtil
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.ClipboardHandler
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class MvcListFragment :
    BaseDaggerFragment(),
    HasPaginatedList by HasPaginatedListImpl(),
    VoucherAdapterListener,
    FilterVoucherStatusBottomSheet.FilterVoucherStatusBottomSheetListener,
    FilterVoucherBottomSheet.FilterVoucherBottomSheetListener,
    OtherPeriodBottomSheet.OtherPeriodBottomSheetListener {

    private val filterList = ArrayList<SortFilterItem>()
    private val filterItem by lazy { SortFilterItem(getString(R.string.smvc_bottomsheet_filter_voucher_all)) }
    private var binding by autoClearedNullable<SmvcFragmentMvcListBinding>()
    private var moreMenuBottomSheet: MoreMenuBottomSheet? = null
    private var voucherEditPeriodBottomSheet: VoucherEditPeriodBottomSheet? = null
    private var displayVoucherBottomSheet: DisplayVoucherBottomSheet? = null
    private var voucherPeriodBottomSheet: VoucherPeriodBottomSheet? = null

    @Inject
    lateinit var viewModel: MvcListViewModel

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentMvcListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        binding?.setupView()
        setupObservables()
    }

    override fun onVoucherListMoreMenuClicked(voucher: Voucher) {
        showMoreMenuBottomSheet(voucher)
    }

    private fun showMoreMenuBottomSheet(voucher: Voucher) {
        activity?.let {
            moreMenuBottomSheet =
                MoreMenuBottomSheet.newInstance(
                    it,
                    voucher
                )
            moreMenuBottomSheet?.setOnMenuClickListener { menu ->
                onClickListenerForMoreMenu(menu, voucher)
            }
            moreMenuBottomSheet?.show(childFragmentManager, "")
        }
    }

    private fun onClickListenerForMoreMenu(menuUiModel: MoreMenuUiModel, voucher: Voucher) {
        moreMenuBottomSheet?.dismiss()
        when (menuUiModel) {
            is MoreMenuUiModel.Coupon -> {
                // TODO change this
                showDisplayVoucherBottomSheet(voucher)
            }
            is MoreMenuUiModel.EditPeriod -> {
                showEditPeriodBottomSheet(voucher)
            }
            is MoreMenuUiModel.Edit -> {
                // TODO change this , using for testing
                showVoucherPeriodBottomSheet()
            }
            is MoreMenuUiModel.Clipboard -> {
            }
            is MoreMenuUiModel.Broadcast -> {
                SharingUtil.shareToBroadCastChat(requireContext(), voucher.id)
            }
            is MoreMenuUiModel.Download -> {
            }
            is MoreMenuUiModel.Clear -> {
            }
            is MoreMenuUiModel.Share -> {
            }
            is MoreMenuUiModel.Stop -> {
            }
            is MoreMenuUiModel.Copy -> {
            }
            is MoreMenuUiModel.TermsAndConditions -> {
            }
            else -> {
            }
        }
    }

    // DUMMY DATA . USED FOR TESTING > REMOVE THEM
    private fun showVoucherPeriodBottomSheet() {
        val list = mutableListOf<DateStartEndData>(
            DateStartEndData("2022-05-10", "2022-05-20", "00:30", "05:30"),
            DateStartEndData("2022-06-11", "2022-06-21", "01:30", "06:30"),
            DateStartEndData("2022-07-12", "2022-07-22", "02:30", "05:30"),
            DateStartEndData("2022-08-13", "2022-08-23", "03:30", "07:30"),
            DateStartEndData("2022-09-14", "2022-09-24", "04:30", "08:30"),
            DateStartEndData("2022-10-15", "2022-10-25", "05:30", "09:30")
        )
        activity?.let {
            voucherPeriodBottomSheet =
                VoucherPeriodBottomSheet.newInstance(
                    title = context?.resources?.getString(R.string.voucher_bs_period_title_1)
                        .toBlankOrString(),
                    dateList = list
                )
            voucherPeriodBottomSheet?.show(childFragmentManager, "")
        }
    }

    private fun showEditPeriodBottomSheet(voucher: Voucher) {
        activity?.let {
            voucherEditPeriodBottomSheet =
                VoucherEditPeriodBottomSheet.newInstance(
                    voucher,
                    onSuccessUpdateVoucherPeriod,
                    onFailureUpdateVoucherPeriod
                )
            voucherEditPeriodBottomSheet?.show(childFragmentManager, "")
        }
    }

    private var onSuccessUpdateVoucherPeriod: () -> Unit = {
        loadInitialDataList()
        view?.run {
            Toaster.build(
                this,
                context?.getString(R.string.edit_period_success_edit_period).toBlankOrString(),
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                context?.getString(R.string.edit_period_button_text).toBlankOrString()
            )
        }
    }

    private var onFailureUpdateVoucherPeriod: (String) -> Unit = { message ->
        val errorMessage = if (message.isNotBlank()) {
            message
        } else {
            context?.getString(R.string.smvc_general_error).toBlankOrString()
        }
        view?.showToasterError(errorMessage)
    }

    private fun showDisplayVoucherBottomSheet(voucher: Voucher) {
        activity?.let {
            displayVoucherBottomSheet =
                DisplayVoucherBottomSheet.newInstance(
                    voucher
                )
            displayVoucherBottomSheet?.show(childFragmentManager, "")
        }
    }

    override fun onVoucherListCopyCodeClicked(voucher: Voucher) {
        activity?.let { ClipboardHandler().copyToClipboard(it, voucher.code) }
        binding?.footer?.root.showToaster(getString(R.string.smvc_voucherlist_copy_to_clipboard_message))
    }

    override fun onVoucherListMultiPeriodClicked(voucher: Voucher) {
        viewModel.getVoucherListChild(voucher.id)
    }

    override fun onVoucherListClicked(voucher: Voucher) {
        VoucherDetailActivity.start(context ?: return, voucher.id)
    }

    override fun onFilterVoucherStatusChanged(status: List<VoucherStatus>, statusText: String) {
        filterItem.title = statusText
        filterItem.selectedItem = arrayListOf(statusText)
        viewModel.setFilterStatus(status)
        loadInitialDataList()
    }

    override fun onFilterVoucherChanged(filter: FilterModel) {
        viewModel.filter = filter
        loadInitialDataList()
        binding?.sortFilter?.indicatorCounter = viewModel.getFilterCount()
    }

    override fun onOtherPeriodMoreMenuClicked(voucher: Voucher) {
        showMoreMenuBottomSheet(voucher)
    }

    private fun setupObservables() {
        viewModel.voucherList.observe(viewLifecycleOwner) { vouchers ->
            val adapter = binding?.rvVoucher?.adapter as? VouchersAdapter
            adapter?.addDataList(vouchers)
            notifyLoadResult(vouchers.size >= PAGE_SIZE)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding?.loaderPage?.gone()
            view?.showToasterError(it)
        }
        viewModel.voucherQuota.observe(viewLifecycleOwner) {
            binding?.footer?.setupVoucherQuota(it)
        }
        viewModel.voucherChilds.observe(viewLifecycleOwner) {
            val bottomSheet = OtherPeriodBottomSheet.newInstance(it)
            bottomSheet.setListener(this)
            bottomSheet.show(this, it.size)
        }
        viewModel.pageState.observe(viewLifecycleOwner) {
            when (it) {
                PageState.NO_DATA_PAGE -> displayNoData()
                PageState.NO_DATA_SEARCH_PAGE -> displayNoDataSearch()
                else -> displayList()
            }
        }
    }

    private fun SmvcFragmentMvcListBinding.setupView() {
        header.setupHeader()
        searchBar.setupSearchBar()
        rvVoucher.setupRvVoucher()
        rvVoucher.setupListScroll()
        sortFilter.setupFilter()
    }

    private fun SmvcFragmentMvcListFooterBinding.setupVoucherQuota(
        voucherCreationQuota: VoucherCreationQuota
    ) {
        tfQuotaCounter.text = voucherCreationQuota.quotaUsageFormatted
        iconInfo.setOnClickListener {
            redirectToQuotaVoucherPage()
        }
        btnAddCoupon.setOnClickListener {
            if (voucherCreationQuota.quotaErrorMessage.isEmpty()) {
                redirectToCreateVoucherPage()
            } else {
                val actionText = getString(R.string.smvc_voucherlist_toaster_actiontext)
                it.showToasterError(voucherCreationQuota.quotaErrorMessage, actionText)
            }
        }
    }

    private fun HeaderUnify.setupHeader() {
        val colorIcon = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        title = context.getString(R.string.smvc_voucherlist_page_title)
        addRightIcon(com.tokopedia.iconunify.R.drawable.iconunify_menu_kebab_horizontal).apply {
            setColorFilter(colorIcon, PorterDuff.Mode.MULTIPLY)
            setOnClickListener {
                EduCenterBottomSheet().show(childFragmentManager, "")
            }
        }
        setNavigationOnClickListener {
            activity?.finish()
        }
    }

    private fun SearchBarUnify.setupSearchBar() {
        clearListener = {
            viewModel.setFilterKeyword("")
            loadInitialDataList()
        }
        searchBarTextField.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setFilterKeyword(textView.text.toString())
                loadInitialDataList()
                KeyboardHandler.hideSoftKeyboard(activity)
            }
            return@setOnEditorActionListener false
        }
    }

    private fun RecyclerView.setupListScroll() {
        binding?.footer?.root?.let {
            attachOnScrollListener(
                { it.slideDown() },
                { it.slideUp() }
            )
        }
    }

    private fun RecyclerView.setupRvVoucher() {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = VouchersAdapter(this@MvcListFragment)
        val config = HasPaginatedList.Config(
            pageSize = PAGE_SIZE,
            onLoadNextPage = {
                // TODO: Implement loading
            },
            onLoadNextPageFinished = {
                // TODO: Implement loading
            }
        )
        loadInitialDataList()
        attachPaging(this, config, ::getDataList)
    }

    private fun SortFilter.setupFilter() {
        filterList.add(filterItem)
        addItem(filterList)
        parentListener = {
            filterItem.selectedItem = arrayListOf()
            val bottomSheet = FilterVoucherBottomSheet.newInstance(viewModel.filter)
            bottomSheet.setListener(this@MvcListFragment)
            bottomSheet.show(childFragmentManager, "")
        }
        dismissListener = parentListener

        filterItem.listener = {
            val bottomSheet = FilterVoucherStatusBottomSheet()
            bottomSheet.setSelected(filterItem.selectedItem)
            bottomSheet.setListener(this@MvcListFragment)
            bottomSheet.show(childFragmentManager, "")
        }
        filterItem.refChipUnify.setChevronClickListener { filterItem.listener.invoke() }
    }

    private fun loadInitialDataList() {
        val adapter = binding?.rvVoucher?.adapter as? VouchersAdapter
        adapter?.clearDataList()
        binding?.loaderPage?.show()
        viewModel.getVoucherList(INITIAL_PAGE, PAGE_SIZE)
        viewModel.getVoucherQuota()
    }

    private fun getDataList(page: Int, pageSize: Int) {
        viewModel.getVoucherList(page, pageSize)
    }

    private fun displayNoDataSearch() {
        binding?.apply {
            loaderPage.gone()
            rvVoucher.gone()
            errorPageSmall.show()
            errorPageLarge.gone()
        }
    }

    private fun displayNoData() {
        binding?.apply {
            loaderPage.gone()
            rvVoucher.gone()
            errorPageSmall.gone()
            errorPageLarge.show()
            val statusName = MvcListPageStateHelper.getStatusName(context, viewModel.filter)
            errorPageLarge.emptyStateTitleID.text = getString(R.string.smvc_voucherlist_empty_data_title_text, statusName)
            errorPageLarge.setPrimaryCTAClickListener {
                binding?.footer?.btnAddCoupon?.performClick()
            }
        }
    }

    private fun displayList() {
        binding?.apply {
            loaderPage.gone()
            rvVoucher.show()
            errorPageSmall.gone()
            errorPageLarge.gone()
        }
    }

    private fun redirectToCreateVoucherPage() {
        // For sample only. Will redirect to add product page.
        val voucherConfiguration = VoucherConfiguration(
            benefitIdr = 25_000,
            benefitMax = 500_000,
            benefitPercent = 0,
            benefitType = BenefitType.NOMINAL,
            promoType = PromoType.FREE_SHIPPING,
            isVoucherProduct = true,
            minPurchase = 50_000,
            productIds = emptyList(),
            targetBuyer = VoucherTargetBuyer.ALL_BUYER,
            0
        )

        val intent = AddProductActivity.buildCreateModeIntent(
            activity ?: return,
            voucherConfiguration
        )

        startActivityForResult(intent, 100)
    }

    private fun redirectToQuotaVoucherPage() {
        // TODO: create redirection here
    }
}
