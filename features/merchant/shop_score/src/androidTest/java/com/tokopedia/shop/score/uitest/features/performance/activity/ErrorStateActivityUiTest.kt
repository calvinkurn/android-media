package com.tokopedia.shop.score.uitest.features.performance.activity

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.uitest.features.performance.base.ShopScoreUiTest
import com.tokopedia.shop.score.stub.common.util.isViewDisplayed
import com.tokopedia.shop.score.stub.common.util.onIdView
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.CoreMatchers
import org.junit.Test
import java.io.IOException

@UiTest
class ErrorStateActivityUiTest : ShopScoreUiTest() {

    @Test
    fun show_global_error_state_with_no_connection_error() {
        getShopInfoPeriodUseCaseStub.responseStub = IOException("Sorry koneksi internet anda sedang gangguan")
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.globalErrorShopPerformance).isViewDisplayed()
        val errorState = activityRule.activity.findViewById<GlobalError>(R.id.globalErrorShopPerformance)
        assertThat(
            errorState.errorTitle.text.toString(),
            CoreMatchers.`is`(context.getString(com.tokopedia.globalerror.R.string.noConnectionTitle))
        )
        assertThat(
            errorState.errorDescription.text.toString(),
            CoreMatchers.`is`(context.getString(com.tokopedia.globalerror.R.string.noConnectionDesc))
        )
    }

    @Test
    fun show_global_error_state_with_server_error() {
        getShopInfoPeriodUseCaseStub.responseStub =
            Throwable("Sorry server sedang berkendala")
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.globalErrorShopPerformance).isViewDisplayed()
        val errorState = activityRule.activity.findViewById<GlobalError>(R.id.globalErrorShopPerformance)
        assertThat(
            errorState.errorTitle.text.toString(),
            CoreMatchers.`is`(context.getString(com.tokopedia.globalerror.R.string.error500Title))
        )
        assertThat(
            errorState.errorDescription.text.toString(),
            CoreMatchers.`is`(context.getString(com.tokopedia.globalerror.R.string.error500Desc))
        )
    }
}