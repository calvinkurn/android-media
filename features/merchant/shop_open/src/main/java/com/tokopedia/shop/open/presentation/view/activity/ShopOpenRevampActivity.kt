package com.tokopedia.shop.open.presentation.view.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.common.ExitDialog
import com.tokopedia.shop.open.common.PageNameConstant
import com.tokopedia.shop.open.listener.FragmentNavigationInterface
import com.tokopedia.shop.open.presentation.view.fragment.ShopOpenRevampFinishFragment
import com.tokopedia.shop.open.presentation.view.fragment.ShopOpenRevampInputShopFragment
import com.tokopedia.shop.open.presentation.view.fragment.ShopOpenRevampInputShopFragment.Companion.FIRST_FRAGMENT_TAG
import com.tokopedia.shop.open.presentation.view.fragment.ShopOpenRevampQuisionerFragment
import com.tokopedia.shop.open.presentation.view.fragment.ShopOpenRevampSplashScreenFragment
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class ShopOpenRevampActivity : BaseActivity(), FragmentNavigationInterface {

    private val userSession: UserSessionInterface by lazy {
        UserSession(this)
    }
    private var isNeedLocation = false
    private var shouldShowExitDialog = true
    private var bundle: Bundle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_shop_open_revamp)
        setupFirstFragment()
        setupStatusBar()

        intent.extras?.let {
            isNeedLocation = intent.getBooleanExtra(ApplinkConstInternalMarketplace.PARAM_IS_NEED_LOC, false)
            bundle = it
        }

        if (userSession.hasShop() && isNeedLocation) {
            val fragmentQuisionerPage = ShopOpenRevampQuisionerFragment()
            fragmentQuisionerPage.arguments = bundle
            navigateToOtherFragment(fragmentQuisionerPage, FIRST_FRAGMENT_TAG)
        } else if (userSession.hasShop() && !isNeedLocation) {
            RouteManager.route(this, ApplinkConst.SHOP, userSession.shopId)
            finish()
        }
    }

    private fun setupFirstFragment() {
        val fragmentValidateShopName = ShopOpenRevampInputShopFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
                .replace(R.id.shop_open_container, fragmentValidateShopName)
                .commit()
    }

    override fun navigateToNextPage(page: String, tag: String) {
        if (page == PageNameConstant.SPLASH_SCREEN_PAGE) {
            val fragmentSplashScreenPage = ShopOpenRevampSplashScreenFragment()
            navigateToOtherFragment(fragmentSplashScreenPage, null)
        } else if (page == PageNameConstant.QUISIONER_PAGE) {
            val fragmentQuisionerPage = ShopOpenRevampQuisionerFragment()
            navigateToOtherFragment(fragmentQuisionerPage, tag)
        } else if (page == PageNameConstant.FINISH_SPLASH_SCREEN_PAGE) {
            val fragmentFinishPage = ShopOpenRevampFinishFragment()
            navigateToOtherFragment(fragmentFinishPage, tag)
            shouldShowExitDialog = false
        }
    }

    override fun onBackPressed() {
        showExitDialog()
    }

    override fun showExitDialog() {
        if (!shouldShowExitDialog) {
            return
        }

        val exitDialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        exitDialog.apply {
            setTitle(ExitDialog.TITLE)
            setDescription(ExitDialog.DESCRIPTION)
            setPrimaryCTAText(getString(R.string.open_shop_cancel))
            setPrimaryCTAClickListener {
                this.dismiss()
            }
            setSecondaryCTAText(getString(R.string.open_shop_logout_button))
            setSecondaryCTAClickListener {
                if (GlobalConfig.isSellerApp()) {
                    RouteManager.route(exitDialog.context, ApplinkConstInternalGlobal.LOGOUT)
                }
                finish()
            }
            show()
        }
    }

    private fun navigateToOtherFragment(fragment: Fragment, tag: String?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
                .replace(R.id.shop_open_container, fragment)
                .addToBackStack(tag)
                .commit()
    }

    private fun setupStatusBar(){
        val window: Window = getWindow()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            getWindow().statusBarColor = resources.getColor(android.R.color.white)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

}
