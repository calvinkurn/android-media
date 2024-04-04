package com.tokopedia.loginregister.shopcreation.view.landingshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.loginregister.databinding.FragmentKycBridgingBinding
import com.tokopedia.loginregister.shopcreation.common.IOnBackPressed
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent
import com.tokopedia.loginregister.shopcreation.view.KycBridgingViewModel
import com.tokopedia.loginregister.shopcreation.view.base.BaseShopCreationFragment
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class KycBridgingFragment : BaseShopCreationFragment(), IOnBackPressed {

    private var viewBinding by autoClearedNullable<FragmentKycBridgingBinding>()

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
            when (viewModel.selectedShopType.value) {
                TYPE_NORMAL_SHOP -> {
                    viewModel.checkKycStatus()

                }
                TYPE_OFFICIAL_SHOP -> {
                    showOfficialShopBottomSheet()
                }
                else -> {
                    // Handle Error
                }
            }
        }
    }

    private fun goToKycFlow() {

    }

    private fun showOfficialShopBottomSheet() {

    }

    private fun onKycFinished() {

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
