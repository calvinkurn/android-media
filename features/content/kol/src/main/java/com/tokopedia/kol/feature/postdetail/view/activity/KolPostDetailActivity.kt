package com.tokopedia.kol.feature.postdetail.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kol.feature.postdetail.view.fragment.KolPostDetailFragment

class KolPostDetailActivity : BaseSimpleActivity() {
    private var postId: String = DEFAULT_POST_ID

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        bundle.putString(PARAM_POST_ID, postId)
        return KolPostDetailFragment.getInstance(bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromIntent()
        super.onCreate(savedInstanceState)
    }

    private fun getDataFromIntent() {
        intent.data?.let {
            postId = it.lastPathSegment ?: DEFAULT_POST_ID
        }
    }

    companion object {
        const val PARAM_POST_ID = "post_id"
        const val DEFAULT_POST_ID = "0"
        const val RESULT_DELETED = 10
    }
}