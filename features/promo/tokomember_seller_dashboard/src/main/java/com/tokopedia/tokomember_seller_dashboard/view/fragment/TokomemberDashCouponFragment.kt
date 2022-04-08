package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.CheckEligibility
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_OPEN_BS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberEligibilityViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokomemberDashCouponFragment : BaseDaggerFragment() {

    private var shopId = 0

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberEligibilityViewModel: TokomemberEligibilityViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberEligibilityViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_coupon_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        observeViewModel()
//        tokomemberEligibilityViewModel.getSellerInfo()
    }

    private fun observeViewModel() {

        tokomemberEligibilityViewModel.sellerInfoResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    shopId = it.data.userShopInfo?.info?.shopId.toIntOrZero()
                    tokomemberEligibilityViewModel.checkEligibility(shopId, true)
                }
                is Fail -> {
                }
            }
        })

        tokomemberEligibilityViewModel.eligibilityCheckResultLiveData.observe(viewLifecycleOwner, {
            when(it){
                is Success ->{
                    checkEligibility(it.data)
                }
                is Fail ->{
                    it.throwable.message
                }
            }
        })
    }

    private fun checkEligibility(data: CheckEligibility?) {
        if(data?.eligibilityCheckData?.isEligible == true)
        {
            if (data.eligibilityCheckData.message.title.isNullOrEmpty() and data.eligibilityCheckData.message.subtitle.isNullOrEmpty())
            {

                val intent = Intent(requireContext(), TokomemberDashIntroActivity::class.java)
                intent.putExtra(BUNDLE_OPEN_BS, true)
                intent.putExtra(BUNDLE_SHOP_ID, shopId)
                startActivity(intent)
                requireActivity().finish()
//                startActivity(Intent(requireContext(), TokomemberDashHomeActivity::class.java))
//                requireActivity().finish()
                // redirect to dashboard
            }
            else{

                val intent = Intent(requireContext(), TokomemberDashIntroActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
                // redirect to intro page
            }
        }
        else{
            val intent = Intent(requireContext(), TokomemberDashIntroActivity::class.java)
            intent.putExtra(BUNDLE_OPEN_BS, true)
            intent.putExtra(BUNDLE_SHOP_ID, shopId)
            startActivity(intent)
            requireActivity().finish()
            // redirect to intro page + bottomsheet
        }
    }


    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    companion object {
        fun newInstance(): TokomemberDashCouponFragment {
            return TokomemberDashCouponFragment()
        }
    }
}