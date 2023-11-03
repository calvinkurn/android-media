package com.tokopedia.shop.info.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.FragmentShopInfoReimagineBinding
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent
import com.tokopedia.shop.info.di.component.ShopInfoComponent
import com.tokopedia.shop.info.di.module.ShopInfoModule
import com.tokopedia.shop.info.view.model.ShopInfoUiState
import com.tokopedia.shop.info.view.viewmodel.ShopInfoReimagineViewModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject


class ShopInfoReimagineFragment : BaseDaggerFragment(), HasComponent<ShopInfoComponent> {

    companion object {
        private const val BUNDLE_KEY_SHOP_ID = "shop_id"
        
        @JvmStatic
        fun newInstance(shopId: String): ShopInfoReimagineFragment {
            return ShopInfoReimagineFragment().apply {
                arguments = Bundle().apply {
                   putString(BUNDLE_KEY_SHOP_ID, shopId)
                }
            }
        }

    }

    private var binding by autoClearedNullable<FragmentShopInfoReimagineBinding>()
   

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession : UserSessionInterface


    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[ShopInfoReimagineViewModel::class.java] }

    private val shopId by lazy { arguments?.getString(BUNDLE_KEY_SHOP_ID, "").orEmpty() }

    override fun getScreenName(): String = ShopInfoReimagineFragment::class.java.canonicalName.orEmpty()

    override fun getComponent() = activity?.run {
        DaggerShopInfoComponent.builder()
            .shopInfoModule(ShopInfoModule())
            .shopComponent(ShopComponentHelper().getComponent(application, this))
            .build()
    }
    

    override fun initInjector() {
        component?.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopInfoReimagineBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        setupView()
        observeUiState()

        val localCacheModel = ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: return
        viewModel.getShopInfo(shopId, localCacheModel)
        registerBackPressEvent()
    }

    private fun registerBackPressEvent() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupView() {
        binding?.run {
            tpgShopPharmacyCtaViewAll.setOnClickListener {  }
            tpgShopPharmacyCtaViewMaps.setOnClickListener {  }
        }
    }

    
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }
    

    private fun handleUiState(uiState: ShopInfoUiState) {
        renderShopCoreInfo(uiState)
        renderShopInfo(uiState)
        renderShopPharmacy(uiState)
        renderShopReview(uiState)
        renderShopPerformance(uiState)
        renderShopNotes(uiState)
        renderShopDescription(uiState)
        renderShopSupportedShipment(uiState)
    }
    
    private fun renderShopCoreInfo(uiState: ShopInfoUiState) {
        binding?.run { 
            
        }
    }
    private fun renderShopInfo(uiState: ShopInfoUiState) {
        binding?.run {

        }
    }
    private fun renderShopPharmacy(uiState: ShopInfoUiState) {
        binding?.run {

        }
    }

    private fun renderShopReview(uiState: ShopInfoUiState) {
        binding?.run {

        }
    }
    private fun renderShopPerformance(uiState: ShopInfoUiState) {
        binding?.run {

        }
    }

    private fun renderShopNotes(uiState: ShopInfoUiState) {
        binding?.run {

        }
    }
    private fun renderShopDescription(uiState: ShopInfoUiState) {
        binding?.run {

        }
    }

    private fun renderShopSupportedShipment(uiState: ShopInfoUiState) {
        binding?.run {

        }
    }


}
