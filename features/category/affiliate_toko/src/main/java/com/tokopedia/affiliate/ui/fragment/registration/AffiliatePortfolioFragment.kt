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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateActivityInterface
import com.tokopedia.affiliate.interfaces.PortfolioClickInterface
import com.tokopedia.affiliate.interfaces.PortfolioUrlTextUpdateInterface
import com.tokopedia.affiliate.model.pojo.AffiliateHeaderItemData
import com.tokopedia.affiliate.model.pojo.AffiliatePortfolioButtonData
import com.tokopedia.affiliate.model.pojo.AffiliatePortfolioUrlInputData
import com.tokopedia.affiliate.model.request.OnBoardingRequest
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
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.affiliate_portfolio_fragment_layout.*
import javax.inject.Inject

class AffiliatePortfolioFragment: BaseViewModelFragment<AffiliatePortfolioViewModel>(),
        PortfolioUrlTextUpdateInterface, AffiliatePromotionBottomSheetInterface , PortfolioClickInterface {

    private lateinit var affiliatePortfolioViewModel: AffiliatePortfolioViewModel
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(onFocusChangeInterface=this, portfolioClickInterface = this))

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

    private lateinit var affiliateNavigationInterface: AffiliateActivityInterface

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
        afterViewCreated()
    }

    private fun afterViewCreated() {
        setUpNavBar()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        social_link_rv.layoutManager=layoutManager
        social_link_rv.adapter = adapter
        affiliatePortfolioViewModel.createDefaultListForSm()
    }

    private fun setUpNavBar() {
        val customView = layoutInflater.inflate(R.layout.affiliate_navbar_custom_content,null,false)
        customView.findViewById<Typography>(R.id.navbar_sub_tittle).apply {
            show()
            text = getString(R.string.affiliate_subtitle_portfolio)
        }
        customView.findViewById<Typography>(R.id.navbar_tittle).text = getString(R.string.daftar_affiliate)
        affiliate_portfolio_toolbar.apply {
            customView(customView)
            setNavigationOnClickListener {
                affiliateNavigationInterface.handleBackButton()
            }
        }
    }

    private fun initObserver() {
        affiliatePortfolioViewModel.getPortfolioUrlList().observe(this, { data ->
            setDataToRV(data)
        })

        affiliatePortfolioViewModel.getUpdateItemIndex().observe(this,{
            index->
            adapter.notifyItemChanged(index)
        })
    }

    private fun setDataToRV(data: ArrayList<Visitable<AffiliateAdapterTypeFactory>>?) {
        adapter.clearAllElements()
        adapter.addMoreData(data)
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
        fun getFragmentInstance(affiliateActivityInterface: AffiliateActivityInterface): Fragment {
            return AffiliatePortfolioFragment().apply {
                affiliateNavigationInterface = affiliateActivityInterface
            }
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
                adapter.notifyItemChanged(position + 1)
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
            val portfolioDataItemText = finEditTextModelWithId(item.id)?.text
            if(portfolioDataItemText?.isNotBlank() == true){
                updateList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData(item.id,"${getString(com.tokopedia.affiliate_toko.R.string.affiliate_link)} ${item.name}",
                        portfolioDataItemText,item.urlSample,getString(com.tokopedia.affiliate_toko.R.string.affiliate_link_not_valid),false)))
            }else {
                updateList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData(item.id,"${getString(com.tokopedia.affiliate_toko.R.string.affiliate_link)} ${item.name}",
                        "",item.urlSample,getString(com.tokopedia.affiliate_toko.R.string.affiliate_link_not_valid),false)))
            }
        }
        updateList.add(AffiliatePortfolioButtonModel(AffiliatePortfolioButtonData(getString(com.tokopedia.affiliate_toko.R.string.affiliate_tambah_sosial_media), UnifyButton.Type.ALTERNATE, UnifyButton.Variant.GHOST)))
        updateList.add(AffiliatePortfolioButtonModel(AffiliatePortfolioButtonData(getString(com.tokopedia.affiliate_toko.R.string.affiliate_portfolio_confirm_btn), UnifyButton.Type.MAIN,UnifyButton.Variant.FILLED,true)))
        affiliatePortfolioViewModel.affiliatePortfolioData.value = updateList
    }

    private fun finEditTextModelWithId(id : Int?) : AffiliatePortfolioUrlInputData?{
        affiliatePortfolioViewModel.affiliatePortfolioData.value?.forEach {
            if(it is AffiliatePortfolioUrlModel && it.portfolioItm.id == id){
                return it.portfolioItm
            }
        }
        return null
    }

    private fun getCurrentSocialIds () : ArrayList<Int> {
        val ids = arrayListOf<Int>()
        affiliatePortfolioViewModel.affiliatePortfolioData.value?.forEach {
            if(it is AffiliatePortfolioUrlModel){
                it.portfolioItm.id?.let { id ->
                    ids.add(id)
                }
            }
        }
        return ids
    }

    override fun addSocialMediaButtonClicked() {
        view?.requestFocus()
        val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, 0)
        AffiliatePromotionBottomSheet.newInstance(AffiliatePromotionBottomSheet.Companion.SheetType.ADD_SOCIAL,
                this, getCurrentSocialIds(),
                "", "", "", "",
                "", AffiliatePromotionBottomSheet.ORIGIN_PORTFOLIO).show(childFragmentManager, "")

    }

    override fun nextButtonClicked() {
        if(affiliatePortfolioViewModel.checkData()){
            val arrayListOfChannels = arrayListOf<OnBoardingRequest.Channel>()
            affiliatePortfolioViewModel.affiliatePortfolioData.value?.forEach { channelItem ->
                (channelItem as? AffiliatePortfolioUrlModel)?.let {
                    arrayListOfChannels.add(OnBoardingRequest.Channel(channelItem.portfolioItm.id,channelItem.portfolioItm.text))
                }
            }
            affiliateNavigationInterface.navigateToTermsFragment(arrayListOfChannels)
        }
    }
}