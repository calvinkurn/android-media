package com.tokopedia.vouchercreation.product.voucherlist.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.datepicker.toZeroIfNull
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.base.BaseSimpleListFragment
import com.tokopedia.vouchercreation.common.bottmsheet.StopVoucherDialog
import com.tokopedia.vouchercreation.common.consts.NumberConstant
import com.tokopedia.vouchercreation.common.consts.ShareComponentConstant
import com.tokopedia.vouchercreation.common.consts.VoucherCreationConst
import com.tokopedia.vouchercreation.common.consts.VoucherUrl.NO_VOUCHER_RESULT_URL
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.common.errorhandler.MvcError
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.exception.VoucherCancellationException
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.*
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.download.CouponImageUiModel
import com.tokopedia.vouchercreation.product.download.DownloadCouponImageBottomSheet
import com.tokopedia.vouchercreation.product.share.LinkerDataGenerator
import com.tokopedia.vouchercreation.product.update.period.UpdateCouponPeriodBottomSheet
import com.tokopedia.vouchercreation.product.update.quota.UpdateCouponQuotaBottomSheet
import com.tokopedia.vouchercreation.product.voucherlist.view.adapter.CouponListAdapter
import com.tokopedia.vouchercreation.product.voucherlist.view.bottomsheet.CouponFilterBottomSheet
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.CANCEL_VOUCHER_ERROR
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.LIST_COUPON_PER_PAGE
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.PAGE_MODE_ACTIVE
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.PAGE_MODE_HISTORY
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.STOP_VOUCHER_ERROR
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.TAG_SCALYR_MVC_CANCEL_VOUCHER_ERROR
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.TAG_SCALYR_MVC_STOP_VOUCHER_ERROR
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.TYPE_MESSAGE_KEY
import com.tokopedia.vouchercreation.product.voucherlist.view.viewmodel.CouponListViewModel
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.filter.CouponStatusFilterBotomSheet
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.*
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.presentation.bottomsheet.MoreMenuBottomSheet
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherStatus
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.CancelVoucherDialog
import java.util.*
import javax.inject.Inject

class CouponListFragment: BaseSimpleListFragment<CouponListAdapter, VoucherUiModel>() {

    companion object {
        fun newInstance(
            pageMode: String,
            onCreateCouponMenuSelected: () -> Unit,
            onEditCouponMenuSelected: (Long) -> Unit,
            onDuplicateCouponMenuSelected: (Long) -> Unit,
            onViewCouponDetailMenuSelected: (Long) -> Unit = {}
        ): CouponListFragment {
            val args = Bundle()
            val fragment = CouponListFragment().apply {
                arguments = args
                this.pageMode = pageMode
                this.onCreateCouponMenuSelected = onCreateCouponMenuSelected
                this.onEditCouponMenuSelected = onEditCouponMenuSelected
                this.onDuplicateCouponMenuSelected = onDuplicateCouponMenuSelected
                this.onViewCouponDetailMenuSelected = onViewCouponDetailMenuSelected
            }
            return fragment
        }
    }

