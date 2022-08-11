package com.tokopedia.gamification.smcreferral

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.webview.BaseSessionWebViewFragment

class SmcReferralActivity : BaseSimpleActivity() {
    private var loginSuccess = false
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = getUrl()
        val userSession = UserSession(this)
        if(userSession.isLoggedIn){
            loadFragmentForUrl()
        }
        else{
            val loginIntent = RouteManager.getIntent(this,ApplinkConst.LOGIN)
            startActivityForResult(loginIntent, LOGIN_REQUEST_CODE)
        }
    }

    private fun getUrl() : String{
        intent?.let {
            it.extras?.let{ ext ->
                var webUrl = ext.getString(
                    KEY_APP_LINK_QUERY_URL, DEFAULT_TOKOPEDIA_WEBSITE_URL
                )
                if(TextUtils.isEmpty(webUrl)){
                    webUrl= DEFAULT_TOKOPEDIA_WEBSITE_URL
                }
                return webUrl
            }
        }
        return ""
    }

    override fun onPostResume() {
        super.onPostResume()
        if(loginSuccess){
            loadFragmentForUrl()
        }
    }

    private fun loadFragmentForUrl(){
        supportFragmentManager.beginTransaction()
            .replace(com.tokopedia.abstraction.R.id.parent_view,BaseSessionWebViewFragment.newInstance(url),
            tagFragment)
            .commit()
    }

    override fun getNewFragment() : Fragment?{
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK){
            loginSuccess = true
        }
        else{
            finish()
        }
    }



    companion object{
        private const val KEY_APP_LINK_QUERY_URL = "url"
        private const  val DEFAULT_TOKOPEDIA_WEBSITE_URL = "https://www.tokopedia.com/"
        private const val LOGIN_REQUEST_CODE = 100
    }
}