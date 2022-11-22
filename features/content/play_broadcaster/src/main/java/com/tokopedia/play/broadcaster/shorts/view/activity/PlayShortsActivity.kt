package com.tokopedia.play.broadcaster.shorts.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.common.onboarding.view.fragment.UGCOnboardingParentFragment
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.bottomsheet.SellerTncBottomSheet
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ActivityPlayShortsBinding
import com.tokopedia.play.broadcaster.shorts.di.DaggerPlayShortsComponent
import com.tokopedia.play.broadcaster.shorts.di.PlayShortsModule
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsBottomSheet
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsOneTimeEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.view.bottomsheet.ShortsAccountNotEligibleBottomSheet
import com.tokopedia.play.broadcaster.shorts.view.fragment.PlayShortsPreparationFragment
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play.broadcaster.util.extension.channelNotFound
import com.tokopedia.play_common.util.extension.withCache
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 02, 2022
 */
@Suppress("LateinitUsage")
class PlayShortsActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityPlayShortsBinding

    private val viewModel by viewModels<PlayShortsViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setupFragmentFactory()

        super.onCreate(savedInstanceState)

        setupBinding()
        setupView()
        setupObserver()

        viewModel.submitAction(PlayShortsAction.PreparePage(getPreferredAccountType()))
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is UGCOnboardingParentFragment -> {
                fragment.setListener(object : UGCOnboardingParentFragment.Listener {
                    override fun onSuccess() {
                        /** TODO: handle tracker */
                        if(getCurrentFragment() == null)
                            viewModel.submitAction(PlayShortsAction.PreparePage(getPreferredAccountType()))
                        else
                            viewModel.submitAction(PlayShortsAction.SwitchAccount)
                    }

                    override fun impressTncOnboarding() {
                        /** TODO: handle tracker */
                    }

                    override fun impressCompleteOnboarding() {
                        /** TODO: handle tracker */
                    }

                    override fun clickNextOnTncOnboarding() {
                        /** TODO: handle tracker */
                    }

                    override fun clickNextOnCompleteOnboarding() {
                        /** TODO: handle tracker */
                    }

                    override fun clickCloseIcon() {
                        /** TODO: handle tracker */
                        if (getCurrentFragment() == null) finish()
                    }
                })
            }
            is SellerTncBottomSheet -> {
                fragment.setDataSource(object : SellerTncBottomSheet.DataSource {
                    override fun getTitle(): String {
                        return getString(R.string.play_shorts_shop_cant_create_content)
                    }

                    override fun getTermsAndCondition(): List<TermsAndConditionUiModel> {
                        return viewModel.tncList
                    }
                })

                fragment.setListener(object : SellerTncBottomSheet.Listener {
                    override fun clickCloseIcon() {
                        if(getCurrentFragment() == null) finish()
                    }
                })
            }
            is ShortsAccountNotEligibleBottomSheet -> {
                fragment.setListener(object : ShortsAccountNotEligibleBottomSheet.Listener {
                    override fun onClose() {
                        if(getCurrentFragment() == null) finish()
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        if (isBackPressedOverridden()) return
        super.onBackPressed()
    }

    private fun inject() {
        DaggerPlayShortsComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .playShortsModule(PlayShortsModule(this))
            .build()
            .inject(this)
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun setupBinding() {
        binding = ActivityPlayShortsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupView() {
        binding.globalError.apply {
            setType(GlobalError.PAGE_NOT_FOUND)
            setActionClickListener { finish() }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderPage(it.prevValue, it.value)
                renderGlobalLoader(it.prevValue, it.value)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                renderBottomSheet(it.bottomSheet)
                renderOneTimeEvent(it.oneTimeEvent)
            }
        }
    }

    private fun renderPage(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.config?.shortsId?.isEmpty() == true && curr.config.shortsId.isNotEmpty() && curr.media.mediaUri.isEmpty()) {
            binding.loader.visibility = View.GONE
            openMediaPicker()
        } else if (prev?.media?.mediaUri?.isEmpty() == true && curr.media.mediaUri.isNotEmpty()) {
            binding.loader.visibility = View.GONE
            openPreparation()
        }
    }

    private fun renderGlobalLoader(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if(prev?.globalLoader == curr.globalLoader) return

        binding.loader.showWithCondition(curr.globalLoader)
    }

    private fun renderBottomSheet(bottomSheet: PlayShortsBottomSheet) {
        when (bottomSheet) {
            is PlayShortsBottomSheet.UGCOnboarding -> {
                showUGCOnboardingBottomSheet(bottomSheet.hasUsername)
            }
            is PlayShortsBottomSheet.AccountNotEligible -> {
                showNoEligibleAccountBottomSheet()
            }
            is PlayShortsBottomSheet.SellerNotEligible -> {
                showSellerNotEligibleBottomSheet()
            }
            else -> {}
        }
    }

    private fun renderOneTimeEvent(oneTimeEvent: PlayShortsOneTimeEvent) {
        when(oneTimeEvent) {
            is PlayShortsOneTimeEvent.ErrorPreparingPage -> {
                binding.loader.visibility = View.GONE
                binding.globalError.visibility = View.VISIBLE
            }
            else -> {}
        }
    }

    private fun openMediaPicker() {
        val intent = MediaPicker.intent(this) {
            pageSource(PageSource.PlayShorts)
            minVideoDuration(1000)
            maxVideoDuration(90000)
            pageType(PageType.GALLERY)
            modeType(ModeType.VIDEO_ONLY)
            singleSelectionMode()
            previewActionText(getString(R.string.play_shorts_next_action_label))
        }

        startActivityForResult(intent, MEDIA_PICKER_REQ)
    }

    private fun openPreparation() {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.container.id,
                PlayShortsPreparationFragment.getFragment(
                    supportFragmentManager,
                    classLoader
                )
            )
            .commit()
    }

    private fun showUGCOnboardingBottomSheet(hasUsername: Boolean) {
        val existingFragment = supportFragmentManager.findFragmentByTag(UGCOnboardingParentFragment.TAG)
        if (existingFragment is UGCOnboardingParentFragment && existingFragment.isVisible) return

        val bundle = Bundle().apply {
            putInt(
                UGCOnboardingParentFragment.KEY_ONBOARDING_TYPE,
                UGCOnboardingParentFragment.getOnboardingType(hasUsername = hasUsername)
            )
        }
        supportFragmentManager.beginTransaction()
            .add(UGCOnboardingParentFragment::class.java, bundle, UGCOnboardingParentFragment.TAG)
            .commit()
    }

    private fun showNoEligibleAccountBottomSheet() {
        ShortsAccountNotEligibleBottomSheet
            .getFragment(supportFragmentManager, classLoader)
            .show(supportFragmentManager)

    }

    private fun showSellerNotEligibleBottomSheet() {
        SellerTncBottomSheet
            .getFragment(supportFragmentManager, classLoader)
            .show(supportFragmentManager)
    }

    private fun getPreferredAccountType(): String {
        return intent.getStringExtra(ContentCommonUserType.KEY_AUTHOR_TYPE).orEmpty()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MEDIA_PICKER_REQ) {
            if (resultCode == RESULT_OK) {
                val mediaUri = MediaPicker.result(data).originalPaths.getOrNull(0).orEmpty()
                viewModel.submitAction(PlayShortsAction.SetMedia(mediaUri))
            } else {
                finish()
            }
        }
    }

    private fun isBackPressedOverridden(): Boolean {
        val currentVisibleFragment = getCurrentFragment()
        if (currentVisibleFragment != null && currentVisibleFragment is PlayShortsBaseFragment) {
            return currentVisibleFragment.onBackPressed()
        }
        return false
    }

    private fun getCurrentFragment() = supportFragmentManager.findFragmentById(R.id.container)

    companion object {
        private const val MEDIA_PICKER_REQ = 123
    }
}
