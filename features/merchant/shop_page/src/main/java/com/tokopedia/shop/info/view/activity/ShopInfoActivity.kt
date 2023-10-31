package com.tokopedia.shop.info.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent
import com.tokopedia.shop.info.di.module.ShopInfoModule
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment
import com.tokopedia.shop.info.view.fragment.ShopInfoScreen
import com.tokopedia.shop.info.view.viewmodel.ShopInfoReimagineViewModel
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Navigate to ShopInfoActivity
 * use [com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_INFO]
 */
class ShopInfoActivity : BaseActivity(), HasComponent<ShopComponent>{
    
    private var fragment : Fragment? = null
    
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[ShopInfoReimagineViewModel::class.java]
    }
    private val shopId by lazy {
        intent.getStringExtra(SHOP_ID) ?: intent.data?.lastPathSegment.orEmpty()
    }
    private val shopData by lazy {
        intent.getParcelableExtra<ShopInfoData?>(EXTRA_SHOP_INFO)
    }
    companion object {
        fun createIntent(
            context: Context,
            shopId: String? = null,
            shopInfo: ShopInfoData? = null
        ): Intent {
            return Intent(context, ShopInfoActivity::class.java).apply {
                putExtra(SHOP_ID, shopId)
                putExtra(EXTRA_SHOP_INFO, shopInfo)
            }
        }

        const val EXTRA_SHOP_INFO = "extra_shop_info"
        const val SHOP_ID = "EXTRA_SHOP_ID"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        setupDependencyInjection()
        super.onCreate(savedInstanceState)

        val useCompose = true
        
        if (useCompose) {
            renderWithCompose()
        } else {
            render()
        }
    }
    private fun setupDependencyInjection() {
        DaggerShopInfoComponent.builder().shopInfoModule(ShopInfoModule())
            .shopComponent(getComponent())
            .build()
            .inject(this)
    }
    
    private fun render() {
        setContentView(R.layout.activity_shop_info)
        setupBackgroundColor()
        showFragment()
    }
    
    
    private fun setupBackgroundColor() {
        window?.decorView?.setBackgroundColor(MethodChecker.getColor(this, unifyprinciplesR.color.Unify_Background))
    }
    
    private fun renderWithCompose() {
        val localCacheModel = ShopUtil.getShopPageWidgetUserAddressLocalData(this) ?: return
        viewModel.getShopInfo(shopId, localCacheModel)
        
        setContent {
          /*  // Observe for any effect / one-time event
            LaunchedEffect(Unit) {
                viewModel.uiEffect.collectLatest { effect ->
                    when (effect) {
                        is CampaignListViewModel.UiEffect.ShowShareBottomSheet -> {
                            val banner = effect.banner
                            viewModel.setMerchantBannerData(banner)
                            showShareBottomSheet(banner)
                        }
                    }
                }
            }*/
            
            val uiState = viewModel.uiState.collectAsState()
            
            NestTheme {
                ShopInfoScreen(uiState = uiState.value)
            }
       
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
        showFragment()
    }

    override fun getScreenName() : String = ShopInfoActivity::class.java.simpleName
   
    override fun onBackPressed() {
        super.onBackPressed()
        (fragment as? ShopInfoFragment)?.onBackPressed()
    }
    

    private fun showFragment() {
        val shopInfoFragment = ShopInfoFragment.createInstance(shopId, shopData)
        
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, shopInfoFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun getComponent(): ShopComponent = ShopComponentHelper().getComponent(application, this)
}
