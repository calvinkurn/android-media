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

class AffiliatePortfolioFragment: BaseViewModelFragment<AffiliatePortfolioViewModel>(),
        PortfolioUrlTextUpdateInterface, AffiliatePromotionBottomSheetInterface , PortfolioClickInterface {

    private lateinit var affiliatePortfolioViewModel: AffiliatePortfolioViewModel
    private val affiliateAdapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(onFocusChangeInterface=this, portfolioClickInterface = this))

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelProvider) }

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

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

    }

    private fun setPortfolioData() {
        if(affiliatePortfolioViewModel.affiliatePortfolioData.value.isNullOrEmpty() && registrationSharedViewModel.affiliatePortfolioData.value.isNullOrEmpty()) affiliatePortfolioViewModel.createDefaultListForSm()
        else if(affiliatePortfolioViewModel.affiliatePortfolioData.value.isNullOrEmpty() && !registrationSharedViewModel.affiliatePortfolioData.value.isNullOrEmpty()) {
            affiliatePortfolioViewModel.affiliatePortfolioData.value = registrationSharedViewModel.affiliatePortfolioData.value
            affiliatePortfolioViewModel.isError.value = registrationSharedViewModel.isFieldError.value
        }
    }

    private fun initButton() {
        view?.findViewById<UnifyButton>(R.id.next_button)?.apply {
            isEnabled = if(affiliatePortfolioViewModel.isError().value == null)false else affiliatePortfolioViewModel.isError().value != true
            setOnClickListener {
                nextButtonClicked()
            }
        }
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
                activity?.onBackPressed()
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
        const val TAG = "AffiliatePortfolioFragment"
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
        affiliateAdapter.notifyItemChanged(position)
        affiliatePortfolioViewModel.affiliatePortfolioData.value?.let {
            if(position + 1 < it.size &&
                    it[position + 1] is AffiliatePortfolioUrlModel) {
                affiliatePortfolioViewModel.updateFocus(position + 1, b)
                affiliateAdapter.notifyItemChanged(position + 1)
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
            if(portfolioDataItemText?.isNotBlank() == true){
                updateList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData(item.id,item.serviceFormat,"${getString(com.tokopedia.affiliate_toko.R.string.affiliate_link)} ${item.name}",
                        portfolioDataItemText,item.urlSample,getString(com.tokopedia.affiliate_toko.R.string.affiliate_link_not_valid),false,regex = item.regex)))
            }else {
                updateList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData(item.id,item.serviceFormat,"${getString(com.tokopedia.affiliate_toko.R.string.affiliate_link)} ${item.name}",
                        "",item.urlSample,getString(com.tokopedia.affiliate_toko.R.string.affiliate_link_not_valid),false,regex = item.regex)))
            }
        }
        updateList.add(AffiliatePortfolioButtonModel(AffiliatePortfolioButtonData(getString(com.tokopedia.affiliate_toko.R.string.affiliate_tambah_sosial_media), UnifyButton.Type.ALTERNATE, UnifyButton.Variant.GHOST)))
         affiliatePortfolioViewModel.affiliatePortfolioData.value = updateList
    }

    override fun addSocialMediaButtonClicked() {
        view?.requestFocus()
        val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, 0)
        AffiliatePromotionBottomSheet.newInstance(AffiliatePromotionBottomSheet.Companion.SheetType.ADD_SOCIAL,
                this, affiliatePortfolioViewModel.getCurrentSocialIds(),
                "", "", "", "",
                "", AffiliatePromotionBottomSheet.ORIGIN_PORTFOLIO).show(childFragmentManager, "")

    }

    private fun nextButtonClicked() {
        if(affiliatePortfolioViewModel.checkDataForAtLeastOne()){
            val arrayListOfChannels = arrayListOf<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>()
            affiliatePortfolioViewModel.affiliatePortfolioData.value?.forEach { channelItem ->
                (channelItem as? AffiliatePortfolioUrlModel)?.let {
                    if(channelItem.portfolioItm.text?.isNotEmpty() == true){
                        arrayListOfChannels.add(OnboardAffiliateRequest.OnboardAffiliateChannelRequest(channelItem.portfolioItm.serviceFormat
                                ,channelItem.portfolioItm.id,channelItem.portfolioItm.text))
                    }
                }
            }
            sendTracker()
            registrationSharedViewModel.navigateToTermsFragment(arrayListOfChannels)
        }else {
            view?.let { view ->
                Toaster.build(view, getString(com.tokopedia.affiliate_toko.R.string.affiliate_please_fill_one_social_media),
                        Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
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

    private fun sendTracker() {
        AffiliateAnalytics.sendEvent(
                AffiliateAnalytics.EventKeys.CLICK_REGISTER,
                AffiliateAnalytics.ActionKeys.CLICK_SELANJUTNYA,
                AffiliateAnalytics.CategoryKeys.REGISTRATION_PAGE,
                "", userSessionInterface.userId)
    }
}