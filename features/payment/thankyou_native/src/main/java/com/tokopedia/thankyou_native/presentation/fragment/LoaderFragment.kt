package com.tokopedia.thankyou_native.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.di.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageDataLoadCallback
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_fragment_loader.*
import javax.inject.Inject

class LoaderFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var thankYouPageAnalytics: ThankYouPageAnalytics

    private lateinit var thanksPageDataViewModel: ThanksPageDataViewModel

    var callback: ThankYouPageDataLoadCallback? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ThankYouPageComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModels()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_loader, container, false)
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        thanksPageDataViewModel = viewModelProvider.get(ThanksPageDataViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        arguments?.let {
            if (it.containsKey(ThankYouPageActivity.ARG_PAYMENT_ID) && it.containsKey(ThankYouPageActivity.ARG_MERCHANT)) {
                thanksPageDataViewModel.getThanksPageData(it.getLong(ThankYouPageActivity.ARG_PAYMENT_ID),
                        it.getString(ThankYouPageActivity.ARG_MERCHANT, ""))
            }
        }
    }

    private fun observeViewModel() {
        thanksPageDataViewModel.thanksPageDataResultLiveData.observe(this, Observer {
            when (it) {
                is Success -> onThankYouPageDataLoaded(it.data)
                is Fail -> onThankYouPageDataLoadingFail(it.throwable)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ThankYouPageDataLoadCallback)
            callback = context
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    private fun onThankYouPageDataLoadingFail(throwable: Throwable) {
        loading_layout.gone()
        globalError.visible()
        globalError.setType(GlobalError.MAINTENANCE)
    }

    private fun onThankYouPageDataLoaded(data: ThanksPageData) {
        sendThankYouPageAnalytics(data)
        loading_layout.gone()
        callback?.onThankYouPageDataLoaded(data)
    }

    private fun sendThankYouPageAnalytics(thanksPageData: ThanksPageData) {
        thankYouPageAnalytics.sendThankYouPageData(thanksPageData)
    }

    companion object {
        fun getLoaderFragmentInstance(bundle: Bundle): LoaderFragment = LoaderFragment().apply {
            arguments = bundle
        }
    }

}

