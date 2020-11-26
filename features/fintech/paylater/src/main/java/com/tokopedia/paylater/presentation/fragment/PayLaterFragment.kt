package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.paylater.R
import com.tokopedia.paylater.presentation.adapter.PayLaterPagerAdapter
import kotlinx.android.synthetic.main.fragment_paylater.*


class PayLaterFragment : BaseDaggerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paylater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderTabAndViewPager()
    }

    override fun getScreenName(): String {
        return "PayLater & Cicilan"
    }

    override fun initInjector() {

    }

    private fun renderTabAndViewPager() {
        payLaterViewPager.adapter = getViewPagerAdapter()
        paylaterTabLayout.setupWithViewPager(payLaterViewPager)
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        val list = mutableListOf<Fragment>()
        list.add(SimulasiFragment.newInstance())
        list.add(PayLaterOffersFragment.newInstance())
        //list.add(PromoFragment.newInstance())
        val pagerAdapter =  PayLaterPagerAdapter(context!!, childFragmentManager, 0)
        pagerAdapter.setList(list)
        return pagerAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                PayLaterFragment()
    }
}