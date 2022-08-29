package com.tokopedia.chatbot.resourceIdGenerator

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.applink.RouteManager
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.activity.InstrumentationChatbotTestActivity
import com.tokopedia.chatbot.view.activity.ChatbotActivity
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds
import org.junit.Rule
import org.junit.Test

class ChatbotResourceIdGenerator {

//    @get:Rule
//    var activityRule = object : ActivityTestRule<InstrumentationChatbotTestActivity>(InstrumentationChatbotTestActivity::class.java) {
//        override fun beforeActivityLaunched() {
//            super.beforeActivityLaunched()
//            InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
//            setupTopAdsDetector()
//        }
//    }

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext


    @get:Rule
    var activityCommonRule: ActivityTestRule<InstrumentationChatbotTestActivity> = IntentsTestRule(
        InstrumentationChatbotTestActivity::class.java,
        false,
        false)

    private val printConditions = listOf(
        PrintCondition { view ->
            val parent = (view.parent as? ViewGroup) ?: return@PrintCondition true
            val packageName = parent::class.java.`package`?.name.orEmpty()
            val className = parent::class.java.name
            !packageName.startsWith("com.tokopedia") || !className.contains(
                "unify",
                ignoreCase = true
            )
        },
        PrintCondition { view ->
            view.id != View.NO_ID || view is ViewGroup
        }
    )

    val viewPrinter = ViewHierarchyPrinter(
        printConditions,
        packageName = "com.tokopedia.chatbot"
    )


    val fileWriter = FileWriter()

    @Test
    fun check() {

        val intent = RouteManager.getIntent(
            targetContext,
            "tokopedia://chatbot/3185892"
        )

        val scenario = ActivityScenario.launch<ChatbotActivity>(intent)
        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onActivity {
            val chatbotFragment = viewPrinter.printAsCSV(
                view = it.findViewById(R.id.main)
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "chatbot_chatbot_fragment.csv",
                text = chatbotFragment
            )
        }

    }



}
