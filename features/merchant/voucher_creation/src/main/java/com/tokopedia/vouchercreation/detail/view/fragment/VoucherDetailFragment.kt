package com.tokopedia.vouchercreation.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.bottmsheet.StopVoucherDialog
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.TipsTrickBottomSheet
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.model.MerchantVoucherModel
import com.tokopedia.vouchercreation.common.model.VoucherMapper
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.DATE_FORMAT
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.HOUR_FORMAT
import com.tokopedia.vouchercreation.create.view.enums.BenefitType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.detail.model.*
import com.tokopedia.vouchercreation.detail.view.viewmodel.VoucherDetailViewModel
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet.ShareVoucherBottomSheet
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailFragment(val voucherId: Int) : BaseDetailFragment() {

    companion object {
        fun newInstance(voucherId: Int): VoucherDetailFragment = VoucherDetailFragment(voucherId)
    }

    private val dummyVoucher = VoucherMapper().mapRemoteModelToUiModel(listOf(MerchantVoucherModel(
            voucherName = "Voucher Hura Test Doang", discountAmtFormatted = "Cashback 10%"
    ))).first()

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
        StopVoucherDialog(context ?: return)
                .setOnPrimaryClickListener {

                }
                .show(dummyVoucher)
    }

    override fun showTipsAndTrickBottomSheet() {
        if (!isAdded) return
        TipsTrickBottomSheet(context ?: return, true)
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
        DownloadVoucherBottomSheet(parent)
                .setOnDownloadClickListener {

                }
                .show(childFragmentManager)
    }

    private fun observeLiveData() {
        viewLifecycleOwner.observe(viewModel.merchantVoucherModelLiveData) { result ->
            when(result) {
                is Success -> {
                    adapter.clearAllElements()
                    //Todo: add information
                    renderVoucherDetailInformation(result.data)
                }
                is Fail -> {
                    //Todo: show error state
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

    private fun renderVoucherDetailInformation(voucherUiModel: VoucherUiModel) {
        with(voucherUiModel) {
            val startDate = DateTimeUtils.reformatUnsafeDateTime(startTime, DATE_FORMAT)
            val endDate = DateTimeUtils.reformatUnsafeDateTime(finishTime, DATE_FORMAT)
            val startHour = DateTimeUtils.reformatUnsafeDateTime(startTime, HOUR_FORMAT)
            val endHour = DateTimeUtils.reformatUnsafeDateTime(finishTime, HOUR_FORMAT)
            val fullDisplayedDate = getDisplayedDateString(startDate, startHour, endDate, endHour)

            val voucherDetailInfoList = listOf(
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
                            }),
                    getVoucherInfoSection(type, name, code),
                    DividerUiModel(DividerUiModel.THIN),
                    getVoucherImageType(type, discountTypeFormatted, discountAmt, discountAmtMax)?.let { imageType ->
                        getVoucherBenefitSection(imageType, minimumAmt, quota)
                    },
                    DividerUiModel(DividerUiModel.THIN),
                    getPeriodSection(fullDisplayedDate),
                    DividerUiModel(DividerUiModel.THICK),
                    getButtonUiModel(status),
                    getFooterUiModel(status))

            renderList(voucherDetailInfoList)
        }
    }

    private fun getVoucherImageType(@VoucherTypeConst voucherType: Int,
                                    @BenefitType benefitType: String,
                                    amount: Int,
                                    amountMax: Int): VoucherImageType? {
        when (voucherType) {
            VoucherTypeConst.FREE_ONGKIR -> {
                return VoucherImageType.FreeDelivery(amount)
            }
            VoucherTypeConst.CASHBACK -> {
                return when(benefitType) {
                    BenefitType.IDR -> {
                        VoucherImageType.Rupiah(amount)
                    }
                    BenefitType.PERCENT -> {
                        VoucherImageType.Percentage(amountMax, amount)
                    }
                    else -> null
                }
            }
            else -> return null
        }
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

    private fun showDummyData() {
        val dummy = listOf(
                //PromoPerformanceUiModel("Rp3.000.000", 30, 120),
//                VoucherHeaderUiModel(),
                UsageProgressUiModel(30),
                DividerUiModel(8),
                TipsUiModel(
                        context?.getString(R.string.mvc_detail_ticker_share).toBlankOrString(),
                        context?.getString(R.string.mvc_detail_ticker_see_tips).toBlankOrString()),
                DividerUiModel(8),
                InfoContainerUiModel(R.string.mvc_detail_voucher_info, listOf(
                        SubInfoItemUiModel(R.string.mvc_voucher_target, "Khusus"),
                        SubInfoItemUiModel(R.string.mvc_detail_voucher_name, "Voucher Hura Test Doang"),
                        SubInfoItemUiModel(R.string.mvc_detail_promo_code, "TESTDOANG")
                )),
                DividerUiModel(2),
                InfoContainerUiModel(R.string.mvc_detail_voucher_benefit, listOf(
                        SubInfoItemUiModel(R.string.mvc_type_of_voucher, "Cashback"),
                        SubInfoItemUiModel(R.string.mvc_detail_discount_amount, "10%"),
                        SubInfoItemUiModel(R.string.mvc_detail_quota, "100"),
                        SubInfoItemUiModel(R.string.mvc_detail_terms, "Min. pembelian Rp50.000 - <br>Max. potongan Rp20.000")
                )),
                DividerUiModel(2),
                InfoContainerUiModel(R.string.mvc_detail_voucher_period, listOf(
                        SubInfoItemUiModel(R.string.mvc_period, "17 Jan 2020, 08:30 WIB - <br>17 Feb 2020, 22:00 WIB")
                )),
                DividerUiModel(8),
                FooterButtonUiModel("Bagikan Voucher", ""),
                FooterUiModel(
                        context?.getString(R.string.mvc_detail_ticker_stop_promo).toBlankOrString(),
                        context?.getString(R.string.mvc_detail_ticker_here).toBlankOrString())
        )
        renderList(dummy)
    }
}