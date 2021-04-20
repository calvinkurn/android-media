package com.tokopedia.shop.settings.notes.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.widget.Toolbar
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNotesListFragment
import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNotesReorderFragment
import kotlinx.android.synthetic.main.partial_toolbar_save_button.*
import java.util.*

/**
 * Deeplink: SHOP_SETTINGS_NOTES
 */
class ShopSettingsNotesActivity : BaseSimpleActivity(),
        ShopSettingsNotesListFragment.OnShopSettingsNoteFragmentListener,
        ShopSettingsNotesReorderFragment.OnShopSettingsNotesReorderFragmentListener {

    private val reorderFragment: ShopSettingsNotesReorderFragment?
        get() = supportFragmentManager
                .findFragmentByTag(ShopSettingsNotesReorderFragment.TAG) as ShopSettingsNotesReorderFragment

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, ShopSettingsNotesActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvSave.setOnClickListener {
            val fragment = reorderFragment
            fragment?.saveReorder()
        }
        tvSave.visibility = View.GONE
    }

    override fun inflateFragment() {
        val newFragment = newFragment ?: return
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, newFragment, tagFragment)
                .commit()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        findViewById<Toolbar>(R.id.toolbar).apply {
            setTitleTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
            setSupportActionBar(this)
        }
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.title = this.title
        }
    }

    override fun getNewFragment(): Fragment {
        return ShopSettingsNotesListFragment.newInstance()
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
        return R.layout.activity_shop_settings_note
    }

    override fun goToReorderFragment(shopNoteUiModels: ArrayList<ShopNoteUiModel>) {
        val fragment = ShopSettingsNotesReorderFragment.newInstance(shopNoteUiModels)
        replaceAndHideOldFragment(fragment, true, ShopSettingsNotesReorderFragment.TAG)
        invalidateOptionsMenu()
        // handler is to prevent flicker when invalidating option menu
        Handler().post { tvSave.visibility = View.VISIBLE }
    }

    override fun onSuccessReorderNotes() {
        onBackPressed()
        if (showFragment(tagFragment)) {
            val fragment = fragment as ShopSettingsNotesListFragment
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
