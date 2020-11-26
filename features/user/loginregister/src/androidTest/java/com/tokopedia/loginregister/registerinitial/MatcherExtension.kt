package com.tokopedia.loginregister.registerinitial

import android.view.View
import androidx.annotation.CheckResult
import androidx.test.espresso.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers.any

object MatcherExtension {
    @CheckResult
    fun exists(interaction: ViewInteraction): Boolean {
        return try {
            interaction.perform(object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return any(View::class.java)
                }

                override fun getDescription(): String {
                    return "check for existence"
                }

                override fun perform(uiController: UiController?, view: View?) {
                    // no op, if this is run, then the execution will continue after .perform(...)
                }
            })
            true
        } catch (ex: AmbiguousViewMatcherException) {
            // if there's any interaction later with the same matcher, that'll fail anyway
            true // we found more than one
        } catch (ex: NoMatchingViewException) {
            false
        } catch (ex: NoMatchingRootException) {
            // optional depending on what you think "exists" means
            false
        }
    }
}