package com.tokopedia.mvc.presentation.list.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.utils.extension.showToaster
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.campaign.utils.extension.slideDown
import com.tokopedia.campaign.utils.extension.slideUp
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.attachOnScrollListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
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
import com.tokopedia.mvc.presentation.bottomsheet.FilterVoucherBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.FilterVoucherStatusBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.MoreMenuVoucherBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.OtherPeriodBottomSheet
import com.tokopedia.mvc.presentation.detail.VoucherDetailActivity
import com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet.EduCenterBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet.EduCenterClickListener
import com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet.model.EduCenterMenuModel
import com.tokopedia.mvc.presentation.list.dialog.StopVoucherConfirmationDialog
import com.tokopedia.mvc.presentation.list.adapter.VoucherAdapterListener
import com.tokopedia.mvc.presentation.list.adapter.VouchersAdapter
import com.tokopedia.mvc.presentation.list.constant.PageState
import com.tokopedia.mvc.presentation.list.dialog.CallTokopediaCareDialog
import com.tokopedia.mvc.presentation.list.helper.MvcListPageStateHelper
import com.tokopedia.mvc.presentation.list.model.DeleteVoucherUiEffect
import com.tokopedia.mvc.presentation.list.model.FilterModel
import com.tokopedia.mvc.presentation.list.viewmodel.MvcListViewModel
import com.tokopedia.mvc.presentation.product.add.AddProductActivity
import com.tokopedia.mvc.presentation.summary.SummaryActivity
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.ClipboardHandler
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MvcListFragment : BaseDaggerFragment(), HasPaginatedList by HasPaginatedListImpl(),
    VoucherAdapterListener, FilterVoucherStatusBottomSheet.FilterVoucherStatusBottomSheetListener,
    FilterVoucherBottomSheet.FilterVoucherBottomSheetListener,
    OtherPeriodBottomSheet.OtherPeriodBottomSheetListener,
    EduCenterClickListener {

    companion object {
        private const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"
        private const val TOKOPEDIA_CARE_PATH = "help"
    }

    private val filterList = ArrayList<SortFilterItem>()
    private val filterItem by lazy { SortFilterItem(getString(R.string.smvc_bottomsheet_filter_voucher_all)) }
    private var binding by autoClearedNullable<SmvcFragmentMvcListBinding>()

    private var eduCenterBottomSheet: EduCenterBottomSheet? = null
    private var stopVoucherDialog: StopVoucherConfirmationDialog? = null

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
        setEduCenterBottomSheet()
        setupStopConfirmationDialog()
        setupObservables()
        setupObserveDeleteUiEffect()
    }

    override fun onVoucherListMoreMenuClicked(voucher: Voucher) {
        MoreMenuVoucherBottomSheet().show(childFragmentManager, "")
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
        MoreMenuVoucherBottomSheet().show(childFragmentManager, "")
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

    private fun setupObserveDeleteUiEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.deleteUIEffect.collect {
                when (it) {
                    is DeleteVoucherUiEffect.SuccessDeletedVoucher-> {
                        stopVoucherDialog?.let {dialog ->
                            dialog.setLoadingProses(false)
                            dialog.setDismissDialog()
                        }
                        val successMessage = getStringSuccessStopVoucher(it.voucherStatus, it.name)
                        showSuccessToaster(successMessage)
                        loadInitialDataList()
                    }

                    is DeleteVoucherUiEffect.ShowToasterErrorDelete-> {
                        stopVoucherDialog?.let {dialog ->
                            dialog.setLoadingProses(false)
                            dialog.setDismissDialog()
                        }
                        val errorMessage = getStringFailedStopVoucher(it.voucherStatus, it.name)
                        view?.showToasterError(errorMessage, getString(R.string.smvc_ok))
                    }

                    is DeleteVoucherUiEffect.OnProgressToDeletedVoucherList-> {
                        stopVoucherDialog?.setLoadingProses(true)
                        stopVoucherDialog?.setCancelable(false)
                    }
                }
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
                eduCenterBottomSheet?.show(childFragmentManager)
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
            }, onLoadNextPageFinished = {
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
        //For sample only. Will redirect to add product page.
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

    private fun setEduCenterBottomSheet() {
        eduCenterBottomSheet = EduCenterBottomSheet.createInstance().apply {
            initRecyclerView(this@MvcListFragment.context?:return, this@MvcListFragment)
        }
    }

    private fun setupStopConfirmationDialog(){
        stopVoucherDialog = StopVoucherConfirmationDialog(context?:return)
    }

    override fun onMenuClicked(menu: EduCenterMenuModel) {
        routeToUrl(menu.urlRoute.toString())
    }

    private fun deleteVoucher(voucher: Voucher) {
        if (voucher.isVps) {
            showCallTokopediaCareDialog(voucher.status)
        } else {
            showConfirmationStopVoucherDialog(voucher)
        }
    }

    private fun showCallTokopediaCareDialog(voucherStatus: VoucherStatus) {
        context?.let {
            val title = getTitleTokopediaCareDialog(voucherStatus)
            val desc = getDescTokopediaCareDialog(voucherStatus)
            CallTokopediaCareDialog(it).apply {
                setTitle(title)
                setDescription(desc)
                setOnPositiveConfirmed {
                    goToTokopediaCare()
                }
                show(getString(R.string.smvc_call_tokopedia_care), getString(R.string.smvc_back))
            }
        }
    }

    private fun getTitleTokopediaCareDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_cannot_deleted_call_tokopedia_care_title_dialog)
        } else {
            getString(R.string.smvc_cannot_canceled_call_tokopedia_care_title_dialog)
        }
    }

    private fun getDescTokopediaCareDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_cannot_deleted_call_tokopedia_care_desc_dialog)
        } else {
            getString(R.string.smvc_cannot_canceled_call_tokopedia_care_desc_dialog)
        }
    }


    private fun showConfirmationStopVoucherDialog(voucher: Voucher) {
        val voucherStatus = voucher.status
        stopVoucherDialog?.let {dialog ->
            with(dialog) {
                setOnPositiveConfirmed {
                    viewModel.stopVoucher(voucher)
                }
                show(
                    getTitleStopVoucherDialog(voucherStatus),
                    getStringDescStopVoucherDialog(voucherStatus),
                    getStringPositiveCtaStopVoucherDialog(voucherStatus)
                )
            }
        }
    }

    private fun getTitleStopVoucherDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_delete_voucher_confirmation_title_of_dialog)
        } else {
            getString(R.string.smvc_canceled_voucher_confirmation_title_of_dialog)
        }
    }

    private fun getStringDescStopVoucherDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_delete_voucher_confirmation_body_dialog)
        } else {
            getString(R.string.smvc_canceled_voucher_confirmation_body_dialog, voucherStatus.name)
        }
    }

    private fun getStringPositiveCtaStopVoucherDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_yes_deleted_voucher)
        } else {
            getString(R.string.smvc_yes_canceled_voucher)
        }
    }

    private fun getStringSuccessStopVoucher(voucherStatus: VoucherStatus, voucherName : String): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_success_to_deleted_voucher, voucherName)
        } else {
            getString(R.string.smvc_success_to_cancel_voucher, voucherName)
        }
    }

    private fun getStringFailedStopVoucher(voucherStatus: VoucherStatus, voucherName : String): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_failed_to_deleted_voucher, voucherName)
        } else {
            getString(R.string.smvc_failed_to_cancel_voucher, voucherName)
        }
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            activity, String.format(
                TOKOPEDIA_CARE_STRING_FORMAT, ApplinkConst.WEBVIEW,
                TokopediaUrl.getInstance().MOBILEWEB.plus(TOKOPEDIA_CARE_PATH)
            )
        )
    }

    private fun showSuccessToaster(message : String){
        view?.let { view ->
            Toaster.build(
                view,
                message,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.smvc_ok)
            ) { }.show()
        }
    }

    private fun redirectToQuotaVoucherPage() {
        //TODO: create redirection here
    }
}
