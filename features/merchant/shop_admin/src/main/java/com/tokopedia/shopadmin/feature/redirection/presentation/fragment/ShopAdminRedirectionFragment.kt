package com.tokopedia.shopadmin.feature.redirection.presentation.fragment

import android.app.Activity
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

    private val fromParam by lazy {
        arguments?.getString(ShopAdminDeepLinkMapper.FROM_PARAM).orEmpty()
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
                    redirectCreateShopIfFail()
                }
            }
        }
    }

    private fun redirectCreateShopIfFail() {
        val errorMessage =
            getString(com.tokopedia.shopadmin.R.string.error_message_shop_admin_redirection)
        val intent = RouteManager.getIntent(context, ApplinkConst.CREATE_SHOP)
        intent.putExtra(ShopAdminDeepLinkMapper.ARGS_ERROR_MESSAGE_FROM_SHOP_ADMIN, errorMessage)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun redirectionShopAdmin(adminTypeUiModel: AdminTypeUiModel) {
        if (GlobalConfig.isSellerApp()) {
            when (fromParam) {
                ShopAdminDeepLinkMapper.LANDING_SHOP_CREATION -> {
                    redirectShopAdminInSAFromLandingShop(adminTypeUiModel)
                }
                ShopAdminDeepLinkMapper.PHONE_SHOP_CREATION -> {
                    redirectShopAdminInSAFromPhoneShop(adminTypeUiModel)
                }
                else -> activity?.finish()
            }
        } else {
            redirectShopAdminMA(adminTypeUiModel)
        }
    }

    private fun redirectShopAdminInSAFromLandingShop(adminTypeUiModel: AdminTypeUiModel) {
        val appLink =
            if (adminTypeUiModel.shopID != DEFAULT_SHOP_ID_NOT_OPEN && adminTypeUiModel.shopID.isNotEmpty()) {
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

        val intent = RouteManager.getIntent(context, ApplinkConst.CREATE_SHOP)
        intent.putExtra(ShopAdminDeepLinkMapper.ARGS_APPLINK_FROM_SHOP_ADMIN, appLink)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun redirectShopAdminInSAFromPhoneShop(adminTypeUiModel: AdminTypeUiModel) {
        val appLink =
            if (adminTypeUiModel.shopID != DEFAULT_SHOP_ID_NOT_OPEN && adminTypeUiModel.shopID.isNotEmpty()) {
                if (adminTypeUiModel.isShopAdmin) {
                    if (adminTypeUiModel.status == AdminStatus.ACTIVE) {
                        ApplinkConstInternalSellerapp.SELLER_HOME
                    } else {
                        ApplinkConstInternalMarketplace.ADMIN_INVITATION
                    }
                } else {
                    ""
                }
            } else {
                ""
            }

        val intent = RouteManager.getIntent(context, ApplinkConst.CREATE_SHOP)
        intent.putExtra(ShopAdminDeepLinkMapper.ARGS_APPLINK_FROM_SHOP_ADMIN, appLink)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun redirectShopAdminMA(adminTypeUiModel: AdminTypeUiModel) {
        val appLink = if (adminTypeUiModel.shopID != DEFAULT_SHOP_ID_NOT_OPEN &&
            adminTypeUiModel.shopID.isNotEmpty() &&
            adminTypeUiModel.status != AdminStatus.ACTIVE
        ) {
            ApplinkConstInternalMarketplace.ADMIN_INVITATION
        } else {
            ApplinkConstInternalGlobal.PHONE_SHOP_CREATION
        }

        val intent = RouteManager.getIntent(context, ApplinkConst.CREATE_SHOP)
        intent.putExtra(ShopAdminDeepLinkMapper.ARGS_APPLINK_FROM_SHOP_ADMIN, appLink)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    companion object {
        fun newInstance(bundle: Bundle?): ShopAdminRedirectionFragment {
            return if (bundle == null) {
                ShopAdminRedirectionFragment()
            } else {
                ShopAdminRedirectionFragment().apply {
                    arguments = bundle
                }
            }
        }

        private const val DEFAULT_SHOP_ID_NOT_OPEN = "0"
    }

}