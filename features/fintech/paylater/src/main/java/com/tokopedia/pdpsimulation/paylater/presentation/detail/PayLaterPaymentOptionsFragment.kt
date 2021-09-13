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
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.Benefit
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.GatewayDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter.PayLaterOfferDescriptionAdapter
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterFaqBottomSheet
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.fragment_paylater_cards_info.*

class PayLaterPaymentOptionsFragment : Fragment() {

    private val responseData by lazy {
        arguments?.getParcelable<Detail>(PAY_LATER_PARTNER_DATA)
    }
    private val applicationStatusData: PayLaterApplicationDetail? by lazy {
        arguments?.getParcelable(PAY_LATER_APPLICATION_DATA)
    }

    private var buttonStatus: RedirectionType? = null
    private var gatewayType: GatewayStatusType? = null
    private var urlToRedirect: String = ""

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

    private fun initListener() {
        btnHowToUse.setOnClickListener {
            buttonStatus?.let {
                when (it) {
                    RedirectionType.HowToDetail -> {
                        openActionBottomSheet()
                    }
                    RedirectionType.RedirectionWebView -> {
                        if (!urlToRedirect.isNullOrEmpty())
                            RouteManager.route(
                                activity,
                                ApplinkConstInternalGlobal.WEBVIEW,
                                urlToRedirect
                            )
                    }

                }

            }

        }

        faqList.setOnClickListener {
            openFaqBottomSheet()
        }
    }

    private fun openActionBottomSheet() {
        val bundle = Bundle()
        bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, responseData)
        (parentFragment as PayLaterOffersFragment).pdpSimulationCallback?.let {
//            it.sendAnalytics(PdpSimulationEvent.PayLater.PayLaterProductImpressionEvent(
//                    responseData?.partnerName ?: "",
//                    if (isUsageType) PAY_LATER_USAGE_ACTION else PAY_LATER_REGISTER_ACTION))

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
//            it.sendAnalytics(PdpSimulationEvent.PayLater.PayLaterProductImpressionEvent(
//                    responseData?.partnerName ?: "",
//                    btnSeeMore.text.toString()
//            ))
            it.openBottomSheet(
                bundle, PayLaterFaqBottomSheet::class.java
            )
        }
    }

    private fun setData() {
        responseData?.let { data ->
            tvTitlePaymentPartner.text = data.gateway_detail?.name
            whyText.text = resources.getString(R.string.whyGateway)+" ${data.gateway_detail?.name?:""}"
            if(data.tenure != 1 && data.tenure !=0)
                duration.text = resources.getString(R.string.cicilian) +" ${data.tenure}x"

            interestText.text = "Interest(${(data.interest_pct?:0)}%)"
            gatewayType =
                if (data.activation_status == 2 || data.activation_status == 10 || data.activation_status == 9)
                    GatewayStatusType.Rejected
                else if (data.activation_status == 1)
                    GatewayStatusType.Processing
                else
                    GatewayStatusType.Accepted

            updateSubHeader(gatewayType, data.gateway_detail?.subheader ?: "")

            tvSubTitlePaylaterPartner.text = data.gateway_detail?.subheader
            urlToRedirect = data.cta?.android_url ?: ""
            data.cta?.cta_type?.let {
                buttonStatus = when (it) {
                    in 1..2 -> RedirectionType.RedirectionWebView
                    in 3..4 -> RedirectionType.HowToDetail
                    else -> RedirectionType.NonClickable
                }
            }
            interestAmount.text = data.total_interest_ceil?.let { interest_ceil ->
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    interest_ceil, false
                )
            }
            serviceFeeAmount.text =
                data.total_fee_ceil?.let { total_fee_ceil ->
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        total_fee_ceil,
                        false
                    )
                }
             data.installment_per_month_ceil?.let { montlyInstallment ->
                 if(data.tenure != 1 && data.tenure !=0)
                 {
                     totalAmount.text = "${CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    montlyInstallment, false
                )}/${resources.getString(R.string.monthText)}"
                 }
                 else
                 {
                     totalAmount.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                         montlyInstallment, false
                     )
                 }

            }
            if (!data.cta?.name.isNullOrEmpty())
                btnHowToUse.text = data.cta?.name
            else
                btnHowToUse.gone()

            if (data.is_recommended == true)
                recommendedImg.visible()
            else
                recommendedImg.gone()

            if (data.cta?.button_color.equals("green", true)) {
                btnHowToUse.buttonVariant = UnifyButton.Variant.FILLED
            } else {
                btnHowToUse.buttonVariant = UnifyButton.Variant.GHOST
            }
            data.gateway_detail?.let { gatewayDetail ->
                setPartnerImage(gatewayDetail)
            }

            if (data.disableDetail?.status == true) {
                disableVisibilityGroup.gone()
                interestText.text = data.disableDetail.description?:""
                simulasiHeading.text = data.disableDetail.header?:""
            }

        }
    }

    private fun updateSubHeader(gatewayType: GatewayStatusType?, subheader: String) {
        when (gatewayType) {
            GatewayStatusType.Processing -> {
                tvSubTitlePaylaterPartner.text = context?.getString(R.string.gateway_processing)
                tvSubTitlePaylaterPartner.setBackgroundColor(resources.getColor(R.color.Unify_Y200))
                tvSubTitlePaylaterPartner.setTextColor(resources.getColor(R.color.Unify_Y500))
            }
            GatewayStatusType.Rejected -> {
                tvSubTitlePaylaterPartner.text = context?.getString(R.string.rejected_gateway)
                tvSubTitlePaylaterPartner.setTextColor(resources.getColor(R.color.Unify_R500))
                tvSubTitlePaylaterPartner.setBackgroundColor(resources.getColor(R.color.Unify_R100))
            }
            else -> {
                tvSubTitlePaylaterPartner.text = subheader
                tvSubTitlePaylaterPartner.setTextColor(resources.getColor(R.color.Unify_N700))
                tvSubTitlePaylaterPartner.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }


    private fun setPartnerImage(data: GatewayDetail) {
        val imageUrl: String? = if (context.isDarkMode())
            data.img_dark_url
        else data.img_light_url

        if (!imageUrl.isNullOrEmpty())
            ivPaylaterPartner.loadImage(imageUrl)
    }


    companion object {
        const val PAY_LATER_PARTNER_DATA = "payLaterPartnerData"
        const val PAY_LATER_APPLICATION_DATA = "payLaterApplicationData"
        fun newInstance(bundle: Bundle): PayLaterPaymentOptionsFragment {
            return PayLaterPaymentOptionsFragment().apply {
                arguments = bundle
            }
        }
    }
}

enum class RedirectionType {
    HowToDetail, RedirectionWebView, NonClickable
}

enum class GatewayStatusType {
    Processing, Rejected, Accepted
}