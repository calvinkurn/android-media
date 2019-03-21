package com.tokopedia.videoplayer.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.videoplayer.view.fragment.MultipleVideoPlayerFragment
import com.tokopedia.videoplayer.view.fragment.SingleVideoPlayerFragment

/**
 * @author by yfsx on 20/03/19.
 */
class VideoPlayerActivity(): BaseSimpleActivity() {

    companion object {
        const val PARAM_SINGLE_URL = "PARAM_SINGLE_URL"
        const val PARAM_MULTIPLE_URL = "PARAM_MULTIPLE_URL"

        fun getInstance(context: Context, url: String): Intent {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            val bundle = Bundle()
            bundle.putString(PARAM_SINGLE_URL, url)
            intent.putExtras(bundle)
            return intent
        }

        fun getInstance(context: Context, urlList: ArrayList<String>): Intent {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            val bundle = Bundle()
            bundle.putStringArrayList(PARAM_MULTIPLE_URL, urlList)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun getNewFragment(): Fragment {
        if (intent.getStringExtra(PARAM_SINGLE_URL).isNotEmpty()) {
            return SingleVideoPlayerFragment.getInstance(intent.extras)
        } else {
            return MultipleVideoPlayerFragment.getInstance(intent.extras)
        }
    }
}