package com.tokopedia.tokopoints.view.tokopointhome

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.DaggerTokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointsQueryModule
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class TokoPointsHomeNewActivity : BaseSimpleActivity(), HasComponent<TokopointBundleComponent>, onAppBarCollapseListener {
    private val tokoPointComponent: TokopointBundleComponent by lazy { initInjector() }
    private var mUserSession: UserSessionInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mUserSession = UserSession(this)
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
        updateTitle(getString(R.string.tp_title_tokopoints))
    }

    override fun getNewFragment(): Fragment? {
        val tokoPointsHomeFragmentNew = TokoPointsHomeFragmentNew.newInstance()
        return if (mUserSession?.isLoggedIn == true) {
            tokoPointsHomeFragmentNew
        } else {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
            null
        }
    }

    override fun getComponent(): TokopointBundleComponent {
        return tokoPointComponent
    }

    private fun initInjector(): TokopointBundleComponent {
        return DaggerTokopointBundleComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .tokopointsQueryModule(TokopointsQueryModule(this))
                .build()
    }

    override fun showToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_4)
        }
    }

    override fun hideToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN) {
            inflateFragment()
        }
    }

    companion object {
        private const val REQUEST_CODE_LOGIN = 1
        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, TokoPointsHomeNewActivity::class.java)
        }
    }
}