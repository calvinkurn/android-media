package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.CheckEligibility
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashHomeActivity
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmEligibilityViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_intro_container.*
import kotlinx.android.synthetic.main.tm_layout_no_access.*
import javax.inject.Inject

class TokomemberMainFragment : BaseDaggerFragment() {

    private var shopId = 0
    private var shopName = ""
    private var shopAvatar = ""
    private var tmTracker: TmTracker? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmEligibilityViewModel: TmEligibilityViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmEligibilityViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_intro_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        tmEligibilityViewModel.getSellerInfo()
        tmTracker = TmTracker()
        btn_no_access.setOnClickListener {
            Toast.makeText(context, "Not allowed", Toast.LENGTH_SHORT).show()
            tmTracker?.clickBackHomeBSNoAccess(shopId.toString())
        }
    }

    private fun observeViewModel() {

        tmEligibilityViewModel.sellerInfoResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    shopId = it.data.userShopInfo?.info?.shopId.toIntOrZero()
                    shopAvatar = it.data.userShopInfo?.info?.shopAvatar?:""
                    shopName = it.data.userShopInfo?.info?.shopName?:""
                    tmEligibilityViewModel.checkUserEligibility(shopId)
                }
                is Fail -> {
                }
            }
        })

        tmEligibilityViewModel.userEligibilityCheckResultLiveData.observe(viewLifecycleOwner, {
            when (it){
                is Success ->{
                    if(it.data.rbacAuthorizeAccess?.isAuthorized == true) {
                        tmEligibilityViewModel.checkEligibility(shopId, true)
                    }
                    else{
                        viewFlipperIntro?.displayedChild = 2
                        tmTracker?.viewBSNoAccess(shopId.toString())
                        Toast.makeText(context, it.data.rbacAuthorizeAccess?.error, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {

                }
            }
        })

        tmEligibilityViewModel.eligibilityCheckResultLiveData.observe(viewLifecycleOwner, {
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
                TokomemberDashHomeActivity.openActivity(shopId, context)
//                TokomemberDashIntroActivity.openActivity(shopId,shopAvatar,shopName, context = context)
                activity?.finish()
                // redirect to dashboard
            }
            else{
                TokomemberDashIntroActivity.openActivity(shopId,shopAvatar,shopName, context = context)
                activity?.finish()
                // redirect to intro page
            }
        }
        else{
            TokomemberDashIntroActivity.openActivity(shopId, shopAvatar,shopName,true, context)
            activity?.finish()
            // redirect to intro page + bottomsheet
        }
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    companion object {
        fun newInstance(): TokomemberMainFragment {
            return TokomemberMainFragment()
        }
    }
}