package com.tokopedia.play.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.R
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.view.contract.PlayNewChannelInteractor
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play_common.util.PlayLifecycleObserver
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalContent#PLAY_DETAIL}
 */
class PlayActivity : BaseActivity(), PlayNewChannelInteractor {

    companion object {
        private const val PLAY_FRAGMENT_TAG = "FRAGMENT_PLAY"
    }

    @Inject
    lateinit var playLifecycleObserver: PlayLifecycleObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        inject()
        setupPage()

        val channelId = intent?.data?.lastPathSegment
        setupView(channelId)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val fragment = supportFragmentManager.findFragmentByTag(PLAY_FRAGMENT_TAG)
        val channelId = intent?.data?.lastPathSegment
        if (fragment != null && fragment is PlayFragment && channelId != null) {
            fragment.onNewChannelId(channelId)
        }
    }

    override fun onNewChannel(channelId: String?) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment, getFragment(channelId), PLAY_FRAGMENT_TAG)
                .commit()
    }

    private fun inject() {
        DaggerPlayComponent.builder()
                .baseAppComponent(
                        (applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
                .inject(this)
    }

    private fun getFragment(channelId: String?): Fragment {
        return PlayFragment.newInstance(channelId)
    }

    private fun setupPage() {
        lifecycle.addObserver(playLifecycleObserver)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun setupView(channelId: String?) {
        if (supportFragmentManager.findFragmentByTag(PLAY_FRAGMENT_TAG) == null) {
            onNewChannel(channelId)
        }
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            val intent = RouteManager.getIntent(this, ApplinkConst.HOME)
            startActivity(intent)
            finish()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                supportFinishAfterTransition()
            } else {
                super.onBackPressed()
            }
        }
    }
}