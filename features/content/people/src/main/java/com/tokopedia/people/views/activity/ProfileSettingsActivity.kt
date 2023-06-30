package com.tokopedia.people.views.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.people.di.DaggerUserProfileComponent
import com.tokopedia.people.di.UserProfileModule
import com.tokopedia.people.viewmodels.UserProfileSettingsViewModel
import com.tokopedia.people.viewmodels.factory.UserProfileSettingsViewModelFactory
import com.tokopedia.people.views.uimodel.action.UserProfileSettingsAction
import javax.inject.Inject
import com.tokopedia.people.R
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.databinding.ActivityUserProfileSettingsBinding
import com.tokopedia.people.utils.showErrorToast
import com.tokopedia.people.views.uimodel.event.UserProfileSettingsEvent
import kotlinx.coroutines.flow.collectLatest

/**
 * Created By : Jonathan Darwin on May 10, 2023
 */
class ProfileSettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactoryCreator: UserProfileSettingsViewModelFactory.Creator

    @Inject
    lateinit var userProfileTracker: UserProfileTracker

    private lateinit var binding: ActivityUserProfileSettingsBinding

    private val viewModel: UserProfileSettingsViewModel by viewModels {
        viewModelFactoryCreator.create(
            this,
            intent?.data?.lastPathSegment.orEmpty()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupListener()
        setupObserver()
    }

    private fun inject() {
        DaggerUserProfileComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication).baseAppComponent,
            )
            .userProfileModule(UserProfileModule(this))
            .build()
            .inject(this)
    }

    private fun setupView() {
        binding.headerUnify.title = getString(R.string.up_profile_settings_title)
    }

    private fun setupListener() {
        binding.headerUnify.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.switchReview.setOnCheckedChangeListener { compoundButton, isChecked ->
            userProfileTracker.clickReviewSettingsToggle(viewModel.userID, isChecked)
            viewModel.submitAction(UserProfileSettingsAction.SetShowReview(isChecked))
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(EXTRA_IS_ANY_SETTINGS_CHANGED, viewModel.isAnySettingsChanged)
                    })

                    finish()
                }
            }
        )
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.reviewSettings.collectLatest {
                binding.tvReview.text = it.title
                binding.switchReview.isChecked = it.isEnabled
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UserProfileSettingsEvent.ErrorSetShowReview -> {
                        binding.root.showErrorToast(event.throwable.message ?: getString(R.string.up_error_unknown))
                    }
                }
            }
        }
    }

    companion object {
        private const val EXTRA_IS_ANY_SETTINGS_CHANGED = "EXTRA_IS_ANY_SETTINGS_CHANGED"

        fun getIsAnySettingsChanged(intent: Intent?): Boolean {
            return intent?.getBooleanExtra(EXTRA_IS_ANY_SETTINGS_CHANGED, false).orFalse()
        }
    }
}
