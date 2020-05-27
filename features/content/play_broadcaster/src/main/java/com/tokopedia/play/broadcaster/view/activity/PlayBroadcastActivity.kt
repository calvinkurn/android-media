package com.tokopedia.play.broadcaster.view.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.contract.PlayBroadcastSetupCoordinator
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayPrepareBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import javax.inject.Inject

/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastActivity: BaseActivity(), PlayBroadcastSetupCoordinator {

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

    override fun openBroadcastSetupPage() {
        val setupFragment = fragmentFactory.instantiate(classLoader, PlayBroadcastSetupBottomSheet::class.java.name) as PlayBroadcastSetupBottomSheet
        setupFragment.show(supportFragmentManager)
    }

    private fun inject() {
        DaggerPlayBroadcasterComponent.create()
                .inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    private fun setFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun setupContent() {
        val prepareFragment = supportFragmentManager.findFragmentByTag(PARENT_FRAGMENT_TAG)
        if (prepareFragment == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fl_prepare, getParentFragment(), PARENT_FRAGMENT_TAG)
                    .commit()
        }
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
                openBroadcastSetupPage()
            }
        })
    }

    companion object {
        private const val PARENT_FRAGMENT_TAG = "parent_fragment"
        private const val PREPARE_FRAGMENT_TAG = "prepare_fragment"
        private const val SETUP_FRAGMENT_TAG = "setup_fragment"
    }
}