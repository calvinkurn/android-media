package com.tokopedia.vouchercreation.product.voucherlist.view.fragment

import android.Manifest
import android.content.Context
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
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.base.BaseSimpleListFragment
import com.tokopedia.vouchercreation.common.bottmsheet.StopVoucherDialog
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherUiModel
import com.tokopedia.vouchercreation.common.bottmsheet.voucherperiodbottomsheet.VoucherPeriodBottomSheet
import com.tokopedia.vouchercreation.common.consts.VoucherCreationConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.common.errorhandler.MvcError
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.exception.VoucherCancellationException
import com.tokopedia.vouchercreation.common.mapper.CouponMapper
import com.tokopedia.vouchercreation.common.utils.*
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.voucherlist.view.adapter.CouponListAdapter
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.CANCEL_VOUCHER_ERROR
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.GET_SHOP_BASIC_DATA_ERROR
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.GET_VOUCHER_DETAIL_ERROR
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.IS_SUCCESS_VOUCHER
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.IS_SUCCESS_VOUCHER_DEFAULT_VALUE
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.IS_UPDATE_VOUCHER
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.IS_UPDATE_VOUCHER_DEFAULT_VALUE
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.LIST_COUPON_PER_PAGE
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.STOP_VOUCHER_ERROR
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.SUCCESS_VOUCHER_DEFAULT_VALUE
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.TAG_SCALYR_MVC_CANCEL_VOUCHER_ERROR
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.TAG_SCALYR_MVC_GET_SHOP_BASIC_DATA_ERROR
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.TAG_SCALYR_MVC_GET_VOUCHER_DETAIL_ERROR
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.TAG_SCALYR_MVC_STOP_VOUCHER_ERROR
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.TYPE_MESSAGE_KEY
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.VOUCHER_ID_KEY
import com.tokopedia.vouchercreation.product.voucherlist.view.viewmodel.CouponListViewModel
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.filter.CouponStatusFilterBotomSheet
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.*
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.presentation.bottomsheet.MoreMenuBottomSheet
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherStatus
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.fragment.VoucherListFragment
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.BroadCastVoucherBottomSheet
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.CancelVoucherDialog
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.EditQuotaBottomSheet
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sharebottomsheet.ShareVoucherBottomSheet
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sharebottomsheet.SocmedType
import java.util.*
import javax.inject.Inject

class CouponListFragment: BaseSimpleListFragment<CouponListAdapter, VoucherUiModel>() {

    companion object {
        fun newInstance(
            onCreateCouponMenuSelected: () -> Unit,
            onEditCouponMenuSelected: (Coupon) -> Unit,
            onDuplicateCouponMenuSelected: (Coupon) -> Unit,
            onViewCouponDetailMenuSelected : (Long) -> Unit = {}
        ): CouponListFragment {
            val args = Bundle()
            val fragment = CouponListFragment().apply {
                arguments = args
                this.onCreateCouponMenuSelected = onCreateCouponMenuSelected
                this.onEditCouponMenuSelected = onEditCouponMenuSelected
                this.onDuplicateCouponMenuSelected = onDuplicateCouponMenuSelected
                this.onViewCouponDetailMenuSelected = onViewCouponDetailMenuSelected
            }
            return fragment
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var couponMapper: CouponMapper

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(CouponListViewModel::class.java)
    }

    private val moreBottomSheet: MoreMenuBottomSheet? by lazy {
        return@lazy MoreMenuBottomSheet.createInstance()
    }

    private val successCouponId by lazy {
        getIntArgs(VOUCHER_ID_KEY, SUCCESS_VOUCHER_DEFAULT_VALUE)
    }

    private val isNeedToShowSuccessDialog by lazy {
        getBooleanArgs(IS_SUCCESS_VOUCHER, IS_SUCCESS_VOUCHER_DEFAULT_VALUE)
    }

    private val isNeedToShowSuccessUpdateDialog by lazy {
        getBooleanArgs(IS_UPDATE_VOUCHER, IS_UPDATE_VOUCHER_DEFAULT_VALUE)
    }

    private var shareCouponBottomSheet: ShareVoucherBottomSheet? = null
    private var shopBasicData: ShopBasicDataResult? = null
    private val filterStatus by lazy { SortFilterItem("Status Aktif") }
    private val filterType by lazy { SortFilterItem("Gratis Ongkir") }
    private val filterTarget by lazy { SortFilterItem("Publik") }

    private var onCreateCouponMenuSelected : () -> Unit = {}
    private var onEditCouponMenuSelected : (Coupon) -> Unit = {}
    private var onDuplicateCouponMenuSelected : (Coupon) -> Unit = {}
    private var onViewCouponDetailMenuSelected : (Long) -> Unit = {}

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
        return inflater.inflate(R.layout.fragment_mvc_coupon_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()
        setupBackButton(view)
        setupSearchField(view)
        setupFilterChips(view)
        setupObserver()
        getInitialValues()
    }

    override fun createAdapter() = CouponListAdapter(::onCouponOptionClicked, ::onCouponIconCopyClicked)

    override fun getRecyclerView(view: View): RecyclerView = view.findViewById(R.id.rvVoucherList)

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout = view.findViewById(R.id.swipeMvcList)

    override fun getPerPage() = LIST_COUPON_PER_PAGE

    override fun addElementToAdapter(list: List<VoucherUiModel>) {
        adapter?.addData(list)
    }

    override fun loadData(page: Int) {
        viewModel.getVoucherList(page)
    }

    override fun clearAdapterData() {
        adapter?.clearData()
    }

    override fun onShowLoading() {
        adapter?.showLoading()
    }

    override fun onHideLoading() {
        adapter?.hideLoading()
    }

    override fun onDataEmpty() {

    }

    override fun onGetListError(message: String) {

    }

    private fun getInitialValues() {
        viewModel.getBroadCastMetaData()
        viewModel.getShopBasicData()
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
            //onCreateCouponMenuSelected()
        }

        filterStatus.chevronListener = {
            CouponStatusFilterBotomSheet(::onStatusSelected).show(childFragmentManager, "")
        }
        filterTarget.initRemovableFilterItem()
        filterType.initRemovableFilterItem()

        chip.addItem(arrayListOf(filterStatus, filterTarget, filterType))
    }

