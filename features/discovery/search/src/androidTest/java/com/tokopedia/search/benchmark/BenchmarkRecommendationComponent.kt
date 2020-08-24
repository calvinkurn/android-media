package com.tokopedia.search.benchmark

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.search.createRecommendationListener
import com.tokopedia.search.env.BlankTestActivity
import com.tokopedia.search.mock.MockSearchProductModel.getRecommendationItemViewModel
import com.tokopedia.search.mock.MockSearchProductModel.getRecommendationTitleViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationTitleViewHolder
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import org.junit.Rule
import org.junit.Test

internal class BenchmarkRecommendationComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var activityRule: ActivityTestRule<BlankTestActivity> = ActivityTestRule(BlankTestActivity::class.java)

    @Test
    fun benchmark_onBind_ViewHolder_recommendation_title() {
        val itemView = BenchmarkObject.simpleViewFromLayout(RecommendationTitleViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = RecommendationTitleViewHolder(itemView)
        val data = getRecommendationTitleViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_recommendation_title() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                RecommendationTitleViewHolder.LAYOUT) {
            RecommendationTitleViewHolder(it)
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_recommendation_item() {
        val itemView = BenchmarkObject.simpleViewFromLayout(RecommendationItemViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = RecommendationItemViewHolder(
                itemView, createRecommendationListener())
        val data = getRecommendationItemViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_recommendation_item() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                RecommendationItemViewHolder.LAYOUT) {
            RecommendationItemViewHolder(it, createRecommendationListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }
}