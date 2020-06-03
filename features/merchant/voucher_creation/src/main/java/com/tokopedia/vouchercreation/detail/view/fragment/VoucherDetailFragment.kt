package com.tokopedia.vouchercreation.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.bottmsheet.StopVoucherDialog
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.TipsTrickBottomSheet
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.DASH_DATE_FORMAT
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.HOUR_FORMAT
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
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
    }

    private var voucherUiModel: VoucherUiModel? = null

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeLiveData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupView()
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

    }

    override fun onFooterButtonClickListener() {
        showShareBottomSheet()
    }

    override fun onFooterCtaTextClickListener() {
        voucherUiModel?.run {
            when(status) {
                VoucherStatusConst.NOT_STARTED -> {
                    CancelVoucherDialog(context ?: return)
                            .setOnPrimaryClickListener {
                                viewModel.cancelVoucher(id)
                            }
                            .show(this)
                }
                VoucherStatusConst.ONGOING -> {
                    StopVoucherDialog(context ?: return)
                            .setOnPrimaryClickListener {
                                viewModel.cancelVoucher(id)
                            }
                            .show(this)
                }
            }
        }
    }

    override fun showTipsAndTrickBottomSheet() {
        if (!isAdded) return
        TipsTrickBottomSheet(context ?: return, !(voucherUiModel?.isPublic ?: true))
                .setOnDownloadClickListener {
                    showDownloadBottomSheet()
                }
                .setOnShareClickListener {
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
                .setOnDownloadClickListener {

                }
                .show(childFragmentManager)
    }

    override fun onErrorTryAgain() {
        setupView()
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

            if (status == VoucherStatusConst.ONGOING) {
                voucherDetailInfoList.addAll(listOf(
                        UsageProgressUiModel(type, quota, remainingQuota, bookedQuota),
                        DividerUiModel(DividerUiModel.THICK),
                        getOngoingTipsSection(isPublic)
                ))
            }

            if (status == VoucherStatusConst.ENDED) {
                voucherDetailInfoList.add(
                        //Todo: Add calculation
                        PromoPerformanceUiModel("Rp.123.456.789", bookedQuota, quota)
                )
            }

            with(voucherDetailInfoList) {
                val voucherTargetType =
                        if (isPublic) {
                            VoucherTargetType.PUBLIC
                        } else {
                            VoucherTargetType.PRIVATE
                        }
                addAll(listOf(
                        DividerUiModel(DividerUiModel.THICK),
                        getVoucherInfoSection(voucherTargetType, name, code),
                        DividerUiModel(DividerUiModel.THIN)
                ))
                getVoucherImageType(type, discountTypeFormatted, discountAmt, discountAmtMax)?.let { imageType ->
                    add(getVoucherBenefitSection(imageType, minimumAmt, quota))
                }
                addAll(listOf(
                        DividerUiModel(DividerUiModel.THIN),
                        getPeriodSection(fullDisplayedDate),
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
}