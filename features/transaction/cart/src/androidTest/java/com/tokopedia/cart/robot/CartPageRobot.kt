package com.tokopedia.cart.robot

import android.content.Context
import android.view.View
import android.widget.CheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedGqlResponse
import com.tokopedia.cart.view.mapper.CartUiModelMapper
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.viewholder.CartGroupViewHolder
import com.tokopedia.cart.view.viewholder.CartItemViewHolder
import com.tokopedia.cart.view.viewholder.CartSelectedAmountViewHolder
import com.tokopedia.cart.view.viewholder.CartShopBottomViewHolder
import com.tokopedia.cart.view.viewholder.DisabledAccordionViewHolder
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import com.tokopedia.cart.test.R as carttestR
import com.tokopedia.promousage.R as promousageR
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR

fun cartPage(func: CartPageRobot.() -> Unit) = CartPageRobot().apply(func)

class CartPageRobot {

    private var cartData: CartData? = null
    private var availableCartList: List<Any> = emptyList()
    private var unavailableCartList: List<Any> = emptyList()

    fun waitForData() {
        Thread.sleep(2000)
    }

    fun initData(context: Context, cartRawRes: Int = carttestR.raw.cart_bundle_happy_flow_response) {
        val jsonString = InstrumentationMockHelper.getRawString(context, cartRawRes)
        val jsonArray: JsonArray = CommonUtils.fromJson(
            jsonString,
            JsonArray::class.java
        )
        val jsonObject = jsonArray.asJsonArray[0].asJsonObject.getAsJsonObject("data")
        cartData = Gson().fromJson(
            jsonObject,
            ShopGroupSimplifiedGqlResponse::class.java
        ).shopGroupSimplifiedResponse.data
        availableCartList = CartUiModelMapper.mapAvailableGroupUiModel(cartData!!)
        unavailableCartList = CartUiModelMapper.mapUnavailableShopUiModel(context, cartData!!).first
    }

    fun assertMainContent() {
        onView(withId(R.id.ll_cart_container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_cart)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun assertTickerAnnouncementViewHolder(position: Int) {
        onView(withId(R.id.rv_cart)).perform(
            RecyclerViewActions.actionOnItemAtPosition<TickerAnnouncementViewHolder>(
                position,
                object : ViewAction {
                    override fun getDescription(): String =
                        "performing assertion action on TickerAnnouncementViewHolder"

                    override fun getConstraints(): Matcher<View>? = null

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(
                            View.VISIBLE,
                            view.findViewById<Ticker>(purchase_platformcommonR.id.cartTicker).visibility
                        )
                    }
                }
            )
        )
    }

    fun assertCartSelectedAmountViewHolder(position: Int) {
        onView(withId(R.id.rv_cart)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CartSelectedAmountViewHolder>(
                position,
                object : ViewAction {
                    override fun getDescription(): String =
                        "performing assertion action on CartSelectedAmountViewHolder"

                    override fun getConstraints(): Matcher<View>? = null

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(
                            View.VISIBLE,
                            view.findViewById<Typography>(R.id.text_action_delete).visibility
                        )
                        assertEquals(
                            View.VISIBLE,
                            view.findViewById<Typography>(R.id.text_selected_amount).visibility
                        )
                    }
                }
            )
        )
    }

    fun assertCartSelectedAmountFloatingLayout() {
        onView(withId(R.id.top_layout)).perform(object : ViewAction {
            override fun getDescription(): String = "performing assertion action on Top Layout"

            override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

            override fun perform(uiController: UiController?, view: View) {
                assertEquals(
                    View.VISIBLE,
                    view.findViewById<Typography>(R.id.text_action_delete).visibility
                )
                assertEquals(
                    View.VISIBLE,
                    view.findViewById<Typography>(R.id.text_selected_amount).visibility
                )
                assertEquals(View.VISIBLE, view.visibility)
            }
        })
    }

