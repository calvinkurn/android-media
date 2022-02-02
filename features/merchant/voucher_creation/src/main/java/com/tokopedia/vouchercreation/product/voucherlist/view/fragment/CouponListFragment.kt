package com.tokopedia.vouchercreation.product.voucherlist.view.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.datepicker.toZeroIfNull
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.base.BaseSimpleListFragment
import com.tokopedia.vouchercreation.common.bottmsheet.StopVoucherDialog
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherUiModel
import com.tokopedia.vouchercreation.common.consts.VoucherCreationConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.common.errorhandler.MvcError
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.exception.VoucherCancellationException
import com.tokopedia.vouchercreation.common.utils.SharingUtil
import com.tokopedia.vouchercreation.common.utils.shareVoucher
import com.tokopedia.vouchercreation.common.utils.showDownloadActionTicker
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.view.fragment.ProductCouponPreviewFragment
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.EditQuotaCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.EditPeriodCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.ViewDetailCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.BroadCastChat
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.DownloadCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.CancelCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.ShareCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.DuplicateCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.StopCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.EditCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.*
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.presentation.bottomsheet.MoreMenuBottomSheet
import com.tokopedia.vouchercreation.product.voucherlist.view.adapter.CouponListAdapter
import com.tokopedia.vouchercreation.product.voucherlist.view.viewmodel.CouponListViewModel
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.CancelVoucherDialog
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sharebottomsheet.ShareVoucherBottomSheet
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sharebottomsheet.SocmedType
import java.util.*
import javax.inject.Inject

class CouponListFragment: BaseSimpleListFragment<CouponListAdapter, VoucherUiModel>() {

    companion object {
        fun newInstance(
            onCreateCouponMenuSelected: () -> Unit,
            onEditCouponMenuSelected: (Coupon) -> Unit,
            onDuplicateCouponMenuSelected: (Coupon) -> Unit
        ): CouponListFragment {
            val args = Bundle()
            val fragment = CouponListFragment().apply {
                arguments = args
                this.onCreateCouponMenuSelected = onCreateCouponMenuSelected
                this.onEditCouponMenuSelected = onEditCouponMenuSelected
                this.onDuplicateCouponMenuSelected = onDuplicateCouponMenuSelected
            }
            return fragment
        }

        private const val CANCEL_VOUCHER_ERROR = "Cancel voucher error - voucher list"
        private const val STOP_VOUCHER_ERROR = "Stop voucher error - voucher list"
        private const val TAG_SCALYR_MVC_CANCEL_VOUCHER_ERROR = "MVC_CANCEL_VOUCHER_ERROR"
        private const val TAG_SCALYR_MVC_STOP_VOUCHER_ERROR = "MVC_STOP_VOUCHER_ERROR"
        private const val KEY_TYPE_MESSAGE = "type"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(CouponListViewModel::class.java)
    }

    private val moreBottomSheet: MoreMenuBottomSheet? by lazy {
        return@lazy MoreMenuBottomSheet.createInstance()
    }

    private var shareVoucherBottomSheet: ShareVoucherBottomSheet? = null
    private var shopBasicData: ShopBasicDataResult? = null

