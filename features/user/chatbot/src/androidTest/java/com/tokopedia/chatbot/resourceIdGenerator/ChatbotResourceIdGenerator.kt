package com.tokopedia.chatbot.resourceIdGenerator

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.RouteManager
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.activity.ChatbotActivity
import com.tokopedia.chatbot.view.fragment.ChatBotProvideRatingFragment
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds
import org.junit.Test


class ChatbotResourceIdGenerator {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

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
    fun generateIdForChatbotFragment() {

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

    @Test
    fun generateIdForChatbotCsatFragment() {
        val scenario = launchFragment<ChatBotCsatFragment>(themeResId = R.style.AppTheme)

        scenario.onFragment {
            var root = it.view?.rootView
            if (root != null) {
                val chatbotCsatFragment = viewPrinter.printAsCSV(
                    view = root.findViewById(R.id.parent_layout)
                )
                fileWriter.writeGeneratedViewIds(
                    fileName = "chatbot_csat_fragment.csv",
                    text = chatbotCsatFragment
                )
            }
        }
    }

    @Test
    fun generateIdForChatbotProvideRatingFragment() {
        val scenario = launchFragment<ChatBotProvideRatingFragment>(themeResId = R.style.AppTheme)

        scenario.onFragment {
            var root = it.view?.rootView
            if (root != null) {
                val chatbotProvideRatingFragment = viewPrinter.printAsCSV(
                    view = root.findViewById(R.id.parent_layout)
                )
                fileWriter.writeGeneratedViewIds(
                    fileName = "chatbot_provide_rating_fragment.csv",
                    text = chatbotProvideRatingFragment
                )
            }
        }
    }

}
