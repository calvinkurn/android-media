package com.tokopedia.loginregister.login.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.facebook.CallbackManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.text.TextDrawable
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.login.di.DaggerLoginComponent
import com.tokopedia.loginregister.login.view.presenter.LoginPresenter
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 18/01/19.
 */
class LoginFragmentWithPhone : BaseDaggerFragment() {

    private val ID_ACTION_REGISTER = 111

    lateinit var callbackManager: CallbackManager

    @Inject
    lateinit var analytics: LoginRegisterAnalytics

    @Inject
    lateinit var presenter: LoginPresenter

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    //*For analytics
    private var actionLoginMethod: String = ""

    fun createInstance(bundle: Bundle): Fragment {
        val fragment = LoginFragmentWithPhone()
        fragment.arguments = bundle
        return fragment
    }

    override fun getScreenName(): String {
        return LoginRegisterAnalytics.SCREEN_LOGIN
    }

    override fun initInjector() {
        val daggerLoginComponent = DaggerLoginComponent
                .builder().loginRegisterComponent(getComponent(LoginRegisterComponent::class.java))
                .build() as DaggerLoginComponent

        daggerLoginComponent.inject(this)
    }

    override fun onStart() {
        super.onStart()
        analytics.trackScreen(activity, screenName)
    }

    override fun onResume() {
        super.onResume()
        if (userSession.isLoggedIn && activity != null) {
            activity!!.setResult(Activity.RESULT_OK)
            activity!!.finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu!!.add(Menu.NONE, ID_ACTION_REGISTER, 0, "")
        val menuItem = menu.findItem(ID_ACTION_REGISTER)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        if (getDraw() != null) {
            menuItem.icon = getDraw()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getDraw(): Drawable? {
        var drawable: TextDrawable? = null
        if (activity != null) {
            drawable = TextDrawable(activity!!)
            drawable.text = resources.getString(R.string.register)
            drawable.setTextColor(resources.getColor(R.color.tkpd_main_green))
        }
        return drawable
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if (id == ID_ACTION_REGISTER) {
            goToRegisterInitial()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_login_with_phone, container, false)

        return view
    }



    private fun goToRegisterInitial() {
        if (activity != null) {
            analytics.eventClickRegisterFromLogin()
            val intent = RegisterInitialActivity.getCallingIntent(activity)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            activity!!.finish()
        }
    }


}