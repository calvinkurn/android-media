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
import com.tokopedia.play.broadcaster.shorts.view.fragment.PlayShortsPreparationFragment
import com.tokopedia.play.broadcaster.shorts.view.fragment.PlayShortsSummaryFragment
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
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
                fragment.initViews(viewModel.tncList)
                fragment.setListener(object : SellerTncBottomSheet.Listener {
                    override fun clickCloseIcon() {
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
        binding = ActivityPlayShortsBinding.inflate(
            LayoutInflater.from(this),
            null,
            false
        )
        setContentView(binding.root)
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderPage(it.prevValue, it.value)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                renderBottomSheet(it.bottomSheet)
                renderOneTimeEvent(event.oneTimeEvent)
            }
        }
    }

    private fun renderPage(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        /**
         * Need to put validation here so render page only run once
         */

        if (prev?.config?.shortsId?.isEmpty() == true && curr.config.shortsId.isNotEmpty() && curr.media.mediaUri.isEmpty()) {
            binding.loader.visibility = View.GONE
            openMediaPicker()
        } else if (prev?.media?.mediaUri?.isEmpty() == true && curr.media.mediaUri.isNotEmpty()) {
            binding.loader.visibility = View.GONE
            openPreparation()
        }
        else if(curr.config.shortsId.isEmpty()) {
            binding.loader.visibility = View.VISIBLE
        }
    }

    private fun renderBottomSheet(bottomSheet: PlayShortsBottomSheet) {
        when (bottomSheet) {
            is PlayShortsBottomSheet.UGCOnboarding -> {
                showUGCOnboardingBottomSheet(bottomSheet.hasUsername)
            }
            is PlayShortsBottomSheet.NoEligibleAccount -> {
                showNoEligibleAccountBottomSheet()
            }
            is PlayShortsBottomSheet.SellerNotEligible -> {
                showSellerNotEligibleBottomSheet()
            }
            else -> {}
        }
    }

    private fun openMediaPicker() {
        val intent = MediaPicker.intent(this) {
            /** TODO: need to decide this pageSource based on our analytics */
            pageSource(PageSource.Unknown)
            minVideoDuration(1000)
            maxVideoDuration(90000)
            pageType(PageType.GALLERY)
            modeType(ModeType.VIDEO_ONLY)
            singleSelectionMode()
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
        /** TODO: show bottosheet based on :
         * 1. if preferred account is shop -> show shop bottomsheet
         * 2. if preferred account is user -> show user bottomsheet not eligible (need to confirm this)
         * 3. else -> show shop bottomsheet (need to confirm this)
         */
        println("PLAY_SHORTS : showNoEligibleAccountBottomSheet")
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
