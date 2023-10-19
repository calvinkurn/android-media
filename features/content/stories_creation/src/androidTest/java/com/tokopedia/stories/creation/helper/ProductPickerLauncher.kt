package com.tokopedia.stories.creation.helper

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import com.tokopedia.stories.creation.container.ProductPickerTestActivity
import com.tokopedia.stories.creation.view.activity.StoriesCreationActivity

/**
 * Created By : Jonathan Darwin on October 19, 2023
 */
class ProductPickerLauncher(
    private val context: Context
) {

    fun launchActivity(initialMock: () -> Unit = {}) {
        initialMock()

        val scenario = ActivityScenario.launch<ProductPickerTestActivity>(
            Intent(context, ProductPickerTestActivity::class.java)
        )

        scenario.moveToState(Lifecycle.State.RESUMED)
    }
}
