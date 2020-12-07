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
import com.tokopedia.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferDescriptionAdapter
import com.tokopedia.paylater.presentation.widget.PayLaterFaqBottomSheet
import com.tokopedia.paylater.presentation.widget.PayLaterRegisterBottomSheet
import kotlinx.android.synthetic.main.fragment_paylater_cards_info.*

class PaymentOptionsFragment: Fragment() {

    private var responseData: PayLaterItemProductData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        responseData = arguments?.getParcelable("payLaterData")
        return inflater.inflate(R.layout.fragment_paylater_cards_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPaymentDesciption.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = PayLaterOfferDescriptionAdapter(responseData?.partnerBenefitList?: ArrayList())
        }
        initListener()
        setData()
    }

    private fun setData() {
        if (!responseData?.subHeader.isNullOrEmpty()) {
            tvSubTitlePaylaterPartner.text = responseData?.subHeader
            tvSubTitlePaylaterPartner.visible()
        }

        ImageHandler.loadImage(context,
                ivPaylaterPartner,
                responseData?.partnerImgLightUrl ?: "https://ecs7.tokopedia.net/assets-fintech-frontend/pdp/kredivo/kredivo.png",
                R.drawable.ic_loading_image)
    }

    private fun initListener() {
        btnHowToUse.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(PayLaterRegisterBottomSheet.REGISTER_DATA, responseData?.partnerUsageDetails)
            PayLaterRegisterBottomSheet.show(bundle, childFragmentManager)
        }

        btnSeeMore.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(PayLaterFaqBottomSheet.FAQ_SEE_MORE_URL, responseData?.partnerFaqUrl)
            bundle.putParcelableArrayList(PayLaterFaqBottomSheet.FAQ_DATA, responseData?.partnerFaqList)
            PayLaterFaqBottomSheet.show(bundle, childFragmentManager)
        }
    }

    companion object {
        fun newInstance(bundle: Bundle) : PaymentOptionsFragment {
            return PaymentOptionsFragment().apply {
                arguments = bundle
            }
        }
    }
}