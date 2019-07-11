package com.tokopedia.twitter_share.view.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.twitter_share.TwitterAuthenticator
import com.tokopedia.twitter_share.view.fragment.TwitterWebViewFragment

class TwitterWebViewActivity : BaseSimpleActivity(), TwitterWebViewActivityListener {

    companion object {
        const val EXTRA_URL = "url"
    }

    override fun getNewFragment(): Fragment {
        return TwitterWebViewFragment.newInstance(intent.getStringExtra(EXTRA_URL))
    }

    override fun onGetCallbackUrl(url: String) {
        TwitterAuthenticator.broadcastCallbackUrl(url)
        finish()
    }
}