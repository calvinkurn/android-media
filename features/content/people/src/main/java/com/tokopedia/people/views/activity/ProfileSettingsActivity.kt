package com.tokopedia.people.views.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.people.di.DaggerUserProfileComponent
import com.tokopedia.people.di.UserProfileModule
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.viewmodels.factory.UserProfileViewModelFactory
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 10, 2023
 */
class ProfileSettingsActivity : ComponentActivity() {

    @Inject
    lateinit var  viewModelFactoryCreator: UserProfileViewModelFactory.Creator

    private val viewModel: UserProfileViewModel by viewModels {
        viewModelFactoryCreator.create(
            this,
            intent?.data?.lastPathSegment.orEmpty()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContent {
            NestTheme {

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

    @Composable
    fun NestIcon(
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
}
