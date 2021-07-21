package com.tokopedia.kol.feature.video.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kol.feature.video.view.fragment.*
import com.tokopedia.kotlin.extensions.view.hide

/**
 * @author by yfsx on 23/03/19.
 */
class VideoDetailActivity : BaseSimpleActivity() {
    private var paramId: String = ""

    companion object {
        const val PARAM_ID = "PARAM_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        getDataFromIntent()
        super.onCreate(savedInstanceState)
    }

    private fun getDataFromIntent() {
        intent.data?.let {
            paramId = it.lastPathSegment ?: ""
        }
    }

    override fun getNewFragment(): Fragment {
        toolbar.hide()
        val extras = Bundle()
        extras.putInt(POST_POSITION, intent.getIntExtra(POST_POSITION, 0))
        extras.putString(PARAM_ID, paramId)
        extras.putInt(PARAM_VIDEO_INDEX, intent.getIntExtra(PARAM_VIDEO_INDEX, 0))
        extras.putString(PARAM_CALL_SOURCE, intent.getStringExtra(PARAM_CALL_SOURCE))
        extras.putString(PARAM_IS_POST_FOLLOWED, intent.getStringExtra(PARAM_IS_POST_FOLLOWED))
        extras.putString(PARAM_POST_TYPE, intent.getStringExtra(PARAM_POST_TYPE))

        return VideoDetailFragment.getInstance(extras)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        intent.putExtra(POST_POSITION, intent.getIntExtra(POST_POSITION, 0))
        setResult(Activity.RESULT_OK, intent)
    }
}