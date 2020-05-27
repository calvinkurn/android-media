package com.tokopedia.play.broadcaster.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.contract.PlayBroadcastSetupCoordinator
import com.tokopedia.play.broadcaster.view.fragment.PlayPrepareBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayPrepareBroadcastViewModel
import javax.inject.Inject

/**
 * Created by mzennis on 19/05/20.
 */
class PlayPrepareBroadcastActivity: BaseActivity(), PlayBroadcastSetupCoordinator {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var viewModel: PlayPrepareBroadcastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        initViewModel()
        setFragmentFactory()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_prepare_broadcast)
        setupContent()
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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayPrepareBroadcastViewModel::class.java)
    }

    private fun setFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun setupContent() {
        val prepareFragment = supportFragmentManager.findFragmentByTag(PREPARE_FRAGMENT_TAG)
        if (prepareFragment == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fl_prepare, getPrepareFragment(), PREPARE_FRAGMENT_TAG)
                    .commit()
        }
    }

    private fun getPrepareFragment() = fragmentFactory.instantiate(classLoader, PlayPrepareBroadcastFragment::class.java.name)

    companion object {
        private const val PREPARE_FRAGMENT_TAG = "prepare_fragment"
        private const val SETUP_FRAGMENT_TAG = "setup_fragment"
    }
}