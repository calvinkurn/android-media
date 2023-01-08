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
        return HashtagLandingPageFragment.createInstance(
            hashtag,
            checkIfHashtagPageSourceIsContentDetail(),
            getContentDetailPageSource()
        )
    }

    private fun getContentDetailPageSource(): String {
        return intent?.extras?.getString(CONTENT_DETAIL_PAGE_SOURCE) ?: SHARE_LINK
    }
    private fun checkIfHashtagPageSourceIsContentDetail(): Boolean {
        return intent?.extras?.getBoolean(ARG_IS_FROM_CONTENT_DETAIL_PAGE) ?: false
    }

    override fun getComponent(): ExploreComponent = DaggerExploreComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent).build()

    companion object {
        const val ARG_IS_FROM_CONTENT_DETAIL_PAGE = "IS_FROM_CONTENT_DETAIL_PAGE"
        const val CONTENT_DETAIL_PAGE_SOURCE = "CONTENT_DETAIL_PAGE_SOURCE"
        const val SHARE_LINK = "share_link"

        private fun decode(param: String): String {
            return try {
                URLDecoder.decode(param, "UTF-8")
            } catch (e: IllegalArgumentException) {
                param
            }
        }

    }
}