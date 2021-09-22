package com.tokopedia.affiliate.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.viewholder.AffiliateSharedProductCardsItemVH
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateSharedProductCardsModel
import com.tokopedia.affiliate.viewmodel.AffiliateHomeViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.affiliate_home_fragment_layout.*
import java.util.ArrayList
import javax.inject.Inject

class AffiliateHomeFragment : BaseViewModelFragment<AffiliateHomeViewModel>(), ProductClickInterface {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

    private lateinit var affiliateHomeViewModel: AffiliateHomeViewModel
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(productClickInterface = this))

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
            affiliateHomeViewModel.getAffiliateValidateUser()
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setVisitables(ArrayList())
        products_rv.layoutManager = layoutManager
        products_rv.adapter = adapter
        user_name.text = affiliateHomeViewModel.getUserName()
        home_navToolbar.run {
            setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_INFORMATION) {
                                AffiliateHowToPromoteBottomSheet.newInstance(AffiliateHowToPromoteBottomSheet.STATE_BETA_INFO).show(childFragmentManager, "")
                            }
            )
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text = getString(R.string.label_affiliate)
        }
        ImageHandler.loadImageCircle2(context, user_image, affiliateHomeViewModel.getUserProfilePicture())
        sendScreenEvent()
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
                    affiliateHomeViewModel.getAffiliateValidateUser()
                }
            }
        })
        affiliateHomeViewModel.getValidateUserdata().observe(this, { validateUserdata ->
            if (validateUserdata.validateAffiliateUserStatus.data?.isEligible == true) {
                affiliateHomeViewModel.getAffiliatePerformance()
            }else {
                validateUserdata.validateAffiliateUserStatus.data?.error?.ctaLink?.androidUrl?.let {
                    activity?.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(it)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
                activity?.finish()
            }
        })
        affiliateHomeViewModel.getAffiliatePerformanceData().observe(this, { affiliatePerformance ->
            affiliatePerformance.getAffiliateItemsPerformanceList?.data?.sectionData?.let { sectionData ->
                affiliate_products_count.text = getString(R.string.affiliate_product_count, sectionData.itemTotalCount.toString())
                if (sectionData.items?.isNotEmpty() == true) {
                    for (product in sectionData.items!!) {
                        product?.let {
                            adapter.addElement(AffiliateSharedProductCardsModel(product))
                        }
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
        getComponent().injectHomeFragment(this)
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
                } else {
                    activity?.finish()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun sendScreenEvent() {
        AffiliateAnalytics.sendEvent(
                AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                AffiliateAnalytics.ActionKeys.IMPRESSION_HOME_PORTAL,
                AffiliateAnalytics.CategoryKeys.HOME_PORTAL,
                "",userSessionInterface.userId)
    }

    override fun onProductClick(productId : String, productName: String, productImage: String, productUrl: String, productIdentifier: String, status : Int?) {
        if(status == AffiliateSharedProductCardsItemVH.PRODUCT_ACTIVE){
            AffiliatePromotionBottomSheet.newInstance(productId , productName,productImage,productUrl,productIdentifier,AffiliatePromotionBottomSheet.ORIGIN_HOME).show(childFragmentManager, "")
        }else {
            AffiliateHowToPromoteBottomSheet.newInstance(AffiliateHowToPromoteBottomSheet.STATE_PRODUCT_INACTIVE).show(childFragmentManager, "")
        }
    }
}
