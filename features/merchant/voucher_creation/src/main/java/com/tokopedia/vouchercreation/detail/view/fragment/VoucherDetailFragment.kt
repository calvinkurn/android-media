package com.tokopedia.vouchercreation.detail.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.ScreenName
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.bottmsheet.StopVoucherDialog
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.TipsTrickBottomSheet
import com.tokopedia.vouchercreation.common.consts.VoucherCreationConst.JPEG_EXT
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.common.errorhandler.MvcError
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringListener
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.DASH_DATE_FORMAT
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.HOUR_FORMAT
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getDisplayedDateString
import com.tokopedia.vouchercreation.common.utils.shareVoucher
import com.tokopedia.vouchercreation.common.utils.showDownloadActionTicker
import com.tokopedia.vouchercreation.common.utils.showErrorToaster
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.activity.CreateMerchantVoucherStepsActivity
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.enums.getVoucherImageType
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.GeneralExpensesInfoBottomSheetFragment
import com.tokopedia.vouchercreation.detail.model.*
import com.tokopedia.vouchercreation.detail.view.viewmodel.VoucherDetailViewModel
import com.tokopedia.vouchercreation.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.widget.CancelVoucherDialog
import com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet.ShareVoucherBottomSheet
import kotlinx.android.synthetic.main.fragment_mvc_voucher_detail.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailFragment : BaseDetailFragment(), DownloadHelper.DownloadHelperListener {

    companion object {
        fun newInstance(voucherId: Int): VoucherDetailFragment = VoucherDetailFragment().apply {
            val bundle = Bundle().apply {
                putInt(VOUCHER_ID_KEY, voucherId)
            }
            arguments = bundle
        }

        const val DOWNLOAD_REQUEST_CODE = 222

        const val COPY_PROMO_CODE_LABEL = "detail_promo_code"

        private const val VOUCHER_ID_KEY = "voucher_id"

        private const val ERROR_DETAIL = "Error get voucher detail"
        private const val ERROR_CANCEL = "Error cancel voucher"
    }

    private var voucherUiModel: VoucherUiModel? = null

    private val voucherId by lazy {
        arguments?.getInt(VOUCHER_ID_KEY)
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(VoucherDetailViewModel::class.java)
    }

    private val shareButtonUiModel by lazy {
        FooterButtonUiModel(context?.getString(R.string.mvc_share_voucher).toBlankOrString(), "")
    }
    private val duplicateButtonUiModel by lazy {
        FooterButtonUiModel(context?.getString(R.string.mvc_duplicate_voucher).toBlankOrString(), "")
    }

    private val generalExpenseBottomSheet by lazy {
        GeneralExpensesInfoBottomSheetFragment.createInstance(context)
    }

    private var shareVoucherBottomSheet: ShareVoucherBottomSheet? = null

    private val impressHolder = ImpressHolder()

    private var shopBasicData: ShopBasicDataResult? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeLiveData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupView()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.flush()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getScreenName(): String = this::class.java.simpleName

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {
        viewModel.getBroadCastMetaData()
    }

    override fun onInfoContainerCtaClick(dataKey: String) {
        val editStep: Int
        val editInfoEventAction: String
        when(dataKey) {
            VOUCHER_INFO_DATA_KEY -> {
                editStep = VoucherCreationStep.TARGET
                editInfoEventAction = Click.EDIT_INFO_VOUCHER
            }
            VOUCHER_BENEFIT_DATA_KEY -> {
                editStep = VoucherCreationStep.BENEFIT
                editInfoEventAction = Click.EDIT_VOUCHER_BENEFIT
            }
            PERIOD_DATA_KEY -> {
                editStep = VoucherCreationStep.PERIOD
                editInfoEventAction = Click.EDIT_PERIOD
            }
            else -> {
                editStep = VoucherCreationStep.REVIEW
                editInfoEventAction = ""
            }
        }

        VoucherCreationTracking.sendVoucherDetailClickTracking(
                isDetailEvent = false,
                status = voucherUiModel?.status ?: VoucherStatusConst.NOT_STARTED,
                action = editInfoEventAction,
                userId = userSession.userId
        )

        val intent = RouteManager.getIntent(context, ApplinkConstInternalSellerapp.CREATE_VOUCHER).apply {
            putExtra(CreateMerchantVoucherStepsActivity.EDIT_VOUCHER, voucherUiModel)
            putExtra(CreateMerchantVoucherStepsActivity.IS_EDIT, true)
            putExtra(CreateMerchantVoucherStepsActivity.EDIT_STEP, editStep)
        }
        startActivity(intent)
    }

    override fun onTickerClicked() {
        VoucherCreationTracking.sendVoucherDetailClickTracking(
                isDetailEvent = voucherUiModel?.status != VoucherStatusConst.NOT_STARTED,
                status = voucherUiModel?.status ?: VoucherStatusConst.NOT_STARTED,
                action = Click.TOOLTIP_SPENDING_ESTIMATION,
                userId = userSession.userId
        )
        generalExpenseBottomSheet.show(childFragmentManager, GeneralExpensesInfoBottomSheetFragment.TAG)
    }

    override fun onFooterButtonClickListener() {
        voucherUiModel?.run {
            // Bagikan Voucher button
            if (status == VoucherStatusConst.ONGOING) {
                VoucherCreationTracking.sendVoucherDetailClickTracking(
                        status = voucherUiModel?.status ?: VoucherStatusConst.NOT_STARTED,
                        action = Click.SHARE_VOUCHER,
                        userId = userSession.userId
                )
                showShareBottomSheet(this)
            } else {
                //Duplikat Voucher button
                VoucherCreationTracking.sendVoucherDetailClickTracking(
                        status = voucherUiModel?.status ?: VoucherStatusConst.NOT_STARTED,
                        action = Click.DUPLICATE_VOUCHER,
                        userId = userSession.userId
                )
                activity?.let {
                    val intent = RouteManager.getIntent(context, ApplinkConstInternalSellerapp.CREATE_VOUCHER).apply {
                        putExtra(CreateMerchantVoucherStepsActivity.DUPLICATE_VOUCHER, this@run)
                        putExtra(CreateMerchantVoucherStepsActivity.IS_DUPLICATE, true)
                    }
                    it.finish()
                    startActivity(intent)
                }
            }
        }
    }

    override fun onFooterCtaTextClickListener() {
        VoucherCreationTracking.sendVoucherDetailClickTracking(
                status = voucherUiModel?.status ?: VoucherStatusConst.NOT_STARTED,
                action = Click.IN_HERE,
                userId = userSession.userId
        )
        voucherUiModel?.run {
            when(status) {
                VoucherStatusConst.NOT_STARTED -> {
                    CancelVoucherDialog(context ?: return)
                            .setOnPrimaryClickListener {
                                viewModel.cancelVoucher(id, CancelVoucherUseCase.CancelStatus.DELETE)
                            }
                            .show(this)
                }
                VoucherStatusConst.ONGOING -> {
                    StopVoucherDialog(context ?: return)
                            .setOnPrimaryClickListener {
                                viewModel.cancelVoucher(id, CancelVoucherUseCase.CancelStatus.STOP)
                            }
                            .show(this)
                }
            }
        }
    }

    override fun showTipsAndTrickBottomSheet() {
        VoucherCreationTracking.sendVoucherDetailClickTracking(
                status = voucherUiModel?.status ?: VoucherStatusConst.NOT_STARTED,
                action = Click.TIPS_TRICKS,
                userId = userSession.userId
        )
        if (!isAdded) return
        TipsTrickBottomSheet.createInstance(!(voucherUiModel?.isPublic ?: true))
                .setOnDownloadClickListener {
                    VoucherCreationTracking.sendVoucherDetailClickTracking(
                            status = voucherUiModel?.status ?: VoucherStatusConst.NOT_STARTED,
                            action = Click.DOWNLOAD_VOUCHER,
                            userId = userSession.userId
                    )
                    showDownloadBottomSheet()
                }
                .setOnShareClickListener {
                    VoucherCreationTracking.sendVoucherDetailClickTracking(
                            status = voucherUiModel?.status ?: VoucherStatusConst.NOT_STARTED,
                            action = Click.SHARE_VOUCHER,
                            userId = userSession.userId
                    )
                    voucherUiModel?.run {
                        showShareBottomSheet(this)
                    }
                }
                .show(childFragmentManager)
    }

    override fun showDownloadBottomSheet() {
        if (!isAdded) return
        DownloadVoucherBottomSheet.createInstance(
                voucherUiModel?.image.toBlankOrString(),
                voucherUiModel?.imageSquare.toBlankOrString(),
                userSession.userId)
                .setOnDownloadClickListener { voucherList ->
                    context?.run {
                        permissionCheckerHelper.checkPermission(this@VoucherDetailFragment,
                                PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
                                object : PermissionCheckerHelper.PermissionCheckListener {
                                    override fun onPermissionDenied(permissionText: String) {
                                        permissionCheckerHelper.onPermissionDenied(this@run, permissionText)
                                    }

                                    override fun onNeverAskAgain(permissionText: String) {
                                        permissionCheckerHelper.onNeverAskAgain(this@run, permissionText)
                                    }

                                    override fun onPermissionGranted() {
                                        if (ActivityCompat.checkSelfPermission(this@run, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                            voucherList.forEach {
                                                downloadFiles(it.downloadVoucherType.imageUrl)
                                            }
                                        }
                                    }
                                })
                    }
                }
                .show(childFragmentManager)
    }

    override fun onErrorTryAgain() {
        setupView()
    }

    override fun onImpression(dataKey: String) {
        when(dataKey) {
            PERIOD_DATA_KEY -> {
                VoucherCreationTracking.sendVoucherDetailImpressionTracking(
                        status = voucherUiModel?.status ?: VoucherStatusConst.NOT_STARTED,
                        action = VoucherCreationAnalyticConstant.EventAction.Impression.DISPLAY_PERIOD,
                        userId = userSession.userId
                )
            }
            else -> return
        }
    }

    override fun onDownloadComplete() {
        view?.showDownloadActionTicker(true)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(context, requestCode, permissions, grantResults)
        }
    }

    private fun observeLiveData() {
        viewLifecycleOwner.run {
            observe(viewModel.merchantVoucherModelLiveData) { result ->
                (activity as? MvcPerformanceMonitoringListener)?.startRenderPerformanceMonitoring()
                when(result) {
                    is Success -> {
                        adapter.clearAllElements()
                        voucherUiModel = result.data
                        sendOpenScreenTracking()
                        renderVoucherDetailInformation(result.data)
                        rvMvcVoucherDetail?.setOnLayoutListenerReady()
                    }
                    is Fail -> {
                        clearAllData()
                        renderList(listOf(ErrorDetailUiModel))
                        MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_DETAIL)
                    }
                }
            }
            observe(viewModel.cancelVoucherResultLiveData) { result ->
                when(result) {
                    is Success -> {
                        voucherUiModel?.run {
                            when(status) {
                                VoucherStatusConst.ONGOING -> showCancellationSuccessToaster(false)
                                VoucherStatusConst.NOT_STARTED -> showCancellationSuccessToaster(true)
                            }
                            activity?.finish()
                        }
                    }
                    is Fail -> {
                        voucherUiModel?.run {
                            when(status) {
                                VoucherStatusConst.ONGOING -> showCancellationFailToaster(false)
                                VoucherStatusConst.NOT_STARTED -> showCancellationFailToaster(true)
                            }
                        }
                        MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_CANCEL)
                    }
                }
            }
            observe(viewModel.shopBasicLiveData) { result ->
                when(result) {
                    is Success -> shopBasicData = result.data
                    is Fail -> MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_DETAIL)
                }
            }
            observe(viewModel.broadCastMetadata) { result ->
                shareVoucherBottomSheet = when(result) {
                    is Success -> {
                        val broadCastMetaData = result.data
                        setupShareBottomSheet(
                                status = broadCastMetaData.status,
                                quota = broadCastMetaData.quota
                        )
                    }
                    is Fail -> {
                        setupShareBottomSheet()
                    }
                }
            }
        }
    }

    private fun setupView() = view?.run {
        showLoadingState()
        voucherId?.run {
            (activity as? MvcPerformanceMonitoringListener)?.startNetworkPerformanceMonitoring()
            viewModel.getVoucherDetail(this)
        }
    }

    private fun setupShareBottomSheet(status: Int = 0, quota: Int = 0): ShareVoucherBottomSheet? {
        val shareVoucherBottomSheet = ShareVoucherBottomSheet.createInstance()
        shareVoucherBottomSheet.setBroadCastChatStatus(status)
        shareVoucherBottomSheet.setBroadCastChatQuota(quota)
        return shareVoucherBottomSheet
    }

    private fun showLoadingState() {
        adapter.clearAllElements()
        renderList(listOf(
                DetailLoadingStateUiModel()
        ))
    }

    private fun showShareBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        shareVoucherBottomSheet?.setOnItemClickListener { socmedType ->
                    context?.run {
                        shopBasicData?.shareVoucher(
                                context = this,
                                socmedType = socmedType,
                                voucher = voucher,
                                userId = userSession.userId,
                                shopId = userSession.shopId)
                    }
                }
        shareVoucherBottomSheet?.show(childFragmentManager)
    }

    private fun setToolbarTitle(toolbarTitle: String) {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.supportActionBar?.title = toolbarTitle
        }
    }

    private fun renderVoucherDetailInformation(voucherUiModel: VoucherUiModel) {
        clearAllData()
        with(voucherUiModel) {
            setToolbarTitle(String.format(context?.getString(R.string.mvc_voucher_name).orEmpty(), name))
            val startDate = DateTimeUtils.reformatUnsafeDateTime(startTime, DASH_DATE_FORMAT)
            val endDate = DateTimeUtils.reformatUnsafeDateTime(finishTime, DASH_DATE_FORMAT)
            val startHour = DateTimeUtils.reformatUnsafeDateTime(startTime, HOUR_FORMAT)
            val endHour = DateTimeUtils.reformatUnsafeDateTime(finishTime, HOUR_FORMAT)

            val fullDisplayedDate = getDisplayedDateString(context, startDate, startHour, endDate, endHour)

            val voucherDetailInfoList: MutableList<VoucherDetailUiModel> = mutableListOf(
                    VoucherHeaderUiModel(
                            status = status,
                            voucherImageUrl = imageSquare,
                            startTime = startTime,
                            finishTime = finishTime,
                            cancelTime =
                            if (status == VoucherStatusConst.STOPPED) {
                                updatedTime
                            } else {
                                null
                            }))

            if (type == VoucherTypeConst.FREE_ONGKIR) {
                voucherDetailInfoList.add(0,
                        when(status) {
                            VoucherStatusConst.NOT_STARTED -> InformationDetailTickerUiModel(true)
                            VoucherStatusConst.ONGOING -> InformationDetailTickerUiModel(true)
                            else -> InformationDetailTickerUiModel(false)
                        })
            }

            if (status == VoucherStatusConst.ONGOING) {
                voucherDetailInfoList.addAll(listOf(
                        UsageProgressUiModel(type, quota, confirmedQuota, bookedQuota),
                        DividerUiModel(DividerUiModel.THICK),
                        getOngoingTipsSection(isPublic)
                ))
            }

            if (status == VoucherStatusConst.ENDED) {
                voucherDetailInfoList.add(
                        // pass empty string for now as product requirement changed temporarily
                        PromoPerformanceUiModel("", confirmedQuota, quota)
                )
            }

            with(voucherDetailInfoList) {
                val voucherTargetType =
                        if (isPublic) {
                            VoucherTargetType.PUBLIC
                        } else {
                            VoucherTargetType.PRIVATE
                        }
                val voucherInfoHasCta = voucherUiModel.status == VoucherStatusConst.NOT_STARTED && voucherUiModel.type != VoucherTypeConst.FREE_ONGKIR
                addAll(listOf(
                        DividerUiModel(DividerUiModel.THICK),
                        getVoucherInfoSection(voucherTargetType, name, code, voucherInfoHasCta).apply {
                            onPromoCodeCopied = {
                                VoucherCreationTracking.sendVoucherDetailClickTracking(
                                        isDetailEvent = status != VoucherStatusConst.NOT_STARTED,
                                        status = voucherUiModel.status,
                                        action = Click.COPY_PROMO_CODE,
                                        userId = userSession.userId
                                )
                            }
                        },
                        DividerUiModel(DividerUiModel.THIN)
                ))
                getVoucherImageType(type, discountTypeFormatted, discountAmt, discountAmtMax)?.let { imageType ->
                    add(getVoucherBenefitSection(imageType, minimumAmt, quota, voucherInfoHasCta))
                }
                if (status == VoucherStatusConst.NOT_STARTED || status == VoucherStatusConst.STOPPED) {
                    add(getExpenseEstimationSection(discountAmtMax, quota))
                }
                addAll(listOf(
                        DividerUiModel(DividerUiModel.THIN),
                        getPeriodSection(fullDisplayedDate, voucherInfoHasCta)
                ))
                if (type != VoucherTypeConst.FREE_ONGKIR) {
                    add(DividerUiModel(DividerUiModel.THICK))
                }
                getButtonUiModel(status, type)?.let { button ->
                    add(button)
                }
                getFooterUiModel(status)?.let { footer ->
                    add(footer)
                }
            }

            renderList(voucherDetailInfoList)
        }
    }

    private fun getOngoingTipsSection(isPublic: Boolean): TipsUiModel {
        val tips: String
        val clickableText: String
        if (isPublic) {
            tips = context?.getString(R.string.mvc_detail_ticker_public).toBlankOrString()
            clickableText = context?.getString(R.string.mvc_detail_ticker_public_clickable).toBlankOrString()
        } else {
            tips = context?.getString(R.string.mvc_detail_ticker_private).toBlankOrString()
            clickableText = context?.getString(R.string.mvc_detail_ticker_private_clickable).toBlankOrString()
        }
        return TipsUiModel(tips, clickableText)
    }

    private fun getButtonUiModel(@VoucherStatusConst status: Int,
                                 @VoucherTypeConst type: Int): FooterButtonUiModel? {
        return if (type == VoucherTypeConst.CASHBACK) {
            when(status) {
                VoucherStatusConst.ENDED -> duplicateButtonUiModel
                VoucherStatusConst.STOPPED -> duplicateButtonUiModel
                VoucherStatusConst.ONGOING -> shareButtonUiModel
                else -> null
            }
        } else {
            null
        }
    }

    private fun getFooterUiModel(@VoucherStatusConst status: Int): FooterUiModel? {
        return when(status) {
            VoucherStatusConst.NOT_STARTED -> {
                FooterUiModel(
                        context?.getString(R.string.mvc_detail_ticker_cancel_promo).toBlankOrString(),
                        context?.getString(R.string.mvc_detail_ticker_here).toBlankOrString())
            }
            VoucherStatusConst.ONGOING -> {
                FooterUiModel(
                        context?.getString(R.string.mvc_detail_ticker_stop_promo).toBlankOrString(),
                        context?.getString(R.string.mvc_detail_ticker_here).toBlankOrString())
            }
            else -> null
        }
    }

    private fun showCancellationSuccessToaster(isCancel: Boolean) {
        val successMessageRes =
                if (isCancel) {
                    R.string.mvc_cancel_success
                } else {
                    R.string.mvc_stop_success
                }
        val successMessage = context?.getString(successMessageRes).toBlankOrString()

        view?.run {
            Toaster.make(this,
                    successMessage,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL)
        }
    }

    private fun showCancellationFailToaster(isCancel: Boolean) {
        val errorMessageRes =
                if (isCancel) {
                    R.string.mvc_cancel_fail
                } else {
                    R.string.mvc_stop_fail
                }
        val errorMessage = context?.getString(errorMessageRes).toBlankOrString()

        view?.showErrorToaster(errorMessage)
    }

    private fun sendOpenScreenTracking() {
        view?.addOnImpressionListener(impressHolder) {
            VoucherCreationTracking.sendOpenScreenTracking(
                    when(voucherUiModel?.status) {
                        VoucherStatusConst.NOT_STARTED -> ScreenName.VoucherDetail.UPCOMING
                        VoucherStatusConst.ONGOING -> ScreenName.VoucherDetail.ONGOING
                        VoucherStatusConst.ENDED -> ScreenName.VoucherDetail.ENDED
                        VoucherStatusConst.STOPPED -> ScreenName.VoucherDetail.CANCELLED
                        else -> return@addOnImpressionListener
                    },
                    userSession.isLoggedIn,
                    userSession.userId)
        }
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun downloadFiles(uri: String) {
        activity?.let {
            try {
                val helper = DownloadHelper(it, uri, System.currentTimeMillis().toString() + JPEG_EXT, this)
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
                view?.showDownloadActionTicker(false)
            }
        }
    }

    private fun View.setOnLayoutListenerReady() {
        viewTreeObserver?.run {
            addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    (activity as? MvcPerformanceMonitoringListener)?.finishMonitoring()
                    removeOnGlobalLayoutListener(this)
                }
            })
        }
    }
}