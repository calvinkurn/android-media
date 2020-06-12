package com.tokopedia.vouchercreation.detail.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.ScreenName
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.bottmsheet.StopVoucherDialog
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.TipsTrickBottomSheet
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.DASH_DATE_FORMAT
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.HOUR_FORMAT
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.activity.CreateMerchantVoucherStepsActivity
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.enums.getVoucherImageType
import com.tokopedia.vouchercreation.detail.model.*
import com.tokopedia.vouchercreation.detail.view.viewmodel.VoucherDetailViewModel
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.widget.CancelVoucherDialog
import com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet.ShareVoucherBottomSheet
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailFragment(val voucherId: Int) : BaseDetailFragment() {

    companion object {
        fun newInstance(voucherId: Int): VoucherDetailFragment = VoucherDetailFragment(voucherId)

        const val DOWNLOAD_REQUEST_CODE = 222
    }

    private var voucherUiModel: VoucherUiModel? = null

    @Inject
    lateinit var userSession: UserSessionInterface

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

    private val impressHolder = ImpressHolder()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeLiveData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupView()

        view.addOnImpressionListener(impressHolder) {
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

    override fun loadData(page: Int) {}

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
                showShareBottomSheet()
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
                action = Click.COPY_PROMO_CODE,
                userId = userSession.userId
        )
        if (!isAdded) return
        TipsTrickBottomSheet(context ?: return, !(voucherUiModel?.isPublic ?: true))
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
                    showShareBottomSheet()
                }
                .show(childFragmentManager)
    }

    override fun showDownloadBottomSheet() {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        DownloadVoucherBottomSheet(
                parent,
                voucherUiModel?.image.toBlankOrString(),
                voucherUiModel?.imageSquare.toBlankOrString())
                .setOnDownloadClickListener { voucherList ->
                    voucherList.forEach {
                        if (activity?.let { it1 -> ActivityCompat.checkSelfPermission(it1, Manifest.permission.WRITE_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED) {
                            downloadFiles(it.downloadVoucherType.imageUrl)
                        } else {
                            downloadFiles(it.downloadVoucherType.imageUrl)
                        }
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
                VoucherCreationTracking.sendVoucherDetailClickTracking(
                        status = voucherUiModel?.status ?: VoucherStatusConst.NOT_STARTED,
                        action = VoucherCreationAnalyticConstant.EventAction.Impression.DISPLAY_PERIOD,
                        userId = userSession.userId
                )
            }
            else -> return
        }
    }

    private fun observeLiveData() {
        viewLifecycleOwner.run {
            observe(viewModel.merchantVoucherModelLiveData) { result ->
                when(result) {
                    is Success -> {
                        adapter.clearAllElements()
                        voucherUiModel = result.data
                        renderVoucherDetailInformation(result.data)
                    }
                    is Fail -> {
                        clearAllData()
                        renderList(listOf(ErrorDetailUiModel))
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
                    }
                }
            }
        }
    }

    private fun setupView() = view?.run {
        showLoadingState()
        viewModel.getVoucherDetail(voucherId)
    }

    private fun showLoadingState() {
        adapter.clearAllElements()
        renderList(listOf(
                DetailLoadingStateUiModel()
        ))
    }

    private fun showShareBottomSheet() {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        ShareVoucherBottomSheet(parent)
                .setOnItemClickListener {

                }
                .show(childFragmentManager)
    }

    private fun setToolbarTitle(toolbarTitle: String) {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.supportActionBar?.title = toolbarTitle
        }
    }

    private fun renderVoucherDetailInformation(voucherUiModel: VoucherUiModel) {
        clearAllData()
        with(voucherUiModel) {
            setToolbarTitle(name)
            val startDate = DateTimeUtils.reformatUnsafeDateTime(startTime, DASH_DATE_FORMAT)
            val endDate = DateTimeUtils.reformatUnsafeDateTime(finishTime, DASH_DATE_FORMAT)
            val startHour = DateTimeUtils.reformatUnsafeDateTime(startTime, HOUR_FORMAT)
            val endHour = DateTimeUtils.reformatUnsafeDateTime(finishTime, HOUR_FORMAT)

            val fullDisplayedDate = getDisplayedDateString(startDate, startHour, endDate, endHour)

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
                        UsageProgressUiModel(type, quota, remainingQuota, bookedQuota),
                        DividerUiModel(DividerUiModel.THICK),
                        getOngoingTipsSection(isPublic)
                ))
            }

            if (status == VoucherStatusConst.ENDED) {
                voucherDetailInfoList.add(
                        // pass empty string for now as product requirement changed temporarily
                        PromoPerformanceUiModel("", bookedQuota, quota)
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
                                        isDetailEvent = status == VoucherStatusConst.NOT_STARTED,
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
                        getPeriodSection(fullDisplayedDate, voucherInfoHasCta),
                        DividerUiModel(DividerUiModel.THICK)
                ))
                getButtonUiModel(status)?.let { button ->
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

    private fun getButtonUiModel(@VoucherStatusConst status: Int): FooterButtonUiModel? {
        return when(status) {
            VoucherStatusConst.ENDED -> duplicateButtonUiModel
            VoucherStatusConst.STOPPED -> duplicateButtonUiModel
            VoucherStatusConst.ONGOING -> shareButtonUiModel
            else -> null
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

        view?.run {
            Toaster.make(this,
                    errorMessage,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR)
        }
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun downloadFiles(uri: String) {
        val missingPermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        activity?.let {
            ActivityCompat.requestPermissions(it, missingPermissions, DOWNLOAD_REQUEST_CODE)
            val helper = DownloadHelper(it, uri, System.currentTimeMillis().toString(), null)
            helper.downloadFile { true }
        }
    }
}