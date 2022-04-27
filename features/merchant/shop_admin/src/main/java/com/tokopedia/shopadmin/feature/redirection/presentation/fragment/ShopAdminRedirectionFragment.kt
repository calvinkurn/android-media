package com.tokopedia.shopadmin.feature.redirection.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.shopadmin.ShopAdminDeepLinkMapper
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.shopadmin.common.constants.AdminStatus
import com.tokopedia.shopadmin.common.presentation.uimodel.AdminTypeUiModel
import com.tokopedia.shopadmin.databinding.FragmentShopAdminRedirectionBinding
import com.tokopedia.shopadmin.feature.redirection.di.component.ShopAdminRedirectionComponent
import com.tokopedia.shopadmin.feature.redirection.presentation.viewmodel.ShopAdminRedirectionViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ShopAdminRedirectionFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            ShopAdminRedirectionViewModel::class.java
        )
    }

    private var binding by autoClearedNullable<FragmentShopAdminRedirectionBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShopAdminRedirectionComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopAdminRedirectionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchAdminType()
        observeAdminType()
    }

    private fun fetchAdminType() {
        viewModel.fetchAdminType()
    }

    private fun observeAdminType() {
        observe(viewModel.adminType) {
            when (it) {
                is Success -> {
                    redirectionShopAdmin(it.data)
                }
                is Fail -> {

                }
            }
        }
    }

    private fun redirectionShopAdmin(adminTypeUiModel: AdminTypeUiModel) {
        if (GlobalConfig.isSellerApp()) {
            redirectionShopAdminToLoginSA(adminTypeUiModel)
        }
    }

    private fun redirectionShopAdminToLoginSA(adminTypeUiModel: AdminTypeUiModel) {
        val appLink = if (adminTypeUiModel.isShopOwner) {
            ApplinkConstInternalSellerapp.SELLER_HOME
        } else {
            if (adminTypeUiModel.shopID !in DEFAULT_SHOP_ID_NOT_OPEN) {
                if (adminTypeUiModel.isShopAdmin) {
                    if (adminTypeUiModel.status == AdminStatus.ACTIVE) {
                        ApplinkConstInternalSellerapp.SELLER_HOME
                    } else {
                        ApplinkConstInternalMarketplace.ADMIN_INVITATION
                    }
                } else {
                    ApplinkConstInternalGlobal.PHONE_SHOP_CREATION
                }
            } else {
                ApplinkConstInternalGlobal.PHONE_SHOP_CREATION
            }
        }
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        intent.putExtra(ShopAdminDeepLinkMapper.ARGS_APPLINK_FROM_SHOP_ADMIN, appLink)
        startActivityForResult(intent, ShopAdminDeepLinkMapper.REQUEST_CODE_SHOP_ADMIN_SA)
        activity?.finish()
    }

    companion object {
        fun newInstance(): ShopAdminRedirectionFragment {
            return ShopAdminRedirectionFragment()
        }

        private val DEFAULT_SHOP_ID_NOT_OPEN = listOf("-1", '0')
    }

}