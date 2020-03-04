package com.tokopedia.tokopoints.view.tokopointhome

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.DaggerTokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointsQueryModule
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.user.session.UserSession

class TokoPointsHomeNewActivity : BaseSimpleActivity(), HasComponent<TokopointBundleComponent>, onAppBarCollapseListener {
    private val tokoPointComponent: TokopointBundleComponent by lazy { initInjector() }
    private var mUserSession: UserSession? = null
    private var initialLoggedInState = false
    override fun onCreate(savedInstanceState: Bundle?) {
        mUserSession = UserSession(applicationContext)
        super.onCreate(savedInstanceState)
        initialLoggedInState = mUserSession!!.isLoggedIn
        toolbar.visibility = View.GONE
        updateTitle(getString(R.string.tp_title_tokopoints))
    }

    override fun getNewFragment(): Fragment {
        val loginStatusBundle = Bundle()
        val isLogin = mUserSession!!.isLoggedIn
        val tokoPointsHomeFragmentNew = TokoPointsHomeFragmentNew.newInstance()
        return if (isLogin) {
            loginStatusBundle.putBoolean(CommonConstant.BUNDLE_ARGS_USER_IS_LOGGED_IN, isLogin)
            tokoPointsHomeFragmentNew.arguments = loginStatusBundle
            tokoPointsHomeFragmentNew
        } else {
            loginStatusBundle.putBoolean(CommonConstant.BUNDLE_ARGS_USER_IS_LOGGED_IN, isLogin)
            tokoPointsHomeFragmentNew.arguments = loginStatusBundle
            tokoPointsHomeFragmentNew
        }
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
        if (requestCode == REQUEST_CODE_LOGIN) {
            inflateFragment()
        }
    }

    protected fun openApplink(applink: String?) {
        if (!TextUtils.isEmpty(applink)) {
            RouteManager.route(this, applink)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mUserSession!!.isLoggedIn != initialLoggedInState) {
            inflateFragment()
        }
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
        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, TokoPointsHomeNewActivity::class.java)
        }
    }
}