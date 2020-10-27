package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.view.ICartListView
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by Irfan Khoirul on 2020-01-31.
 */

object CartListPresenterLocalDataChangeTest : Spek({

    val view: ICartListView = mockk(relaxed = true)

    Feature("Local data changes") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("Quantity changed") {

            var result = false

            val cartDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData(originalQty = 1)
                    updatedData = CartItemData.UpdatedData(quantity = 2)
                })
            }

            Given("cart data") {
                every { view.getAllCartDataList() } returns cartDataList
            }

            When("check is data changed") {
                result = cartListPresenter.dataHasChanged()
            }

            Then("data should be changed") {
                Assert.assertTrue(result)
            }

        }

        Scenario("Notes changed") {

            var result = false

            val cartDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData(originalRemark = "note")
                    updatedData = CartItemData.UpdatedData(remark = "note note")
                })
            }

            Given("cart data") {
                every { view.getAllCartDataList() } returns cartDataList
            }

            When("check is data changed") {
                result = cartListPresenter.dataHasChanged()
            }

            Then("data should be changed") {
                Assert.assertTrue(result)
            }

        }

        Scenario("Quantity and notes changed") {

            var result = false

            val cartDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData(originalQty = 1, originalRemark = "note")
                    updatedData = CartItemData.UpdatedData(quantity = 2, remark = "note note")
                })
            }

            Given("cart data") {
                every { view.getAllCartDataList() } returns cartDataList
            }

            When("check is data changed") {
                result = cartListPresenter.dataHasChanged()
            }

            Then("data should be changed") {
                Assert.assertTrue(result)
            }

        }

        Scenario("Quantity and notes did not changed") {

            var result = false

            val cartDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData(originalQty = 1, originalRemark = "note")
                    updatedData = CartItemData.UpdatedData(quantity = 1, remark = "note")
                })
            }

            Given("cart data") {
                every { view.getAllCartDataList() } returns cartDataList
            }

            When("check is data changed") {
                result = cartListPresenter.dataHasChanged()
            }

            Then("data should not be changed") {
                Assert.assertFalse(result)
            }

        }

    }

})