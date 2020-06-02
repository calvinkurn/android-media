package com.tokopedia.play.broadcaster.view.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.di.PlayBroadcasterModule
import com.tokopedia.play.broadcaster.util.event.EventObserver
import com.tokopedia.play.broadcaster.view.contract.PlayBroadcastCoordinator
import com.tokopedia.play.broadcaster.view.event.ScreenStateEvent
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastFragment.Companion.PARENT_FRAGMENT_TAG
import com.tokopedia.play.broadcaster.view.fragment.PlayLiveBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayPrepareBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import javax.inject.Inject

/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastActivity: BaseActivity(), PlayBroadcastCoordinator {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var viewModel: PlayBroadcastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        initViewModel()
        setFragmentFactory()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_broadcast)
        setupContent()
        getConfiguration()

        observeScreenStateEvent()
    }

    private fun inject() {
        DaggerPlayBroadcasterComponent.builder()
                .playBroadcasterModule(PlayBroadcasterModule(this))
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    private fun setFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun setupContent() {
        val currentFragment = supportFragmentManager.findFragmentByTag(PARENT_FRAGMENT_TAG)
        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fl_broadcast, getParentFragment(), PARENT_FRAGMENT_TAG)
                    .commit()
        }
    }

    private fun getConfiguration() {
        viewModel.getConfiguration()
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun navigateToFragment(fragmentClass: Class<out Fragment>, extras: Bundle) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val destFragment = getFragmentByClassName(fragmentClass)
        destFragment.arguments = extras
        fragmentTransaction
                .replace(R.id.fl_setup, destFragment, fragmentClass.name)
                .commit()
    }

    private fun getParentFragment() = getFragmentByClassName(PlayLiveBroadcastFragment::class.java)

    private fun getFragmentByClassName(fragmentClass: Class<out Fragment>): Fragment {
        return fragmentFactory.instantiate(classLoader, fragmentClass.name)
    }

    //region observe
    /**
     * Observe
     */

    private fun observeScreenStateEvent() {
        viewModel.observableScreenStateEvent.observe(this, EventObserver{
            when(it) {
                is ScreenStateEvent.ShowPreparePage -> {
                    navigateToFragment(PlayPrepareBroadcastFragment::class.java)
                }
                is ScreenStateEvent.ShowLivePage -> {
                    navigateToFragment(PlayLiveBroadcastFragment::class.java,
                            Bundle().apply {
                                putString(PlayLiveBroadcastFragment.KEY_CHANNEL_ID, it.channelId)
                                putString(PlayLiveBroadcastFragment.KEY_INGEST_URL, it.ingestUrl)
                            })
                }
            }
        })
    }
    //endregion
}