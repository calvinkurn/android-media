package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.AffiliateSearchData
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.custom.AffiliateLinkTextFieldInterface
import com.tokopedia.affiliate.ui.viewholder.AffiliatePromotionErrorCardItemVH
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionCardModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionErrorCardModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.affiliate_promo_fragment_layout.*
import java.util.*
import javax.inject.Inject

class AffiliatePromoFragment : BaseViewModelFragment<AffiliatePromoViewModel>(), PromotionClickInterface , AffiliateLinkTextFieldInterface {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

    private lateinit var affiliatePromoViewModel: AffiliatePromoViewModel
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(null, null, this))

    companion object {
        fun getFragmentInstance(): Fragment {
            return AffiliatePromoFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setVisitables(ArrayList())
        promotion_recycler_view.layoutManager = layoutManager
        promotion_recycler_view.adapter = adapter
        product_link_et.run {
            setRelatedView(dim_layer)
            setDoneAction {
                affiliatePromoViewModel.getSearch(editText.text.toString())
            }
            setEventListener(this@AffiliatePromoFragment)
        }
        promo_navToolbar.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_INFORMATION) {
                                AffiliateHowToPromoteBottomSheet.newInstance(AffiliateHowToPromoteBottomSheet.STATE_HOW_TO_PROMOTE).show(childFragmentManager, "")
                            }
                            .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text = getString(R.string.affiliate_promo)
        }
        promo_global_error.run {
            show()
            errorIllustration.hide()
            errorTitle.text = getString(R.string.affiliate_never_bought_product)
            errorDescription.text = getString(R.string.affiliate_still_buy_products)
            setButtonFull(true)
            errorAction.text = getString(R.string.affiliate_paste_link)
            errorAction.setOnClickListener {
                product_link_et.editingState(true)
            }
            errorSecondaryAction.gone()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_promo_fragment_layout, container, false)
    }

    private fun setObservers() {

        affiliatePromoViewModel.progressBar().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility) {
                    hideAllElements()
                    promo_affiliate_progress_view?.show()
                }
                else
                    promo_affiliate_progress_view?.gone()
            }
        })

        affiliatePromoViewModel.getErrorMessage().observe(this, { error ->
            promo_global_error.run {
                show()
                errorTitle.text = error
                setActionClickListener {
                    affiliatePromoViewModel.getSearch(product_link_et.editText.toString())
                }
            }
        })

        affiliatePromoViewModel.getAffiliateSearchData().observe(this, { affiliateSearchData ->
            adapter.clearAllElements()
            if (affiliateSearchData.searchAffiliate?.data?.status == 0) {
                showData(true)
                if (affiliateSearchData.searchAffiliate?.data?.error?.errorStatus == 0) {
                    view?.rootView?.let {
                        Toaster.build(it, getString(R.string.affiliate_product_link_invalid),
                                Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                    }
                    showDefaultState()
                    AffiliateAnalytics.sendEvent(
                            AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                            AffiliateAnalytics.ActionKeys.IMPRESSION_NOT_LINK_ERROR,
                            AffiliateAnalytics.CategoryKeys.PROMOSIKAN_SRP,
                            "",userSessionInterface.userId)
                } else {
                    affiliateSearchData.searchAffiliate?.data?.error?.let {
                        adapter.addElement(AffiliatePromotionErrorCardModel(it))
                    }
                    var errorAction = AffiliateAnalytics.ActionKeys.IMPRESSION_NOT_FOUND_ERROR
                    when (affiliateSearchData.searchAffiliate?.data?.error?.errorStatus){
                        AffiliatePromotionErrorCardItemVH.ERROR_STATUS_NOT_FOUND ->
                                errorAction = AffiliateAnalytics.ActionKeys.IMPRESSION_NOT_FOUND_ERROR
                        AffiliatePromotionErrorCardItemVH.ERROR_STATUS_NOT_ELIGIBLE ->
                                errorAction = AffiliateAnalytics.ActionKeys.IMPRESSION_NOT_ELIGIBLE
                        AffiliatePromotionErrorCardItemVH.ERROR_NON_PM_OS ->
                                errorAction = AffiliateAnalytics.ActionKeys.IMPRESSION_NOT_OS_PM_ERROR
                    }
                    AffiliateAnalytics.sendEvent(
                            AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                            errorAction,
                            AffiliateAnalytics.CategoryKeys.PROMOSIKAN_SRP,
                            "",userSessionInterface.userId)
                }
            } else {
                affiliateSearchData.searchAffiliate?.data?.cards?.firstOrNull()?.items?.let { items ->
                    showData(false)
                    items.forEach {
                        it?.let {
                            adapter.addElement(AffiliatePromotionCardModel(it))
                        }
                    }
                    AffiliateAnalytics.sendEvent(
                            AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                            AffiliateAnalytics.ActionKeys.IMPRESSION_PROMOSIKAN_SRP,
                            AffiliateAnalytics.CategoryKeys.PROMOSIKAN_SRP,
                            "",userSessionInterface.userId)
                }
            }
        })
    }

    private fun showData(isErrorData: Boolean) {
        if (isErrorData) promotion_card_title.hide() else promotion_card_title.show()
        error_group.hide()
        promotion_recycler_view.show()
    }

    private fun showDefaultState(){
        promotion_card_title.hide()
        error_group.show()
        promotion_recycler_view.hide()
    }

    private fun hideAllElements(){
        promotion_card_title.hide()
        error_group.hide()
        promotion_recycler_view.hide()
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectPromoFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun getViewModelType(): Class<AffiliatePromoViewModel> {
        return AffiliatePromoViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliatePromoViewModel = viewModel as AffiliatePromoViewModel
    }

    override fun onPromotionClick(productId: String, productName: String, productImage: String, productUrl: String, productIdentifier: String) {
        AffiliatePromotionBottomSheet.newInstance(productId, productName, productImage, productUrl,
                productIdentifier,AffiliatePromotionBottomSheet.ORIGIN_PROMOSIKAN).show(childFragmentManager, "")
    }

    override fun onButtonClick(errorCta: AffiliateSearchData.SearchAffiliate.Data.Error.ErrorCta?) {
        if(errorCta?.ctaAction == AffiliatePromotionErrorCardItemVH.ACTION_REDIRECT){
            errorCta.ctaLink?.androidUrl?.let {
                RouteManager.routeNoFallbackCheck(context, it, it)
            }
        }else {
            showDefaultState()
            product_link_et.editingState(true)
        }
    }

    override fun onEditState(state: Boolean) {
        AffiliateAnalytics.sendEvent(
                AffiliateAnalytics.EventKeys.EVENT_VALUE_CLICK,
                AffiliateAnalytics.ActionKeys.CLICK_SEARCH,
                AffiliateAnalytics.CategoryKeys.PROMOSIKAN_SRP,
                "",userSessionInterface.userId)
    }
}