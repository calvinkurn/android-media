package com.tokopedia.explore.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.explore.di.DaggerExploreComponent
import com.tokopedia.explore.di.ExploreComponent
import com.tokopedia.explore.view.fragment.HashtagLandingPageFragment
import java.net.URLDecoder

class HashtagLandingPageActivity : BaseSimpleActivity(), HasComponent<ExploreComponent> {

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        inflateFragment()
    }

    override fun getNewFragment(): Fragment {
        val uri = intent.data
        val hashtag = if (uri != null && uri.scheme == DeeplinkConstant.SCHEME_INTERNAL){
             uri.lastPathSegment?.let { URLDecoder.decode(it) } ?: ""
        } else {
            intent.extras?.getString(HashtagLandingPageFragment.ARG_HASHTAG) ?: ""
        }
        return HashtagLandingPageFragment.createInstance(hashtag)
    }

    override fun getComponent(): ExploreComponent = DaggerExploreComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent).build()

    companion object {

        @JvmStatic
        fun createIntent(context: Context, hastag: String) = Intent(context, HashtagLandingPageActivity::class.java)
                .putExtra(HashtagLandingPageFragment.ARG_HASHTAG, hastag)
    }
}