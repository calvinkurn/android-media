package com.tokopedia.play.view.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.play.R
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play_common.util.PlayLifecycleObserver
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalContent#PLAY_DETAIL}
 */
class PlayActivity : BaseActivity() {

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
        setupView()
    }

    private fun inject() {
        DaggerPlayComponent.builder()
                .baseAppComponent(
                        (applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
                .inject(this)
    }

    private fun getFragment(): Fragment {
        val channelId = intent?.data?.lastPathSegment
        return PlayFragment.newInstance(channelId)
    }

    private fun setupPage() {
        lifecycle.addObserver(playLifecycleObserver)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun setupView() {
        if (supportFragmentManager.findFragmentByTag(PLAY_FRAGMENT_TAG) == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_fragment, getFragment(), PLAY_FRAGMENT_TAG)
                    .commit()
        }
    }
}