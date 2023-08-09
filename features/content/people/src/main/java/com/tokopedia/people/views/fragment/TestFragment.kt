package com.tokopedia.people.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.testTag
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.people.views.screen.ProfileSettingsScreen
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel

/**
 * Created By : Jonathan Darwin on August 09, 2023
 */
class TestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                NestTheme {
                    var reviewSettings by remember {
                        mutableStateOf(
                            ProfileSettingsUiModel.Empty
                        )
                    }

                    ProfileSettingsScreen(
                        reviewSettings = reviewSettings,
                        onBackPressed = {  },
                        onCheckedChanged = {
                            reviewSettings = reviewSettings.copy(
                                isEnabled = !reviewSettings.isEnabled
                            )
                        }
                    )
//                    NestTypography(
//                        modifier = Modifier.testTag("text"),
//                        text = "Hello World"
//                    )
                }
            }
        }
    }
}
