package com.tokopedia.play.broadcaster.shorts.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.common.onboarding.view.fragment.UGCOnboardingParentFragment
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.util.Router
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ActivityPlayShortsBinding
import com.tokopedia.play.broadcaster.shorts.analytic.PlayShortsAnalytic
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.view.bottomsheet.ShortsAccountNotEligibleBottomSheet
import com.tokopedia.play.broadcaster.shorts.view.fragment.PlayShortsPreparationFragment
import com.tokopedia.play.broadcaster.shorts.view.fragment.PlayShortsSummaryFragment
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play_common.util.extension.withCache
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
@Suppress("LateinitUsage")
abstract class BasePlayShortsActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytic: PlayShortsAnalytic

    @Inject
    lateinit var router: Router

    private lateinit var binding: ActivityPlayShortsBinding

    protected val viewModel by viewModels<PlayShortsViewModel> { viewModelFactory }

    abstract fun inject()

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
                        if(isFragmentContainerEmpty()) {
                            showNoEligibleAccountBackground(false)
                            viewModel.submitAction(PlayShortsAction.PreparePage(getPreferredAccountType()))
                        }
                        else
                            viewModel.submitAction(PlayShortsAction.SwitchAccount)
                    }

                    override fun impressTncOnboarding() {
                        analytic.viewOnboardingUGC(viewModel.selectedAccount)
                    }

                    override fun impressCompleteOnboarding() {
                        analytic.viewOnboardingUGC(viewModel.selectedAccount)
                    }

                    override fun clickUsernameFieldOnCompleteOnboarding() {
                        analytic.clickTextFieldUsernameOnboardingUGC(viewModel.selectedAccount)
                    }

                    override fun clickAcceptTnc(isChecked: Boolean) {
                        if(isChecked) analytic.clickAcceptTncOnboardingUGC(viewModel.selectedAccount)
                    }

                    override fun clickNextOnTncOnboarding() {
                        analytic.clickContinueOnboardingUGC(viewModel.selectedAccount)
                    }

                    override fun clickNextOnCompleteOnboarding() {
                        analytic.clickContinueOnboardingUGC(viewModel.selectedAccount)
                    }

                    override fun clickCloseIcon() {
                        analytic.clickCancelOnboardingUGC(viewModel.selectedAccount)
                        if (isFragmentContainerEmpty()) finish()
                    }
                })
            }
            is ShortsAccountNotEligibleBottomSheet -> {
                fragment.setListener(object : ShortsAccountNotEligibleBottomSheet.Listener {
                    override fun onClose() {
                        if(isFragmentContainerEmpty()) finish()
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        if (isBackPressedOverridden()) return
        super.onBackPressed()
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
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is PlayShortsUiEvent.UGCOnboarding -> {
                        showUGCOnboardingBottomSheet(event.hasUsername)

                        if(isFragmentContainerEmpty()) showNoEligibleAccountBackground(true)
                    }
                    is PlayShortsUiEvent.AccountNotEligible,
                    is PlayShortsUiEvent.SellerNotEligible -> {
                        /**
                         * For MVP, apps only use 1 bottomsheet for both user & seller ineligibility
                         */
                        showNoEligibleAccountBottomSheet()

                        if(isFragmentContainerEmpty()) showNoEligibleAccountBackground(true)
                    }
                    is PlayShortsUiEvent.ErrorPreparingPage -> {
                        binding.loader.visibility = View.GONE
                        binding.globalError.visibility = View.VISIBLE
                    }
                    is PlayShortsUiEvent.GoToSummary -> {
                        openSummaryFragment()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun renderPage(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.config?.shortsId.isNullOrEmpty() && curr.config.shortsId.isNotEmpty() && curr.media.mediaUri.isEmpty()) {
            binding.loader.visibility = View.GONE
            openMediaPicker()
        } else if (prev?.media?.mediaUri.isNullOrEmpty() && curr.media.mediaUri.isNotEmpty()) {
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

        router.route(this, intent, MEDIA_PICKER_REQ)
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

    private fun openSummaryFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.container.id,
                PlayShortsSummaryFragment.getFragment(
                    supportFragmentManager,
                    classLoader
                )
            )
            .addToBackStack(null)
            .commit()
    }

    private fun showUGCOnboardingBottomSheet(hasUsername: Boolean) {
        val existingFragment = supportFragmentManager.findFragmentByTag(UGCOnboardingParentFragment.TAG)
        if (existingFragment is UGCOnboardingParentFragment && existingFragment.isVisible) return

        val bundle = UGCOnboardingParentFragment.createBundle(
            if(hasUsername) UGCOnboardingParentFragment.OnboardingType.Tnc
            else UGCOnboardingParentFragment.OnboardingType.Complete
        )

        supportFragmentManager.beginTransaction()
            .add(UGCOnboardingParentFragment::class.java, bundle, UGCOnboardingParentFragment.TAG)
            .commit()
    }

    private fun showNoEligibleAccountBottomSheet() {
        ShortsAccountNotEligibleBottomSheet
            .getFragment(supportFragmentManager, classLoader)
            .show(supportFragmentManager)
    }

    private fun showNoEligibleAccountBackground(isShow: Boolean) {
        binding.clNoEligibleAccount.showWithCondition(isShow)
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

    private fun isFragmentContainerEmpty() = getCurrentFragment() == null

    companion object {
        private const val MEDIA_PICKER_REQ = 123
    }
}
