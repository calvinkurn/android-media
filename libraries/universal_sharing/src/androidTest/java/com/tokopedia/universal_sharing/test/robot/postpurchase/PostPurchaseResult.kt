package com.tokopedia.universal_sharing.test.robot.postpurchase

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.stub.common.matcher.atPositionCheckErrorType
import com.tokopedia.universal_sharing.stub.common.matcher.atPositionCheckInstanceOf
import com.tokopedia.universal_sharing.stub.common.matcher.withRecyclerView
import com.tokopedia.universal_sharing.view.model.UniversalSharingGlobalErrorUiModel
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseProductUiModel
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseShopTitleUiModel

object PostPurchaseResult {

    fun assertShopItemAt(position: Int) {
        onView(
            withId(R.id.universal_sharing_post_purchase_rv)
        ).check(
            atPositionCheckInstanceOf(
                position,
                UniversalSharingPostPurchaseShopTitleUiModel::class.java
            )
        )
    }

    fun assertShopNameAt(position: Int, text: String) {
        onView(
            withRecyclerView(R.id.universal_sharing_post_purchase_rv)
                .atPositionOnView(position, R.id.universal_sharing_tv_shop)
        ).check(matches(withSubstring(text)))
    }

    fun assertProductItemAt(position: Int) {
        onView(
            withId(R.id.universal_sharing_post_purchase_rv)
        ).check(
            atPositionCheckInstanceOf(
                position,
                UniversalSharingPostPurchaseProductUiModel::class.java
            )
        )
    }

    fun assertProductNameAt(position: Int, text: String) {
        onView(
            withRecyclerView(R.id.universal_sharing_post_purchase_rv)
                .atPositionOnView(position, R.id.universal_sharing_tv_product_name)
        ).check(matches(withSubstring(text)))
    }

    fun assertProductPriceAt(position: Int, text: String) {
        onView(
            withRecyclerView(R.id.universal_sharing_post_purchase_rv)
                .atPositionOnView(position, R.id.universal_sharing_tv_product_price)
        ).check(matches(withSubstring(text)))
    }

    fun assertGlobalErrorType(
        position: Int,
        errorType: UniversalSharingGlobalErrorUiModel.ErrorType
    ) {
        onView(
            withId(R.id.universal_sharing_post_purchase_rv)
        ).check(
            atPositionCheckErrorType(position, errorType)
        )
    }
}
