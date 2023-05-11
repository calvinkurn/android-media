package com.tokopedia.people.views.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.people.di.DaggerUserProfileComponent
import com.tokopedia.people.di.UserProfileModule
import com.tokopedia.people.viewmodels.UserProfileSettingsViewModel
import com.tokopedia.people.viewmodels.factory.UserProfileSettingsViewModelFactory
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.action.UserProfileSettingsAction
import javax.inject.Inject
import com.tokopedia.people.R
import com.tokopedia.people.databinding.ActivityUserProfileSettingsBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

/**
 * Created By : Jonathan Darwin on May 10, 2023
 */
class ProfileSettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var  viewModelFactoryCreator: UserProfileSettingsViewModelFactory.Creator

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
//        setContent {
//            NestTheme {
//                Column(
//                    modifier = Modifier
//                        .background(
//                            color = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_Background)
//                        )
//                        .fillMaxHeight()
//                ) {
//                    NestHeader(
//                        title = "Pengaturan Profil",
//                        onNavigationClickListener = {
//                            finish()
//                        }
//                    )
//
//                    ReviewSettingRow()
//                }
//            }
//        }
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
            /** TODO: setResult here if the settings is changed */
            finish()
        }

        binding.switchReview.setOnCheckedChangeListener { compoundButton, isChecked ->
            viewModel.submitAction(UserProfileSettingsAction.SetShowReview(isChecked))
        }
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.reviewSettings.collectLatest {
                binding.tvReview.text = it.title
                binding.switchReview.isChecked = it.isEnabled
            }
        }
    }

    @Composable
    private fun ReviewSettingRow() {

        val state by viewModel.reviewSettings.collectAsState(initial = ProfileSettingsUiModel.Empty)

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            NestIcon(icon = IconUnify.STAR)

            NestTypography(
                state.settingID,
                textStyle = NestTheme.typography.display3,
            )

            NestSwitch(
                isEnabled = state.isEnabled,
                onChanged = {
                    viewModel.submitAction(UserProfileSettingsAction.SetShowReview(isShow = it))
                }
            )
        }
    }

    @Composable
    private fun NestIcon(
        modifier: Modifier = Modifier,
        icon: Int,
    ) {
        AndroidView(
            factory = { context ->
                IconUnify(context)
            },
            update = {
                it.setImage(icon)
            },
        )
    }

    @Composable
    private fun NestHeader(
        modifier: Modifier = Modifier,
        title: String,
        onNavigationClickListener: () -> Unit,
    ) {
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                HeaderUnify(context)
            },
            update = {
                it.title = title
                it.isShowBackButton = true
                it.setNavigationOnClickListener {
                    onNavigationClickListener()
                }
            }
        )
    }

    @Composable
    private fun NestSwitch(
        isEnabled: Boolean,
        onChanged: (isEnabled: Boolean) -> Unit
    ) {
//        AndroidView(
//            factory = { context ->
//                SwitchUnify(this, object : AttributeSet {
//                    override fun getAttributeCount(): Int = 0
//
//                    override fun getAttributeName(p0: Int): String = ""
//
//                    override fun getAttributeValue(p0: Int): String = ""
//
//                    override fun getAttributeValue(p0: String?, p1: String?): String = ""
//
//                    override fun getPositionDescription(): String = ""
//
//                    override fun getAttributeNameResource(p0: Int): Int = 0
//
//                    override fun getAttributeListValue(
//                        p0: String?,
//                        p1: String?,
//                        p2: Array<out String>?,
//                        p3: Int
//                    ): Int = 0
//
//                    override fun getAttributeListValue(
//                        p0: Int,
//                        p1: Array<out String>?,
//                        p2: Int
//                    ): Int = 0
//
//                    override fun getAttributeBooleanValue(
//                        p0: String?,
//                        p1: String?,
//                        p2: Boolean
//                    ): Boolean = false
//
//                    override fun getAttributeBooleanValue(p0: Int, p1: Boolean): Boolean = false
//
//                    override fun getAttributeResourceValue(p0: String?, p1: String?, p2: Int): Int = 0
//
//                    override fun getAttributeResourceValue(p0: Int, p1: Int): Int = 0
//
//                    override fun getAttributeIntValue(p0: String?, p1: String?, p2: Int): Int = 0
//
//                    override fun getAttributeIntValue(p0: Int, p1: Int): Int = 0
//
//                    override fun getAttributeUnsignedIntValue(
//                        p0: String?,
//                        p1: String?,
//                        p2: Int
//                    ): Int = 0
//
//                    override fun getAttributeUnsignedIntValue(p0: Int, p1: Int): Int = 0
//
//                    override fun getAttributeFloatValue(
//                        p0: String?,
//                        p1: String?,
//                        p2: Float
//                    ): Float = 0f
//
//                    override fun getAttributeFloatValue(p0: Int, p1: Float): Float = 0f
//
//                    override fun getIdAttribute(): String = ""
//
//                    override fun getClassAttribute(): String = ""
//
//                    override fun getIdAttributeResourceValue(p0: Int): Int = 0
//
//                    override fun getStyleAttribute(): Int = 0
//                })
//            },
//            update = {
//                it.isEnabled = isEnabled
//                it.setOnCheckedChangeListener { compoundButton, isEnable ->
//                    onChanged(isEnabled)
//                }
//            }
//        )
    }
}
