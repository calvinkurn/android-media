package com.tokopedia.recommendation_widget_common.test

import android.content.Context
import android.content.Intent
import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.recommendation_widget_common.RecommendationTypeConst.PAGENAME_VERTICAL
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.di.recomwidget.RecommendationComponent
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.test.RecommendationWidgetViewTestActivity.Companion.PAGE_NAME_KEY
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetComponentProvider
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetState
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

class RecommendationWidgetViewTest {

    private val context: Context
        get() = getInstrumentation().context!!

    private val trackingQueue = mockk<TrackingQueue>(relaxed = true)

    private fun viewModelFactory(
        recommendationWidgetState: RecommendationWidgetState
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val mutableStateFlow = MutableStateFlow(recommendationWidgetState)
                return mockk<RecommendationWidgetViewModel>(relaxed = true) {
                    every { stateFlow } returns mutableStateFlow
                } as T
            }
        }
    }

    private fun recommendationComponent(recommendationWidgetState: RecommendationWidgetState) =
        object : RecommendationComponent {
            override fun inject(application: BaseMainApplication) {}

            override fun getViewModelFactory(): ViewModelProvider.Factory {
                return viewModelFactory(recommendationWidgetState)
            }
        }

    private fun stateFrom(pageName: String, @RawRes rawResponseId: Int): RecommendationWidgetState {
        val model = RecommendationWidgetModel(
            metadata = RecommendationWidgetMetadata(
                pageName = pageName,
            ),
        )

        val widgetList = rawToObject<RecommendationEntity>(rawResponseId)
            .productRecommendationWidget
            .data
            .mappingToRecommendationModel()

        return RecommendationWidgetState().from(model, widgetList)
    }

    private fun openTestActivity(
        recommendationWidgetState: RecommendationWidgetState,
        pageName: String,
    ) {
        RecommendationWidgetComponentProvider.setRecommendationComponent(
            recommendationComponent(recommendationWidgetState)
        )

        val intent = intent(pageName)

        getInstrumentation().startActivitySync(intent)

//        Thread.sleep(300_000)
    }

    private fun intent(pageName: String): Intent =
        Intent(Intent.ACTION_MAIN).also {
            it.setClassName(
                getInstrumentation().targetContext.packageName,
                RecommendationWidgetViewTestActivity::class.java.name,
            )
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            it.putExtra(PAGE_NAME_KEY, pageName)
        }

    @Test
    fun recommendation_widget_comparison_bpc() {
        val pageName = "comparison_bpc_61"
        val state = stateFrom(
            pageName = pageName,
            rawResponseId = R.raw.comparison_widget_bpc,
        )

        openTestActivity(state, pageName)
    }

    @Test
    fun recommendation_widget_carousel() {
        val pageName = "pdp_1"
        val state = stateFrom(
            pageName = pageName,
            rawResponseId = R.raw.custom_horizontal,
        )

        openTestActivity(state, pageName)
    }

    @Test
    fun recommendation_widget_carousel_shimmering() {
        val pageName = "pdp_1"
        val state = RecommendationWidgetState().loading(
            RecommendationWidgetModel(
                metadata = RecommendationWidgetMetadata(
                    pageName = pageName,
                )
            )
        )

        openTestActivity(state, pageName)
    }

    @Test
    fun recommendation_vertical() {
        val pageName = PAGENAME_VERTICAL
        val state = stateFrom(
            pageName = pageName,
            rawResponseId = R.raw.recom_vertical,
        )

        openTestActivity(state, pageName)
    }

    @Test
    fun recommendation_widget_carousel_empty_data() {
        val pageName = "pdp_1"
        val state = stateFrom(
            pageName = pageName,
            rawResponseId = R.raw.custom_horizontal_empty,
        )

        openTestActivity(state, pageName)
    }
}
