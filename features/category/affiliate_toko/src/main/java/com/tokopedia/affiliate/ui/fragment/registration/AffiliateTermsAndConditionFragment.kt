package com.tokopedia.affiliate.ui.fragment.registration


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.createListForTermsAndCondition
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateActivityInterface
import com.tokopedia.affiliate.model.request.OnBoardingRequest
import com.tokopedia.affiliate.viewmodel.AffiliateTermsAndConditionViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.affiliate_terms_and_condition_fragment_layout.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AffiliateTermsAndConditionFragment: BaseViewModelFragment<AffiliateTermsAndConditionViewModel>(){

    private lateinit var affiliateTermsAndConditionViewModel: AffiliateTermsAndConditionViewModel
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory())

    private var channels = arrayListOf<OnBoardingRequest.Channel>()

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var affiliateNavigationInterface: AffiliateActivityInterface

    override fun getViewModelType(): Class<AffiliateTermsAndConditionViewModel> {
        return AffiliateTermsAndConditionViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateTermsAndConditionViewModel = viewModel as AffiliateTermsAndConditionViewModel
    }
    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.affiliate_terms_and_condition_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        setUpNavBar()
        initClickListener()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        terms_condition_rv.layoutManager=layoutManager
        terms_condition_rv.adapter = adapter
        setDataToRV(createListForTermsAndCondition(context))
    }

    private fun initClickListener() {
        checkbox_terms.setOnCheckedChangeListener { _, isChecked ->
                terms_accept_btn.isEnabled = isChecked
        }
        terms_accept_btn.setOnClickListener {
            affiliateTermsAndConditionViewModel.affiliateOnBoarding(channels)
        }
    }

    private fun setUpNavBar() {
        val customView = layoutInflater.inflate(R.layout.affiliate_navbar_custom_content,null,false)
        customView.findViewById<Typography>(R.id.navbar_sub_tittle).apply {
            show()
            text = getString(R.string.affiliate_subtitle_terms)
        }
        customView.findViewById<Typography>(R.id.navbar_tittle).text = getString(R.string.daftar_affiliate)
        affiliate_terms_toolbar.apply {
            customView(customView)
            setNavigationOnClickListener {
                affiliateNavigationInterface.handleBackButton()
            }
        }

    }

    private fun initObserver() {
        affiliateTermsAndConditionViewModel.getOnBoardingData().observe(this, { onBoardingData ->
            if(onBoardingData.data?.status == 1){
                affiliateNavigationInterface.onRegistrationSuccessful()
            }else {
                view?.let {
                    Toaster.build(it, getString(com.tokopedia.affiliate_toko.R.string.affiliate_registeration_error),
                            Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                }

            }
        })

        affiliateTermsAndConditionViewModel.getErrorMessage().observe(this , {
            if (it is UnknownHostException
                    || it is SocketTimeoutException) {
                view?.let {
                    Toaster.build(it, getString(com.tokopedia.affiliate_toko.R.string.affiliate_internet_error),
                            Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                }
            } else {
                view?.let {
                    Toaster.build(it, getString(com.tokopedia.affiliate_toko.R.string.affiliate_registeration_error),
                            Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                }
            }
        })

        affiliateTermsAndConditionViewModel.progressBar().observe(this , { progress ->
            if(progress){
                affiliate_progress_bar.show()
            }else {
                affiliate_progress_bar.hide()
            }
        })
    }

    private fun setDataToRV(data: ArrayList<Visitable<AffiliateAdapterTypeFactory>>?) {
        adapter.addMoreData(data)
    }

    fun setChannels(listOfChannels: ArrayList<OnBoardingRequest.Channel>) {
        channels = listOfChannels
    }

    override fun initInject() {
        getComponent().injectTermAndConditionFragment(this)
    }
    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    companion object {
        fun getFragmentInstance(affiliateActivityInterface: AffiliateActivityInterface): AffiliateTermsAndConditionFragment {
            return AffiliateTermsAndConditionFragment().apply {
                affiliateNavigationInterface = affiliateActivityInterface
            }
        }
    }

}