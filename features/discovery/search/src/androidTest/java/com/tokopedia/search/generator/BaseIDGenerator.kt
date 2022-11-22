package com.tokopedia.search.generator

import android.app.Activity
import android.app.Instrumentation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.search.*
import com.tokopedia.search.RecyclerViewHasItemIdlingResource
import com.tokopedia.search.SearchMockModelConfig
import com.tokopedia.search.createIntent
import com.tokopedia.search.disableOnBoarding
import com.tokopedia.search.generator.utils.IDGeneratorHelper
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseIDGenerator {
    @get:Rule
    val activityRule = IntentsTestRule(
        SearchActivity::class.java,
        false,
        false
    )

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val recyclerViewId = R.id.recyclerview
    protected var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null

    abstract val queryParam: String
    abstract val mockModel: Int

    @Before
    fun setUp() {
        setupGraphqlMockResponse(
            SearchMockModelConfig(
                mockModel
            )
        )

        disableOnBoarding(context)

        activityRule.launchActivity(createIntent(queryParam))

        setupIdlingResource()
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)
        recyclerViewIdlingResource = RecyclerViewHasItemIdlingResource(recyclerView)

        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }
}
