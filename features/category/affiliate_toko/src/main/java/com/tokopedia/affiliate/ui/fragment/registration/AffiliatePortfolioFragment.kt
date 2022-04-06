package com.tokopedia.affiliate.ui.fragment.registration


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import com.tokopedia.affiliate.interfaces.AffiliateActivityInterface
import com.tokopedia.affiliate.interfaces.PortfolioClickInterface
import com.tokopedia.affiliate.interfaces.PortfolioUrlTextUpdateInterface
import com.tokopedia.affiliate.model.pojo.AffiliateHeaderItemData
import com.tokopedia.affiliate.model.pojo.AffiliatePortfolioButtonData
import com.tokopedia.affiliate.model.pojo.AffiliatePortfolioUrlInputData
import com.tokopedia.affiliate.model.request.OnboardAffiliateRequest
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheetInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioButtonModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioUrlModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShareModel
import com.tokopedia.affiliate.viewmodel.AffiliatePortfolioViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliatePortfolioFragment: BaseViewModelFragment<AffiliatePortfolioViewModel>(),
        PortfolioUrlTextUpdateInterface, AffiliatePromotionBottomSheetInterface , PortfolioClickInterface {

    private lateinit var affiliatePortfolioViewModel: AffiliatePortfolioViewModel
    private val affiliateAdapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(onFocusChangeInterface=this, portfolioClickInterface = this))

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

    private var affiliateNavigationInterface: AffiliateActivityInterface? = null

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
        (activity as? AffiliateActivityInterface)?.let {
            affiliateNavigationInterface = it
        }
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
        affiliatePortfolioViewModel.createDefaultListForSm()
        sendOpenScreenTracking()
    }

    private fun sendOpenScreenTracking() {
        val loginText = if(userSessionInterface.isLoggedIn)AffiliateAnalytics.LabelKeys.LOGIN else AffiliateAnalytics.LabelKeys.NON_LOGIN
        AffiliateAnalytics.sendOpenScreenEvent(
            AffiliateAnalytics.EventKeys.OPEN_SCREEN,
            "${AffiliateAnalytics.ScreenKeys.AFFILIATE_PORTFOLIO_NAME}$loginText",
            userSessionInterface.isLoggedIn,
            userSessionInterface.userId
        )
    }

    private fun initButton() {
        view?.findViewById<UnifyButton>(R.id.next_button)?.setOnClickListener {
            nextButtonClicked()
            sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_SELANJUTNYA)
        }
    }

    private fun sendButtonClick(eventAction: String) {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            eventAction,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_REGISTRATION_PAGE_PROMOTION_CHANNEL,
            if(userSessionInterface.isLoggedIn)AffiliateAnalytics.LabelKeys.LOGIN else AffiliateAnalytics.LabelKeys.NON_LOGIN,
            userSessionInterface.userId
        )
    }

    private fun setUpNavBar() {
        val customView = layoutInflater.inflate(R.layout.affiliate_navbar_custom_content,null,false)
        customView.findViewById<Typography>(R.id.navbar_sub_tittle).apply {
            show()
            text = getString(R.string.affiliate_subtitle_portfolio)
        }
        customView.findViewById<Typography>(R.id.navbar_tittle).text = getString(R.string.daftar_affiliate)
        view?.findViewById<HeaderUnify>(R.id.affiliate_portfolio_toolbar)?.apply {
            customView(customView)
            setNavigationOnClickListener {
                affiliateNavigationInterface?.handleBackButton(false)
            }
        }
    }

    private fun initObserver() {
        affiliatePortfolioViewModel.getPortfolioUrlList().observe(this, { data ->
            setDataToRV(data)
        })

        affiliatePortfolioViewModel.getUpdateItemIndex().observe(this,{
            index->
            affiliateAdapter.notifyItemChanged(index)
        })
        affiliatePortfolioViewModel.isError().observe(this ,{ isError ->
                view?.findViewById<UnifyButton>(R.id.next_button)?.apply {
                    isEnabled = !isError
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
        fun getFragmentInstance(): Fragment {
            return AffiliatePortfolioFragment()
        }
    }

    override fun onUrlUpdate(position: Int, text: String) {
        affiliatePortfolioViewModel.updateList(position,text)
    }

    override fun onError(position: Int) {
        affiliatePortfolioViewModel.updateListErrorState(position)
    }

    override fun onUrlSuccess(position: Int) {
        affiliatePortfolioViewModel.updateListSuccess(position)
    }

    override fun onNextKeyPressed(position: Int, b: Boolean) {
        affiliatePortfolioViewModel.affiliatePortfolioData.value?.let {
            if(position + 1 < it.size &&
                    it[position + 1] is AffiliatePortfolioUrlModel) {
                affiliatePortfolioViewModel.updateFocus(position + 1, b)
                affiliatePortfolioViewModel.updateFocus(position,false)
            }
            else{
                view?.hideKeyboard(context)
            }
        }
    }

    override fun onButtonClick(checkedSocialList: List<AffiliateShareModel>) {
        convertToPortfolioModel(checkedSocialList)
    }

    private fun convertToPortfolioModel(checkedSocialList : List<AffiliateShareModel>) {
        val updateList : java.util.ArrayList<Visitable<AffiliateAdapterTypeFactory>> = java.util.ArrayList()
        updateList.add(AffiliateHeaderModel(AffiliateHeaderItemData(userSessionInterface.name,true)))
        for (item in checkedSocialList){
            val portfolioDataItemText = affiliatePortfolioViewModel.finEditTextModelWithId(item.id)?.text
            val firstItem = affiliatePortfolioViewModel.finEditTextModelWithId(item.id)?.firstTime
            if(portfolioDataItemText?.isNotBlank() == true){
                updateList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData(item.id,item.serviceFormat,"${getString(com.tokopedia.affiliate_toko.R.string.affiliate_link)} ${item.name}",
                        portfolioDataItemText,item.urlSample,getString(com.tokopedia.affiliate_toko.R.string.affiliate_link_not_valid),false,regex = item.regex ,firstTime = firstItem)))
            }else {
                updateList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData(item.id,item.serviceFormat,"${getString(com.tokopedia.affiliate_toko.R.string.affiliate_link)} ${item.name}",
                        item.defaultText,item.urlSample,getString(com.tokopedia.affiliate_toko.R.string.affiliate_link_not_valid),false,regex = item.regex,firstTime = true)))
            }
        }
        updateList.add(AffiliatePortfolioButtonModel(AffiliatePortfolioButtonData(getString(com.tokopedia.affiliate_toko.R.string.affiliate_tambah_sosial_media), UnifyButton.Type.ALTERNATE, UnifyButton.Variant.GHOST)))
         affiliatePortfolioViewModel.affiliatePortfolioData.value = updateList
    }

    override fun addSocialMediaButtonClicked() {
        view?.hideKeyboard(context)
        AffiliatePromotionBottomSheet.newInstance(AffiliatePromotionBottomSheet.Companion.SheetType.ADD_SOCIAL,
                this, affiliatePortfolioViewModel.getCurrentSocialIds(),
                "", "", "", "",
                "", AffiliatePromotionBottomSheet.ORIGIN_PORTFOLIO).show(childFragmentManager, "")
        sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_TAMBAH_SOCIAL_MEDIA)

    }

    private fun nextButtonClicked() {
        if(affiliatePortfolioViewModel.checkDataForAtLeastOne()){
            val arrayListOfChannels = arrayListOf<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>()
            affiliatePortfolioViewModel.affiliatePortfolioData.value?.forEach { channelItem ->
                (channelItem as? AffiliatePortfolioUrlModel)?.let {
                    if(channelItem.portfolioItm.text?.isNotEmpty() == true && channelItem.portfolioItm.firstTime != true){
                        arrayListOfChannels.add(OnboardAffiliateRequest.OnboardAffiliateChannelRequest(channelItem.portfolioItm.serviceFormat
                                ,channelItem.portfolioItm.id,channelItem.portfolioItm.text))
                    }
                }
            }
            affiliateNavigationInterface?.navigateToTermsFragment(arrayListOfChannels)
            view?.hideKeyboard(context)
        }else {
            view?.let { view ->
                Toaster.build(view, getString(com.tokopedia.affiliate_toko.R.string.affiliate_please_fill_one_social_media),
                        Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
            }
        }
    }
}