package com.tokopedia.kol.feature.postdetail.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kol.feature.postdetail.view.fragment.ContentDetailPageRevampedFragment

class ContentDetailActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle().apply {
            putString(
                PARAM_POST_ID, postId()
            )
        }
        return ContentDetailPageRevampedFragment.newInstance(bundle)
    }

    private fun postId(): String {
        return intent?.data?.lastPathSegment ?: DEFAULT_POST_ID
    }

    companion object {
        const val PARAM_POST_ID = "post_id"
        const val DEFAULT_POST_ID = "0"
    }
}