package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.data.mapper.PayLaterPartnerTypeMapper
import com.tokopedia.paylater.data.mapper.RegisterStepsPartnerType
import com.tokopedia.paylater.data.mapper.UsageStepsPartnerType
import com.tokopedia.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferDescriptionAdapter
import com.tokopedia.paylater.presentation.widget.PayLaterFaqBottomSheet
import com.tokopedia.paylater.presentation.widget.PayLaterActionStepsBottomSheet
import kotlinx.android.synthetic.main.fragment_paylater_cards_info.*

class PaymentOptionsFragment : Fragment() {

    private val responseData by lazy {
        arguments?.getParcelable<PayLaterItemProductData>(PAY_LATER_DATA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_paylater_cards_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPaymentDesciption.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            val orderedBenefitList = responseData?.partnerBenefitList?.sortedWith(compareBy { !it.isHighlighted })
                    ?: ArrayList()
            adapter = PayLaterOfferDescriptionAdapter(orderedBenefitList)
        }
        initListener()
        setData()
    }

    private fun setData() {
        responseData?.subHeader?.let {
            tvSubTitlePaylaterPartner.text = it
            tvSubTitlePaylaterPartner.visible()
        }

        ImageHandler.loadImage(context,
                ivPaylaterPartner,
                responseData?.partnerImgLightUrl
                        ?: "https://ecs7.tokopedia.net/assets-fintech-frontend/pdp/kredivo/kredivo.png",
                R.drawable.ic_loading_image)
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

    private fun setBundleData(bundle: Bundle) {
        responseData?.let { data ->
            bundle.putString(PayLaterActionStepsBottomSheet.ACTION_URL, data.applyWebUrl)
            when (PayLaterPartnerTypeMapper.getPayLaterPartnerType(data)) {
                is RegisterStepsPartnerType -> {
                    bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, data.partnerApplyDetails)
                    bundle.putString(PayLaterActionStepsBottomSheet.ACTION_TITLE, "Cara daftar ${data.partnerName}")
                }
                is UsageStepsPartnerType -> {
                    bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, data.partnerUsageDetails)
                    bundle.putString(PayLaterActionStepsBottomSheet.ACTION_TITLE, "Cara gunakan ${data.partnerName}")

                }
            }
        }
    }

    companion object {
        const val PAY_LATER_DATA = "payLaterData"
        fun newInstance(bundle: Bundle): PaymentOptionsFragment {
            return PaymentOptionsFragment().apply {
                arguments = bundle
            }
        }
    }
}