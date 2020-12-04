package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.OfferListResponse
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferDescriptionAdapter
import com.tokopedia.paylater.presentation.widget.PayLaterFaqBottomSheet
import com.tokopedia.paylater.presentation.widget.PayLaterRegisterBottomSheet
import kotlinx.android.synthetic.main.fragment_paylater_cards_info.*

class PaymentOptionsFragment: Fragment() {
    private var responseData: OfferListResponse = OfferListResponse()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        responseData = arguments?.getParcelable("dummy") ?: OfferListResponse()
        return inflater.inflate(R.layout.fragment_paylater_cards_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPaymentDesciption.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = PayLaterOfferDescriptionAdapter(responseData.offerItemList)
        }
        initListener()
        ImageHandler.loadImage(context,
                ivPaylaterPartner,
                "https://ecs7.tokopedia.net/assets-fintech-frontend/pdp/kredivo/kredivo.png",
                R.drawable.ic_loading_image)
    }

    private fun initListener() {
        btnHowToUse.setOnClickListener {
            PayLaterRegisterBottomSheet.show(Bundle(), childFragmentManager)
        }

        btnSeeMore.setOnClickListener {
            PayLaterFaqBottomSheet.show(Bundle(), childFragmentManager)
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