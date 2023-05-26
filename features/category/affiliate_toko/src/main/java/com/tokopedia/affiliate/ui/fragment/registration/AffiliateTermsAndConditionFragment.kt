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
import com.tokopedia.affiliate.AFFILIATE_PRIVACY_POLICY_URL_WEBVIEW
import com.tokopedia.affiliate.AFFILIATE_TANDC_URL
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.createListForTermsAndCondition
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.model.request.OnboardAffiliateRequest
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateWebViewBottomSheet
import com.tokopedia.affiliate.viewmodel.AffiliateRegistrationSharedViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateTermsAndConditionViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.orFalse
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

class AffiliateTermsAndConditionFragment :
    BaseViewModelFragment<AffiliateTermsAndConditionViewModel>() {

    private var affiliateTermsAndConditionViewModel: AffiliateTermsAndConditionViewModel? = null
    private val affiliateAdapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory())

    private var channels: ArrayList<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>? =
        arrayListOf()

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null
    private val viewModelFragmentProvider by lazy {
        viewModelProvider?.let { viewModelProvider ->
            activity?.let { activity ->
                ViewModelProvider(
                    activity,
                    viewModelProvider
                )
            }
        }
    }

    private var registrationSharedViewModel: AffiliateRegistrationSharedViewModel? = null

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    override fun getViewModelType(): Class<AffiliateTermsAndConditionViewModel> {
        return AffiliateTermsAndConditionViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateTermsAndConditionViewModel = viewModel as AffiliateTermsAndConditionViewModel
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationSharedViewModel =
            viewModelFragmentProvider?.get(AffiliateRegistrationSharedViewModel::class.java)
        getChannel(savedInstanceState)
        initObserver()
    }

    private fun getChannel(savedInstanceState: Bundle?) {
        if (registrationSharedViewModel?.listOfChannels.isNullOrEmpty()) {
            savedInstanceState?.getSerializable(CHANNEL_LIST)?.let {
                channels = it as ArrayList<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>
            }
        } else {
            channels = registrationSharedViewModel?.listOfChannels
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.affiliate_terms_and_condition_fragment_layout,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        setUpNavBar()
        initClickListener()
        view?.findViewById<RecyclerView>(R.id.terms_condition_rv)?.run {
            val linearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = linearLayoutManager
            adapter = affiliateAdapter
        }
        setDataToRV(createListForTermsAndCondition(context))
        sendOpenScreenTracking()
    }

    private fun sendOpenScreenTracking() {
        val loginText =
            if (userSessionInterface?.isLoggedIn == true) {
                AffiliateAnalytics.LabelKeys.LOGIN
            } else {
                AffiliateAnalytics.LabelKeys.NON_LOGIN
            }
        AffiliateAnalytics.sendOpenScreenEvent(
            AffiliateAnalytics.EventKeys.OPEN_SCREEN,
            "${AffiliateAnalytics.ScreenKeys.AFFILIATE_TERMS_AND_CONDITION}$loginText",
            userSessionInterface?.isLoggedIn.orFalse(),
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun sendButtonClick(eventAction: String) {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            eventAction,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_REG_T_ANC_C,
            if (userSessionInterface?.isLoggedIn == true) {
                AffiliateAnalytics.LabelKeys.LOGIN
            } else {
                AffiliateAnalytics.LabelKeys.NON_LOGIN
            },
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun initClickListener() {
        view?.findViewById<CheckboxUnify>(R.id.checkbox_terms)
            ?.setOnCheckedChangeListener { _, isChecked ->
                view?.findViewById<UnifyButton>(R.id.terms_accept_btn)?.isEnabled = isChecked
            }
        view?.findViewById<UnifyButton>(R.id.terms_accept_btn)?.setOnClickListener {
            sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_DAFTAR)
            if (channels != null) {
                affiliateTermsAndConditionViewModel?.affiliateOnBoarding(channels!!)
            }
        }
        view?.findViewById<Typography>(R.id.syarat_text)?.setOnClickListener {
            sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_SYARAT)
            AffiliateWebViewBottomSheet.newInstance(
                getString(R.string.terms_and_condition_upper),
                AFFILIATE_TANDC_URL
            ).show(childFragmentManager, "")
        }
        view?.findViewById<Typography>(R.id.kebijakan_text)?.setOnClickListener {
            context?.let {
                RouteManager.route(it, AFFILIATE_PRIVACY_POLICY_URL_WEBVIEW)
            }
        }
    }

    private fun setUpNavBar() {
        val customView =
            layoutInflater.inflate(R.layout.affiliate_navbar_custom_content, null, false)
        customView.findViewById<Typography>(R.id.navbar_sub_tittle).apply {
            show()
            text = getString(R.string.affiliate_subtitle_terms)
        }
        customView.findViewById<Typography>(R.id.navbar_tittle).text =
            getString(R.string.daftar_affiliate)
        view?.findViewById<com.tokopedia.header.HeaderUnify>(R.id.affiliate_terms_toolbar)?.apply {
            customView(customView)
            setNavigationOnClickListener {
                sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_BACK)
                activity?.onBackPressed()
            }
        }
    }

    private fun initObserver() {
        affiliateTermsAndConditionViewModel?.getOnBoardingData()?.observe(this) { onBoardingData ->
            if (onBoardingData.data?.status == REGISTRATION_SUCCESS) {
                registrationSharedViewModel?.onRegisterationSuccess()
            } else {
                view?.let {
                    Toaster.build(
                        it,
                        getString(R.string.affiliate_registeration_error),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_ERROR
                    ).show()
                }
            }
        }

        affiliateTermsAndConditionViewModel?.getErrorMessage()?.observe(this) {
            if (it is UnknownHostException ||
                it is SocketTimeoutException
            ) {
                view?.let { view ->
                    Toaster.build(
                        view,
                        getString(R.string.affiliate_internet_error),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_ERROR
                    ).show()
                }
            } else {
                view?.let { view ->
                    Toaster.build(
                        view,
                        getString(R.string.affiliate_registeration_error),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_ERROR
                    ).show()
                }
            }
        }

        affiliateTermsAndConditionViewModel?.progressBar()?.observe(this) { progress ->
            if (progress) {
                view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.show()
            } else {
                view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.hide()
            }
        }
    }

    private fun setDataToRV(data: ArrayList<Visitable<AffiliateAdapterTypeFactory>>?) {
        affiliateAdapter.addMoreData(data)
    }

    override fun initInject() {
        getComponent().injectTermAndConditionFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(CHANNEL_LIST, registrationSharedViewModel?.listOfChannels)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val TAG = "AffiliateTermsAndConditionFragment"
        const val REGISTRATION_SUCCESS = 1
        const val CHANNEL_LIST = "channel_list"
        fun getFragmentInstance(): AffiliateTermsAndConditionFragment {
            return AffiliateTermsAndConditionFragment()
        }
    }
}
