package com.tokopedia.loginregister.shopcreation.view.landingshop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.loginregister.databinding.FragmentKycBridgingBinding
import com.tokopedia.loginregister.shopcreation.common.IOnBackPressed
import com.tokopedia.loginregister.shopcreation.data.ShopStatus
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent
import com.tokopedia.loginregister.shopcreation.domain.ProjectInfoResult
import com.tokopedia.loginregister.shopcreation.view.KycBridgingViewModel
import com.tokopedia.loginregister.shopcreation.view.base.BaseShopCreationFragment
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class KycBridgingFragment : BaseShopCreationFragment(), IOnBackPressed {

    private var viewBinding by autoClearedNullable<FragmentKycBridgingBinding>()

    private val startReVerifyKycForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            onKycFinished()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(KycBridgingViewModel::class.java)
    }

    override fun getScreenName(): String = ShopCreationAnalytics.SCREEN_LANDING_SHOP_CREATION

    override fun initInjector() = getComponent(ShopCreationComponent::class.java).inject(this)

    override fun getToolbar(): Toolbar? = viewBinding?.toolbarShopCreation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentKycBridgingBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        viewBinding?.normalShopCard?.run {
            setOnClickListener {
                viewModel.setSelectedShopType(TYPE_NORMAL_SHOP)
            }
        }

        viewBinding?.officialShopCard?.run {
            setOnClickListener {
                viewModel.setSelectedShopType(TYPE_OFFICIAL_SHOP)
            }
        }

        viewModel.selectedShopType.observe(viewLifecycleOwner) {
            viewBinding?.normalShopCard?.hasCheckIcon = it == TYPE_NORMAL_SHOP
            viewBinding?.officialShopCard?.hasCheckIcon = it == TYPE_OFFICIAL_SHOP
            when (it) {
                TYPE_NORMAL_SHOP -> {
                    viewBinding?.normalShopCard?.changeTypeWithTransition(CardUnify2.TYPE_BORDER_ACTIVE)
                    viewBinding?.officialShopCard?.changeTypeWithTransition(CardUnify2.TYPE_BORDER_FROM_SHADOW)
                }
                TYPE_OFFICIAL_SHOP -> {
                    viewBinding?.officialShopCard?.changeTypeWithTransition(CardUnify2.TYPE_BORDER_ACTIVE)
                    viewBinding?.normalShopCard?.changeTypeWithTransition(CardUnify2.TYPE_BORDER_FROM_SHADOW)
                }
            }
        }

        viewBinding?.btnContinue?.setOnClickListener {
            viewModel.getShopStatus()
        }

        viewModel.projectInfo.observe(viewLifecycleOwner) {
            when (it) {
                is ProjectInfoResult.Verified -> {
                    gotoCreateShop()
                }
                is ProjectInfoResult.NotVerified -> {
                    goToKycFlow()
                }
                is ProjectInfoResult.Failed -> {
                    handleApiError()
                }
                else -> {
                    gotoStatusPage()
                }
            }
        }

        viewModel.shopStatus.observe(viewLifecycleOwner) {
            when (it) {
                is ShopStatus.NotRegistered -> {
                    if (isNormalShopSelected()) {
                        viewModel.checkKycStatus()
                    } else {
                        showOfficialShopBottomSheet()
                    }
                }

                is ShopStatus.Pending -> {
                    Toaster.build(viewBinding?.root!!, "Pendaftaran Official Store kamu lagi diproses dalam 14 hari kerja. Cek statusnya lewat desktop.", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                }

                is ShopStatus.Error -> {
                    Toaster.build(viewBinding?.root!!, it.throwable.message ?: "", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun isNormalShopSelected(): Boolean {
        return viewModel.selectedShopType.value == TYPE_NORMAL_SHOP
    }

    private fun handleApiError() {

    }

    private fun gotoStatusPage() {
        activity?.let {
            if (it is LandingShopCreationActivity) {
                it.switchToKycStatusFragment()
            }
        }
    }

    private fun gotoCreateShop() {
        activity?.let {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.OPEN_SHOP)
            intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            it.startActivity(intent)
            it.finish()
        }
    }

    private fun goToKycFlow() {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalUserPlatform.GOTO_KYC).apply {
            putExtra(ApplinkConstInternalUserPlatform.PARAM_SOURCE, "")
            putExtra(ApplinkConstInternalUserPlatform.PARAM_CALL_BACK, "")
            putExtra(ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID, "10")
        }
        startReVerifyKycForResult.launch(intent)
    }

    private fun showOfficialShopBottomSheet() {
        OfficialShopLandingBottomSheet.createInstance().apply {
            setOnStayClick {  }
            setOnWebviewClick {  }
        }.show(childFragmentManager)
    }

    private fun onKycFinished() {
        viewModel.checkKycStatus()
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    companion object {
        fun createInstance(bundle: Bundle): KycBridgingFragment {
            val fragment = KycBridgingFragment()
            fragment.arguments = bundle
            return fragment
        }

        const val TYPE_NONE = -1
        const val TYPE_NORMAL_SHOP = 1
        const val TYPE_OFFICIAL_SHOP = 2
    }
}
