package com.tokopedia.pdpsimulation.paylater.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics.Companion.PAY_LATER_REGISTER_ACTION
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics.Companion.PAY_LATER_USAGE_ACTION
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterPartnerTypeMapper
import com.tokopedia.pdpsimulation.paylater.mapper.UsageStepsPartnerType
import com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter.PayLaterOfferDescriptionAdapter
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterFaqBottomSheet
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.fragment_paylater_cards_info.*

class PayLaterPaymentOptionsFragment : Fragment() {

    private var isUsageType: Boolean = false
    private val responseData by lazy {
        arguments?.getParcelable<PayLaterItemProductData>(PAY_LATER_PARTNER_DATA)
    }
    private val applicationStatusData: PayLaterApplicationDetail? by lazy {
        arguments?.getParcelable(PAY_LATER_APPLICATION_DATA)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_paylater_cards_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPaymentDesciption.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            val orderedBenefitList = responseData?.partnerBenefitList?.sortedWith(compareBy { it.isHighlighted == false })
                    ?: arrayListOf()
            adapter = PayLaterOfferDescriptionAdapter(orderedBenefitList)
        }
        initListener()
        setData()
    }

    private fun initListener() {
        btnHowToUse.setOnClickListener {
            openActionBottomSheet()
        }

        btnSeeMore.setOnClickListener {
            openFaqBottomSheet()
        }
    }

    private fun openActionBottomSheet() {
        val bundle = Bundle()
        bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, responseData)
        (parentFragment as PayLaterOffersFragment).pdpSimulationCallback?.let {
            it.sendAnalytics(PdpSimulationEvent.PayLater.PayLaterProductImpressionEvent(
                    responseData?.partnerName ?: "",
                    if (isUsageType) PAY_LATER_USAGE_ACTION else PAY_LATER_REGISTER_ACTION))

            it.openBottomSheet(
                    bundle, PayLaterActionStepsBottomSheet::class.java)
        }
    }

    private fun openFaqBottomSheet() {
        val bundle = Bundle()
        bundle.putString(PayLaterFaqBottomSheet.FAQ_SEE_MORE_URL, responseData?.partnerFaqUrl)
        bundle.putParcelableArrayList(PayLaterFaqBottomSheet.FAQ_DATA, responseData?.partnerFaqList)
        (parentFragment as PayLaterOffersFragment).pdpSimulationCallback?.let {
            it.sendAnalytics(PdpSimulationEvent.PayLater.PayLaterProductImpressionEvent(
                    responseData?.partnerName ?: "",
                    btnSeeMore.text.toString()
            ))
            it.openBottomSheet(
                    bundle, PayLaterFaqBottomSheet::class.java)
        }
    }

    private fun setData() {
        responseData?.let { data ->
            when (PayLaterPartnerTypeMapper.getPayLaterPartnerType(data, applicationStatusData)) {
                is UsageStepsPartnerType -> {
                    isUsageType = true
                    btnHowToUse.text = context?.getString(R.string.pay_later_see_how_to_use)
                }
                else -> {
                    isUsageType = false
                    btnHowToUse.text = context?.getString(R.string.pay_later_see_how_to_register)
                }
            }

            tvTitlePaymentPartner.text = data.partnerName

            applicationStatusData?.let {
                setSubHeaderText(it, data.subHeader)
                setLabelData(it)
            } ?: setSubHeaderText(null, data.subHeader)
            setPartnerImage(data)
        }
    }

    /*
    * if sub header from application state api is non empty set it otherwise
    * set pay later product detail response in sub header text
    * */
    private fun setSubHeaderText(detail: PayLaterApplicationDetail?, productDetailSubHeader: String?) {
        tvSubTitlePaylaterPartner.visible()
        if (!detail?.payLaterStatusContent?.verificationContentSubHeader.isNullOrEmpty()) {
            tvSubTitlePaylaterPartner.text = detail?.payLaterStatusContent?.verificationContentSubHeader?.parseAsHtml()
        } else if (!productDetailSubHeader.isNullOrEmpty()) {
            tvSubTitlePaylaterPartner.text = productDetailSubHeader
        } else tvSubTitlePaylaterPartner.gone()
    }

    private fun setPartnerImage(data: PayLaterItemProductData) {
        val imageUrl: String? = if (context.isDarkMode())
            data.partnerImgDarkUrl
        else data.partnerImgLightUrl

        if (!imageUrl.isNullOrEmpty())
            ivPaylaterPartner.loadImage(imageUrl)
    }

    private fun setLabelData(payLaterApplicationDetail: PayLaterApplicationDetail) {
        context?.let {
            payLaterApplicationDetail.payLaterApplicationStatusLabelStringId.also { resId ->
                if (resId != 0) {
                    tvPaylaterPartnerStatus.text = it.getString(resId)
                    tvPaylaterPartnerStatus.visible()
                    tvPaylaterPartnerStatus.setLabelType(payLaterApplicationDetail.payLaterApplicationStatusLabelType)
                } else {
                    tvPaylaterPartnerStatus.gone()
                }
            }
        }
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