    private var pageMode: String = PAGE_MODE_ACTIVE

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var linkerDataGenerator: LinkerDataGenerator

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(CouponListViewModel::class.java)
    }

    private val moreBottomSheet: MoreMenuBottomSheet? by lazy {
        return@lazy MoreMenuBottomSheet.createInstance()
    }

    private val globalError: GlobalError? by lazy { view?.findViewById(R.id.geEmptyData) }
    private val loadingList: View? by lazy { view?.findViewById(R.id.loadingList) }
    private val emptyStateList: View? by lazy { view?.findViewById(R.id.emptyStateList) }

    private val filterStatus by lazy { SortFilterItem("Status Aktif") }
    private val filterType by lazy { SortFilterItem("Gratis Ongkir") }
    private val filterTarget by lazy { SortFilterItem("Publik") }

    private var onCreateCouponMenuSelected : () -> Unit = {}
    private var onEditCouponMenuSelected : (Long) -> Unit = {}
    private var onDuplicateCouponMenuSelected : (Long) -> Unit = {}
    private var onViewCouponDetailMenuSelected : (Long) -> Unit = {}
    private var shareComponentBottomSheet : UniversalShareBottomSheet? = null

    override fun getScreenName(): String = CouponListFragment::class.java.simpleName

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupPageMode()
        return inflater.inflate(R.layout.fragment_mvc_coupon_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()
        setupBackButton(view)
        setupSearchField(view)
        setupFilterChips(view)
        setupObserver()
    }

    override fun createAdapter() = CouponListAdapter(::onCouponOptionClicked, ::onCouponIconCopyClicked)

    override fun getRecyclerView(view: View): RecyclerView = view.findViewById(R.id.rvVoucherList)

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout = view.findViewById(R.id.swipeMvcList)

    override fun getPerPage() = LIST_COUPON_PER_PAGE

    override fun addElementToAdapter(list: List<VoucherUiModel>) {
        adapter?.addData(list)
    }

    override fun loadData(page: Int) {
        globalError?.gone()
        viewModel.getCouponList(page)
    }

    override fun clearAdapterData() {
        adapter?.clearData()
    }

    override fun onShowLoading() {
        if (adapter?.itemCount.isMoreThanZero()) {
            loadingList?.show()
        }
        adapter?.showLoading()
    }

    override fun onHideLoading() {
        loadingList?.gone()
        emptyStateList?.gone()
        adapter?.hideLoading()
    }

    override fun onDataEmpty() {
        if (viewModel.couponSearchKeyword.orEmpty().isEmpty()) {
            showEmptyDataGlobalError()
        } else {
            emptyStateList?.show()
        }
    }

    override fun onGetListError(message: String) {
        showGetListDataGlobalError()
    }

    private fun showGetListDataGlobalError() {
        globalError?.show()
        globalError?.setType(SERVER_ERROR)
        globalError?.setActionClickListener {
            loadInitialData()
        }
    }

    private fun showEmptyDataGlobalError() {
        swipeToRefresh?.isEnabled = false // need to disable, to enable Coba Lagi button listener
        globalError?.apply {
            errorTitle.text = "Nggak ada Kupon aktif"
            errorDescription.text = "Yuk, gunakan Kupon Produk untuk meningkatkan peluang penjualan tokomu."
            errorAction.text = "Buat Kupon Produk"
            setActionClickListener {
                onCreateCouponMenuSelected.invoke()
            }
            post { errorIllustration.loadImage(NO_VOUCHER_RESULT_URL) }
            show()
        }
    }

    /**
     * Initiate page mode, should be called before setupObservers() function to prevent
     * re-invoking data
     */
    private fun setupPageMode() {
        if (pageMode == PAGE_MODE_HISTORY) {
            viewModel.setStatusFilter(VoucherStatus.HISTORY)
            filterStatus.title = getString(R.string.mvc_coupon_status_inactive)
        }
    }

    private fun onFilterSelected(
        selectedType: CouponFilterBottomSheet.FilterType,
        selectedTarget: CouponFilterBottomSheet.FilterTarget
    ) {
        if (viewModel.selectedFilterType.value != selectedType) {
            viewModel.setSelectedFilterType(selectedType)
        }
        if (viewModel.selectedFilterTarget.value != selectedTarget) {
            viewModel.setSelectedFilterTarget(selectedTarget)
        }
    }

    private fun getTypeSelectionText(selectedType: CouponFilterBottomSheet.FilterType): String {
        return when (selectedType) {
            CouponFilterBottomSheet.FilterType.CASHBACK -> {
                getString(R.string.mvc_cashback)
            }
            CouponFilterBottomSheet.FilterType.FREE_SHIPPING -> {
                getString(R.string.mvc_free_shipping)
            }
            CouponFilterBottomSheet.FilterType.NOT_SELECTED -> {
                getString(R.string.mvc_free_shipping)
            }
        }
    }

    private fun getTargetSelectionText(selectedTarget: CouponFilterBottomSheet.FilterTarget): String {
        return when (selectedTarget) {
            CouponFilterBottomSheet.FilterTarget.PUBLIC -> {
                getString(R.string.mvc_public)
            }
            CouponFilterBottomSheet.FilterTarget.PRIVATE -> {
                getString(R.string.mvc_special)
            }
            CouponFilterBottomSheet.FilterTarget.NOT_SELECTED -> {
                getString(R.string.mvc_public)
            }
        }
    }

    private fun onResetFilter(
        selectedType: CouponFilterBottomSheet.FilterType,
        selectedTarget: CouponFilterBottomSheet.FilterTarget
    ) {
        viewModel.setSelectedFilterType(selectedType)
        viewModel.setSelectedFilterTarget(selectedTarget)
        view?.post {
            filterTarget.type = ChipsUnify.TYPE_NORMAL
            filterTarget.refChipUnify.gone()
            filterType.type = ChipsUnify.TYPE_NORMAL
            filterType.refChipUnify.gone()
        }
    }

    private fun onStatusSelected(couponName: String, @VoucherStatus couponStatus: String) {
        viewModel.setStatusFilter(couponStatus)
        filterStatus.refChipUnify.chipText = couponName
        loadInitialData()
    }

    private fun onCouponIconCopyClicked(couponCode: String) {
        context?.let { SharingUtil.copyTextToClipboard(it, couponCode, couponCode) }
    }

    private fun onCouponOptionClicked(coupon: VoucherUiModel) {
        moreBottomSheet?.show(childFragmentManager)
        moreBottomSheet?.setOnItemClickListener(
            couponStatus = coupon.status,
            couponTitle = coupon.name
        ) { menu ->
            moreBottomSheet?.dismiss()
            clickMoreMenuItem(menu, coupon)
        }
    }

    private fun setupBackButton(view: View) {
        val toolbar = view.findViewById<HeaderUnify>(R.id.toolbarMvcList)
        toolbar.headerTitle = getString(R.string.mvc_coupon_list_title)
        toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
    }

    private fun setupSearchField(view: View) {
        val searchField = view.findViewById<SearchBarUnify>(R.id.searchBarMvc)
        searchField.searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setCouponSearchKeyword(searchField.searchBarTextField.text.toString())
                loadInitialData()
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setupFilterChips(view: View) {
        val chip = view.findViewById<SortFilter>(R.id.sf_voucher_list)
        chip.parentListener = {
            val bottomSheet = CouponFilterBottomSheet.newInstance(
                viewModel.selectedFilterType.value ?: CouponFilterBottomSheet.FilterType.NOT_SELECTED,
                viewModel.selectedFilterTarget.value ?: CouponFilterBottomSheet.FilterTarget.NOT_SELECTED,
                ::onFilterSelected,
                ::onResetFilter
            )
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        filterStatus.chevronListener = {
            CouponStatusFilterBotomSheet(::onStatusSelected).show(childFragmentManager, "")
        }

        val sortFilterItems = arrayListOf(filterStatus, filterType, filterTarget)
        chip.addItem(sortFilterItems)
        filterTarget.initRemovableFilterItem {
            viewModel.setSelectedFilterTarget(CouponFilterBottomSheet.FilterTarget.NOT_SELECTED)
        }
        filterType.initRemovableFilterItem {
            viewModel.setSelectedFilterType(CouponFilterBottomSheet.FilterType.NOT_SELECTED)
        }
    }

    private fun setupObserver() {
        observeCouponList()
        observeCancelCoupon()
        observeStopCoupon()
        observeSelectedFilterType()
        observeSelectedFilterTarget()
        observeCouponDetail()
        observeGenerateImage()
    }

    private fun observeCouponList() = viewModel.couponList.observe(viewLifecycleOwner) {
        if (it is Success) {
            renderList(it.data, it.data.size == getPerPage())
        } else if (it is Fail) {
            showGetListError(it.throwable)
        }
    }

    private fun observeCancelCoupon() = viewModel.cancelCoupon.observe(viewLifecycleOwner) { result ->
        when(result) {
            is Success -> {
                loadInitialData()
                showCancellationSuccessToaster(
                    successMessageRes = R.string.mvc_cancel_success,
                    couponId = result.data
                )
            }
            is Fail -> {
                if (result.throwable is VoucherCancellationException) {
                    showCancellationFailToaster(
                        messageRes = R.string.mvc_cancel_fail,
                        couponId = (result.throwable as? VoucherCancellationException)?.voucherId.toZeroIfNull(),
                        status = CancelVoucherUseCase.CancelStatus.STOP
                    )
                }
                // send crash report to firebase crashlytics
                MvcErrorHandler.logToCrashlytics(
                    throwable = result.throwable,
                    message = CANCEL_VOUCHER_ERROR
                )
                // log error type to scalyr
                ServerLogger.log(
                    priority = Priority.P2,
                    tag = TAG_SCALYR_MVC_CANCEL_VOUCHER_ERROR,
                    message = mapOf(TYPE_MESSAGE_KEY to ErrorHandler.getErrorMessage(context, result.throwable))
                )
            }
        }
    }

    private fun observeCouponDetail() {
        viewModel.couponDetail.observe(viewLifecycleOwner, { result ->
            when(result) {
                is Success -> {
                    viewModel.setCoupon(result.data)
                    viewModel.generateImage(result.data)
                }
                is Fail -> {
                    showError(result.throwable)
                }
            }
        })
    }

    private fun observeGenerateImage() {
        viewModel.couponImageWithShop.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    displayShareBottomSheet(
                        viewModel.getCoupon() ?: return@observe,
                        result.data.imageUrl,
                        result.data.shop
                    )
                }
                is Fail -> {
                    showError(result.throwable)
                }
            }
        })
    }

    private fun observeStopCoupon() = viewModel.stopCoupon.observe(viewLifecycleOwner) { result ->
        when(result) {
            is Success -> {
                loadInitialData()
                showCancellationSuccessToaster(
                    successMessageRes = R.string.mvc_stop_success,
                    couponId = result.data
                )
            }
            is Fail -> {
                if (result.throwable is VoucherCancellationException) {
                    showCancellationFailToaster(
                        messageRes = R.string.mvc_stop_fail,
                        couponId = (result.throwable as? VoucherCancellationException)?.voucherId.toZeroIfNull(),
                        status = CancelVoucherUseCase.CancelStatus.DELETE
                    )
                }
                // send crash report to firebase crashlytics
                MvcErrorHandler.logToCrashlytics(
                    throwable = result.throwable,
                    message = STOP_VOUCHER_ERROR
                )
                // log error type to scalyr
                ServerLogger.log(
                    priority = Priority.P2,
                    tag = TAG_SCALYR_MVC_STOP_VOUCHER_ERROR,
                    message = mapOf(TYPE_MESSAGE_KEY to ErrorHandler.getErrorMessage(context, result.throwable))
                )
            }
        }
    }


    private fun observeSelectedFilterTarget() {
        viewModel.selectedFilterTarget.observe(viewLifecycleOwner) {
            if (it != CouponFilterBottomSheet.FilterTarget.NOT_SELECTED) {
                activateFilterTarget()
                view?.post { filterTarget.refChipUnify.chipText = getTargetSelectionText(it) }
            }
            loadInitialData()
        }
    }

    private fun observeSelectedFilterType() {
        viewModel.selectedFilterType.observe(viewLifecycleOwner) {
            if (it != CouponFilterBottomSheet.FilterType.NOT_SELECTED) {
                activateFilterType()
                view?.post { filterType.refChipUnify.chipText = getTypeSelectionText(it) }
            }
            loadInitialData()
        }
    }

    private fun activateFilterTarget() {
        filterTarget.type = ChipsUnify.TYPE_SELECTED
        filterTarget.refChipUnify.show()
        view?.post { filterTarget.refChipUnify.displayRemoveIcon() }
    }

    private fun activateFilterType() {
        filterType.type = ChipsUnify.TYPE_SELECTED
        filterType.refChipUnify.show()
        view?.post { filterType.refChipUnify.displayRemoveIcon() }
    }

    private fun clickMoreMenuItem(menu: MoreMenuUiModel, coupon: VoucherUiModel) {
        when (menu) {
            is EditQuotaCoupon -> editQuotaCoupon(coupon)
            is ViewDetailCoupon -> viewDetailCoupon(coupon)
            is EditCoupon -> editCoupon(coupon)
            is BroadCastChat -> broadCastChat(coupon)
            is ShareCoupon -> shareCoupon(coupon)
            is EditPeriodCoupon -> editPeriodCoupon(coupon)
            is DownloadCoupon -> downloadCoupon(coupon.image, coupon.imageSquare, coupon.imagePortrait)
            is CancelCoupon -> cancelCoupon(coupon)
            is StopCoupon -> stopCoupon(coupon)
            is DuplicateCoupon -> duplicateCoupon(coupon)
            else -> { /* do nothing */ }
        }
    }

    private fun editQuotaCoupon(coupon: VoucherUiModel) {
        showEditQuotaBottomSheet(coupon)
    }

    private fun editCoupon(coupon: VoucherUiModel) {
        onEditCouponMenuSelected(coupon.id.toLong())
    }

    private fun viewDetailCoupon(coupon: VoucherUiModel) {
        onViewCouponDetailMenuSelected(coupon.id.toLong())
    }

    private fun broadCastChat(coupon: VoucherUiModel) {
        context?.apply {
            SharingUtil.shareToBroadCastChat(
                context = this,
                voucherId = coupon.id
            )
        }
    }

    private fun shareCoupon(coupon: VoucherUiModel) {
        if (!isAdded) return
        viewModel.getCouponDetail(coupon.id.toLong())
    }

    private fun editPeriodCoupon(coupon: VoucherUiModel) {
        showUpdateCouponPeriodBottomSheet(coupon)
    }


    private fun downloadCoupon(bannerImageUrl : String, squareImageUrl : String, portraitImageUrl : String) {
        if (!isAdded) return
        val bottomSheet = DownloadCouponImageBottomSheet.newInstance(
            bannerImageUrl,
            squareImageUrl,
            portraitImageUrl,
            userSession.userId
        )
        bottomSheet.setOnDownloadClickListener { couponList -> checkDownloadPermission(couponList) }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }


    private fun cancelCoupon(coupon: VoucherUiModel) {
        CancelVoucherDialog(
            context = context ?: return
        ).setOnPrimaryClickListener {
            viewModel.cancelCoupon(coupon.id, CancelVoucherUseCase.CancelStatus.DELETE)
        }.show(coupon)
    }

    private fun stopCoupon(coupon: VoucherUiModel) {
        StopVoucherDialog(
            context = context ?: return
        ).setOnPrimaryClickListener {
            viewModel.cancelCoupon(coupon.id, CancelVoucherUseCase.CancelStatus.STOP)
        }.show(coupon)
    }

    private fun duplicateCoupon(coupon: VoucherUiModel) {
        onDuplicateCouponMenuSelected(coupon.id.toLong())
    }

    private fun showUpdateCouponPeriodBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        val bottomSheet = UpdateCouponPeriodBottomSheet.newInstance(voucher.id.toLong())
        bottomSheet.setOnSuccessClickListener { onSuccessUpdateVoucherPeriod() }
        bottomSheet.setOnFailClickListener { message ->
            val errorMessage = if (message.isNotBlank()) {
                message
            } else {
                context?.getString(R.string.mvc_general_error).toBlankOrString()
            }
            view?.showErrorToaster(errorMessage)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun onSuccessUpdateVoucherPeriod() {
        loadInitialData()
        view?.run {
            Toaster.build(
                this,
                context?.getString(R.string.mvc_success_update_period).toBlankOrString(),
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                context?.getString(R.string.mvc_oke).toBlankOrString()
            ).show()
        }
    }

    private fun showEditQuotaBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return

        val bottomSheet = UpdateCouponQuotaBottomSheet.newInstance(voucher)
        bottomSheet.setOnUpdateQuotaSuccess {
            loadInitialData()
            view?.run {
                Toaster.build(
                    this,
                    getString(R.string.update_quota_success).toBlankOrString(),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    context?.getString(R.string.mvc_oke).toBlankOrString()
                ).show()
            }
        }
        bottomSheet.setOnUpdateQuotaError { message ->
            val errorMessage = if (message.isNotBlank()) {
                message
            } else {
                getString(R.string.mvc_general_error).toBlankOrString()
            }
            view?.showErrorToaster(errorMessage)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showCancellationSuccessToaster(successMessageRes: Int, couponId: Int) {
        val successMessage = context?.getString(successMessageRes).toBlankOrString()
        val actionText = context?.getString(R.string.mvc_lihat).toBlankOrString()

        view?.run {
            Toaster.build(this,
                successMessage,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                actionText
            ) {
                onViewCouponDetailMenuSelected(couponId.toLong())
            }.show()
        }
    }

    private fun showCancellationFailToaster(messageRes: Int, couponId: Int, @CancelVoucherUseCase.CancelStatus status: String) {
        val errorMessage = context?.getString(messageRes).toBlankOrString()
        val actionText = context?.getString(R.string.mvc_retry).toBlankOrString()

        view?.run {
            Toaster.build(this,
                errorMessage,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                actionText
            ) {
                viewModel.cancelCoupon(couponId, status)
            }.show()
        }
    }

    private fun SortFilterItem.initRemovableFilterItem(onRemoveIconClicked: () -> Unit) {
        view?.post {
            refChipUnify.setOnRemoveListener {
                type = ChipsUnify.TYPE_NORMAL
                refChipUnify.gone()
                onRemoveIconClicked()
            }
            refChipUnify.gone()
        }
    }

    private fun checkDownloadPermission(couponList: List<CouponImageUiModel>) {
        val listener = object : PermissionCheckerHelper.PermissionCheckListener {
            override fun onPermissionDenied(permissionText: String) {
                permissionCheckerHelper.onPermissionDenied(requireActivity(), permissionText)
                Toaster.build(
                    view = view ?: return,
                    text = getString(R.string.mvc_storage_permission_enabled_needed),
                    duration = Toast.LENGTH_LONG
                ).show()
            }

            override fun onNeverAskAgain(permissionText: String) {
                permissionCheckerHelper.onNeverAskAgain(requireActivity(), permissionText)
            }

            override fun onPermissionGranted() {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    couponList.forEach {
                        downloadFiles(it.imageType.imageUrl)
                    }
                }
            }
        }

        permissionCheckerHelper.checkPermission(
            fragment = this,
            permission = PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
            listener = listener
        )

        VoucherCreationTracking.sendVoucherListClickTracking(
            action = VoucherCreationAnalyticConstant.EventAction.Click.DOWNLOAD_VOUCHER,
            isActive = false,
            userId = userSession.userId
        )
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun downloadFiles(uri: String) {
        val downloadCompleteListener = object : DownloadHelper.DownloadHelperListener {
            override fun onDownloadComplete() {
                showDownloadCompleteStatus(true)
            }
        }

        try {
            val helper = DownloadHelper(
                context = requireActivity(),
                uri = uri,
                filename = System.currentTimeMillis()
                    .toString() + VoucherCreationConst.JPEG_EXT,
                listener = downloadCompleteListener
            )
            helper.downloadFile { true }
        } catch (se: SecurityException) {
            MvcErrorHandler.logToCrashlytics(se, MvcError.ERROR_SECURITY)
            showDownloadCompleteStatus(
                isSuccess = false,
                isInternetProblem = false
            )
        } catch (iae: IllegalArgumentException) {
            MvcErrorHandler.logToCrashlytics(iae, MvcError.ERROR_URI)
            showDownloadCompleteStatus(
                isSuccess = false,
                isInternetProblem = false
            )
        } catch (ex: Exception) {
            MvcErrorHandler.logToCrashlytics(ex, MvcError.ERROR_DOWNLOAD)
            showDownloadCompleteStatus(
                isSuccess = false
            )
        }

    }

    fun showDownloadCompleteStatus(isSuccess: Boolean, isInternetProblem: Boolean = true) {
        val toasterType: Int
        val toasterMessage: String

        if (isSuccess) {
            toasterType = Toaster.TYPE_NORMAL
            toasterMessage = getString(R.string.download_coupon_product_success).toBlankOrString()
        } else {
            toasterType = Toaster.TYPE_ERROR
            val errorMessageSuffix =
                if (isInternetProblem) {
                    getString(R.string.mvc_fail_download_voucher_suffix).toBlankOrString()
                } else {
                    ""
                }
            toasterMessage = "${getString(R.string.download_coupon_product_failed).toBlankOrString()}$errorMessageSuffix"
        }

        longToaster(toasterMessage, toasterType)
    }


    private fun longToaster(text: String, toasterType: Int) {
        if (text.isEmpty()) return
        Toaster.build(
            view ?: return,
            text,
            Toaster.LENGTH_LONG,
            toasterType,
            getString(R.string.mvc_oke)
        ).show()
    }

    private fun displayShareBottomSheet(coupon: CouponUiModel, imageUrl: String, shop: ShopBasicDataResult) {
        val title = String.format(getString(R.string.placeholder_share_component_outgoing_title), shop.shopName)
        val endDate = coupon.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
            .parseTo(DateTimeUtils.DATE_FORMAT)
        val endHour = coupon.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
            .parseTo(DateTimeUtils.HOUR_FORMAT)
        val description = String.format(getString(R.string.placeholder_share_component_text_description), shop.shopName, endDate, endHour)


        shareComponentBottomSheet = buildShareComponentInstance(
            imageUrl,
            title,
            coupon.id.toLong(),
            onShareOptionsClicked = { shareModel ->
                handleShareOptionSelection(shareModel, title, description, shop.shopDomain)
            }, onCloseOptionClicked = {}
        )
        shareComponentBottomSheet?.show(childFragmentManager, shareComponentBottomSheet?.tag)
    }

    private fun buildShareComponentInstance(
        imageUrl: String,
        title: String,
        couponId: Long,
        onShareOptionsClicked : (ShareModel) -> Unit,
        onCloseOptionClicked : () -> Unit
    ): UniversalShareBottomSheet {

        return UniversalShareBottomSheet.createInstance().apply {
            val listener = object : ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    onShareOptionsClicked(shareModel)
                }

                override fun onCloseOptionClicked() {
                    onCloseOptionClicked()
                }
            }

            init(listener)
            setMetaData(tnTitle = title, tnImage = ShareComponentConstant.THUMBNAIL_ICON_IMAGE_URL)
            setOgImageUrl(imageUrl)
            setUtmCampaignData(
                pageName = ShareComponentConstant.PAGE_NAME,
                userId = userSession.userId,
                pageId = couponId.toString(),
                feature = ShareComponentConstant.SHARE
            )
        }
    }

    private fun handleShareOptionSelection(
        shareModel: ShareModel,
        title: String,
        description: String,
        shopDomain: String
    ) {
        val shareCallback = object : ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult?) {
                val wording = "$description ${linkerShareData?.shareUri.orEmpty()}"
                com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil.executeShareIntent(
                    shareModel,
                    linkerShareData,
                    activity,
                    view,
                    wording
                )
                shareComponentBottomSheet?.dismiss()
            }

            override fun onError(linkerError: LinkerError?) {}
        }

        val outgoingDescription = getString(R.string.share_component_outgoing_text_description)
        val linkerShareData = linkerDataGenerator.generate(
            userSession.shopId,
            shopDomain,
            shareModel,
            title,
            outgoingDescription
        )
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                NumberConstant.ZERO,
                linkerShareData,
                shareCallback
            )
        )
    }

    private fun showError(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(requireActivity(), throwable)
        Toaster.build(view ?: return, errorMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }
}