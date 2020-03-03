package com.tokopedia.shop.open.shop_open_revamp.presentation.view.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.shop_open_revamp.common.ExitDialog
import com.tokopedia.shop.open.shop_open_revamp.common.PageNameConstant
import com.tokopedia.shop.open.shop_open_revamp.listener.FragmentNavigationInterface
import com.tokopedia.shop.open.shop_open_revamp.presentation.view.fragment.*
import com.tokopedia.shop.open.shop_open_revamp.presentation.view.fragment.ShopOpenRevampInputShopFragment.Companion.FIRST_FRAGMENT_TAG

class ShopOpenRevampActivity : BaseActivity(), FragmentNavigationInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_shop_open_revamp)
        setupFirstFragment()
        setupStatusBar()

        if (intent.extras != null) {
            val isNeedLocation = intent.getBooleanExtra(ApplinkConstInternalMarketplace.PARAM_IS_NEED_LOC, false)
            if (isNeedLocation) {
                val fragmentQuisionerPage = ShopOpenRevampQuisionerFragment()
                fragmentQuisionerPage.arguments = intent.extras
                navigateToOtherFragment(fragmentQuisionerPage, FIRST_FRAGMENT_TAG)
            }
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
        }
    }

    override fun onBackPressed() {
        showExitDialog()
    }

    private fun navigateToOtherFragment(fragment: Fragment, tag: String?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
                .replace(R.id.shop_open_container, fragment)
                .addToBackStack(tag)
                .commit()
    }

    private fun showExitDialog() {
        var exitDialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        exitDialog.apply {
            setTitle(ExitDialog.TITLE)
            setDescription(ExitDialog.DESCRIPTION)
            setPrimaryCTAText("Batal")
            setPrimaryCTAClickListener {
                this.dismiss()
            }
            setSecondaryCTAText("Keluar")
            setSecondaryCTAClickListener {
                finish()
            }
            show()
        }
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
