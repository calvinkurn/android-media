package com.tokopedia.play.analytic

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.play.R
import com.tokopedia.play.analytic.robot.prepare
import com.tokopedia.play.data.PlayVodMockModelConfig
import com.tokopedia.play.test.espresso.clickOnViewChild
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.util.*
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.variant_common.view.holder.VariantContainerViewHolder
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 15/09/20.
 */
class CavPlayTrackingVodTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(PlayActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Test
    fun validateTrackingChannelVod() {
        prepare {
            setup(intentsTestRule)
            setMockModel(PlayVodMockModelConfig())
            launch("1")
            setJsonAbsolutePath("play_vod_analytic.json")
        } test {
            fakeLogin()
            fakeLaunch()
            performLike()
            performPinnedProduct()
            performRotateByClick()
            performClose()
            swipeRoom()
            Thread.sleep(2000)
            validate(cassavaTestRule)
        }
    }

    private fun performLike() {
        register(idlResLike)
        Espresso.onView(ViewMatchers.withId(R.id.icon_like)).perform(ViewActions.click()) // like
        Espresso.onView(ViewMatchers.withId(R.id.icon_like)).perform(ViewActions.click()) // unlike
        unregister(idlResLike)
    }

    private fun performPinnedProduct() {
        register(idlResVouchers)
        Espresso.onView(ViewMatchers.withId(R.id.rv_voucher_list)).perform(ViewActions.swipeLeft())
        unregister(idlResVouchers)

        register(idlResProducts)
        Espresso.onView(ViewMatchers.withId(R.id.rv_product_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ProductLineViewHolder>(0, ViewActions.click())) // product detail
        unregister(idlResProducts)

        Espresso.onView(ViewMatchers.withId(R.id.rv_product_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ProductLineViewHolder>(0 , clickOnViewChild(R.id.btn_product_buy))) // buy without variant
                .perform(RecyclerViewActions.actionOnItemAtPosition<ProductLineViewHolder>(0 , clickOnViewChild(R.id.btn_product_atc))) // atc without variant

        register(idlResToaster)
        Espresso.onView(ViewMatchers.withId(R.id.snackbar_btn)).perform(ViewActions.click()) // lihat message ticker
        unregister(idlResToaster)

        Espresso.onView(ViewMatchers.withId(R.id.rv_product_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ProductLineViewHolder>(1 , clickOnViewChild(R.id.btn_product_buy))) // buy => variant sheet

        register(idlResVariants)
        Espresso.onView(ViewMatchers.withId(R.id.rv_variant))
                .perform(RecyclerViewActions.actionOnItemAtPosition<VariantContainerViewHolder>(1 , ViewActions.click()))
        unregister(idlResVariants)

        Espresso.onView(ViewMatchers.withId(R.id.btn_action)).perform(ViewActions.click()) // buy with variant

        register(idlResBuyProduct)
        Espresso.onView(ViewMatchers.withId(R.id.rv_product_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ProductLineViewHolder>(1 , clickOnViewChild(R.id.btn_product_buy))) // atc => variant sheet
        unregister(idlResBuyProduct)

        register(idlResVariants)
        Espresso.onView(ViewMatchers.withId(R.id.rv_variant))
                .perform(RecyclerViewActions.actionOnItemAtPosition<VariantContainerViewHolder>(1 , ViewActions.click()))
        unregister(idlResVariants)

        Espresso.onView(ViewMatchers.withId(R.id.btn_action)).perform(ViewActions.click()) // atc with variant

        Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.iv_sheet_close), ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    private fun performRotateByClick() {
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.iv_fullscreen_control)).perform(ViewActions.click()) // to landscape
        Espresso.onView(ViewMatchers.withId(R.id.view_video_settings)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.isRoot()).perform(orientationPortrait())
    }

    private fun performClose() {
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.iv_back)).perform(ViewActions.click())
    }

    private fun swipeRoom(){
        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.swipeLeft())
    }

    private val idlResLike by lazy {
        ComponentIdlingResource(
                object : PlayIdlingResource {
                    override fun getName(): String = "clickLike"

                    override fun idleState(): Boolean {
                        val view = intentsTestRule.activity.findViewById<View>(R.id.icon_like)
                        return view.isClickable
                    }
                }
        ) }

    private val idlResVouchers by lazy {
        ComponentIdlingResource(
                object : PlayIdlingResource {
                    override fun getName(): String = "scrollVoucher"

                    override fun idleState(): Boolean {
                        val recyclerView = intentsTestRule.activity.findViewById<RecyclerView>(R.id.rv_voucher_list)
                        return recyclerView != null && recyclerView.adapter?.itemCount.toZeroIfNull() > 0
                    }
                }
        ) }

    private val idlResProducts by lazy {
        ComponentIdlingResource(
                object : PlayIdlingResource {
                    override fun getName(): String = "clickProduct"

                    override fun idleState(): Boolean {
                        val recyclerView = intentsTestRule.activity.findViewById<RecyclerView>(R.id.rv_product_list)
                        return recyclerView != null && recyclerView.adapter?.itemCount.toZeroIfNull() > 0
                    }
                }
        ) }

    private val idlResToaster by lazy {
        ComponentIdlingResource(
                object : PlayIdlingResource {
                    override fun getName(): String = "clickToaster"

                    override fun idleState(): Boolean {
                        val view = intentsTestRule.activity.findViewById<TextView>(R.id.snackbar_txt)
                        return view != null && view.visibility == View.VISIBLE
                    }
                }
        ) }


    private val idlResVariants by lazy {
        ComponentIdlingResource(
                object : PlayIdlingResource {
                    override fun getName(): String = "clickVariant"

                    override fun idleState(): Boolean {
                        val recyclerView = intentsTestRule.activity.findViewById<RecyclerView>(R.id.rv_variant)
                        return recyclerView != null && recyclerView.adapter?.itemCount.toZeroIfNull() > 0
                    }
                }
        ) }

    private val idlResBuyProduct by lazy {
        ComponentIdlingResource(
                object : PlayIdlingResource {
                    override fun getName(): String = "clickBuyProduct"

                    override fun idleState(): Boolean {
                        val recyclerView = intentsTestRule.activity.findViewById<RecyclerView>(R.id.rv_product_list)
                        return recyclerView != null && recyclerView.visibility == View.VISIBLE
                    }
                }
        ) }
}