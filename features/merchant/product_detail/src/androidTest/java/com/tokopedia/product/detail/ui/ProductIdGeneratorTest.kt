package com.tokopedia.product.detail.ui

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.product.detail.BuildConfig
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.ui.base.BaseProductDetailUiTest
import com.tokopedia.product.detail.ui.base.ProductDetailActivityMock
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_DISCUSSION_MOSTHELPFUL
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_P1_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_P2_DATA_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_TICKER_PATH
import com.tokopedia.product.detail.util.ProductDetailNetworkIdlingResource
import com.tokopedia.product.detail.util.ProductIdlingInterface
import com.tokopedia.test.application.espresso_component.CommonActions.findViewHolderAndDo
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds
import org.junit.After
import org.junit.Test

class ProductIdGeneratorTest : BaseProductDetailUiTest() {

    private var activity: ProductDetailActivityMock? = null

    private val buttonIdlingResource by lazy {
        ProductDetailNetworkIdlingResource(object : ProductIdlingInterface {
            override fun getName(): String = "networkFinish"

            override fun idleState(): Boolean {
                if (activityCommonRule.activity.getPdpFragment().view?.findViewById<ConstraintLayout>(R.id.partial_layout_button_action) == null) {
                    throw RuntimeException("button not found")
                }

                return activityCommonRule.activity.getPdpFragment().view?.findViewById<ConstraintLayout>(R.id.partial_layout_button_action)?.visibility == View.VISIBLE
            }
        })
    }

    override fun before() {
        customInterceptor.resetInterceptor()
        IdlingRegistry.getInstance().register(buttonIdlingResource)
        createMockModelConfig()
    }

    override fun doBeforeRun() {
        super.doBeforeRun()
        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)
        activity = activityCommonRule.activity
    }

    @After
    fun after() {
        IdlingRegistry.getInstance().unregister(buttonIdlingResource)
    }

    private val printConditions = listOf(
            PrintCondition { view ->
                val parent = (view.parent as? ViewGroup) ?: return@PrintCondition true
                val packageName = parent::class.java.`package`?.name.orEmpty()
                val className = parent::class.java.name

                !packageName.startsWith("com.tokopedia")
                        || !className.contains("unify", ignoreCase = true)
            },
            PrintCondition { view ->
                view.id != View.NO_ID || view is ViewGroup
            }
    )

    private val parentPrintCondition = listOf(
            PrintCondition { view ->
                val parent = (view.parent as? ViewGroup) ?: return@PrintCondition true
                // don't print recyclerview child view, will be printed in separate process
                parent !is RecyclerView
            }
    ) + printConditions

    private val parentViewPrinter = ViewHierarchyPrinter(parentPrintCondition,
            customIdPrefix = "P",
            packageName = BuildConfig.LIBRARY_PACKAGE_NAME)
    private val vhViewPrinter = ViewHierarchyPrinter(printConditions,
            customIdPrefix = "P",
            packageName = BuildConfig.LIBRARY_PACKAGE_NAME)

    @Test
    fun captureParentFragmentId() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_pdp))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val parentProductView = activity?.getPdpFragment()?.view
        val result = parentViewPrinter.printAsCSV(parentProductView!!)
        FileWriter().writeGeneratedViewIds(
                "product_detail_fragment_resid.csv",
                result
        )
    }

    @Test
    fun captureVhId() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_pdp))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val rvSize = activityCommonRule.activity.getPdpFragment().productAdapter?.currentList?.size
                ?: 0

        Thread.sleep(2000)

        repeat(rvSize - 1) {
            findViewHolderAndDo(R.id.rv_pdp, it) { viewResult ->
                viewResult?.let { view ->
                    printCsvAndSave(view, "product_detail_vh_$it", vhViewPrinter)
                }
            }
        }
    }

    private fun printCsvAndSave(view: View?,
                                fileName: String,
                                viewHierarchyPrinter: ViewHierarchyPrinter) {
        view?.let {
            val result = viewHierarchyPrinter.printAsCSV(view)
            FileWriter().writeGeneratedViewIds(
                    "$fileName.csv",
                    result
            )
        }
    }

    private fun createMockModelConfig() {
        customInterceptor.customP1ResponsePath = RESPONSE_P1_PATH
        customInterceptor.customP2DataResponsePath = RESPONSE_P2_DATA_PATH
        customInterceptor.customTickerResponsePath = RESPONSE_TICKER_PATH
        customInterceptor.customDiscussionResponsePath = RESPONSE_DISCUSSION_MOSTHELPFUL
    }
}
