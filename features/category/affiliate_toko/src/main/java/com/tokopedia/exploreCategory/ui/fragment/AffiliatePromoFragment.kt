package com.tokopedia.exploreCategory.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.exploreCategory.di.AffiliateComponent
import com.tokopedia.exploreCategory.di.DaggerAffiliateComponent
import com.tokopedia.exploreCategory.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.exploreCategory.ui.custom.AffiliatePromotionProductCard
import com.tokopedia.exploreCategory.viewmodel.AffiliatePromoViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import kotlinx.android.synthetic.main.affiliate_promo_fragment_layout.*
import kotlinx.android.synthetic.main.affiliate_promotion_product_card.*
import javax.inject.Inject

class AffiliatePromoFragment : BaseViewModelFragment<AffiliatePromoViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var affiliatePromoViewModel: AffiliatePromoViewModel

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
        product_link_et.run {
            setRelatedView(dim_layer)
            setDoneAction { affiliatePromoViewModel.getSearch() }
        }
        promo_navToolbar.setIcon(
                IconBuilder()
                        .addIcon(IconList.ID_INFORMATION) {
                            AffiliateHowToPromoteBottomSheet.newInstance().show(childFragmentManager, "")
                        }
                )
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
                if (visibility)
                    promo_affiliate_progress_bar?.show()
                else
                    promo_affiliate_progress_bar?.gone()
            }
        })

        affiliatePromoViewModel.getErrorMessage().observe(this, { error ->
            promo_global_error.run {
                show()
                errorTitle.text = error
                setActionClickListener {
                    affiliatePromoViewModel.getSearch()
                }
            }
        })

        affiliatePromoViewModel.getAffiliateSearchData().observe(this, { affiliateSearchData ->
            affiliateSearchData.cards?.items?.firstOrNull()?.let {
                error_group.hide()
                promotion_product_card.show()
                affiliate_product_card.setProductModel(AffiliatePromotionProductCard.toAffiliateProductModel(it))
            }
        })

        affiliatePromoViewModel.getAffiliateProductCommissionData().observe(this, { affiliateCommissionData ->

        })
    }

    private fun showErrorScreen(){

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
}