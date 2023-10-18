package com.tokopedia.universal_sharing.stub.common.matcher

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.UniversalSharingPostPurchaseAdapter
import com.tokopedia.universal_sharing.view.model.UniversalSharingGlobalErrorUiModel
import org.hamcrest.core.IsInstanceOf

fun atPositionCheckInstanceOf(
    position: Int,
    expectedClass: Class<*>
): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) throw noViewFoundException
        val item = getItem(view, position)
        val matcher = IsInstanceOf(expectedClass)
        ViewMatchers.assertThat(item, matcher)
    }
}

fun atPositionCheckErrorType(
    position: Int,
    errorType: UniversalSharingGlobalErrorUiModel.ErrorType
): ViewAssertion {
    return ViewAssertion { view, noViewFoundExpection ->
        if (noViewFoundExpection != null) throw noViewFoundExpection
        val item = getItem(view, position)
        if (item is UniversalSharingGlobalErrorUiModel) {
            if (item.errorType != errorType) {
                throw AssertionError("Wrong error type, actual: ${item.errorType}")
            }
        } else {
            throw AssertionError("Not Global Error Ui Model")
        }
    }
}

private fun getItem(
    view: View,
    position: Int
): Any {
    val recyclerView: RecyclerView = view as RecyclerView
    val adapter: UniversalSharingPostPurchaseAdapter =
        recyclerView.adapter as UniversalSharingPostPurchaseAdapter
    return adapter.getItem(position)
}
