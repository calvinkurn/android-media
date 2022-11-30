package com.tokopedia.mvc.presentation.list.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.mvc.presentation.bottomsheet.EduCenterBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.FilterVoucherBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.FilterVoucherStatusBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.MoreMenuVoucherBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.OtherPeriodBottomSheet
import com.tokopedia.mvc.presentation.list.adapter.VoucherAdapterListener
import com.tokopedia.mvc.presentation.list.adapter.VouchersAdapter
import com.tokopedia.mvc.presentation.list.model.FilterModel
import com.tokopedia.mvc.presentation.list.viewmodel.MvcListViewModel
import com.tokopedia.mvc.presentation.product.add.AddProductActivity
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.utils.clipboard.ClipboardHandler
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class MvcListFragment: BaseDaggerFragment(), HasPaginatedList by HasPaginatedListImpl(),
    VoucherAdapterListener, FilterVoucherStatusBottomSheet.FilterVoucherStatusBottomSheetListener,
    FilterVoucherBottomSheet.FilterVoucherBottomSheetListener {

    private val filterList = ArrayList<SortFilterItem>()
    private val filterItem by lazy { SortFilterItem(getString(R.string.smvc_bottomsheet_filter_voucher_all)) }
    private var binding by autoClearedNullable<SmvcFragmentMvcListBinding>()

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
        MoreMenuVoucherBottomSheet().show(childFragmentManager, "")
    }

    override fun onVoucherListCopyCodeClicked(voucher: Voucher) {
        context?.let { ClipboardHandler.copyToClipboard(it, voucher.code) }
        binding?.footer?.root.showToaster(getString(R.string.smvc_voucherlist_copy_to_clipboard_message))
    }

    override fun onVoucherListMultiPeriodClicked(voucher: Voucher) {
        OtherPeriodBottomSheet().show(this, voucher.totalChild)
    }

    override fun onVoucherListClicked(voucher: Voucher) {
        println("card")
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

    private fun setupObservables() {
        viewModel.voucherList.observe(viewLifecycleOwner) { vouchers ->
            val adapter = binding?.rvVoucher?.adapter as? VouchersAdapter
            adapter?.addDataList(vouchers)
            notifyLoadResult(vouchers.size >= PAGE_SIZE)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            view?.showToasterError(it)
        }
        viewModel.voucherQuota.observe(viewLifecycleOwner) {
            binding?.footer?.setupVoucherQuota(it)
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
            val bottomSheet = FilterVoucherBottomSheet(viewModel.filter)
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
        viewModel.getVoucherList(INITIAL_PAGE, PAGE_SIZE)
        viewModel.getVoucherQuota()
    }

    private fun getDataList(page: Int, pageSize: Int) {
        viewModel.getVoucherList(page, pageSize)
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
            productIds = emptyList()
        )

        val intent = AddProductActivity.buildCreateModeIntent(
            activity ?: return,
            voucherConfiguration
        )

        startActivityForResult(intent, 100)
    }

    private fun redirectToQuotaVoucherPage() {
        //TODO: create redirection here
    }
}
