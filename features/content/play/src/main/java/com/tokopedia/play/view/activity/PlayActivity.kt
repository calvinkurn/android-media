package com.tokopedia.play.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ProcessLifecycleOwner
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.play.*
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.util.observer.PlayVideoUtilObserver
import com.tokopedia.play.view.contract.PlayNewChannelInteractor
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play_common.util.PlayVideoPlayerObserver
import com.tokopedia.play_common.util.PlayProcessLifecycleObserver
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalContent#PLAY_DETAIL}
 */
class PlayActivity : BaseActivity(), PlayNewChannelInteractor {

    companion object {
        private const val PLAY_FRAGMENT_TAG = "FRAGMENT_PLAY"

        @TestOnly
        fun createIntent(context: Context, channelId: String) =
                Intent(context, PlayActivity::class.java).apply {
                    data = Uri.parse("${ApplinkConstInternalContent.INTERNAL_PLAY}/$channelId")
                }
    }

    @Inject
    lateinit var playLifecycleObserver: PlayVideoPlayerObserver

    @Inject
    lateinit var playVideoUtilObserver: PlayVideoUtilObserver

    @Inject
    lateinit var playProcessLifecycleObserver: PlayProcessLifecycleObserver

    private lateinit var pageMonitoring: PageLoadTimePerformanceInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        startPageMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        inject()
        setupPage()

        val channelId = intent?.data?.lastPathSegment
        setupView(channelId)
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
                .playModule(PlayModule(this))
                .build()
                .inject(this)
    }

    private fun getFragment(channelId: String?): Fragment {
        return PlayFragment.newInstance(channelId)
    }

    private fun setupPage() {
        lifecycle.addObserver(playLifecycleObserver)
        lifecycle.addObserver(playVideoUtilObserver)
        ProcessLifecycleOwner.get()
                .lifecycle.addObserver(playProcessLifecycleObserver)
    }

    private fun setupView(channelId: String?) {
        if (supportFragmentManager.findFragmentByTag(PLAY_FRAGMENT_TAG) == null) {
            onNewChannel(channelId)
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(PLAY_FRAGMENT_TAG)
        if (fragment != null && fragment is PlayFragment) {
            if (!fragment.onBackPressed()) {
                if (isTaskRoot) {
                    val intent = RouteManager.getIntent(this, ApplinkConst.HOME)
                    startActivity(intent)
                    finish()
                } else {
                    fragment.setResultBeforeFinish()
                    supportFinishAfterTransition()
                }
            }
        } else super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        ProcessLifecycleOwner.get()
                .lifecycle.removeObserver(playProcessLifecycleObserver)
    }

    private fun startPageMonitoring() {
        pageMonitoring = PageLoadTimePerformanceCallback(
                PLAY_TRACE_PREPARE_PAGE,
                PLAY_TRACE_REQUEST_NETWORK,
                PLAY_TRACE_RENDER_PAGE
        )
        pageMonitoring.startMonitoring(PLAY_TRACE_PAGE)
        starPrepareMonitoring()
    }

    private fun starPrepareMonitoring() {
        pageMonitoring.startPreparePagePerformanceMonitoring()
    }

    fun getPageMonitoring(): PageLoadTimePerformanceInterface {
        return pageMonitoring
    }

    fun getPltPerformanceResultData(): PltPerformanceData? {
        return pageMonitoring.getPltPerformanceData()
    }
}