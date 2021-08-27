package com.tokopedia.exploreCategory.ui.fragment

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
import com.tokopedia.exploreCategory.AFFILIATE_LOGIN_REQUEST_CODE
import com.tokopedia.exploreCategory.adapter.AffiliateAdapter
import com.tokopedia.exploreCategory.adapter.AffiliateAdapterFactory
import com.tokopedia.exploreCategory.di.AffiliateComponent
import com.tokopedia.exploreCategory.di.DaggerAffiliateComponent
import com.tokopedia.exploreCategory.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateProductCardVHViewModel
import com.tokopedia.exploreCategory.viewmodel.AffiliateHomeViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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
        affiliate_progress_bar?.gone()
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
        ImageHandler.loadImageCircle2(context, user_image, affiliateHomeViewModel.getUserProfilePicture())
    }

    private fun showNoAffiliate() {
        affiliate_no_product_iv.show()
        global_error.run {
            show()
            errorIllustration.hide()
            errorTitle.text = getString(R.string.affiliate_choose_product)
            errorDescription.text = getString(R.string.affiliate_choose_product_description)
            setButtonFull(true)
            errorAction.text = getString(R.string.affiliate_promote_affiliatw)
            errorSecondaryAction.gone()
            setActionClickListener {
                AffiliatePromotionBottomSheet.newInstance().show(childFragmentManager, "")
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
        affiliateHomeViewModel.getErrorMessage().observe(this, { error ->
            global_error.run {
                show()
                errorTitle.text = error
                setActionClickListener {
                    affiliateHomeViewModel.getAffiliateValidateUser()
                }
            }
        })
        affiliateHomeViewModel.validateUserdata().observe(this, { validateUserdata ->
            //TODO
        })
        affiliateHomeViewModel.getProductCards().observe(this, { products ->
            if (products.isNotEmpty()) {
                for (product in products) {
                    adapter.addElement(AffiliateProductCardVHViewModel(product))
                }
                //TODO
            } else {
                showNoAffiliate()
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
                affiliate_progress_bar?.gone()
                if (resultCode == Activity.RESULT_OK) {
                    affiliateHomeViewModel.getAffiliateValidateUser()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
