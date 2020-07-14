package com.tokopedia.updateinactivephone.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.view.View
import android.view.WindowManager

import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.view.fragment.ChangeInactivePhoneFragment

class ChangeInactivePhoneActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return ChangeInactivePhoneFragment.instance
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        setupToolbar()
        initView()
    }

    override fun getLayoutRes(): Int {
        return R.layout.change_inactive_phone_layout
    }

    private fun initView() {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        var fragment = supportFragmentManager.findFragmentByTag(ChangeInactivePhoneFragment::class.java.name)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (fragment == null) {
            fragment = ChangeInactivePhoneFragment.instance
        }
        fragmentTransaction.replace(R.id.parent_view, fragment, fragment.javaClass.name)
        fragmentTransaction.commit()

    }


    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_icon_back_black)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    companion object {
        private fun getChangeInactivePhoneIntent(context: Context): Intent {
            return Intent(context, ChangeInactivePhoneActivity::class.java)
        }
    }

    object DeeplinkIntent{
        @JvmStatic
        @DeepLink(ApplinkConst.CHANGE_INACTIVE_PHONE)
        fun getCallingApplinkIntent(context: Context, bundle: Bundle): Intent {
            val uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon()
            val intent = getChangeInactivePhoneIntent(context)
            return intent.setData(uri.build())
        }
    }
}
