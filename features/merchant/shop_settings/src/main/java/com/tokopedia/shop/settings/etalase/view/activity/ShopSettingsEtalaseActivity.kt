package com.tokopedia.shop.settings.etalase.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseListFragment
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseReorderFragment
import kotlinx.android.synthetic.main.partial_toolbar_save_button.*
import java.util.*

/**
 * Deeplink: SHOP_SETTINGS_ETALASE
 */
class ShopSettingsEtalaseActivity : BaseSimpleActivity(),
        ShopSettingsEtalaseListFragment.OnShopSettingsEtalaseFragmentListener,
        ShopSettingsEtalaseReorderFragment.OnShopSettingsEtalaseReorderFragmentListener {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, ShopSettingsEtalaseActivity::class.java)
    }

    private val reorderFragment: ShopSettingsEtalaseReorderFragment?
        get() = supportFragmentManager
                .findFragmentByTag(ShopSettingsEtalaseReorderFragment.TAG) as ShopSettingsEtalaseReorderFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvSave.setOnClickListener {
            val fragment = reorderFragment
            fragment?.saveReorder()
        }
        tvSave.visibility = View.GONE
    }

    override fun getNewFragment(): Fragment {
        return ShopSettingsEtalaseListFragment.newInstance()
    }

    override fun inflateFragment() {
        val newFragment = newFragment ?: return
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, newFragment, tagFragment)
                .commit()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
            supportActionBar!!.title = this.title
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount != 0) {
            tvSave.visibility = View.GONE
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_shop_setting_etalase
    }

    override fun goToReorderFragment(shopEtalaseViewModelsDefault: ArrayList<ShopEtalaseViewModel>, shopEtalaseViewModels: ArrayList<ShopEtalaseViewModel>) {
        val fragment = ShopSettingsEtalaseReorderFragment.newInstance(shopEtalaseViewModelsDefault, shopEtalaseViewModels)
        replaceAndHideOldFragment(fragment, true, ShopSettingsEtalaseReorderFragment.TAG)
        invalidateOptionsMenu()
        // handler is to prevent flicker when invalidating option menu
        Handler().post { tvSave.visibility = View.VISIBLE }
    }

    override fun onSuccessReorderEtalase() {
        onBackPressed()
        if (showFragment(tagFragment)) {
            val fragment = fragment as ShopSettingsEtalaseListFragment
            fragment.refreshData()
        }
    }

    fun replaceAndHideOldFragment(fragment: Fragment, addToBackstack: Boolean, tag: String) {
        val fragmentManager = supportFragmentManager
        val ft = fragmentManager.beginTransaction()
        val currentVisibileFragment = getCurrentVisibleFragment(fragmentManager)
        if (currentVisibileFragment != null) {
            ft.hide(currentVisibileFragment)
        }
        if (!addToBackstack) {
            ft.add(R.id.parent_view, fragment, tag).show(fragment).commit()
        } else {
            ft.add(R.id.parent_view, fragment, tag).addToBackStack(tag).show(fragment).commit()
        }
    }

    fun showFragment(tag: String): Boolean {
        val f = supportFragmentManager.findFragmentByTag(tag)
        if (f == null) {
            return false
        } else {
            val fragmentManager = supportFragmentManager
            val ft = fragmentManager.beginTransaction()
            val currentVisibileFragment = getCurrentVisibleFragment(fragmentManager)
            if (currentVisibileFragment != null) {
                ft.hide(currentVisibileFragment)
            }
            ft.show(f).commit()
            return true
        }
    }

    // assume only 1 fragment visible
    private fun getCurrentVisibleFragment(fragmentManager: FragmentManager): Fragment? {
        val fragmentList = fragmentManager.fragments
        var i = 0
        val sizei = fragmentList.size
        while (i < sizei) {
            val f = fragmentList[i]
            if (f != null && f.isVisible) {
                return f
            }
            i++
        }
        return null
    }

}
