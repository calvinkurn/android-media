package com.tokopedia.tokopoints.view.cataloglisting

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.*
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener
import com.tokopedia.user.session.UserSession

class CatalogListingActivity : BaseSimpleActivity(), HasComponent<TokopointBundleComponent>, onAppBarCollapseListener {
    private val tokoPointComponent: TokopointBundleComponent by lazy { initInjector() }
    private var mUserSession: UserSession? = null
    private var bundle: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        mUserSession = UserSession(applicationContext)
        forDeeplink()
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.tp_label_exchange_points))
    }

    private fun forDeeplink() {
        bundle = intent.extras
        if (bundle == null) bundle = Bundle()
        if (intent.data != null) {
            UriUtil.destructiveUriBundle(ApplinkConstInternalPromo.TOKOPOINTS_CATALOG_LISTING, intent.data, bundle)
        }
    }

    override fun getNewFragment(): Fragment? {
        return CatalogListingFragment.newInstance(bundle)
    }

    override fun getComponent(): TokopointBundleComponent {
        return tokoPointComponent
    }

    private fun initInjector() : TokopointBundleComponent {
        return DaggerTokopointBundleComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .tokopointsQueryModule(TokopointsQueryModule(this))
                .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = resources.getDimension(com.tokopedia.design.R.dimen.dp_4)
        }
    }

    override fun hideToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = resources.getDimension(com.tokopedia.design.R.dimen.dp_0)
        }
    }

    companion object {
        private const val REQUEST_CODE_LOGIN = 1
        fun getCallingIntent(context: Context?, extras: Bundle?): Intent {
            val intent = Intent(context, CatalogListingActivity::class.java)
            intent.putExtras(extras)
            return intent
        }
    }
}