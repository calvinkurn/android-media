package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
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
        ActivityScenarioRule(StubDebugMediaLoaderActivity::class.java)
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

    lateinit var activity: StubDebugMediaLoaderActivity

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

        activityTestRule.inner.scenario.onActivity {
            activity = it as StubDebugMediaLoaderActivity
        }
    }

    @After
    open fun tearDown() {
        Intents.release()
    }

    protected fun onImageView(imgView: (ImageView) -> Unit) {
        Espresso.onView(ViewMatchers.withId(R.id.img_sample))
            .check { view, _ -> imgView(view as ImageView) }
    }

    protected fun setImageViewContent(content: Bitmap) {
        activity.setImageViewContent(content)
    }

    companion object {
        const val invalidUrl = "https://images.tokopedia.com/img/invalid_url.png"
        const val secureImageUrl = "https://chat.tokopedia.com/tc/v1/download_secure/1844584535/2022-04-19/99f532b4-bfb0-11ec-9296-42010a2942a0"
        const val publicImageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2021/11/13/819f37fd-cff6-4212-ae58-5618ac2b5e07.jpg"
        const val iconUrl = "https://images.tokopedia.net/img/cache/100-square/iEWsxH/2021/10/5/75f2fb8a-a4ca-4cd6-a166-7279daef1d5b.png"
        const val notFoundUrl = "https://images.tokopedia.net/img/cache/1200/BgtCLw/2023/4/10/de89952a-3ae6-40d7-9cad-226c24953f8.jpg"
        const val badUrl = "https://images.tokopedia.net/img/haryot/20/7/4/0d8b7db3-4609-4357-8ab6-3138d7477186.jpg"
        const val goneUrl = "https://images.tokopedia.net/img/haryot/2023/7/4/0d8b7db3-4609-4357-8ab6-3138d7477186.jpg"
    }
}
