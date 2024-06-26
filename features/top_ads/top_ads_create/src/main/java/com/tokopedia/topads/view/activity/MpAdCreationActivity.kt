package com.tokopedia.topads.view.activity


import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.common.domain.model.TopadsShopInfoV2Model
import com.tokopedia.topads.constants.MpTopadsConst.PRODUCT_ID_PARAM
import com.tokopedia.topads.create.R
import com.tokopedia.topads.create.databinding.MpAdCreationActivityBinding
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.view.fragment.MpAdCreationOnboardingFragment
import com.tokopedia.topads.view.fragment.MpAdGroupFragment
import com.tokopedia.topads.view.fragment.MpCreateAdGroupFragment
import com.tokopedia.topads.view.model.MpAdCreationViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class MpAdCreationActivity : BaseActivity(),HasComponent<CreateAdsComponent> ,RoutingCallback{

    companion object{
        private const val TAG = "MP_TOPADS_CREATE"
        private const val PRODUCT_TYPE = "product"
    }


    @JvmField @Inject
    var userSession: UserSessionInterface?=null

    @JvmField @Inject
    var factory:ViewModelProvider.Factory? = null

    private val adCreationViewModel by lazy {
        if(factory==null) null
        else ViewModelProvider(this,factory!!).get(MpAdCreationViewModel::class.java)
    }

    private var binding:MpAdCreationActivityBinding?=null

    private var productId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MpAdCreationActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)
        getProductIdFromIntent()
        initInjector()
        observeViewModel()
        adCreationViewModel?.getShopInfo(userSession?.shopId.orEmpty())
    }

    private fun getProductIdFromIntent(){
        productId = intent.data?.getQueryParameter(PRODUCT_ID_PARAM).orEmpty()
    }

    private fun initInjector(){
        component.inject(this)
    }

    private fun observeViewModel(){
        adCreationViewModel?.shopInfoResult?.observe(this){
            when(it){
                is Success -> {
                    binding?.mpAdCreationFlipper?.displayedChild = 1
                    handleFragmentRender(it.data)
                }
                else -> {}
            }
        }
    }

    private fun handleFragmentRender(data:TopadsShopInfoV2Model){
        val productType = data.topadsGetShopInfo.data.ads.find { it.type == PRODUCT_TYPE }
        if(productType!=null && productType.isUsed){
            addFragment(MpAdGroupFragment.newInstance(productId))
        }
        else{
            addFragment(MpAdCreationOnboardingFragment.newInstance(productId))
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1){
            supportFragmentManager.popBackStack()
        }
        super.onBackPressed()
    }

    private fun addFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                com.tokopedia.abstraction.R.anim.slide_in_right,
                com.tokopedia.abstraction.R.anim.slide_out_left,
                com.tokopedia.abstraction.R.anim.slide_in_left,
                com.tokopedia.abstraction.R.anim.slide_out_right
            )
            .replace(R.id.mp_ad_creation_container,fragment)
            .addToBackStack(TAG)
            .commit()
    }

    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun addCreateAds() {
        addFragment(MpCreateAdGroupFragment.newInstance(productId))
    }
}

interface RoutingCallback{
    fun addCreateAds()
}
