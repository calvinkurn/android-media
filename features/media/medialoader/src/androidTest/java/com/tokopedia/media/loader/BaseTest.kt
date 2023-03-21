package com.tokopedia.media.loader

import android.content.Context
import android.widget.ImageView
import androidx.test.espresso.Espresso
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.media.loader.di.DaggerMediaLoaderComponent
import com.tokopedia.media.loader.di.MediaLoaderModule
import com.tokopedia.media.loader.rule.ActivityCustomRule
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class BaseTest {

    @get:Rule
    val activityTestRule = object : ActivityCustomRule(
        ActivityScenarioRule(DebugMediaLoaderActivity::class.java)
    ) {
        override fun before() {
            InstrumentationAuthHelper
                .loginInstrumentationTestUser1()
        }
    }

    private val instrumentation = InstrumentationRegistry
        .getInstrumentation()

    protected val applicationContext: Context
        get() = instrumentation
            .context
            .applicationContext

    protected val interceptor = StubInterceptor()
    protected val countingIdlingResource = CountingIdlingResource("media-loader")

    @Before
    open fun setUp() {
        DaggerMediaLoaderComponent
            .builder()
            .mediaLoaderModule(
                MediaLoaderModule(applicationContext)
            )
            .build()
            .inject(interceptor)

        Intents.init()
    }

    @After
    open fun tearDown() {
        Intents.release()
    }

    protected fun onImageView(imgView: (ImageView) -> Unit) {
        Espresso.onView(ViewMatchers.withId(R.id.img_sample))
            .check { view, _ -> imgView(view as ImageView) }
    }

    companion object {
        const val secureImageUrl = "https://chat.tokopedia.com/tc/v1/download_secure/1844584535/2022-04-19/99f532b4-bfb0-11ec-9296-42010a2942a0"
        const val publicImageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2021/11/13/819f37fd-cff6-4212-ae58-5618ac2b5e07.jpg"
        const val iconUrl = "https://images.tokopedia.net/img/cache/100-square/iEWsxH/2021/10/5/75f2fb8a-a4ca-4cd6-a166-7279daef1d5b.png"
        const val invalidUrl = "https://images.tokopedia.com/img/invalid_url.png"
    }
}
