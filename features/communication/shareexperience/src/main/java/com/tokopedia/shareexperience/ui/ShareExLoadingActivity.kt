package com.tokopedia.shareexperience.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.data.di.DaggerShareExComponent
import com.tokopedia.shareexperience.data.di.ShareExComponent
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.databinding.ShareexperienceLoadingActivityBinding
import com.tokopedia.shareexperience.ui.util.getStringExtraFromIntentOrQuery
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShareExLoadingActivity : BaseActivity(), HasComponent<ShareExComponent> {

    private var bottomSheet: ShareExBottomSheet? = null
    private var shareExComponent: ShareExComponent? = null

    private val binding: ShareexperienceLoadingActivityBinding? by viewBinding()

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
        val id = this.getStringExtraFromIntentOrQuery(ApplinkConstInternalCommunication.ID) ?: ""
        val source = this.getStringExtraFromIntentOrQuery(ApplinkConstInternalCommunication.SOURCE) ?: ""
        val sourceEnum = ShareExPageTypeEnum.fromValue(source)
        val defaultUrl = this.getStringExtraFromIntentOrQuery(ApplinkConstInternalCommunication.SHARE_DEFAULT_URL) ?: ""
        val selectedIdChip = this.getStringExtraFromIntentOrQuery(ApplinkConstInternalCommunication.SELECTED_ID) ?: ""
        viewModel.processAction(
            ShareExAction.FetchShareData(
                id,
                sourceEnum,
                defaultUrl,
                selectedIdChip
            )
        )
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
            openBottomSheet()
        }
    }

    private fun initBottomSheet() {
        if (bottomSheet == null) {
            bottomSheet = ShareExBottomSheet()
            bottomSheet?.setCloseClickListener {
                finishActivityWithoutAnimation()
            }
        }
    }

    private fun openBottomSheet() {
        binding?.shareexLoader?.hide()
        binding?.shareexDimOverlay?.hide()
        bottomSheet?.show(supportFragmentManager, "")
    }

    private fun closeBottomSheet() {
        binding?.shareexLoader?.show()
        binding?.shareexDimOverlay?.show()
        bottomSheet?.dismiss()
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

    fun refreshPage() {
        closeBottomSheet()
        fetchData()
    }
}
