package com.tokopedia.pdpsimulation.paylater.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterPartnerTypeMapper
import com.tokopedia.pdpsimulation.paylater.mapper.RegisterStepsPartnerType
import com.tokopedia.pdpsimulation.paylater.mapper.UsageStepsPartnerType
import com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter.PayLaterOfferDescriptionAdapter
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterFaqBottomSheet
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.fragment_paylater_cards_info.*

class PayLaterPaymentOptionsFragment : Fragment() {

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
            val bundle = Bundle()
            setBundleData(bundle)
            PayLaterActionStepsBottomSheet.show(bundle, childFragmentManager)
        }

        btnSeeMore.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(PayLaterFaqBottomSheet.FAQ_SEE_MORE_URL, responseData?.partnerFaqUrl)
            bundle.putParcelableArrayList(PayLaterFaqBottomSheet.FAQ_DATA, responseData?.partnerFaqList)
            PayLaterFaqBottomSheet.show(bundle, childFragmentManager)
        }
    }

    private fun setData() {
        tvTitlePaymentPartner.text = responseData?.partnerName
        responseData?.subHeader?.let {
            tvSubTitlePaylaterPartner.text = it
            tvSubTitlePaylaterPartner.visible()
        }
        applicationStatusData?.let {
            setLabelData(it)
        }
        val imageUrl: String?
        if (context.isDarkMode())
            imageUrl = responseData?.partnerImgDarkUrl
        else imageUrl = responseData?.partnerImgLightUrl
        if (!imageUrl.isNullOrEmpty())
            ImageHandler.loadImage(context,
                    ivPaylaterPartner,
                    imageUrl,
                    com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
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

    private fun setBundleData(bundle: Bundle) {
        responseData?.let { data ->
            bundle.putString(PayLaterActionStepsBottomSheet.ACTION_URL, data.actionWebUrl)
            when (PayLaterPartnerTypeMapper.getPayLaterPartnerType(data, null)) {
                is RegisterStepsPartnerType -> {
                    bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, data.partnerApplyDetails)
                    bundle.putString(PayLaterActionStepsBottomSheet.ACTION_TITLE, "${context?.getString(R.string.pay_later_how_to_register)} ${data.partnerName}")
                }
                is UsageStepsPartnerType -> {
                    bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, data.partnerUsageDetails)
                    bundle.putString(PayLaterActionStepsBottomSheet.ACTION_TITLE, "${context?.getString(R.string.pay_later_how_to_use)} ${data.partnerName}")
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