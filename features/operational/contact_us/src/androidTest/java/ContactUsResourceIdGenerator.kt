package com.tokopedia.contactus

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.RouteManager
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity
import com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds
import org.junit.Rule
import org.junit.Test


class ContactUsResourceIdGenerator {

    @get:Rule
    val intentsTestRuleForInboxListActivity = IntentsTestRule(InboxListActivity::class.java, false, false)

    @get:Rule
    val intentsTestRuleForInboxDetailActivity = IntentsTestRule(InboxDetailActivity::class.java, false, false)

    @get:Rule
    var mActivityRule: ActivityScenarioRule<InboxDetailActivity> = ActivityScenarioRule<InboxDetailActivity>(
        InboxDetailActivity::class.java
    )

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
        packageName = "com.tokopedia.contact_us"
    )

    val fileWriter = FileWriter()

    @Test
    fun generateResourceIdForInboxListActivity() {
        val intent = RouteManager.getIntent(
            targetContext,
            "tokopedia-android-internal://customercare-inbox-list/"
        )

        intentsTestRuleForInboxListActivity.launchActivity(intent)

        val inboxListActivity = viewPrinter.printAsCSV(
            view = intentsTestRuleForInboxListActivity.activity.findViewById(R.id.root_view)
        )
        fileWriter.writeGeneratedViewIds(
            fileName = "contact_us_inbox_list.csv",
            text = inboxListActivity
        )
    }

    //TODO fix this issue
    @Test
    fun generateResourceIdForInboxDetailActivity() {
//        val intent = RouteManager.getIntent(
//            targetContext,
//            "tokopedia//customercare/123456"
//        )

//        intentsTestRuleForInboxDetailActivity.launchActivity(InboxDetailActivity.getIntent(
//            targetContext,
//            "5005D000008WeZtQAK",
//            false
//        ))

//        val notAnimatedDrawable = ContextCompat.getDrawable(intentsTestRuleForInboxDetailActivity.activity, R.drawable.abc_vector_test)
//        (intentsTestRuleForInboxDetailActivity.activity.findViewById(R.id.progress_bar_layout) as ProgressBar).indeterminateDrawable =
//            notAnimatedDrawable

//        mActivityRule.scenario.onActivity {

        val intent = Intent(targetContext.applicationContext, InboxDetailActivity::class.java)
        intent.putExtra("ticket_id", "5005D000008WeZtQAK")
        intent.putExtra("is_official_store",false)
        var scenario = ActivityScenario.launch<InboxDetailActivity>(intent)

        scenario.onActivity {
            val inboxDetailActivity = viewPrinter.printAsCSV(
                view = intentsTestRuleForInboxDetailActivity.activity.findViewById(R.id.root_view)
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "contact_us_inbox_detail.csv",
                text = inboxDetailActivity
            )
        }
   //     }



    }

}