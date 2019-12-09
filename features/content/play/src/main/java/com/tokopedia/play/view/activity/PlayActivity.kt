package com.tokopedia.play.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.view.fragment.PlayFragment

/**
 * Created by jegul on 29/11/19
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalContent#PLAY_DETAIL}
 */
class PlayActivity : BaseActivity() {

    companion object {
        fun createIntent(context: Context, channelId: Int) = Intent(context, PlayActivity::class.java).apply {
            putExtra(PLAY_KEY_CHANNEL_ID, channelId.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        initView()
        setupView()
    }

    private fun getFragment(): Fragment {
        return PlayFragment.newInstance(intent?.getStringExtra(PLAY_KEY_CHANNEL_ID)?:"")
    }

    private fun initView() {}

    private fun setupView() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment, getFragment())
                .commit()
    }
}