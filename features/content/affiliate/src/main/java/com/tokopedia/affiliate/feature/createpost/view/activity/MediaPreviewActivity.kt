package com.tokopedia.affiliate.feature.createpost.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.affiliate.feature.createpost.view.fragment.MediaPreviewFragment
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel

/**
 * @author by milhamj on 25/02/19.
 */
class MediaPreviewActivity : BaseSimpleActivity() {

    companion object {
        internal const val CREATE_POST_MODEL = "create_post_model"

        fun createIntent(context: Context, createPostViewModel: CreatePostViewModel): Intent {
            val intent = Intent(context, MediaPreviewActivity::class.java)
            intent.putExtra(CREATE_POST_MODEL, createPostViewModel)
            return intent
        }
    }

    override fun getNewFragment(): Fragment {
        return MediaPreviewFragment.createInstance(intent.extras ?: Bundle())
    }
}