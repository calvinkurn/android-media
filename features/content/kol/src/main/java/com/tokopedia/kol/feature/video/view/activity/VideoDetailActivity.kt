package com.tokopedia.kol.feature.video.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import android.view.Window
import android.view.WindowManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kol.feature.video.view.fragment.VideoDetailFragment
import com.tokopedia.kotlin.extensions.view.hide

/**
 * @author by yfsx on 23/03/19.
 */
class VideoDetailActivity: BaseSimpleActivity() {

    companion object {
        const val PARAM_ID = "PARAM_ID"

        @Deprecated("Use ApplinkConstInternalContent")
        @JvmStatic
        fun getInstance(context: Context,
                        id: String): Intent {
            val intent = Intent(context, VideoDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString(PARAM_ID, id)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun getNewFragment(): Fragment {
        toolbar.hide()
        val extras = intent.extras ?: Bundle()
        intent.data?.lastPathSegment?.let { id -> extras.putString(PARAM_ID, id) }

        return VideoDetailFragment.getInstance(extras)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_OK)
    }
}