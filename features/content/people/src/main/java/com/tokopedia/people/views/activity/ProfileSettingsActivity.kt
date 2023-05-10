package com.tokopedia.people.views.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tokopedia.common_compose.ui.NestTheme

/**
 * Created By : Jonathan Darwin on May 10, 2023
 */
class ProfileSettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NestTheme {

            }
        }
    }
}
