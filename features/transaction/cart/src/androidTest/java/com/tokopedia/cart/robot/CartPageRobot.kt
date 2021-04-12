package com.tokopedia.cart.robot

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedGqlResponse
import com.tokopedia.cart.domain.mapper.CartSimplifiedMapper
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.view.viewholder.CartItemViewHolder
import com.tokopedia.cart.view.viewholder.CartSelectAllViewHolder
import com.tokopedia.cart.view.viewholder.CartShopViewHolder
import com.tokopedia.cart.view.viewholder.CartTickerErrorViewHolder
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Assert

fun cartPage(func: CartPageRobot.() -> Unit) = CartPageRobot().apply(func)

class CartPageRobot {

    var cartListData: CartListData? = null

    fun waitForData() {
        Thread.sleep(2000)
    }

    fun initData(context: Context) {
        val jsonString = InstrumentationMockHelper.getRawString(context, com.tokopedia.cart.test.R.raw.cart_happy_flow_response)
        val jsonArray: JsonArray = CommonUtils.fromJson(
                jsonString,
                JsonArray::class.java
        )
        val jsonObject = jsonArray.asJsonArray[0].asJsonObject.getAsJsonObject("data")
        val tmpData = Gson().fromJson(jsonObject, ShopGroupSimplifiedGqlResponse::class.java)
        cartListData = CartSimplifiedMapper(context).convertToCartItemDataList(tmpData.shopGroupSimplifiedResponse.data)
    }

    fun assertMainContent() {
        onView(withId(R.id.ll_cart_container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_cart)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun assertTickerAnnouncementViewHolder(position: Int) {
        onView(withId(R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<TickerAnnouncementViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on TickerAnnouncementViewHolder"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                Assert.assertEquals(View.VISIBLE, view.findViewById<Ticker>(R.id.cartTicker).visibility)
            }
        }))
    }

    fun assertCartSelectAllViewHolder(position: Int) {
        onView(withId(R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<CartSelectAllViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on CartSelectAllViewHolder"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                Assert.assertEquals(View.VISIBLE, view.findViewById<CheckboxUnify>(R.id.checkbox_global).visibility)
                Assert.assertEquals(true, view.findViewById<CheckboxUnify>(R.id.checkbox_global).isChecked)
                Assert.assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.text_select_all).visibility)
            }
        }))
    }

    fun assertCartTickerErrorViewHolder(position: Int, message: String) {
        onView(withId(R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<CartTickerErrorViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on CartTickerErrorViewHolder"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                Assert.assertEquals(message, view.findViewById<Typography>(R.id.ticker_description).text)
            }
        }))
    }

    fun assertFirstCartShopViewHolder(view: View,
                                      position: Int,
                                      shopIndex: Int) {
        onView(withId(R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<CartShopViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on first CartShopViewHolder"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                Assert.assertEquals(cartListData?.shopGroupAvailableDataList?.get(shopIndex)?.shopName
                        ?: "", view.findViewById<Typography>(R.id.tv_shop_name).text)
                Assert.assertEquals(cartListData?.shopGroupAvailableDataList?.get(shopIndex)?.fulfillmentName
                        ?: "", view.findViewById<Typography>(R.id.tv_fulfill_district).text)
                Assert.assertEquals(View.VISIBLE, view.findViewById<ImageView>(R.id.img_shop_badge).visibility)
                Assert.assertEquals(View.VISIBLE, view.findViewById<ImageUnify>(R.id.img_free_shipping).visibility)
                Assert.assertEquals(View.VISIBLE, view.findViewById<RecyclerView>(R.id.rv_cart_item).visibility)
                Assert.assertTrue(view.findViewById<CheckBox>(R.id.cb_select_shop).isChecked)
            }
        }))

        assertOnEachCartItem(
                view = view,
                recyclerViewId = R.id.rv_cart_item,
                shopIndex = shopIndex
        )

    }

    fun assertOnEachCartItem(view: View, recyclerViewId: Int, shopIndex: Int) {
        val childRecyclerView: RecyclerView = view.findViewById(recyclerViewId)

        val tempStoreDesc = childRecyclerView.contentDescription
        childRecyclerView.contentDescription = CommonActions.UNDER_TEST_TAG

        val childItemCount = childRecyclerView.adapter?.itemCount ?: 0
        for (i in 0 until childItemCount) {
            try {
                onView(allOf(withId(recyclerViewId), withContentDescription(CommonActions.UNDER_TEST_TAG)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<CartItemViewHolder>(i, object : ViewAction {
                            override fun getDescription(): String = "performing assertion action on first CartShopViewHolder"

                            override fun getConstraints(): Matcher<View>? = null

                            override fun perform(uiController: UiController?, view: View) {
                                Assert.assertEquals(cartListData?.shopGroupAvailableDataList?.get(shopIndex)?.cartItemDataList?.get(i)?.cartItemData?.originData?.productName, view.findViewById<Typography>(R.id.text_product_name).text.toString())
                            }
                        }))

            } catch (e: PerformException) {
                e.printStackTrace()
            }
        }
        childRecyclerView.contentDescription = tempStoreDesc
    }

    fun assertCartShopViewHolderOnPosition(position: Int, func: CartShopViewHolderRobot.() -> Unit) {
        onView(withId(R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<CartShopViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on CartShopViewHolder position $position"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                CartShopViewHolderRobot(view).apply(func)
            }
        }))
    }

    fun clickPromoButton() {
        onView(withId(R.id.promo_checkout_btn_cart)).perform(ViewActions.click())
    }

    fun clickBuyButton() {
        onView(withId(R.id.go_to_courier_page_button)).perform(ViewActions.click())
    }

    infix fun validateAnalytics(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot().apply(func)
    }

}

class ResultRobot {

    fun hasPassedAnalytics(gtmLogDBSource: GtmLogDBSource, context: Context, queryFileName: String) {
        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, queryFileName), hasAllSuccess())
    }

}