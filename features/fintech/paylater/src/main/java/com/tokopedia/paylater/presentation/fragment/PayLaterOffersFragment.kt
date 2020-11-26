package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.OfferDescriptionItem
import com.tokopedia.paylater.domain.model.OfferListResponse
import com.tokopedia.paylater.presentation.adapter.PayLaterOffersAdapter
import kotlinx.android.synthetic.main.fragment_paylater_offers.*

class PayLaterOffersFragment  : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paylater_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPaylaterCards.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = PayLaterOffersAdapter(populateDummyData())
        }
    }

    private fun populateDummyData(): ArrayList<OfferListResponse> {
        val data = ArrayList<OfferListResponse>()
        val descList = ArrayList<OfferDescriptionItem>()
        for(i in 1..8)
            descList.add(OfferDescriptionItem("Daftar 5 menit, waktu persetujuan maksimal 1 x 24 jam."))
        for (i in 1..4)
        data.add(OfferListResponse("Kredivo", descList))
        return data
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                PayLaterOffersFragment()
    }
}