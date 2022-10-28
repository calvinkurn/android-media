package com.tokopedia.tokomember_seller_dashboard.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.CheckEligibility
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.PATH_TOKOMEMBER_COUPON_CREATION
import com.tokopedia.tokomember_seller_dashboard.util.PATH_TOKOMEMBER_PROGRAM_CREATION
import com.tokopedia.tokomember_seller_dashboard.util.PATH_TOKOMEMBER_PROGRAM_EXTENSION
import com.tokopedia.tokomember_seller_dashboard.util.REQUEST_CODE_REFRESH_PROGRAM_LIST
import com.tokopedia.tokomember_seller_dashboard.util.TM_SELLER_NO_OS
import com.tokopedia.tokomember_seller_dashboard.view.activity.TmDashCreateActivity
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashHomeActivity
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmEligibilityViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_intro_new.*
import kotlinx.android.synthetic.main.tm_layout_no_access.*
import javax.inject.Inject

class TokomemberMainFragment : BaseDaggerFragment(),TmCouponListRefreshCallback {

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
        return inflater.inflate(R.layout.tm_dash_intro_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        tmEligibilityViewModel.getSellerInfo()
        tmTracker = TmTracker()
        iv_error.loadImage(TM_SELLER_NO_OS)
        btn_error.setOnClickListener {
            tmTracker?.clickBackHomeBSNoAccess(shopId.toString())
            activity?.finish()
        }
    }

    private fun observeViewModel() {

        tmEligibilityViewModel.sellerInfoResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    shopId = it.data.userShopInfo?.info?.shopId.toIntOrZero()
                    shopAvatar = it.data.userShopInfo?.info?.shopAvatar?:""
                    shopName = it.data.userShopInfo?.info?.shopName?:""
                    tmEligibilityViewModel.getOnboardingInfo(shopId)
                }
                is Fail -> {
                    viewFlipperIntro.displayedChild = 2
                }
            }
        })

        tmEligibilityViewModel.tokomemberOnboardingResultLiveData.observe(viewLifecycleOwner,{
          when(it) {
              is Success -> {
                  if (it.data.membershipGetSellerOnboarding?.resultStatus?.code == "200"){
                      if (it.data.membershipGetSellerOnboarding?.sellerHomeContent?.isShowContent == true){
                          tmEligibilityViewModel.checkEligibility(shopId, true)
                      } else{
                          if(!routeToCreationScreen(it.data.membershipGetSellerOnboarding?.cardID?.toIntOrZero()?:0))
                            TokomemberDashHomeActivity.openActivity(shopId,it.data.membershipGetSellerOnboarding?.cardID?.toIntOrZero()?:0, context)
                          activity?.finish()
                      }
                  } else{
                      viewFlipperIntro?.displayedChild = 2
                      headerNoAccess.show()
                      headerNoAccess.title = context?.getString(R.string.tm_tokomember).toString()
                      headerNoAccess.setNavigationOnClickListener {
                          activity?.finish()
                      }
                      tmTracker?.viewBSNoAccess(shopId.toString())
                  }
              }
              is Fail -> {
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
        if (data?.eligibilityCheckData?.isEligible == true) {
            TokomemberDashIntroActivity.openActivity(
                shopId,
                shopAvatar,
                shopName,
                context = context
            )
            activity?.finish()
        } else {
            TokomemberDashIntroActivity.openActivity(shopId, shopAvatar, shopName, true, context)
            activity?.finish()
        }
    }

    private fun routeToCreationScreen(cardId:Int) : Boolean{
        activity?.intent?.data?.let{
            when(it.lastPathSegment){
                PATH_TOKOMEMBER_PROGRAM_CREATION -> {
                    TmDashCreateActivity.openActivity(
                        shopId, activity, CreateScreenType.PROGRAM, ProgramActionType.CREATE_BUAT, REQUEST_CODE_REFRESH_PROGRAM_LIST, null, cardId
                    )
                    return true
                }
                PATH_TOKOMEMBER_COUPON_CREATION -> {
                    TmDashCreateActivity.openActivity(
                        activity,
                        CreateScreenType.COUPON_SINGLE,
                        null,
                        this,
                        edit = false
                    )
                    return true
                }
                else -> {
                    val paths = it.pathSegments
                    if(paths.size>=2 && paths[1]== PATH_TOKOMEMBER_PROGRAM_EXTENSION){
                        TmDashCreateActivity.openActivity(shopId, activity, CreateScreenType.PROGRAM, ProgramActionType.EXTEND, null, it.lastPathSegment.toIntOrZero())
                        return true
                    }
                }
            }
        }
        return false
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

    override fun refreshCouponList(action: String) {}
}
