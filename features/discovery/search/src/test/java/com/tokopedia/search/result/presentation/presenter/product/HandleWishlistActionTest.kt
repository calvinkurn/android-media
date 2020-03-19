package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel.WishlistResult
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.slot
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleWishlistActionTest: Spek({

    Feature("Handle wishlist action") {
        defaultTimeout = 9999999
        
        createTestInstance()

        Scenario("Handle wishlist action with null product card options model") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            When("Handle wishlist action") {
                presenter.handleWishlistAction(null)
            }

            Then("verify view does not do anything") {
                confirmVerified(productListView)
            }
        }

        Scenario("Handle wishlist action for recommendation product with non-login user") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter
            val productCardOptionsModel = ProductCardOptionsModel(
                    productId = "12345",
                    isRecommendation = true
            ).also {
                it.wishlistResult = WishlistResult(isUserLoggedIn = false)
            }

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            When("Handle wishlist action") {
                presenter.handleWishlistAction(productCardOptionsModel)
            }

            Then("Verify view interaction") {
                verifyOrder {
                    productListView.trackWishlistRecommendationProductNonLoginUser()
                    productListView.launchLoginActivity(productCardOptionsModel.productId)
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle wishlist action for non-recommendation product with non-login user") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter
            val productCardOptionsModel = ProductCardOptionsModel(
                    productId = "12345",
                    isTopAds = false,
                    isWishlisted = false,
                    isRecommendation = false
            ).also {
                it.wishlistResult = WishlistResult(isUserLoggedIn = false)
            }
            val keyword = "samsung"
            val slotWishlistTrackingModel = slot<WishlistTrackingModel>()

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            Given("Keyword from view") {
                every { productListView.queryKey } returns keyword
            }

            When("Handle wishlist action") {
                presenter.handleWishlistAction(productCardOptionsModel)
            }

            Then("Verify view interaction") {
                verifyOrder {
                    productListView.queryKey
                    productListView.trackWishlistProduct(capture(slotWishlistTrackingModel))
                    productListView.launchLoginActivity(productCardOptionsModel.productId)
                }

                confirmVerified(productListView)
            }

            Then("Verify wishlist tracking model is correct for non login user") {
                val wishlistTrackingModel = slotWishlistTrackingModel.captured

                wishlistTrackingModel.assert(
                        WishlistTrackingModel(
                                isAddWishlist = !productCardOptionsModel.isWishlisted,
                                productId = productCardOptionsModel.productId,
                                isTopAds = productCardOptionsModel.isTopAds,
                                isUserLoggedIn = productCardOptionsModel.wishlistResult.isUserLoggedIn,
                                keyword = keyword
                        )
                )
            }
        }

        Scenario("Handle success wishlist action for recommendation product") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter
            val productCardOptionsModel = ProductCardOptionsModel(
                    productId = "12345",
                    isTopAds = false,
                    isWishlisted = false,
                    isRecommendation = true
            ).also {
                it.wishlistResult = WishlistResult(isUserLoggedIn = true, isSuccess = true, isAddWishlist = true)
            }

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            When("Handle wishlist action") {
                presenter.handleWishlistAction(productCardOptionsModel)
            }

            Then("Verify view interaction") {
                verifyOrder {
                    productListView.trackWishlistRecommendationProductLoginUser(true)
                    productListView.updateWishlistStatus(productCardOptionsModel.productId, true)
                    productListView.showMessageSuccessWishlistAction(productCardOptionsModel.wishlistResult.isAddWishlist)
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle success wishlist action for non-recommendation product") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter
            val productCardOptionsModel = ProductCardOptionsModel(
                    productId = "12345",
                    isTopAds = false,
                    isWishlisted = false,
                    isRecommendation = false
            ).also {
                it.wishlistResult = WishlistResult(isUserLoggedIn = true, isSuccess = true, isAddWishlist = true)
            }
            val keyword = "samsung"
            val slotWishlistTrackingModel = slot<WishlistTrackingModel>()

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            Given("Keyword from view") {
                every { productListView.queryKey } returns keyword
            }

            When("Handle wishlist action") {
                presenter.handleWishlistAction(productCardOptionsModel)
            }

            Then("Verify view interaction") {
                verifyOrder {
                    productListView.queryKey
                    productListView.trackWishlistProduct(capture(slotWishlistTrackingModel))
                    productListView.updateWishlistStatus(productCardOptionsModel.productId, true)
                    productListView.showMessageSuccessWishlistAction(productCardOptionsModel.wishlistResult.isAddWishlist)
                }

                confirmVerified(productListView)
            }

            Then("Verify wishlist tracking model is correct for non login user") {
                val wishlistTrackingModel = slotWishlistTrackingModel.captured

                wishlistTrackingModel.assert(
                        WishlistTrackingModel(
                                isAddWishlist = productCardOptionsModel.wishlistResult.isAddWishlist,
                                productId = productCardOptionsModel.productId,
                                isTopAds = productCardOptionsModel.isTopAds,
                                isUserLoggedIn = productCardOptionsModel.wishlistResult.isUserLoggedIn,
                                keyword = keyword
                        )
                )
            }
        }

        Scenario("Handle failed wishlist action for recommendation product") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter
            val productCardOptionsModel = ProductCardOptionsModel(
                    productId = "12345",
                    isTopAds = false,
                    isWishlisted = false,
                    isRecommendation = true
            ).also {
                it.wishlistResult = WishlistResult(isUserLoggedIn = true, isSuccess = false, isAddWishlist = true)
            }

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            When("Handle wishlist action") {
                presenter.handleWishlistAction(productCardOptionsModel)
            }

            Then("Verify view interaction") {
                verifyOrder {
                    productListView.showMessageFailedWishlistAction(productCardOptionsModel.wishlistResult.isAddWishlist)
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle failed wishlist action for non-recommendation product") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter
            val productCardOptionsModel = ProductCardOptionsModel(
                    productId = "12345",
                    isTopAds = false,
                    isWishlisted = false,
                    isRecommendation = false
            ).also {
                it.wishlistResult = WishlistResult(isUserLoggedIn = true, isSuccess = false, isAddWishlist = true)
            }

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            When("Handle wishlist action") {
                presenter.handleWishlistAction(productCardOptionsModel)
            }

            Then("Verify view interaction") {
                verifyOrder {
                    productListView.showMessageFailedWishlistAction(productCardOptionsModel.wishlistResult.isAddWishlist)
                }

                confirmVerified(productListView)
            }
        }
    }
})
