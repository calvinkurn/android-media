package com.tokopedia.content.common.producttag.testcase

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.autocompletecomponent.suggestion.TYPE_SHOP
import com.tokopedia.content.common.producttag.container.ContentProductTagTestActivity
import com.tokopedia.content.common.producttag.container.ContentProductTagTestActivity.Companion.EXTRA_ARGUMENT
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.play.test.espresso.delay
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ContentProductTagConfigUiTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private fun launchActivity(argumentBuilder: ContentProductTagArgument.Builder) {
        ActivityScenario.launch<ContentProductTagTestActivity>(
            Intent(targetContext, ContentProductTagTestActivity::class.java).apply {
                putExtra(EXTRA_ARGUMENT, argumentBuilder.build())
            }
        )
    }

    /**
     * Test:
     * 1. authorType -> breadcrumb is not showing
     * 2. productTagSource
     * 3. isMultipleSelectionProduct -> checkbox show or not & select unselect
     * 4. isFullPageAutocomplete -> from us or not
     * 5. isShowActionBarDivider
     */

    @Test
    fun contentProductTag_ugc_breadcrumbVisible() {
        val argument = ContentProductTagArgument.Builder()
            .setAuthorType(TYPE_USER)
        launchActivity(argument)

        delay(3000)
    }

    @Test
    fun contentProductTag_seller_breadcrumbHidden() {
        val argument = ContentProductTagArgument.Builder()
            .setAuthorType(TYPE_SHOP)
        launchActivity(argument)

        delay(3000)
    }
}