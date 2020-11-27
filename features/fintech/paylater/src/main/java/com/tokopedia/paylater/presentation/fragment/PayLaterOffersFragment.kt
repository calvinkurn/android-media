package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.OfferDescriptionItem
import com.tokopedia.paylater.domain.model.OfferListResponse
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferPagerAdapter
import kotlinx.android.synthetic.main.fragment_paylater_offers.*

class PayLaterOffersFragment  : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paylater_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderTabAndViewPager()
    }

    private fun renderTabAndViewPager() {
        paymentOptionViewPager.adapter = getViewPagerAdapter()
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        val list: ArrayList<Fragment> = ArrayList()
        val dataList = populateDummyData()
        for (i in 0 until dataList.size) {
            val bundle = Bundle().apply {
                putParcelable("dummy", dataList[i])
            }
            list.add(PaymentOptionsFragment.newInstance(bundle))
        }
        return PayLaterOfferPagerAdapter(context!!, childFragmentManager, 0, list)
    }

    private fun populateDummyData(): ArrayList<OfferListResponse> {
        val data = ArrayList<OfferListResponse>()
        val descList = ArrayList<OfferDescriptionItem>()
        for(i in 1..10)
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