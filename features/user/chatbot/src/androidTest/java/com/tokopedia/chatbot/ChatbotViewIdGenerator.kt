package com.tokopedia.chatbot

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.chatbot.view.activity.ChatbotActivity
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ChatbotViewIdGenerator {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

//    @Rule
//    var rule: ActivityScenarioRule<ChatbotActivity> =
//        ActivityScenarioRule<ChatbotActivity>(
//            ChatbotActivity::class.java
//        )
//    var scenario: ActivityScenario<ChatbotActivity>? = null

    @get:Rule
    var activityCommonRule: ActivityTestRule<ChatbotActivity> = IntentsTestRule(ChatbotActivity::class.java,
        false,
        true)

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

//    private val parentViewPrinter = ViewHierarchyPrinter(
//        parentPrintCondition, customIdPrefix = "P", packageName = BuildConfig.LIBRARY_PACKAGE_NAME
//    )

     val fileWriter = FileWriter()

    lateinit  var activity : Activity

    @Before
    fun doBeforeRun() {
        activity = activityCommonRule.activity
 //       var intent = Intent(activityCommonRule.activity, ChatbotActivity::class.java)
        activityCommonRule.launchActivity(ChatbotActivity.getCallingIntent("3185892", targetContext))
////        scenario = ActivityScenario.launch(ChatbotActivity::class.java)
////        (scenario as ActivityScenario<ChatbotActivity>?)?.moveToState(Lifecycle.State.CREATED)

//        val intent = Intent()
//        intent.putExtra("message_id", "tokopedia-android-internal://global/chatbot/3185892")
//        activity = activityCommonRule.launchActivity(intent)

    }


    @Test
    fun methodTest() {

//        ChatbotInjector.set(
//            DaggerChatbotTestComponent.builder()
//                .baseAppComponent(
//                    (targetContext.applicationContext as BaseMainApplication)
//                        .baseAppComponent)
//                .chatbotTestModule(ChatbotTestModule(targetContext))
//                .build()
//        )
//
//        chatTestComponent?.inject(this)


//        val chatbotComponent = DaggerChatbotComponent.builder().baseAppComponent(
////                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
////                    .chatbotModule(context?.let { ChatbotModule(it) })
////                    .build()
//

//        val intent = RouteManager.getIntent(
//            targetContext,
//            "tokopedia://chatbot/3185892"
//        )

   //     val hierarchyInfo = viewPrinter.print(view =findViewById(R.id.main))


//        val scenario = ActivityScenario.launch<ChatbotActivity>(intent)
//        scenario.moveToState(Lifecycle.State.RESUMED)
//
//        scenario.onActivity {
//            val chatbotFragment = viewPrinter.printAsCSV(
//                view = it.findViewById(R.id.main)
//            )
//            fileWriter.writeGeneratedViewIds(
//                fileName = "chatbot_chatbot_fragment.csv",
//                text = chatbotFragment
//            )
//        }

   //    val hierarchyInfo = viewPrinter.printAsCSV(view = activity.findViewById(R.id.main))
//



    }


}