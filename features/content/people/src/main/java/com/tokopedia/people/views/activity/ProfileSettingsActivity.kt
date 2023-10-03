package com.tokopedia.people.views.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.people.di.DaggerUserProfileComponent
import com.tokopedia.people.di.UserProfileModule
import com.tokopedia.people.viewmodels.UserProfileSettingsViewModel
import com.tokopedia.people.viewmodels.factory.UserProfileSettingsViewModelFactory
import com.tokopedia.people.views.uimodel.action.UserProfileSettingsAction
import javax.inject.Inject
import com.tokopedia.people.R
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.utils.showErrorToast
import com.tokopedia.people.views.screen.ProfileSettingsScreen
import com.tokopedia.people.views.uimodel.event.UserProfileSettingsEvent
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created By : Jonathan Darwin on May 10, 2023
 */
class ProfileSettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactoryCreator: UserProfileSettingsViewModelFactory.Creator

    @Inject
    lateinit var userProfileTracker: UserProfileTracker

    private val viewModel: UserProfileSettingsViewModel by viewModels {
        viewModelFactoryCreator.create(
            this,
            intent?.data?.lastPathSegment.orEmpty()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)

        setContent {
            val reviewSettings by viewModel.reviewSettings.collectAsState()

            NestTheme {

                val view = LocalView.current

                LaunchedEffect(Unit) {
                    setupListener()

                    lifecycleScope.launchWhenStarted {
                        viewModel.uiEvent.collect { event ->
                            when (event) {
                                is UserProfileSettingsEvent.ErrorSetShowReview -> {
                                    val message = when (event.throwable) {
                                        is UnknownHostException, is SocketTimeoutException -> {
                                            getString(R.string.up_error_local_error)
                                        }
                                        else -> {
                                            event.throwable.message ?: getString(R.string.up_error_unknown)
                                        }
                                    }

                                    view.showErrorToast(message)
                                }
                            }
                        }
                    }
                }

                ProfileSettingsScreen(
                    reviewSettings = reviewSettings,
                    onBackPressed = {
                        onBackPressedDispatcher.onBackPressed()
                    },
                    onCheckedChanged = { isChecked ->
                        userProfileTracker.clickReviewSettingsToggle(viewModel.userID, isChecked)
                        viewModel.submitAction(UserProfileSettingsAction.SetShowReview(isChecked))
                    }
                )
            }
        }
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

    private fun setupListener() {
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

    companion object {
        private const val EXTRA_IS_ANY_SETTINGS_CHANGED = "EXTRA_IS_ANY_SETTINGS_CHANGED"

        fun getIsAnySettingsChanged(intent: Intent?): Boolean {
            return intent?.getBooleanExtra(EXTRA_IS_ANY_SETTINGS_CHANGED, false).orFalse()
        }
    }
}