    fun assertFirstCartGroupViewHolder(
        position: Int,
        shopIndex: Int
    ) {
        onView(withId(R.id.rv_cart)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CartGroupViewHolder>(
                position,
                object : ViewAction {
                    override fun getDescription(): String =
                        "performing assertion action on first CartGroupViewHolder"

                    override fun getConstraints(): Matcher<View>? = null

                    override fun perform(uiController: UiController?, view: View) {
                        val cartGroupHolderData =
                            availableCartList[shopIndex] as CartGroupHolderData
                        assertEquals(
                            if (cartGroupHolderData.groupBadge.isNotEmpty()) View.VISIBLE else View.GONE,
                            view.findViewById<View>(R.id.image_shop_badge).visibility
                        )
                        assertEquals(
                            cartGroupHolderData.groupName,
                            view.findViewById<Typography>(R.id.tv_group_name).text
                        )
                        assertEquals(
                            cartGroupHolderData.fulfillmentName,
                            view.findViewById<Typography>(R.id.tv_fulfill_district).text
                        )
                        assertEquals(
                            if (cartGroupHolderData.freeShippingBadgeUrl.isNotEmpty()) View.VISIBLE else View.GONE,
                            view.findViewById<ImageUnify>(R.id.img_free_shipping).visibility
                        )
                        assertEquals(
                            if (cartGroupHolderData.isCollapsed) View.VISIBLE else View.GONE,
                            view.findViewById<RecyclerView>(R.id.rv_cart_item).visibility
                        )
                        assertTrue(view.findViewById<CheckBox>(R.id.cb_select_shop).isChecked)
                    }
                }
            )
        )

        assertOnEachCartItem(
            shopIndex = shopIndex,
            position = position
        )
    }

    fun assertFirstUnavailableCartGroupViewHolder(
        position: Int,
        shopIndex: Int
    ) {
        onView(withId(R.id.rv_cart)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CartGroupViewHolder>(
                position,
                object : ViewAction {
                    override fun getDescription(): String =
                        "performing assertion action on first unavailable CartGroupViewHolder"

                    override fun getConstraints(): Matcher<View>? = null

                    override fun perform(uiController: UiController?, view: View) {
                        val cartGroupHolderData =
                            unavailableCartList[shopIndex] as CartGroupHolderData
                        assertEquals(
                            if (cartGroupHolderData.groupBadge.isNotEmpty()) View.VISIBLE else View.GONE,
                            view.findViewById<View>(R.id.image_shop_badge).visibility
                        )
                        assertEquals(
                            cartGroupHolderData.groupName,
                            view.findViewById<Typography>(R.id.tv_group_name).text
                        )
                        assertEquals(
                            cartGroupHolderData.fulfillmentName,
                            view.findViewById<Typography>(R.id.tv_fulfill_district).text
                        )
                        assertEquals(
                            if (cartGroupHolderData.freeShippingBadgeUrl.isNotEmpty()) View.VISIBLE else View.GONE,
                            view.findViewById<ImageUnify>(R.id.img_free_shipping).visibility
                        )
                        assertEquals(
                            if (cartGroupHolderData.isCollapsed) View.VISIBLE else View.GONE,
                            view.findViewById<RecyclerView>(R.id.rv_cart_item).visibility
                        )
                    }
                }
            )
        )

        assertOnEachUnavailableCartItem(
            shopIndex = shopIndex,
            position = position
        )
    }

