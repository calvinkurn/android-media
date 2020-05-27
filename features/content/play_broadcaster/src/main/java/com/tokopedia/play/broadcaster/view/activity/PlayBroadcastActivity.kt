package com.tokopedia.play.broadcaster.view.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.di.PlayBroadcasterModule
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayPrepareBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import javax.inject.Inject

/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastActivity: BaseActivity() {

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
        setupPage()
    }

    private fun inject() {
        DaggerPlayBroadcasterComponent.builder()
                .baseAppComponent(
                        (applicationContext as BaseMainApplication).baseAppComponent
                )
                .playBroadcasterModule(PlayBroadcasterModule())
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
        openFragment(getParentFragment(), PARENT_FRAGMENT_TAG)
    }

    private fun getPrepareFragment() = fragmentFactory.instantiate(classLoader, PlayPrepareBroadcastFragment::class.java.name)

    private fun setupPage() {
        viewModel.getConfiguration()

        observeConfiguration()
    }

    private fun getParentFragment() = PlayBroadcastFragment.newInstance()

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun observeConfiguration() {
        viewModel.observablePage.observe(this, Observer {
            if (it) {
                openFragment(getPrepareFragment(), PREPARE_FRAGMENT_TAG)
            }
        })
    }

    private fun openFragment(fragment: Fragment, tag: String) {
        val currentFragment = supportFragmentManager.findFragmentByTag(tag)
        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fl_broadcast, fragment, tag)
                    .commit()
        }
    }

    companion object {
        private const val PARENT_FRAGMENT_TAG = "parent_fragment"
        private const val PREPARE_FRAGMENT_TAG = "prepare_fragment"
        private const val SETUP_FRAGMENT_TAG = "setup_fragment"
    }
}