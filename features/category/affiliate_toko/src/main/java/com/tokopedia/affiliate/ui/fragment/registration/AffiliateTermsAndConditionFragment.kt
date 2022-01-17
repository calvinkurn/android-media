package com.tokopedia.affiliate.ui.fragment.registration


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.AFFILIATE_HELP_URL
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.createListForTermsAndCondition
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateActivityInterface
import com.tokopedia.affiliate.model.request.OnboardAffiliateRequest
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateWebViewBottomSheet
import com.tokopedia.affiliate.viewmodel.AffiliateTermsAndConditionViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AffiliateTermsAndConditionFragment: BaseViewModelFragment<AffiliateTermsAndConditionViewModel>(){

    private lateinit var affiliateTermsAndConditionViewModel: AffiliateTermsAndConditionViewModel
    private val affiliateAdapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory())

    private var channels = arrayListOf<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>()

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

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
        view?.findViewById<RecyclerView>(R.id.terms_condition_rv)?.run {
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = linearLayoutManager
            adapter = affiliateAdapter
        }
        setDataToRV(createListForTermsAndCondition(context))
    }

    private fun initClickListener() {
        view?.findViewById<CheckboxUnify>(R.id.checkbox_terms)?.setOnCheckedChangeListener { _, isChecked ->
                view?.findViewById<UnifyButton>(R.id.terms_accept_btn)?.isEnabled = isChecked
        }
        view?.findViewById<UnifyButton>(R.id.terms_accept_btn)?.setOnClickListener {
            sendTracker()
            affiliateTermsAndConditionViewModel.affiliateOnBoarding(channels)
        }
        view?.findViewById<Typography>(R.id.syarat_text)?.setOnClickListener {
            AffiliateWebViewBottomSheet.newInstance("HELP", AFFILIATE_HELP_URL).show(childFragmentManager,"")
        }
    }

    private fun sendTracker() {
        AffiliateAnalytics.sendEvent(
                AffiliateAnalytics.EventKeys.CLICK_REGISTER,
                AffiliateAnalytics.ActionKeys.CLICK_DAFTAR,
                AffiliateAnalytics.CategoryKeys.REGISTRATION_PAGE,
                "", userSessionInterface.userId)
    }

    private fun setUpNavBar() {
        val customView = layoutInflater.inflate(R.layout.affiliate_navbar_custom_content,null,false)
        customView.findViewById<Typography>(R.id.navbar_sub_tittle).apply {
            show()
            text = getString(R.string.affiliate_subtitle_terms)
        }
        customView.findViewById<Typography>(R.id.navbar_tittle).text = getString(R.string.daftar_affiliate)
        view?.findViewById<com.tokopedia.header.HeaderUnify>(R.id.affiliate_terms_toolbar)?.apply {
            customView(customView)
            setNavigationOnClickListener {
                affiliateNavigationInterface.handleBackButton()
            }
        }

    }

    private fun initObserver() {
        affiliateTermsAndConditionViewModel.getOnBoardingData().observe(this, { onBoardingData ->
            if(onBoardingData.data?.status == REGISTRATION_SUCCESS){
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
                view?.let { view ->
                    Toaster.build(view, getString(com.tokopedia.affiliate_toko.R.string.affiliate_internet_error),
                            Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                }
            } else {
                view?.let { view ->
                    Toaster.build(view, getString(com.tokopedia.affiliate_toko.R.string.affiliate_registeration_error),
                            Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                }
            }
        })

        affiliateTermsAndConditionViewModel.progressBar().observe(this , { progress ->
            if(progress){
                view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.show()
            }else {
                view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.hide()
            }
        })
    }

    private fun setDataToRV(data: ArrayList<Visitable<AffiliateAdapterTypeFactory>>?) {
        affiliateAdapter.addMoreData(data)
    }

    fun setChannels(listOfChannels: ArrayList<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>) {
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
        const val REGISTRATION_SUCCESS = 1
        fun getFragmentInstance(affiliateActivityInterface: AffiliateActivityInterface): AffiliateTermsAndConditionFragment {
            return AffiliateTermsAndConditionFragment().apply {
                affiliateNavigationInterface = affiliateActivityInterface
            }
        }
    }

}