    fun assertCartItemOnPosition(position: Int, func: CartItemViewHolderRobot.() -> Unit) {
        onView(withId(R.id.rv_cart))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<CartItemViewHolder>(
                    position,
                    object : ViewAction {
                        override fun getDescription(): String =
                            "performing assertion action on CartItemViewHolder"

                        override fun getConstraints(): Matcher<View>? = null

                        override fun perform(uiController: UiController?, view: View) {
                            CartItemViewHolderRobot(view).apply(func)
                        }
                    }
                )
            )
    }

    private fun assertOnEachCartItem(shopIndex: Int, position: Int) {
        val shop = availableCartList[shopIndex] as CartGroupHolderData

        for (i in 0 until shop.productUiModelList.size) {
            onView(withId(R.id.rv_cart))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<CartItemViewHolder>(
                        position + 1 + i,
                        object : ViewAction {
                            override fun getDescription(): String =
                                "performing assertion action on CartItemViewHolder"

                            override fun getConstraints(): Matcher<View>? = null

                            override fun perform(uiController: UiController?, view: View) {
                                val cartItemHolderData = shop.productUiModelList[i]
                                if (cartItemHolderData.isShopShown) {
                                    assertEquals(
                                        View.VISIBLE,
                                        view.findViewById<ImageUnify>(R.id.image_shop_badge).visibility
                                    )
                                    assertEquals(
                                        View.VISIBLE,
                                        view.findViewById<Typography>(R.id.tv_shop_name).visibility
                                    )
                                    assertEquals(
                                        cartItemHolderData.shopHolderData.shopName,
                                        view.findViewById<Typography>(R.id.tv_shop_name).text
                                    )
                                } else {
                                    assertEquals(
                                        View.GONE,
                                        view.findViewById<ConstraintLayout>(R.id.ll_shop_header).visibility
                                    )
                                }

                                assertEquals(
                                    cartItemHolderData.productName,
                                    view.findViewById<Typography>(R.id.text_product_name).text.toString()
                                )
                            }
                        }
                    )
                )
        }
    }

    private fun assertOnEachUnavailableCartItem(shopIndex: Int, position: Int) {
        val shop = unavailableCartList[shopIndex] as CartGroupHolderData

        for (i in 0 until shop.productUiModelList.size) {
            onView(withId(R.id.rv_cart))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<CartItemViewHolder>(
                        position + 1 + i,
                        object : ViewAction {
                            override fun getDescription(): String =
                                "performing assertion action on first CartItemViewHolder"

                            override fun getConstraints(): Matcher<View>? = null

                            override fun perform(uiController: UiController?, view: View) {
                                val cartItemHolderData = shop.productUiModelList[i]
                                assertEquals(
                                    View.GONE,
                                    view.findViewById<ConstraintLayout>(R.id.ll_shop_header).visibility
                                )

                                assertEquals(
                                    cartItemHolderData.productName,
                                    view.findViewById<Typography>(R.id.text_product_name).text.toString()
                                )
                            }
                        }
                    )
                )
        }
    }

    fun assertCartGroupViewHolderOnPosition(
        position: Int,
        func: CartGroupViewHolderRobot.() -> Unit
    ) {
        onView(withId(R.id.rv_cart)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CartGroupViewHolder>(
                position,
                object : ViewAction {
                    override fun getDescription(): String =
                        "performing assertion action on CartGroupViewHolder position $position"

                    override fun getConstraints(): Matcher<View>? = null

                    override fun perform(uiController: UiController?, view: View) {
                        CartGroupViewHolderRobot(view).apply(func)
                    }
                }
            )
        )
    }

    fun assertCartShopBottomViewHolderOnPosition(
        position: Int,
        func: CartGroupViewHolderRobot.() -> Unit
    ) {
        onView(withId(R.id.rv_cart)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CartShopBottomViewHolder>(
                position,
                object : ViewAction {
                    override fun getDescription(): String =
                        "performing assertion action on CartShopBottomViewHolder position $position"

                    override fun getConstraints(): Matcher<View>? = null

                    override fun perform(uiController: UiController?, view: View) {
                        CartGroupViewHolderRobot(view).apply(func)
                    }
                }
            )
        )
    }

    fun clickPromoButton() {
        onView(withId(promousageR.id.active_promo_checkout_view)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        ).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isDisplayed()
            }

            override fun getDescription(): String {
                return "click promo button"
            }

            override fun perform(uiController: UiController?, view: View?) {
                view?.performClick()
            }
        })
    }

    fun clickBuyButton() {
        onView(withId(R.id.go_to_courier_page_button)).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isDisplayed()
            }

            override fun getDescription(): String {
                return "click buy button"
            }

            override fun perform(uiController: UiController?, view: View?) {
                view?.performClick()
            }
        })
    }

    fun clickDisabledItemAccordion(
        position: Int
    ) {
        onView(withId(R.id.rv_cart)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DisabledAccordionViewHolder>(
                position,
                object : ViewAction {
                    override fun getDescription(): String =
                        "performing assertion action on DisabledAccordionViewHolder position $position"

                    override fun getConstraints(): Matcher<View>? = null

                    override fun perform(uiController: UiController?, view: View) {
                        view.performClick()
                    }
                }
            )
        )
    }

    infix fun validateAnalytics(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot().apply(func)
    }
}

class ResultRobot {

    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
        assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
    }
}
