package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticaddaddress.R

/**
 * Created by fwidjaja on 2019-05-07.
 */
class AddNewAddressActivity: BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_add_new_address
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        // setupToolbar()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getNewFragment(): Fragment? {
        /*var fragment: Fragment? = null
        if (intent.extras != null) {
            val bundle = intent.extras
            fragment = AddNewAddressFragment.newInstance(bundle)
        }*/
        return AddNewAddressFragment.newInstance()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_add_new_address)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        /*toolbar_add_new_address.setNavigationIcon(R.drawable.ic_icon_back_black)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.title = this.title
            updateTitle("")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }*/
    }
}