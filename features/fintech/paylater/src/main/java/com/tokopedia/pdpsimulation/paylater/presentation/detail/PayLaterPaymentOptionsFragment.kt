package com.tokopedia.pdpsimulation.paylater.presentation.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.common.utils.Utils
import com.tokopedia.pdpsimulation.paylater.domain.model.Benefit
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.GatewayDetail
import com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter.PayLaterOfferDescriptionAdapter
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterAdditionalFeeInfo
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterFaqBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterTokopediaGopayBottomsheet
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.fragment_paylater_cards_info.*

class PayLaterPaymentOptionsFragment : Fragment() {

    private val responseData by lazy {
        arguments?.getParcelable<Detail>(PAY_LATER_PARTNER_DATA)
    }

    private val position by lazy {
        arguments?.getInt(PAYLATER_PARTNER_POSITION, 0)
    }


    private var buttonStatus: RedirectionType? = null
    private var gatewayType: GatewayStatusType? = null
    private var urlToRedirect: String = ""
    private var partnerName: String? = ""
    private var tenure: Int? = 0
    private var montlyInstallment: Double? = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_paylater_cards_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateHighLightList()
        initListener()
        setData()
    }





    private fun updateHighLightList() {
        rvPaymentDesciption.apply {

            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            val orderedBenefitList: MutableList<Benefit?> = ArrayList()
            if (!responseData?.gateway_detail?.benefit.isNullOrEmpty()) {
                for (i in 0 until (responseData?.gateway_detail?.benefit?.size ?: 0)) {
                    if (responseData?.gateway_detail?.benefit?.get(i)?.is_highlight == true) {
                        responseData?.gateway_detail?.benefit?.let { benefitList ->
                            orderedBenefitList.add(benefitList[i])
                        }
                    }
                }
                adapter = PayLaterOfferDescriptionAdapter(orderedBenefitList)
            }


        }
    }

    private fun initListener() {
        additionalFeeDetail.setOnClickListener {
            openAdditionalPaymentBottomSheet()
        }
        btnHowToUse.setOnClickListener {
            buttonStatus?.let {
                when (it) {
                    RedirectionType.HowToDetail ->
                        openActionBottomSheet()

                    RedirectionType.RedirectionWebView -> {
                        if (!urlToRedirect.isNullOrEmpty()) {
                            (parentFragment as PayLaterOffersFragment).pdpSimulationCallback?.let { pdpCallBack ->
                                pdpCallBack.setViewModelData(
                                    Utils.UpdateViewModelVariable.RefreshType,
                                    true
                                )
                                pdpCallBack.setViewModelData(
                                    Utils.UpdateViewModelVariable.PartnerPosition,
                                    position ?: 0
                                )
                            }

                            RouteManager.route(
                                activity,
                                ApplinkConstInternalGlobal.WEBVIEW,
                                urlToRedirect
                            )
                        }

                    }

                    RedirectionType.RedirectionApp -> {
                        if (urlToRedirect.isNotEmpty()) {
                            RouteManager.route(activity, urlToRedirect)
                        }
                    }
                    RedirectionType.NonClickable -> {
                        btnHowToUse.isClickable = false
                    }
                    RedirectionType.GopayBottomSheet -> {
                        openGopayBottomSheet()
                    }
                }

            }

        }

        faqList.setOnClickListener {
            openFaqBottomSheet()
        }
    }

    private fun openAdditionalPaymentBottomSheet() {
        val bundle = Bundle()
        (parentFragment as PayLaterOffersFragment).pdpSimulationCallback?.let {
            it.openBottomSheet(bundle, PayLaterAdditionalFeeInfo::class.java)
        }
    }

    private fun openGopayBottomSheet() {
        val bundle = Bundle()
        bundle.putParcelable(
            PayLaterTokopediaGopayBottomsheet.GOPAY_BOTTOMSHEET_DETAIL,
            responseData?.cta
        )
        bundle.putInt(PAYLATER_PARTNER_POSITION, position ?: 0)
        (parentFragment as PayLaterOffersFragment).pdpSimulationCallback?.let {
            it.openBottomSheet(bundle, PayLaterTokopediaGopayBottomsheet::class.java)
        }

    }


    private fun openActionBottomSheet() {
        val bundle = Bundle()
        bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, responseData)
        (parentFragment as PayLaterOffersFragment).pdpSimulationCallback?.let {
            it.sendAnalytics(
                PdpSimulationEvent.PayLater.ClickCardButton(
                    responseData?.tenure ?: 0,
                    responseData?.gateway_detail?.name ?: "",
                    responseData?.cta?.name ?: "",
                    responseData?.cta?.android_url ?: ""
                )
            )
            bundle.putString(PARTER_NAME, partnerName)
            bundle.putInt(TENURE, tenure ?: 0)
            bundle.putDouble(EMI_AMOUNT, montlyInstallment ?: 0.0)
            it.openBottomSheet(
                bundle, PayLaterActionStepsBottomSheet::class.java
            )
        }
    }


    private fun openFaqBottomSheet() {
        val bundle = Bundle()
        bundle.putString(
            PayLaterFaqBottomSheet.FAQ_SEE_MORE_URL,
            responseData?.gateway_detail?.faq_url
        )
        bundle.putParcelableArrayList(
            PayLaterFaqBottomSheet.FAQ_DATA,
            responseData?.gateway_detail?.faq as ArrayList
        )
        (parentFragment as PayLaterOffersFragment).pdpSimulationCallback?.let {

            it.sendAnalytics(
                PdpSimulationEvent.PayLater.FaqOptionClicked(
                    responseData?.gateway_detail?.name ?: "", responseData?.tenure ?: 0
                )
            )
            bundle.putString("partnerName", partnerName)
            bundle.putInt("tenure", tenure ?: 0)
            it.openBottomSheet(
                bundle, PayLaterFaqBottomSheet::class.java
            )
        }
    }

    /**
     * This method set values to all view from the api success response
     */
    private fun setData() {
        responseData?.let { data ->
            updateHeaderPartnerCard(data)
            updateButtonDetail(data)
            updateRepaymentDetail(data)
            updateAdditionalPartnerDetail(data)
            if (data.disableDetail?.status == true)
                setUIIfDisable(data)
        }
    }

    /**
     * THis method update the recommended text and why text
     * @param data this is the partner response
     */
    @SuppressLint("SetTextI18n")
    private fun updateAdditionalPartnerDetail(data: Detail) {
        data.installationDescription?.let {
            instructionDetail.text = it.parseAsHtml()
        }
        whyText.text =
            resources.getString(R.string.pay_later_partner_why_gateway) + " ${data.gateway_detail?.name ?: ""}?"
        if (data.is_recommended == true) {
            recommendationText.visible()
            recommendationText.text = data.is_recommended_string ?: ""
        } else {
            recommendationText.gone()
            paylaterPartnerCard.cardType = CardUnify.TYPE_SHADOW
        }
    }

    /**
     * This method is to show the detail of all repayment detail like interest , service fee etc
     * for the selected partner
     * @param data this the the base detail response
     */

    @SuppressLint("SetTextI18n")
    private fun updateRepaymentDetail(data: Detail) {
        if (data.tenure != PAY_LATER_BASE_TENURE)
            duration.text =
                resources.getString(R.string.pay_later_installment_text) + " ${data.tenure}x"

        interestText.text =
            "${resources.getString(R.string.pay_later_partner_interest)} (${(data.interest_pct ?: 0)}%)"



        interestAmount.text = data.total_interest_ceil?.let { interest_ceil ->
            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                interest_ceil, false
            )
        }
        serviceFeeAmount.text =
            data.total_with_provision_ceil?.let { total_provision_ceil ->
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    total_provision_ceil,
                    false
                )
            }
        data.installment_per_month_ceil?.let { montlyInstallment ->
            if (data.tenure != PAY_LATER_BASE_TENURE) {
                this.montlyInstallment = montlyInstallment
                totalAmount.text = "${
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        montlyInstallment, false
                    )
                }/${resources.getString(R.string.monthText)}"
            } else {
                totalAmount.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    montlyInstallment, false
                )
            }

        }

    }

    /**
     * THis method set the header for the partner brand
     * @param data this is the partner detail response
     */

    private fun updateHeaderPartnerCard(data: Detail) {
        tvTitlePaymentPartner.text = data.gateway_detail?.name ?: ""
        partnerName = data.gateway_detail?.name ?: ""
        tenure = data.tenure ?: 0
        if (!data.gateway_detail?.smallSubHeader.isNullOrEmpty()) {
            tvSmallSubTitlePaylaterPartner.visible()
            tvSmallSubTitlePaylaterPartner.text = data.gateway_detail?.smallSubHeader
        } else {
            tvSmallSubTitlePaylaterPartner.gone()
        }

        updateSubHeader(gatewayType, data.gateway_detail?.subheader ?: "")
        tvSubTitlePaylaterPartner.text = data.gateway_detail?.subheader ?: ""
        if (!data.serviceFeeInfo.isNullOrEmpty()) {
            serviceFeeInfoText.visible()
            serviceFeeInfoText.text = data.serviceFeeInfo
        } else {
            serviceFeeInfoText.gone()

        }

        data.gateway_detail?.let { gatewayDetail ->
            setPartnerImage(gatewayDetail)
        }

        gatewayType =
            when {
                rejectionList.contains(data.activation_status) -> GatewayStatusType.Rejected
                processiongList.contains(data.activation_status) -> GatewayStatusType.Processing
                else -> GatewayStatusType.Accepted
            }


    }


    /**
     * This is the method to update the button detail of the partner card
     * @param data this is the detail partner detail
     */

    private fun updateButtonDetail(data: Detail) {
        urlToRedirect = data.cta?.android_url ?: ""

        data.cta?.cta_type?.let {
            buttonStatus = when {
                buttonRedirectionWeb.contains(it) && data.cta.bottomSheet?.isShow == true ->
                    RedirectionType.GopayBottomSheet

                buttonRedirectionWeb.contains(it) && data.cta.bottomSheet?.isShow == false ->
                    RedirectionType.RedirectionWebView

                buttonRedirectApp.contains(it) ->
                    RedirectionType.RedirectionApp

                buttonRedirectionBottomSheet.contains(it) ->
                    RedirectionType.HowToDetail

                else ->
                    RedirectionType.NonClickable
            }
        }
        if (!data.cta?.name.isNullOrEmpty())
            btnHowToUse.text = data.cta?.name
        else
            btnHowToUse.gone()
        if (data.cta?.button_color.equals("filled", true)) {
            btnHowToUse.buttonVariant = UnifyButton.Variant.FILLED
        } else {
            btnHowToUse.buttonVariant = UnifyButton.Variant.GHOST
        }
    }


    /**
     * THis method is to set the UI if disabled is true
     * @param data this is the base detail response
     */

    private fun setUIIfDisable(data: Detail) {
        disableVisibilityGroup.gone()
        tvTitlePaymentPartner.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
        tvSubTitlePaylaterPartner.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N400_32))
        if (tvSmallSubTitlePaylaterPartner.isVisible)
            tvSmallSubTitlePaylaterPartner.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
        interestText.text = data.disableDetail?.description ?: ""
        simulasiHeading.text = data.disableDetail?.header ?: ""
    }

    /**
     * This method update the subheader of the partner card
     * @param gatewayType this is the gatewayType ENUM
     * @param subheader this is the string for displaying the subheader
     */
    private fun updateSubHeader(gatewayType: GatewayStatusType?, subheader: String) {
        when (gatewayType) {
            GatewayStatusType.Processing ->
                setProcessingHeaderUI()
            GatewayStatusType.Rejected ->
                setRejectedHeaderUI()
            else ->
                setDefaultHeaderUI(subheader)

        }
    }

    private fun setDefaultHeaderUI(subheader: String) {
        tvSubTitlePaylaterPartner.text = subheader
        tvSubTitlePaylaterPartner.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N400_96))
        tvSubTitlePaylaterPartner.setBackgroundColor(Color.TRANSPARENT)

    }

    private fun setRejectedHeaderUI() {
        tvSubTitlePaylaterPartner.text =
            context?.getString(R.string.pay_later_rejected_gateway)
        tvSubTitlePaylaterPartner.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_R500))
        tvSubTitlePaylaterPartner.setBackgroundColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_R100))

    }

    private fun setProcessingHeaderUI() {
        tvSubTitlePaylaterPartner.text =
            context?.getString(R.string.pay_later_gateway_processing)
        tvSubTitlePaylaterPartner.setBackgroundColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_Y200))
        tvSubTitlePaylaterPartner.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_Y500))
    }


    private fun setPartnerImage(data: GatewayDetail) {
        val imageUrl: String? = if (context.isDarkMode())
            data.img_dark_url
        else data.img_light_url

        if (!imageUrl.isNullOrEmpty())
            ivPaylaterPartner.setImageUrl(imageUrl)
    }


    companion object {
        const val PAY_LATER_PARTNER_DATA = "payLaterPartnerData"
        const val PAYLATER_PARTNER_POSITION = "partnerPosition"
        const val PAY_LATER_BASE_TENURE = 1
        const val PARTER_NAME = "partnerName"
        const val TENURE = " tenure"
        const val EMI_AMOUNT = "emiAmount"
        val rejectionList = listOf(2, 10, 9)
        val processiongList = listOf(1)
        val buttonRedirectionWeb = listOf(2)
        val buttonRedirectApp = listOf(1)
        val buttonRedirectionBottomSheet = listOf(3, 4)


        fun newInstance(bundle: Bundle): PayLaterPaymentOptionsFragment {
            return PayLaterPaymentOptionsFragment().apply {
                arguments = bundle
            }
        }
    }
}

enum class RedirectionType {
    HowToDetail, GopayBottomSheet, RedirectionWebView, NonClickable, RedirectionApp
}

enum class GatewayStatusType {
    Processing, Rejected, Accepted
}