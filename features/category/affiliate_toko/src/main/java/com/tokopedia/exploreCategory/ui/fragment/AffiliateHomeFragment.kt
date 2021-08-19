package com.tokopedia.exploreCategory.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.exploreCategory.viewmodel.AffiliateHomeViewModel
import javax.inject.Inject

class AffiliateHomeFragment : BaseViewModelFragment<AffiliateHomeViewModel>() {

    companion object {
        private const val EXTRA_USER_ID = "USER_ID"
        private const val EXTRA_EMAIL_ID = "EMAIL_ID"

        val fragmentInstance: Fragment
            get() = AffiliateHomeFragment()

        fun getFragmentInstance(userId: String, email: String): Fragment {
            val fragment = AffiliateHomeFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_USER_ID, userId)
            bundle.putString(EXTRA_EMAIL_ID, email)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        handleBundle()
    }

    private fun handleBundle() {
        if (arguments?.containsKey(EXTRA_USER_ID) == true) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_home_fragment_layout, container, false)
    }

    private fun setObservers() {
    }

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var affiliateHomeViewModel: AffiliateHomeViewModel

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<AffiliateHomeViewModel> {
        return AffiliateHomeViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateHomeViewModel = viewModel as AffiliateHomeViewModel
    }
}