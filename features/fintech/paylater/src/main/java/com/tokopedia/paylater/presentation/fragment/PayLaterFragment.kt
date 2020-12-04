package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PayLaterComponent
import com.tokopedia.paylater.presentation.adapter.PayLaterPagerAdapter
import com.tokopedia.paylater.presentation.viewModel.PayLaterViewModel
import kotlinx.android.synthetic.main.fragment_paylater.*
import javax.inject.Inject


class PayLaterFragment : BaseDaggerFragment() {


    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    override fun getScreenName(): String {
        return "PayLater & Cicilan"
    }

    override fun initInjector() {
        getComponent(PayLaterComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paylater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderTabAndViewPager()
        payLaterViewModel.getPayLaterData()
    }

    private fun renderTabAndViewPager() {
        payLaterViewPager.adapter = getViewPagerAdapter()
        paylaterTabLayout.setupWithViewPager(payLaterViewPager)
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        val list = mutableListOf<Fragment>()
        list.add(SimulationFragment.newInstance())
        list.add(PayLaterOffersFragment.newInstance())
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