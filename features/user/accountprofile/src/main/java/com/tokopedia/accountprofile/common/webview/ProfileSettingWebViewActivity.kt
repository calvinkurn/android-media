package com.tokopedia.accountprofile.common.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.accountprofile.di.ActivityComponentFactory
import com.tokopedia.accountprofile.di.ProfileCompletionSettingComponent
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_TITLEBAR
import com.tokopedia.webview.KEY_URL

class ProfileSettingWebViewActivity : BaseSimpleWebViewActivity(),
    HasComponent<ProfileCompletionSettingComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ProfileSettingWebViewFragment.instance(bundle)
    }

    companion object {
        fun createIntent(context: Context, url: String, titleBar: Boolean = true): Intent {
            val intent = Intent(context, ProfileSettingWebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            intent.putExtra(KEY_TITLEBAR, titleBar)
            return intent
        }
    }

    override fun getComponent(): ProfileCompletionSettingComponent =
        ActivityComponentFactory.instance.createProfileCompletionComponent(
            this,
            application as BaseMainApplication
        )
}
