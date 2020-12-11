package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PayLaterComponent
import com.tokopedia.paylater.domain.model.OfferDescriptionItem
import com.tokopedia.paylater.domain.model.OfferListResponse
import com.tokopedia.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.paylater.domain.model.PayLaterProductData
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferPagerAdapter
import com.tokopedia.paylater.presentation.viewModel.PayLaterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_paylater_offers.*
import javax.inject.Inject

class PayLaterOffersFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireParentFragment(), viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    override fun initInjector() {
        getComponent(PayLaterComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_paylater_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        payLaterViewModel.payLaterActivityResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onPayLaterDataLoaded(it.data)
                is Fail -> onPayLaterDataLoadingFail(it.throwable)
            }
        })
    }

    override fun getScreenName(): String {
        return "Detail Penawaran"
    }

    private fun renderTabAndViewPager(productList: ArrayList<PayLaterItemProductData>) {
        context?.let {
            paymentOptionViewPager.adapter = getViewPagerAdapter(productList)
            paymentOptionViewPager.pageMargin = 16.dpToPx(it.resources.displayMetrics)
        }

    }

    private fun getViewPagerAdapter(productList: ArrayList<PayLaterItemProductData>): PagerAdapter {
        val list: ArrayList<Fragment> = ArrayList()
        // for dummy data
        productList.add(productList[0])
        productList.add(productList[0])

        for (productData in productList) {
            val bundle = Bundle().apply {
                putParcelable(PaymentOptionsFragment.PAY_LATER_DATA, productData)
            }
            list.add(PaymentOptionsFragment.newInstance(bundle))
        }
        return PayLaterOfferPagerAdapter(context!!, childFragmentManager, 0, list)
    }

    private fun onPayLaterDataLoaded(data: PayLaterProductData) {
        // hide loading
        if (!data.productList.isNullOrEmpty()) {
            renderTabAndViewPager(data.productList)
        }
    }

    private fun onPayLaterDataLoadingFail(throwable: Throwable) {
        // show error layout
    }

    private fun populateDummyData(): ArrayList<OfferListResponse> {
        val data = ArrayList<OfferListResponse>()
        val descList = ArrayList<OfferDescriptionItem>()
        for (i in 1..3)
            descList.add(OfferDescriptionItem("Bunga 0% untuk bayar dalam 30 hari", true))
        for (i in 4..8)
            descList.add(OfferDescriptionItem("Daftar 5 menit, waktu persetujuan maksimal 1 x 24 jam.", false))
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