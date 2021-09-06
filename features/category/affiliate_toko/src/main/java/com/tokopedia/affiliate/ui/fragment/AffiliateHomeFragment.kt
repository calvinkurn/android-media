package com.tokopedia.affiliate.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.affiliate.AFFILIATE_LOGIN_REQUEST_CODE
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateProductCardVHViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateHomeViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import kotlinx.android.synthetic.main.affiliate_home_fragment_layout.*
import java.util.ArrayList
import javax.inject.Inject

class AffiliateHomeFragment : BaseViewModelFragment<AffiliateHomeViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var affiliateHomeViewModel: AffiliateHomeViewModel
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory())

    companion object {
        fun getFragmentInstance(): Fragment {
            return AffiliateHomeFragment()
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
        if (!affiliateHomeViewModel.isUserLoggedIn()) {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                    AFFILIATE_LOGIN_REQUEST_CODE)
        } else {
            //TODO
            affiliateHomeViewModel.getAffiliatePerformance()
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setVisitables(ArrayList())
        products_rv.layoutManager = layoutManager
        products_rv.adapter = adapter
        user_name.text = affiliateHomeViewModel.getUserName()
        home_navToolbar.setIcon(
                IconBuilder()
                        .addIcon(IconList.ID_INFORMATION) {
                            AffiliateHowToPromoteBottomSheet.newInstance().show(childFragmentManager, "")
                        }
        )
        ImageHandler.loadImageCircle2(context, user_image, affiliateHomeViewModel.getUserProfilePicture())
    }

    private fun showNoAffiliate() {
        affiliate_no_product_iv.show()
        home_global_error.run {
            show()
            errorIllustration.hide()
            errorTitle.text = getString(R.string.affiliate_choose_product)
            errorDescription.text = getString(R.string.affiliate_choose_product_description)
            setButtonFull(true)
            errorAction.text = getString(R.string.affiliate_promote_affiliatw)
            errorSecondaryAction.gone()
            setActionClickListener {
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_home_fragment_layout, container, false)
    }

    private fun setObservers() {
        affiliateHomeViewModel.getShimmerVisibility().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility)
                    adapter.startShimmer()
                else
                    adapter.stopShimmer()
            }
        })
        affiliateHomeViewModel.progressBar().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility) {
                    nested_scroll_view?.hide()
                    affiliate_progress_bar?.show()
                } else {
                    nested_scroll_view?.show()
                    affiliate_progress_bar?.gone()
                }
            }
        })
        affiliateHomeViewModel.getErrorMessage().observe(this, { error ->
            home_global_error.run {
                show()
                errorTitle.text = error
                setActionClickListener {
                    AffiliatePromotionBottomSheet.newInstance("","","","").show(childFragmentManager, "")
//                    affiliateHomeViewModel.getAffiliateValidateUser()
                }
            }
        })
        affiliateHomeViewModel.getValidateUserdata().observe(this, { validateUserdata ->
            if (validateUserdata.validateUserStatus.data.isEligible) {
                affiliateHomeViewModel.getAffiliatePerformance()
            }
        })
        affiliateHomeViewModel.getAffiliatePerformanceData().observe(this, { affiliatePerformance ->
            affiliatePerformance.affiliatePerformance.data.links?.items?.let { products ->
                if (products.isNotEmpty()) {
                    for (product in products) {
                        adapter.addElement(AffiliateProductCardVHViewModel(product))
                    }
                } else {
                    showNoAffiliate()
                }
            }
        })
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun getViewModelType(): Class<AffiliateHomeViewModel> {
        return AffiliateHomeViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateHomeViewModel = viewModel as AffiliateHomeViewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            AFFILIATE_LOGIN_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    affiliateHomeViewModel.getAffiliateValidateUser()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
