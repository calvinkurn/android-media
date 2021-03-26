package com.tokopedia.product.addedit.utils

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.test.application.espresso_component.CommonMatcher
import org.hamcrest.Matchers

object InstrumentedTestUtil {

    fun createIntentEditProduct(productId: String): Intent {
        val applink = Uri.parse(ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
                .buildUpon()
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_ID, productId)
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_MODE, ApplinkConstInternalMechant.MODE_EDIT_PRODUCT)
                .build()

        return Intent(InstrumentationRegistry.getInstrumentation().targetContext, AddEditProductPreviewActivity::class.java).also {
            it.data = applink
        }
    }

    fun performDialogSecondaryClick() {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(R.id.dialog_btn_secondary)))
                .perform(ViewActions.click())
    }

    fun performDialogPrimaryClick() {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(R.id.dialog_btn_primary)))
                .perform(ViewActions.click())
    }

    fun performClick(id: Int) {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.click())
    }

    fun performScrollAndClick(id: Int) {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.scrollTo())

        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.click())
    }

    fun performReplaceText(id: Int, text: String) {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.scrollTo())

        Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.text_field_input), ViewMatchers.isDescendantOfA(ViewMatchers.withId(id))))
                .perform(ViewActions.typeText(text), ViewActions.closeSoftKeyboard())
    }

    fun performPressBack() {
        Espresso.pressBackUnconditionally()
    }
}