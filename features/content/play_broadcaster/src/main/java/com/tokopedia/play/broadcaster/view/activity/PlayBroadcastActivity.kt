package com.tokopedia.play.broadcaster.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.di.PlayBroadcasterModule
import com.tokopedia.play.broadcaster.util.event.EventObserver
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionState
import com.tokopedia.play.broadcaster.view.contract.PlayActionBarInteraction
import com.tokopedia.play.broadcaster.view.contract.PlayBroadcastCoordinator
import com.tokopedia.play.broadcaster.view.custom.PlayRequestPermissionView
import com.tokopedia.play.broadcaster.view.event.ScreenStateEvent
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastFragment.Companion.PARENT_FRAGMENT_TAG
import com.tokopedia.play.broadcaster.view.fragment.PlayLiveBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayPrepareBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.unifyprinciples.Typography
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

    private lateinit var containerSetup: FrameLayout
    private lateinit var viewActionBar: View
    private lateinit var viewRequestPermission: PlayRequestPermissionView
    private lateinit var ivSwitchCamera: AppCompatImageView
    private lateinit var tvClose: AppCompatTextView
    private lateinit var tvTitle: Typography

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        initViewModel()
        setFragmentFactory()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_broadcast)
        setupContent()
        setupPermission()
        getConfiguration()
        setupToolbar()

        observeScreenStateEvent()
        observePermissionStateEvent()
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
        containerSetup = findViewById(R.id.fl_setup)
        viewActionBar = findViewById(R.id.view_action_bar)
        val currentFragment = supportFragmentManager.findFragmentByTag(PARENT_FRAGMENT_TAG)
        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fl_broadcast, getParentFragment(), PARENT_FRAGMENT_TAG)
                    .commit()
        }
    }

    private fun setupPermission() {
        viewRequestPermission = findViewById(R.id.view_request_permission)
    }

    private fun setupToolbar() {
        tvTitle = findViewById(R.id.tv_title)
        ivSwitchCamera = findViewById(R.id.iv_switch)
        tvClose = findViewById(R.id.tv_close)

        ivSwitchCamera.setOnClickListener {
            doSwitchCamera()
        }
        tvClose.setOnClickListener {
            doClosePage()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        viewModel.getPermissionUtil().onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.getPermissionUtil().onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun navigateToFragment(fragmentClass: Class<out Fragment>, extras: Bundle) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val destFragment = getFragmentByClassName(fragmentClass)
        destFragment.arguments = extras
        fragmentTransaction
                .replace(R.id.fl_setup, destFragment, fragmentClass.name)
                .commit()
    }

    override fun setupTitle(title: String) {
        tvTitle.text = title
    }

    private fun getParentFragment() = getFragmentByClassName(PlayBroadcastFragment::class.java)

    private fun getFragmentByClassName(fragmentClass: Class<out Fragment>): Fragment {
        return fragmentFactory.instantiate(classLoader, fragmentClass.name)
    }

    private fun getCurrentVisibleFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.fl_setup)
    }

    private fun doSwitchCamera() {
        viewModel.getPlayPusher().switchCamera()
    }

    private fun doClosePage() {
        val currentVisibleFragment = getCurrentVisibleFragment()
        if (currentVisibleFragment == null
                || currentVisibleFragment !is PlayActionBarInteraction
                || !currentVisibleFragment.onCloseActionBar()) {
            this.onBackPressed()
        }
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

    private fun observePermissionStateEvent() {
        viewModel.observablePermissionState.observe(this, Observer {
            when(it) {
                is PlayPermissionState.Granted -> onPermissionGranted()
                is PlayPermissionState.Denied -> onPermissionDisabled(it.permissions)
                is PlayPermissionState.Error -> onError(it.throwable)
            }
        })
    }
    //endregion

    private fun onPermissionGranted() {
        containerSetup.show()
        viewActionBar.show()
        viewRequestPermission.hide()
    }

    private fun onPermissionDisabled(permissions: List<String>) {
        containerSetup.hide()
        viewActionBar.hide()
        viewRequestPermission.show()
        viewRequestPermission.setPermissionGranted(permissions)
    }

    private fun onError(throwable: Throwable) {
        viewRequestPermission.hide()
        // TODO("handle error app")
        if (GlobalConfig.DEBUG) {
            throw IllegalStateException(throwable)
        }
    }
}