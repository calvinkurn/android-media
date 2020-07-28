package com.tokopedia.play.broadcaster.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
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
import com.tokopedia.play.broadcaster.di.broadcast.DaggerPlayBroadcastComponent
import com.tokopedia.play.broadcaster.di.broadcast.PlayBroadcastComponent
import com.tokopedia.play.broadcaster.di.broadcast.PlayBroadcastModule
import com.tokopedia.play.broadcaster.di.provider.PlayBroadcastComponentProvider
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionState
import com.tokopedia.play.broadcaster.view.contract.PlayBroadcastCoordinator
import com.tokopedia.play.broadcaster.view.custom.PlayRequestPermissionView
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastFragment.Companion.PARENT_FRAGMENT_TAG
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastPrepareFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastUserInteractionFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.ActionBarPartialView
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import javax.inject.Inject

/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastActivity : BaseActivity(), PlayBroadcastCoordinator, PlayBroadcastComponentProvider {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var viewModel: PlayBroadcastViewModel

    private lateinit var containerSetup: FrameLayout
    private lateinit var viewActionBar: ActionBarPartialView
    private lateinit var viewRequestPermission: PlayRequestPermissionView

    private lateinit var playBroadcastComponent: PlayBroadcastComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        initViewModel()
        setFragmentFactory()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_broadcast)
        setupContent()
        initView()
        getConfiguration()

        observeConfiguration()
        observePermissionStateEvent()
    }

    private fun inject() {
        playBroadcastComponent = DaggerPlayBroadcastComponent.builder()
                .playBroadcastModule(PlayBroadcastModule(this))
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()

        playBroadcastComponent.inject(this)
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

    private fun initView() {
        containerSetup = findViewById(R.id.fl_setup)
        viewRequestPermission = findViewById(R.id.view_request_permission)

        viewActionBar = ActionBarPartialView(findViewById(android.R.id.content), object : ActionBarPartialView.Listener{
            override fun onCameraIconClicked() {
                viewModel.getPlayPusher().switchCamera()
            }

            override fun onCloseIconClicked() {
                onBackPressed()
            }
        })
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
        if (!viewModel.getPermissionUtil().onRequestPermissionsResult(requestCode, permissions, grantResults))
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.getPermissionUtil().onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun <T : Fragment> navigateToFragment(fragmentClass: Class<out T>, extras: Bundle, sharedElements: List<View>, onFragment: (T) -> Unit) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val destFragment = getFragmentByClassName(fragmentClass)
        destFragment.arguments = extras
        fragmentTransaction
                .replace(R.id.fl_setup, destFragment, fragmentClass.name)
                .commit()
    }

    override fun setupTitle(title: String) {
        viewActionBar.setTitle(title)
    }

    override fun setupCloseButton(actionTitle: String) {
        viewActionBar.setupCloseButton(actionTitle)
    }

    override fun showActionBar(shouldShow: Boolean) {
        if (shouldShow) viewActionBar.show() else viewActionBar.hide()
    }

    override fun getBroadcastComponent(): PlayBroadcastComponent {
        return playBroadcastComponent
    }

    private fun getParentFragment() = getFragmentByClassName(PlayBroadcastFragment::class.java)

    private fun getFragmentByClassName(fragmentClass: Class<out Fragment>): Fragment {
        return fragmentFactory.instantiate(classLoader, fragmentClass.name)
    }

    private fun getCurrentFragment() = supportFragmentManager.findFragmentById(R.id.fl_setup)

    private fun shouldClosePage(): Boolean {
        val currentVisibleFragment = getCurrentFragment()
        if (currentVisibleFragment != null && currentVisibleFragment is PlayBaseBroadcastFragment) {
            return currentVisibleFragment.onBackPressed()
        }
        return false
    }

    override fun onBackPressed() {
        if (shouldClosePage()) return
        super.onBackPressed()
    }

    //region observe
    /**
     * Observe
     */
    private fun observeConfiguration() {
        viewModel.observableConfigInfo.observe(this, Observer {
            if (it.streamAllowed) {
                when {
                    it.haveOnGoingLive -> openBroadcastActivePage()
                    else -> openBroadcastSetupPage()
                }
            } else {
                // TODO("handle when stream not allowed")
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

    private fun openBroadcastSetupPage() {
        navigateToFragment(PlayBroadcastPrepareFragment::class.java)
    }

    private fun openBroadcastActivePage() {
        navigateToFragment(PlayBroadcastUserInteractionFragment::class.java)
    }
}