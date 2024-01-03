package com.tokopedia.shareexperience.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.data.di.DaggerShareExComponent
import com.tokopedia.shareexperience.data.di.ShareExComponent
import com.tokopedia.unifycomponents.LoaderUnify
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShareExLoadingActivity : BaseActivity(), HasComponent<ShareExComponent> {

    private var bottomSheet: ShareExBottomSheet? = null
    private var shareExComponent: ShareExComponent? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ShareExViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shareexperience_loading_activity)
        removeActivityAnimation() // Remove open activity animation
        initInjector()
        initObservers()
        fetchData()
    }

    override fun getComponent(): ShareExComponent {
        return shareExComponent ?: initializeComponent()
    }

    private fun initializeComponent(): ShareExComponent {
        return DaggerShareExComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .also {
                shareExComponent = it
            }
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun fetchData() {
        viewModel.processAction(ShareExAction.FetchShareData)
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    observeFetchedDataState()
                }
            }
        }
        viewModel.setupViewModelObserver()
    }

    private suspend fun observeFetchedDataState() {
        viewModel.fetchedDataState.collectLatest {
            initBottomSheet()
        }
    }

    private fun initBottomSheet() {
        if (bottomSheet == null) {
            bottomSheet = ShareExBottomSheet()
            bottomSheet?.setOnDismissListener {
                finishActivityWithoutAnimation()
            }
            openBottomSheet()
        }
    }

    private fun openBottomSheet() {
        findViewById<LoaderUnify>(R.id.shareex_loader).hide()
        findViewById<View>(R.id.shareex_dim_overlay).hide()
        bottomSheet?.show(supportFragmentManager, "")
    }

    private fun finishActivityWithoutAnimation() {
        finish()
        removeActivityAnimation() // Remove finish activity animation
    }

    private fun removeActivityAnimation() {
        overridePendingTransition(
            R.anim.shareexperience_no_animation_transition,
            R.anim.shareexperience_no_animation_transition
        )
    }
}
