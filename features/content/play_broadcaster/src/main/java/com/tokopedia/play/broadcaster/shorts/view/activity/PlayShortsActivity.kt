package com.tokopedia.play.broadcaster.shorts.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ActivityPlayShortsBinding
import com.tokopedia.play.broadcaster.shorts.di.DaggerPlayShortsComponent
import com.tokopedia.play.broadcaster.shorts.di.PlayShortsModule
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.view.fragment.PlayShortsPreparationFragment
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play_common.util.extension.withCache
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

        val preferredAccountType = intent.getStringExtra(ContentCommonUserType.KEY_AUTHOR_TYPE).orEmpty()
        viewModel.submitAction(PlayShortsAction.PreparePage(preferredAccountType))

        /** For mocking purpose */
        openPreparation()
    }

    override fun onBackPressed() {
        if(isBackPressedOverridden()) return
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
    }

    private fun renderPage(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        /**
         * Need to put validation here so render page only run once
         */

        /**
         * shortsId != null && mediaUri == null -> MediaPicker
         * shortsId != null && mediaUri != null -> Preparation
         */
        when {
            curr.shortsId.isNotEmpty() && curr.media.mediaUri.isEmpty() -> {
                openMediaPicker()
            }
            curr.shortsId.isNotEmpty() && curr.media.mediaUri.isNotEmpty() -> {
                openPreparation()
            }
        }
    }

    private fun openMediaPicker() {
        val intent = MediaPicker.intent(this) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MEDIA_PICKER_REQ) {
            if (resultCode == RESULT_OK) {
                val data = MediaPicker.result(data)
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
