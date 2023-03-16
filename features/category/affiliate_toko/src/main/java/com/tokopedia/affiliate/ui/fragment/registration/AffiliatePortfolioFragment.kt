package com.tokopedia.affiliate.ui.fragment.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.hideKeyboard
import com.tokopedia.affiliate.interfaces.PortfolioClickInterface
import com.tokopedia.affiliate.interfaces.PortfolioUrlTextUpdateInterface
import com.tokopedia.affiliate.model.request.OnboardAffiliateRequest
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePortfolioSocialMediaBottomSheet
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioUrlModel
import com.tokopedia.affiliate.viewmodel.AffiliatePortfolioViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateRegistrationSharedViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliatePortfolioFragment :
    BaseViewModelFragment<AffiliatePortfolioViewModel>(),
    PortfolioUrlTextUpdateInterface,
    PortfolioClickInterface {

    private lateinit var affiliatePortfolioViewModel: AffiliatePortfolioViewModel
    private val affiliateAdapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(onFocusChangeInterface = this, portfolioClickInterface = this))

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelProvider) }

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private lateinit var registrationSharedViewModel: AffiliateRegistrationSharedViewModel

    override fun getViewModelType(): Class<AffiliatePortfolioViewModel> {
        return AffiliatePortfolioViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliatePortfolioViewModel = viewModel as AffiliatePortfolioViewModel
    }
    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationSharedViewModel = viewModelFragmentProvider.get(AffiliateRegistrationSharedViewModel::class.java)
        initObserver()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.affiliate_portfolio_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        initButton()
        setUpNavBar()
        view?.findViewById<RecyclerView>(R.id.social_link_rv)?.run {
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = linearLayoutManager
            adapter = affiliateAdapter
        }
        setPortfolioData()
        sendOpenScreenTracking()
    }

    private fun setPortfolioData() {
        if (affiliatePortfolioViewModel.affiliatePortfolioData.value.isNullOrEmpty() && registrationSharedViewModel.affiliatePortfolioData.value.isNullOrEmpty()) {
            affiliatePortfolioViewModel.createDefaultListForSm()
        } else if (affiliatePortfolioViewModel.affiliatePortfolioData.value.isNullOrEmpty() && !registrationSharedViewModel.affiliatePortfolioData.value.isNullOrEmpty()) {
            affiliatePortfolioViewModel.affiliatePortfolioData.value = registrationSharedViewModel.affiliatePortfolioData.value
            affiliatePortfolioViewModel.isError.value = registrationSharedViewModel.isFieldError.value
        }
    }

    private fun sendOpenScreenTracking() {
        val loginText = if (userSessionInterface.isLoggedIn)AffiliateAnalytics.LabelKeys.LOGIN else AffiliateAnalytics.LabelKeys.NON_LOGIN
        AffiliateAnalytics.sendOpenScreenEvent(
            AffiliateAnalytics.EventKeys.OPEN_SCREEN,
            "${AffiliateAnalytics.ScreenKeys.AFFILIATE_PORTFOLIO_NAME}$loginText",
            userSessionInterface.isLoggedIn,
            userSessionInterface.userId
        )
    }

    private fun initButton() {
        view?.findViewById<UnifyButton>(R.id.next_button)?.apply {
            isEnabled = if (affiliatePortfolioViewModel.isError().value == null)false else affiliatePortfolioViewModel.isError().value != true
            setOnClickListener {
                nextButtonClicked()
                sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_SELANJUTNYA)
            }
        }
    }

    private fun sendButtonClick(eventAction: String) {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            eventAction,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_REGISTRATION_PAGE_PROMOTION_CHANNEL,
            if (userSessionInterface.isLoggedIn)AffiliateAnalytics.LabelKeys.LOGIN else AffiliateAnalytics.LabelKeys.NON_LOGIN,
            userSessionInterface.userId
        )
    }

    private fun setUpNavBar() {
        val customView = layoutInflater.inflate(R.layout.affiliate_navbar_custom_content, null, false)
        customView.findViewById<Typography>(R.id.navbar_sub_tittle).apply {
            show()
            text = getString(R.string.affiliate_subtitle_portfolio)
        }
        customView.findViewById<Typography>(R.id.navbar_tittle).text = getString(R.string.daftar_affiliate)
        view?.findViewById<HeaderUnify>(R.id.affiliate_portfolio_toolbar)?.apply {
            customView(customView)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun initObserver() {
        affiliatePortfolioViewModel.getPortfolioUrlList().observe(this, { data ->
            setDataToRV(data)
        })

        affiliatePortfolioViewModel.getUpdateItemIndex().observe(this, {
                index ->
            affiliateAdapter.notifyItemChanged(index)
        })
        affiliatePortfolioViewModel.isError().observe(this, { isError ->
            view?.findViewById<UnifyButton>(R.id.next_button)?.apply {
                isError?.let {
                    isEnabled = !it
                }
            }
        })
    }

    private fun setDataToRV(data: ArrayList<Visitable<AffiliateAdapterTypeFactory>>?) {
        view?.findViewById<RecyclerView>(R.id.social_link_rv)?.adapter = null
        view?.findViewById<RecyclerView>(R.id.social_link_rv)?.adapter = affiliateAdapter
        affiliateAdapter.clearAllElements()
        affiliateAdapter.addMoreData(data)
    }

    override fun initInject() {
        getComponent().injectPortfolioFragment(this)
    }
    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    companion object {
        const val TAG = "AffiliatePortfolioFragment"
        fun getFragmentInstance(): Fragment {
            return AffiliatePortfolioFragment()
        }
    }

    override fun onUrlUpdate(position: Int, text: String) {
        affiliatePortfolioViewModel.updateList(position, text)
    }

    override fun onError(position: Int) {
        affiliatePortfolioViewModel.updateListErrorState(position)
    }

    override fun onUrlSuccess(position: Int) {
        affiliatePortfolioViewModel.updateListSuccess(position)
    }

    override fun onNextKeyPressed(position: Int, b: Boolean) {
        affiliatePortfolioViewModel.affiliatePortfolioData.value?.let {
            if (position + 1 < it.size &&
                it[position + 1] is AffiliatePortfolioUrlModel
            ) {
                affiliatePortfolioViewModel.updateFocus(position + 1, b)
                affiliatePortfolioViewModel.updateFocus(position, false)
            } else {
                view?.hideKeyboard(context)
            }
        }
    }

    override fun addSocialMediaButtonClicked() {
        view?.hideKeyboard(context)
        sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_TAMBAH_SOCIAL_MEDIA)
        AffiliatePortfolioSocialMediaBottomSheet.newInstance().show(childFragmentManager, "")
    }

    private fun nextButtonClicked() {
        if (affiliatePortfolioViewModel.checkDataForAtLeastOne()) {
            val arrayListOfChannels = arrayListOf<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>()
            affiliatePortfolioViewModel.affiliatePortfolioData.value?.forEach { channelItem ->
                (channelItem as? AffiliatePortfolioUrlModel)?.let {
                    if (channelItem.portfolioItm.text?.isNotEmpty() == true && channelItem.portfolioItm.firstTime != true) {
                        arrayListOfChannels.add(
                            OnboardAffiliateRequest.OnboardAffiliateChannelRequest(
                                channelItem.portfolioItm.serviceFormat,
                                channelItem.portfolioItm.id,
                                channelItem.portfolioItm.text
                            )
                        )
                    }
                }
            }
            view?.hideKeyboard(context)
            registrationSharedViewModel.navigateToTermsFragment(arrayListOfChannels)
        } else {
            view?.let { view ->
                Toaster.build(
                    view,
                    getString(com.tokopedia.affiliate_toko.R.string.affiliate_please_fill_one_social_media),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        setDataInSharedViewModel()
    }

    private fun setDataInSharedViewModel() {
        registrationSharedViewModel.affiliatePortfolioData.value = affiliatePortfolioViewModel.affiliatePortfolioData.value
        registrationSharedViewModel.isFieldError.value = affiliatePortfolioViewModel.isError().value
    }
}
