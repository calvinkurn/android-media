package com.tokopedia.vouchercreation.create.view.fragment.step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.PostVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.detail.model.*
import com.tokopedia.vouchercreation.detail.view.fragment.BaseDetailFragment

class ReviewVoucherFragment(private val getVoucherReviewUiModel: () -> VoucherReviewUiModel,
                            private val getToken: () -> String,
                            private val getIgPostVoucherUrl: () -> String) : BaseDetailFragment() {

    companion object {
        @JvmStatic
        fun createInstance(getVoucherReviewUiModel: () -> VoucherReviewUiModel,
                           getToken: () -> String,
                           getIgPostVoucherUrl: () -> String): ReviewVoucherFragment = ReviewVoucherFragment(getVoucherReviewUiModel, getToken, getIgPostVoucherUrl)

        private const val VOUCHER_INFO_DATA_KEY = "voucher_info"
        private const val VOUCHER_BENEFIT_DATA_KEY = "voucher_benefit"
        private const val PERIOD_DATA_KEY = "period"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_list, container, false)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun onResume() {
        super.onResume()
        renderReviewInformation(getVoucherReviewUiModel())
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun loadData(page: Int) {}

    override fun showDownloadBottomSheet() {}

    private fun renderReviewInformation(voucherReviewUiModel: VoucherReviewUiModel) {
        voucherReviewUiModel.run {
            val reviewInfoList = listOf(
                    getVoucherPreviewSection(),
                    getVoucherInfoSection(targetType, voucherName, promoCode),
                    DividerUiModel(DividerUiModel.THIN),
                    getVoucherBenefitSection(voucherType, minPurchase, voucherQuota),
                    getExpenseEstimationSection(voucherType.value, voucherQuota),
                    DividerUiModel(DividerUiModel.THIN),
                    getPeriodSection(),
                    DividerUiModel(DividerUiModel.THICK),
                    FooterButtonUiModel(context?.getString(R.string.mvc_add_voucher).toBlankOrString(), ""),
                    FooterUiModel(
                            context?.getString(R.string.mvc_review_agreement).toBlankOrString(),
                            context?.getString(R.string.mvc_review_terms).toBlankOrString())
            )
            renderList(reviewInfoList)
        }
    }

    private fun getVoucherPreviewSection() : PostVoucherUiModel {
        return PostVoucherUiModel(
                VoucherImageType.FreeDelivery(0),
                "harusnyadaristep1",
                "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2020/5/6/1479278/1479278_3bab5e93-003a-4819-a68a-421f69224a59.jpg",
                "Ini Harusnya dari Backend",
                "CODETEST",
                "10 Mei 2020",
                getIgPostVoucherUrl())
    }

    private fun getVoucherInfoSection(@VoucherTargetType targetType: Int,
                                      voucherName: String,
                                      promoCode: String) : InfoContainerUiModel {
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
                hasCta = true)
    }

    private fun getVoucherBenefitSection(voucherType: VoucherImageType,
                                         minPurchase: Int,
                                         voucherQuota: Int) : InfoContainerUiModel {
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
                hasCta = true
        )
    }

    private fun getExpenseEstimationSection(benefitMax: Int,
                                            voucherQuota: Int) : VoucherTickerUiModel {
        val expenseEstimation = benefitMax * voucherQuota
        val formattedExpenseEstimation = CurrencyFormatHelper.convertToRupiah(expenseEstimation.toString())
        val expenseEstimationString = String.format(context?.getString(R.string.mvc_rp_value).toBlankOrString(), formattedExpenseEstimation)
        return VoucherTickerUiModel(
                nominalStr = expenseEstimationString,
                hasTooltip = true
        )
    }

    private fun getPeriodSection() : InfoContainerUiModel {
        val dummyDate = "01 Jun 2020, 12:00 WIB - <br>30 Jun 2020, 12:00 WIB"

        val subInfoList = listOf(
                SubInfoItemUiModel(R.string.mvc_period, dummyDate)
        )

        return InfoContainerUiModel(
                titleRes = R.string.mvc_detail_voucher_period,
                informationList = subInfoList,
                dataKey = PERIOD_DATA_KEY,
                hasCta = true
        )
    }

}