    private fun setupObserver() {
        observeCouponList()
        observeCancelCoupon()
        observeStopCoupon()
        observeBroadCastMetadata()
        observeDetailCoupon()
        observeShopBasicData()
    }

    private fun observeCouponList() = viewModel.couponList.observe(viewLifecycleOwner) {
        if (it is Success) {
            renderList(it.data, it.data.size == getPerPage())
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

    private fun observeBroadCastMetadata() = viewModel.broadCastMetadata.observe(viewLifecycleOwner) { result ->
        shareCouponBottomSheet = when (result) {
            is Success -> {
                val broadCastMetaData = result.data
                // determine the free broadcast icon on success bottom sheet
                viewModel.setIsFreeBroadCastIconVisible(broadCastMetaData.promo)
                setupShareCouponBottomSheet(
                    status = broadCastMetaData.status,
                    promo = broadCastMetaData.promo
                )
            }
            is Fail -> {
                setupShareCouponBottomSheet()
            }
        }

        // execute get detail coupon to show bottomsheet
        if (successCouponId != VoucherListFragment.INVALID_VOUCHER_ID && isNeedToShowSuccessDialog && !viewModel.getIsSuccessDialogDisplayed()) {
            viewModel.getDetailCoupon(successCouponId)
        } else if (isNeedToShowSuccessUpdateDialog) {
            showSuccessUpdateToaster()
        }
    }

    private fun observeDetailCoupon() = viewModel.detailCoupon.observe(viewLifecycleOwner) { result ->
        when (result) {
            is Success -> {
                result.data.let { uiModel ->
                    uiModel.isFreeIconVisible = viewModel.getIsFreeBroadCastIconVisible()
                    showBroadCastCouponBottomSheet(uiModel)
                    viewModel.setIsSuccessDialogDisplayed(true)
                }
            }
            is Fail -> {
                // send crash report to firebase crashlytics
                MvcErrorHandler.logToCrashlytics(
                    throwable = result.throwable,
                    message = GET_VOUCHER_DETAIL_ERROR
                )
                // log error type to scalyr
                val errorMessage = ErrorHandler.getErrorMessage(
                    context = context,
                    e = result.throwable
                )
                ServerLogger.log(
                    priority = Priority.P2,
                    tag = TAG_SCALYR_MVC_GET_VOUCHER_DETAIL_ERROR,
                    message = mapOf(TYPE_MESSAGE_KEY to errorMessage)
                )
            }
        }
    }

    private fun observeShopBasicData() = viewModel.shopBasicData.observe(viewLifecycleOwner) { result ->
        when (result) {
            is Success -> {
                shopBasicData = result.data
            }
            is Fail -> {
                // send crash report to firebase crashlytics
                MvcErrorHandler.logToCrashlytics(
                    throwable = result.throwable,
                    message = GET_SHOP_BASIC_DATA_ERROR
                )
                // log error type to scalyr
                val errorMessage = ErrorHandler.getErrorMessage(
                    context = context,
                    e = result.throwable
                )
                ServerLogger.log(
                    priority = Priority.P2,
                    tag = TAG_SCALYR_MVC_GET_SHOP_BASIC_DATA_ERROR,
                    message = mapOf(TYPE_MESSAGE_KEY to errorMessage)
                )
            }
        }
    }

    private fun activateFilterTarget() {
        filterTarget.type = ChipsUnify.TYPE_SELECTED
        filterTarget.refChipUnify.show()
        view?.post { filterTarget.refChipUnify.displayRemoveIcon() }
    }

    private fun activateFilterType() {
        filterType.type = ChipsUnify.TYPE_SELECTED
        filterTarget.refChipUnify.show()
        view?.post { filterType.refChipUnify.displayRemoveIcon() }
    }

    private fun showBroadCastCouponBottomSheet(coupon: VoucherUiModel) {
        BroadCastVoucherBottomSheet.createInstance(coupon)
            .setOnShareClickListener {
                VoucherCreationTracking.sendCreateVoucherClickTracking(
                    step = VoucherCreationStep.REVIEW,
                    action = VoucherCreationAnalyticConstant.EventAction.Click.VOUCHER_SUCCESS_SHARE_NOW,
                    userId = userSession.userId
                )
                shareCoupon(coupon)
            }
            .setOnBroadCastClickListener {
                VoucherCreationTracking.sendBroadCastChatClickTracking(
                    category = VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.PAGE,
                    shopId = userSession.shopId
                )
                broadCastChat(coupon)
            }
            .apply {
                clearContentPadding = true
                setCloseClickListener {
                    VoucherCreationTracking.sendCreateVoucherClickTracking(
                        step = VoucherCreationStep.REVIEW,
                        action = VoucherCreationAnalyticConstant.EventAction.Click.VOUCHER_SUCCESS_CLICK_BACK_BUTTON,
                        userId = userSession.userId
                    )
                    dismiss()
                }
            }
            .show(childFragmentManager)
    }

    private fun showSuccessUpdateToaster() {
        view?.run {
            Toaster.build(
                view = this,
                text = context?.getString(R.string.mvc_success_update_toaster).toBlankOrString(),
                duration = Toaster.LENGTH_LONG,
                type = Toaster.TYPE_NORMAL,
                actionText = context?.getString(R.string.mvc_oke).toBlankOrString()
            ) {
                /* do nothing */
            }.show()
        }
    }

    private fun clickMoreMenuItem(menu: MoreMenuUiModel, coupon: VoucherUiModel) {
        when (menu) {
            is EditQuotaCoupon -> editQuotaCoupon(coupon)
            is ViewDetailCoupon -> viewDetailCoupon(coupon)
            is EditCoupon -> editCoupon(coupon)
            is BroadCastChat -> broadCastChat(coupon)
            is ShareCoupon -> shareCoupon(coupon)
            is EditPeriodCoupon -> editPeriodCoupon(coupon)
            is DownloadCoupon -> downloadCoupon(coupon)
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
        onEditCouponMenuSelected(couponMapper.map(coupon))
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

        shareCouponBottomSheet?.show(childFragmentManager)
        shareCouponBottomSheet?.setOnItemClickListener { socmedType ->
            clickShareCoupon(coupon, socmedType)
        }
    }

    private fun editPeriodCoupon(coupon: VoucherUiModel) {
        showEditPeriodBottomSheet(coupon)
    }

    private fun downloadCoupon(coupon: VoucherUiModel) {
        if (!isAdded) return

        DownloadVoucherBottomSheet.createInstance(
            bannerUrl = coupon.image,
            squareUrl = coupon.imageSquare,
            userId = userSession.userId
        ).setOnDownloadClickListener { couponList ->
            clickDownloadCoupon(couponList)
        }.show(childFragmentManager)
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
        onDuplicateCouponMenuSelected(couponMapper.map(coupon))
    }

    private fun showEditPeriodBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        VoucherPeriodBottomSheet.createInstance(voucher)
            .setOnSuccessClickListener {
                onSuccessUpdateVoucherPeriod()
            }
            .setOnFailClickListener { message ->
                val errorMessage =
                    if (message.isNotBlank()) {
                        message
                    } else {
                        context?.getString(R.string.mvc_general_error).toBlankOrString()
                    }
                view?.showErrorToaster(errorMessage)
            }
            .show(childFragmentManager)
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

        EditQuotaBottomSheet.createInstance(voucher)
            .setOnSuccessUpdateVoucher {
                loadInitialData()
                view?.run {
                    Toaster.build(
                        this,
                        context?.getString(R.string.mvc_quota_success).toBlankOrString(),
                        Toaster.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        context?.getString(R.string.mvc_oke).toBlankOrString()
                    ).show()
                }
            }
            .setOnFailUpdateVoucher { message ->
                val errorMessage =
                    if (message.isNotBlank()) {
                        message
                    } else {
                        context?.getString(R.string.mvc_general_error).toBlankOrString()
                    }
                view?.showErrorToaster(errorMessage)
            }.show(childFragmentManager)
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

    private fun setupShareCouponBottomSheet(status: Int = 0, promo: Int = 0): ShareVoucherBottomSheet {
        val shareCouponBottomSheet = ShareVoucherBottomSheet.createInstance()
        shareCouponBottomSheet.setBroadCastChatStatus(status)
        shareCouponBottomSheet.setBroadCastChatPromo(promo)
        return shareCouponBottomSheet
    }

    private fun clickShareCoupon(coupon: VoucherUiModel, @SocmedType socmedType: Int) {
        context?.run {
            shopBasicData?.shareVoucher(
                context = this,
                socmedType = socmedType,
                voucher = coupon,
                userId = userSession.userId,
                shopId = userSession.shopId
            )
        }
    }

    private fun clickDownloadCoupon(couponList: List<DownloadVoucherUiModel>) {
        context?.run {
            permissionCheckerHelper.checkPermission(
                fragment = this@CouponListFragment,
                permission = PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
                listener = permissionCheckListenerImpl(this@run, couponList)
            )
        }

        VoucherCreationTracking.sendVoucherListClickTracking(
            action = VoucherCreationAnalyticConstant.EventAction.Click.DOWNLOAD_VOUCHER,
            isActive = false,
            userId = userSession.userId
        )
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun downloadFiles(uri: String) {
        activity?.let {
            try {
                val helper = DownloadHelper(
                    context = it,
                    uri = uri,
                    filename = System.currentTimeMillis().toString() + VoucherCreationConst.JPEG_EXT,
                    listener = downloadHelperListenerImpl()
                )
                helper.downloadFile { true }
            } catch (se: SecurityException) {
                MvcErrorHandler.logToCrashlytics(se, MvcError.ERROR_SECURITY)
                view?.showDownloadActionTicker(
                    isSuccess = false,
                    isInternetProblem = false
                )
            } catch (iae: IllegalArgumentException) {
                MvcErrorHandler.logToCrashlytics(iae, MvcError.ERROR_URI)
                view?.showDownloadActionTicker(
                    isSuccess = false,
                    isInternetProblem = false
                )
            } catch (ex: Exception) {
                MvcErrorHandler.logToCrashlytics(ex, MvcError.ERROR_DOWNLOAD)
                view?.showDownloadActionTicker(
                    isSuccess = false
                )
            }
        }
    }

    private fun permissionCheckListenerImpl(context: Context, couponList: List<DownloadVoucherUiModel>) = object : PermissionCheckerHelper.PermissionCheckListener {
        override fun onPermissionDenied(permissionText: String) {
            permissionCheckerHelper.onPermissionDenied(context, permissionText)
            view?.let {
                Toaster.build(
                    view = it,
                    text = getString(R.string.mvc_storage_permission_enabled_needed),
                    duration = Toast.LENGTH_LONG
                ).show()
            }
        }

        override fun onNeverAskAgain(permissionText: String) {
            permissionCheckerHelper.onNeverAskAgain(context, permissionText)
        }

        override fun onPermissionGranted() {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                couponList.forEach {
                    downloadFiles(it.downloadVoucherType.imageUrl)
                }
            }
        }
    }

    private fun downloadHelperListenerImpl() = object : DownloadHelper.DownloadHelperListener {
        override fun onDownloadComplete() {
            view?.showDownloadActionTicker(true)
        }
    }

    private fun SortFilterItem.initRemovableFilterItem() {
        view?.post {
            refChipUnify.setOnRemoveListener {
                type = ChipsUnify.TYPE_NORMAL
                refChipUnify.gone()
            }
            refChipUnify.gone()
        }
    }
}