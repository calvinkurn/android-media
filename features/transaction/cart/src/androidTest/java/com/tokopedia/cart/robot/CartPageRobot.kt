package com.tokopedia.cart.robot

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
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
import com.tokopedia.cart.view.viewholder.CartShopBottomViewHolder
import com.tokopedia.cart.view.viewholder.CartTickerErrorViewHolder
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

fun cartPage(func: CartPageRobot.() -> Unit) = CartPageRobot().apply(func)

class CartPageRobot {

    private var cartData: CartData? = null
    private var availableCartList: List<Any> = emptyList()

    fun waitForData() {
        Thread.sleep(2000)
    }

    fun initData(context: Context) {
        val jsonString = InstrumentationMockHelper.getRawString(context, com.tokopedia.cart.test.R.raw.cart_bundle_happy_flow_response)
        val jsonArray: JsonArray = CommonUtils.fromJson(
            jsonString,
            JsonArray::class.java
        )
        val jsonObject = jsonArray.asJsonArray[0].asJsonObject.getAsJsonObject("data")
        cartData = Gson().fromJson(jsonObject, ShopGroupSimplifiedGqlResponse::class.java).shopGroupSimplifiedResponse.data
        availableCartList = CartUiModelMapper.mapAvailableGroupUiModel(cartData!!)
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
                    override fun getDescription(): String = "performing assertion action on TickerAnnouncementViewHolder"

                    override fun getConstraints(): Matcher<View>? = null

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(View.VISIBLE, view.findViewById<Ticker>(R.id.cartTicker).visibility)
                    }
                }
            )
        )
    }

    fun assertCartSelectAllViewHolder() {
        onView(withId(R.id.top_layout)).perform(object : ViewAction {
            override fun getDescription(): String = "performing assertion action on Top Layout"

            override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

            override fun perform(uiController: UiController?, view: View) {
                assertEquals(View.VISIBLE, view.findViewById<CheckboxUnify>(R.id.checkbox_global).visibility)
                assertEquals(true, view.findViewById<CheckboxUnify>(R.id.checkbox_global).isChecked)
                assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.text_select_all).visibility)
                assertEquals(View.VISIBLE, view.visibility)
            }
        })
    }

    fun assertCartTickerErrorViewHolder(position: Int, message: String) {
        onView(withId(R.id.rv_cart)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CartTickerErrorViewHolder>(
                position,
                object : ViewAction {
                    override fun getDescription(): String = "performing assertion action on CartTickerErrorViewHolder"

                    override fun getConstraints(): Matcher<View>? = null

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(message, view.findViewById<Typography>(R.id.ticker_description).text)
                    }
                }
            )
        )
    }

    fun assertFirstCartShopViewHolder(
        view: View,
        position: Int,
        shopIndex: Int
    ) {
        onView(withId(R.id.rv_cart)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CartGroupViewHolder>(
                position,
                object : ViewAction {
                    override fun getDescription(): String = "performing assertion action on first CartShopViewHolder"

                    override fun getConstraints(): Matcher<View>? = null

                    override fun perform(uiController: UiController?, view: View) {
                        val cartGroupHolderData = availableCartList[shopIndex] as CartGroupHolderData
                        assertEquals(cartGroupHolderData.groupName, view.findViewById<Typography>(R.id.tv_shop_name).text)
                        assertEquals(cartGroupHolderData.fulfillmentName, view.findViewById<Typography>(R.id.tv_fulfill_district).text)
                        assertEquals(View.VISIBLE, view.findViewById<ImageView>(R.id.image_shop_badge).visibility)
                        assertEquals(View.VISIBLE, view.findViewById<ImageUnify>(R.id.img_free_shipping).visibility)
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
            view = view,
            recyclerViewId = R.id.rv_cart,
            shopIndex = shopIndex,
            position = position
        )
    }

    fun assertOnEachCartItem(view: View, recyclerViewId: Int, shopIndex: Int, position: Int) {
//        val childRecyclerView: RecyclerView = view.findViewById(recyclerViewId)

//        val tempStoreDesc = childRecyclerView.contentDescription
//        childRecyclerView.contentDescription = CommonActions.UNDER_TEST_TAG
        val shop = availableCartList[shopIndex] as CartGroupHolderData

        for (i in 0 until shop.productUiModelList.size) {
//            try {
            onView(withId(R.id.rv_cart))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<CartItemViewHolder>(
                        position + 1 + i,
                        object : ViewAction {
                            override fun getDescription(): String = "performing assertion action on first CartShopViewHolder"

                            override fun getConstraints(): Matcher<View>? = null

                            override fun perform(uiController: UiController?, view: View) {
                                assertEquals(shop.productUiModelList[i].productName, view.findViewById<Typography>(R.id.text_product_name).text.toString())
                            }
                        }
                    )
                )
//            } catch (e: PerformException) {
//                e.printStackTrace()
//            }
        }
//        childRecyclerView.contentDescription = tempStoreDesc
    }

    fun assertCartShopViewHolderOnPosition(position: Int, func: CartShopViewHolderRobot.() -> Unit) {
        onView(withId(R.id.rv_cart)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CartGroupViewHolder>(
                position,
                object : ViewAction {
                    override fun getDescription(): String = "performing assertion action on CartShopViewHolder position $position"

                    override fun getConstraints(): Matcher<View>? = null

                    override fun perform(uiController: UiController?, view: View) {
                        CartShopViewHolderRobot(view).apply(func)
                    }
                }
            )
        )
    }

    fun assertCartShopBottomViewHolderOnPosition(position: Int, func: CartShopViewHolderRobot.() -> Unit) {
        onView(withId(R.id.rv_cart)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CartShopBottomViewHolder>(
                position,
                object : ViewAction {
                    override fun getDescription(): String = "performing assertion action on CartShopBottomViewHolder position $position"

                    override fun getConstraints(): Matcher<View>? = null

                    override fun perform(uiController: UiController?, view: View) {
                        CartShopViewHolderRobot(view).apply(func)
                    }
                }
            )
        )
    }

    fun clickPromoButton() {
        onView(withId(R.id.promo_checkout_btn_cart)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(object : ViewAction {
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

    infix fun validateAnalytics(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot().apply(func)
    }
}

class ResultRobot {

    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
        assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
    }
}
