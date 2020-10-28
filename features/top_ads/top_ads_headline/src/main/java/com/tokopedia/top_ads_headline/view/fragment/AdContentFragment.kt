package com.tokopedia.top_ads_headline.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.CreateHeadlineAdsStepperModel
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.HeadlineStepperActivity
import com.tokopedia.top_ads_headline.view.viewmodel.AdContentViewModel
import kotlinx.android.synthetic.main.ad_content_fragment.*
import javax.inject.Inject

class AdContentFragment : BaseHeadlineStepperFragment<CreateHeadlineAdsStepperModel>() {

    companion object {
        fun newInstance() = AdContentFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AdContentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AdContentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ad_content_fragment, container, false)
    }

    override fun getScreenName(): String {
        return AdContentFragment::class.java.simpleName
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateHeadlineAdsStepperModel()
    }

    override fun gotoNextPage() {
        stepperListener?.goToNextPage(stepperModel)
    }

    override fun updateToolBar() {
        if (activity is HeadlineStepperActivity) {
            (activity as HeadlineStepperActivity).updateToolbarTitle(getString(R.string.topads_headline_ad_content_fragment_label))
        }
    }

    override fun populateView() {
        contentSelectedText.text = getString(R.string.topads_headline_product_selected, "0")
    }
}