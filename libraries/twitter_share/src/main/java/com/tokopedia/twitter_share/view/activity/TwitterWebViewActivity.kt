package com.tokopedia.twitter_share.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.twitter_share.R
import com.tokopedia.twitter_share.TwitterAuthenticator
import com.tokopedia.twitter_share.view.fragment.TwitterWebViewFragment

class TwitterWebViewActivity : BaseSimpleActivity(), TwitterWebViewActivityListener {

    companion object {
        const val EXTRA_URL = "url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(
                getString(R.string.log_in_with_twitter)
        )
    }

    override fun getNewFragment(): Fragment {
        return TwitterWebViewFragment.newInstance(intent.getStringExtra(EXTRA_URL))
    }

    override fun onGetCallbackUrl(url: String) {
        TwitterAuthenticator.broadcastState(
                TwitterAuthenticator.TwitterAuthenticationState.Success(url)
        )
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        TwitterAuthenticator.broadcastState(
                TwitterAuthenticator.TwitterAuthenticationState.Cancel
        )
    }
}