    private var onRedirectToCouponPreview : (Coupon, ProductCouponPreviewFragment.Mode) -> Unit = { _, _ -> }
    private var onCreateCouponMenuSelected : () -> Unit = {}
    private var onEditCouponMenuSelected : (Coupon) -> Unit = {}
    private var onDuplicateCouponMenuSelected : (Coupon) -> Unit = {}

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
        val chip = view.findViewById<SortFilter>(R.id.sf_voucher_list)
        chip.setOnClickListener {
            print("sss")
        }
        chip.parentListener = {
            onCreateCouponMenuSelected()
        }
        observer()
    }

    override fun createAdapter() = CouponListAdapter(::onCouponOptionClicked, ::onCouponIconCopyClicked)

    override fun getRecyclerView(view: View): RecyclerView = view.findViewById(R.id.rvVoucherList)

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout = view.findViewById(R.id.swipeMvcList)

    override fun addElementToAdapter(list: List<VoucherUiModel>) {
        adapter?.addData(list)
    }

    override fun loadData(page: Int) {
        viewModel.getVoucherList()
    }

    override fun clearAdapterData() {
        adapter?.clearData()
    }

    override fun onShowLoading() {

    }

    override fun onHideLoading() {

    }

    override fun onDataEmpty() {

    }

    override fun onGetListError(message: String) {

    }

    fun setOnRedirectToCouponPreview(onRedirectToCouponPreview : (Coupon, ProductCouponPreviewFragment.Mode) -> Unit) {
        this.onRedirectToCouponPreview = onRedirectToCouponPreview
    }

    private fun onCouponIconCopyClicked(couponCode: String) {
        context?.let { SharingUtil.copyTextToClipboard(it, couponCode, couponCode) }
    }

    private fun onCouponOptionClicked(coupon: VoucherUiModel) {
        moreBottomSheet?.show(childFragmentManager)
        moreBottomSheet?.setOnItemClickListener(VoucherStatusConst.ONGOING) { menu ->
            moreBottomSheet?.dismiss()
            clickMoreMenuItem(menu, coupon)
        }
    }

    private fun observer() {
        observeCouponList()
        observeCancelCoupon()
        observeStopCoupon()
    }

    private fun observeCouponList() = viewModel.couponList.observe(viewLifecycleOwner) {
        if (it is Success) {
            renderList(it.data, false)
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
                    message = mapOf(KEY_TYPE_MESSAGE to ErrorHandler.getErrorMessage(context, result.throwable))
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
                    message = mapOf(KEY_TYPE_MESSAGE to ErrorHandler.getErrorMessage(context, result.throwable))
                )
            }
        }
    }
    
    private fun clickMoreMenuItem(menu: MoreMenuUiModel, coupon: VoucherUiModel) {
        when (menu) {
            is EditQuotaCoupon -> editQuotaCoupon()
            is ViewDetailCoupon -> viewDetailCoupon()
            is EditCoupon -> editCoupon()
            is BroadCastChat -> broadCastChat(coupon)
            is ShareCoupon -> shareCoupon(coupon)
            is EditPeriodCoupon -> editPeriodCoupon()
            is DownloadCoupon -> downloadCoupon(coupon)
            is CancelCoupon -> cancelCoupon(coupon)
            is StopCoupon -> stopCoupon(coupon)
            is DuplicateCoupon -> duplicateCoupon()
            else -> { /* do nothing */ }
        }
    }

    private fun editQuotaCoupon() {
        /* waiting quota coupon BottomSheet revamp */
    }

    private fun editCoupon() {
        onEditCouponMenuSelected(populateDummyCoupon())
    }

    private fun viewDetailCoupon() {
        /* waiting navigation detail coupon */
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

        shareVoucherBottomSheet?.apply {
            setOnItemClickListener { socmedType ->
                clickShareCoupon(coupon, socmedType)
            }
            show(childFragmentManager)
        }
    }

    private fun editPeriodCoupon() {
        /* waiting edit period coupon BottomSheet revamp */
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

    private fun duplicateCoupon() {
        onDuplicateCouponMenuSelected(populateDummyCoupon())
    }

    private fun populateDummyCoupon(): Coupon {
        //Stub the coupon preview data for testing purpose
        val startDate = Calendar.getInstance().apply { set(2022, 0, 28, 22, 30, 0) }
        val endDate = Calendar.getInstance().apply { set(2022, 0, 30, 22, 0, 0) }
        val period = CouponInformation.Period(startDate.time, endDate.time)

        val information = CouponInformation(
            CouponInformation.Target.PUBLIC,
            "Kupon Kopi Soe",
            "KOPSOE",
            period

        )

        val setting = CouponSettings(
            CouponType.FREE_SHIPPING,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOMINAL,
            10000,
            100,
            10000,
            10,
            5000,
            1000000
        )

        val products =
            listOf(
                CouponProduct(
                    "2147956088",
                    18000,
                    5.0F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    19
                ),
                CouponProduct(
                    "15455652",
                    18000,
                    4.7F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    1000
                ),
                CouponProduct(
                    "15429644",
                    18000,
                    5.0F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    2100
                ),
                CouponProduct(
                    "15409031",
                    25000,
                    4.0F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    31000
                )
            )

        return Coupon(9094, information, setting, products)
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
                /* waiting navigation detail coupon */
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

    private fun setupShareBottomSheet(status: Int = 0, promo: Int = 0): ShareVoucherBottomSheet {
        val shareVoucherBottomSheet = ShareVoucherBottomSheet.createInstance()
        shareVoucherBottomSheet.setBroadCastChatStatus(status)
        shareVoucherBottomSheet.setBroadCastChatPromo(promo)
        return shareVoucherBottomSheet
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
}