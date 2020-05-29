package com.tokopedia.vouchercreation.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.bottmsheet.description.DescriptionBottomSheet
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.detail.model.InfoContainerUiModel
import com.tokopedia.vouchercreation.detail.model.SubInfoItemUiModel
import com.tokopedia.vouchercreation.detail.model.VoucherDetailUiModel
import com.tokopedia.vouchercreation.detail.model.VoucherTickerUiModel
import com.tokopedia.vouchercreation.detail.view.VoucherDetailListener
import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactoryImpl
import kotlinx.android.synthetic.main.fragment_mvc_voucher_detail.view.*
import java.text.SimpleDateFormat

/**
 * Created By @ilhamsuaib on 09/05/20
 */

abstract class BaseDetailFragment : BaseListFragment<VoucherDetailUiModel, VoucherDetailAdapterFactoryImpl>(),
        VoucherDetailListener {

    companion object {
        const val VOUCHER_INFO_DATA_KEY = "voucher_info"
        const val VOUCHER_BENEFIT_DATA_KEY = "voucher_benefit"
        const val PERIOD_DATA_KEY = "period"

        private const val DISPLAYED_DATE_FORMAT = "dd MMM yyyy"
        private const val RAW_DATE_FORMAT = "yyyy-MM-dd"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mvc_voucher_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionBar()
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvMvcVoucherDetail

    override fun getAdapterTypeFactory(): VoucherDetailAdapterFactoryImpl {
        return VoucherDetailAdapterFactoryImpl(this)
    }

    override fun onItemClicked(t: VoucherDetailUiModel?) {

    }

    override fun onFooterButtonClickListener() {

    }

    override fun showTipsAndTrickBottomSheet() {

    }

    override fun onFooterCtaTextClickListener() {

    }

    override fun onInfoContainerCtaClick(dataKey: String) {

    }

    override fun onTickerClicked() {

    }

    override fun showDescriptionBottomSheet(title: String, content: String) {
        if (!isAdded) return
        DescriptionBottomSheet(context ?: return)
                .show(title, content, childFragmentManager)
    }

    private fun setupActionBar() = view?.run {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(toolbarMvcVoucherDetail)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private val rawDateFormatter by lazy {
        SimpleDateFormat(RAW_DATE_FORMAT, LocaleUtils.getIDLocale())
    }
    private val displayedDateFormatter by lazy {
        SimpleDateFormat(DISPLAYED_DATE_FORMAT, LocaleUtils.getIDLocale())
    }

    protected fun getVoucherInfoSection(@VoucherTargetType targetType: Int,
                                        voucherName: String,
                                        promoCode: String,
                                        hasCta: Boolean = false) : InfoContainerUiModel {
        val targetTypeStringRes = when(targetType) {
            VoucherTargetType.PUBLIC -> R.string.mvc_create_target_public
            VoucherTargetType.PRIVATE -> R.string.mvc_create_target_private
            else -> R.string.mvc_create_target_public
        }
        val targetTypeString = context?.getString(targetTypeStringRes).toBlankOrString()

        val subInfoList = mutableListOf(
                SubInfoItemUiModel(R.string.mvc_voucher_target, targetTypeString),
                SubInfoItemUiModel(R.string.mvc_detail_voucher_name, voucherName)
        )
        if (targetType == VoucherTargetType.PRIVATE) {
            subInfoList.add(
                    SubInfoItemUiModel(R.string.mvc_detail_promo_code, promoCode)
            )
        }
        return InfoContainerUiModel(
                titleRes = R.string.mvc_detail_voucher_info,
                informationList = subInfoList,
                dataKey = VOUCHER_INFO_DATA_KEY,
                hasCta = hasCta)
    }

    protected fun getVoucherBenefitSection(voucherType: VoucherImageType,
                                           minPurchase: Int,
                                           voucherQuota: Int,
                                           hasCta: Boolean = false) : InfoContainerUiModel {
        val voucherTypeRes =
                when(voucherType) {
                    is VoucherImageType.FreeDelivery -> R.string.mvc_free_shipping
                    is VoucherImageType.Rupiah -> R.string.mvc_cashback
                    is VoucherImageType.Percentage -> R.string.mvc_cashback
                }
        val voucherTypeString = context?.getString(voucherTypeRes).toBlankOrString()
        val minPurchaseString = CurrencyFormatHelper.convertToRupiah(minPurchase.toString())
        val maxBenefitString = CurrencyFormatHelper.convertToRupiah(voucherType.value.toString())
        val quotaString = voucherQuota.toString()
        val purchaseTermString = String.format(context?.getString(R.string.mvc_detail_purchase_terms).toBlankOrString(), minPurchaseString, maxBenefitString)

        val subInfoList = mutableListOf(
                SubInfoItemUiModel(R.string.mvc_type_of_voucher, voucherTypeString)
        )
        if (voucherType is VoucherImageType.Percentage) {
            val discountPercentString = voucherType.percentage.toString()
            val discountAmountString = String.format(context?.getString(R.string.mvc_percent_value).toBlankOrString(), discountPercentString)
            subInfoList.add(SubInfoItemUiModel(R.string.mvc_detail_discount_amount, discountAmountString))
        }

        subInfoList.add(SubInfoItemUiModel(R.string.mvc_detail_quota, quotaString))
        subInfoList.add(SubInfoItemUiModel(R.string.mvc_detail_terms, purchaseTermString))

        return InfoContainerUiModel(
                titleRes = R.string.mvc_detail_voucher_benefit,
                informationList = subInfoList,
                dataKey = VOUCHER_BENEFIT_DATA_KEY,
                hasCta = hasCta
        )
    }

    protected fun getExpenseEstimationSection(benefitMax: Int,
                                              voucherQuota: Int) : VoucherTickerUiModel {
        val expenseEstimation = benefitMax * voucherQuota
        val formattedExpenseEstimation = CurrencyFormatHelper.convertToRupiah(expenseEstimation.toString())
        val expenseEstimationString = String.format(context?.getString(R.string.mvc_rp_value).toBlankOrString(), formattedExpenseEstimation)
        return VoucherTickerUiModel(
                nominalStr = expenseEstimationString,
                hasTooltip = true
        )
    }

    protected fun getPeriodSection(dateString: String, hasCta: Boolean = false) : InfoContainerUiModel {
        val subInfoList = listOf(
                SubInfoItemUiModel(R.string.mvc_period, dateString)
        )

        return InfoContainerUiModel(
                titleRes = R.string.mvc_detail_voucher_period,
                informationList = subInfoList,
                dataKey = PERIOD_DATA_KEY,
                hasCta = hasCta
        )
    }

    protected fun getDisplayedDateString(startDate: String,
                                       startHour: String,
                                       endDate: String,
                                       endHour: String): String {
        val formattedStartDate = startDate.formatToDisplayedDate()
        val formattedEndDate = endDate.formatToDisplayedDate()
        return String.format(context?.getString(R.string.mvc_displayed_date_format).toBlankOrString(),
                formattedStartDate, startHour, formattedEndDate, endHour)
    }

    protected fun getDisplayedDateString(startDate: String,
                                       endDate: String): String {
        val formattedStartDate = startDate.formatToDisplayedDate()
        val formattedEndDate = endDate.formatToDisplayedDate()
        return String.format(context?.getString(R.string.mvc_displayed_date_post_format).toBlankOrString(),
                formattedStartDate, formattedEndDate)
    }

    private fun String.formatToDisplayedDate(): String {
        try {
            return displayedDateFormatter.format(rawDateFormatter.parse(this))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
    }
}