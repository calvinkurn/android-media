package com.tokopedia.salam.umrah.common.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.di.UmrahComponent
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance.getUmrahComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheets_umrah_menu.view.*

/**
 * @author by firman on 5/12/19
 */
abstract class UmrahBaseActivity : BaseSimpleActivity() {

    private lateinit var umrahComponent: UmrahComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        initInjector()

    }

    abstract fun getMenuButton(): Int
    abstract fun getShareLink(): String

    private fun initInjector() {
        getUmrahComponent().inject(this)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        showBottomMenus()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(getMenuButton(), menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId ?: "" == R.id.action_overflow_menu) {
            showBottomMenus()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("InflateParams")
    private fun showBottomMenus() {
        val menuBottomSheet = BottomSheetUnify()
        menuBottomSheet.setCloseClickListener { menuBottomSheet.dismiss() }
        menuBottomSheet.setTitle(resources.getString(R.string.umrah_menu_title))
        val view = LayoutInflater.from(applicationContext).inflate(R.layout.bottom_sheets_umrah_menu, null)
        menuBottomSheet.setChild(view)
        menuBottomSheet.show(supportFragmentManager, "")
        setMenuListener(menuBottomSheet, view)
    }


    private fun setMenuListener(menuBottomSheet: BottomSheetUnify, view: View) {
        view.apply {
            tg_umrah_help.setOnClickListener {
                RouteManager.route(this@UmrahBaseActivity, getString(R.string.umrah_help_link))
                menuBottomSheet.dismiss()
            }
            tg_umrah_share.visibility = GONE
            tg_umrah_share.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_TEXT, getShareLink())
                    intent.type = "text/plain"
                    startActivity(intent)
                } catch (e: Exception) {

                }
                menuBottomSheet.dismiss()
            }
            tg_umrah_salam.setOnClickListener {
                RouteManager.route(this@UmrahBaseActivity, getString(R.string.umrah_salam_app_link))
                menuBottomSheet.dismiss()
            }
        }
    }

    protected fun getUmrahComponent(): UmrahComponent {
        if (!::umrahComponent.isInitialized) {
            umrahComponent = getUmrahComponent(application)
        }
        return umrahComponent
    }


    open fun shouldShowMenuWhite(): Boolean = false
}