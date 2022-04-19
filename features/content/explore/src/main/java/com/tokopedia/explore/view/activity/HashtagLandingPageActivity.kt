package com.tokopedia.explore.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.explore.di.DaggerExploreComponent
import com.tokopedia.explore.di.ExploreComponent
import com.tokopedia.explore.view.fragment.HashtagLandingPageFragment
import java.net.URLDecoder

class HashtagLandingPageActivity : BaseSimpleActivity(), HasComponent<ExploreComponent> {

    override fun getNewFragment(): Fragment {
        val uri = intent.data
        val hashtag = if (uri != null && uri.scheme == DeeplinkConstant.SCHEME_INTERNAL){
             uri.lastPathSegment?.let { decode(it) } ?: ""
        } else {
            intent.extras?.getString(HashtagLandingPageFragment.ARG_HASHTAG) ?: ""
        }
        return HashtagLandingPageFragment.createInstance(hashtag)
    }

    override fun getComponent(): ExploreComponent = DaggerExploreComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent).build()

    companion object {

        private fun decode(param: String): String {
            return try {
                URLDecoder.decode(param, "UTF-8")
            } catch (e: IllegalArgumentException) {
                param
            }
        }

    }
}