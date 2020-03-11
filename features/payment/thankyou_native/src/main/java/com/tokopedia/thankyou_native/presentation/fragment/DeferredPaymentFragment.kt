package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.ThanksPageData
import javax.inject.Inject

class DeferredPaymentFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var thanksPageData: ThanksPageData

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ThankYouPageComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_THANK_PAGE_DATA)) {
                thanksPageData = it.getParcelable(InstantPaymentFragment.ARG_THANK_PAGE_DATA)
            }
        }
//        initViewModels()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_loader, container, false)
    }

    private fun initViewModels() {
//        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        //  thanksPageDataViewModel = viewModelProvider.get(ThanksPageDataViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindDataToUi()
        observeViewModel()
//        thanksPageDataViewModel.getThanksPageData(654186, "tokopediatest")
    }

    private fun bindDataToUi() {

    }

    private fun observeViewModel() {
        /*thanksPageDataViewModel.thanksPageDataResultLiveData.observe(this, Observer {
            when (it) {
                is Success -> onThankYouPageDataLoaded(it.data)
                is Fail -> onThankYouPageDataLoadingFail(it.throwable)
            }
        })*/
    }

    companion object {
        private const val ARG_THANK_PAGE_DATA = "arg_thank_page_data"
        fun getFragmentInstance(thanksPageData: ThanksPageData): DeferredPaymentFragment = DeferredPaymentFragment().apply {
            val bundle = Bundle()
            bundle.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
            }
        }
    }

}
