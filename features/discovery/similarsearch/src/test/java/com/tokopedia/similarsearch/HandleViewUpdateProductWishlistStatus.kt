package com.tokopedia.similarsearch

import com.tokopedia.similarsearch.testinstance.getSimilarProductModelCommon
import com.tokopedia.similarsearch.testinstance.getSimilarSearchSelectedProductNotWishlisted
import com.tokopedia.similarsearch.testinstance.getSimilarSearchSelectedProductWishlisted
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewUpdateProductWishlistStatus: Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Update Product Wishlist Status") {
        createTestInstance()

        Scenario("Degenerate cases") {
            val similarProductModel = getSimilarProductModelCommon()
            val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("view already created and has similar search data") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view update product wishlist status with empty product id") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus("", false)
            }

            Then("update wishlist similar product event is null") {
                val updateWishlistSimilarProductEventLiveData = similarSearchViewModel.getUpdateWishlistSimilarProductEventLiveData().value

                updateWishlistSimilarProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist similar product event should be null"
                )
            }

            Then("update wishlist selected product event is null") {
                val updateWishlistSelectedProductEventLiveData = similarSearchViewModel.getUpdateWishlistSelectedProductEventLiveData().value

                updateWishlistSelectedProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist selected product event should be null"
                )
            }
        }

        Scenario("Wishlist updated for selected product to true, and selected product isWishlisted already true") {
            val similarSearchSelectedProductWishlisted = getSimilarSearchSelectedProductWishlisted()
            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel(similarSearchSelectedProductWishlisted)
            }

            When("handle view update product wishlist status") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus(similarSearchSelectedProductWishlisted.id, true)
            }

            Then("assert selected product isWishlisted is true, and update wishlist selected product event is null") {
                val similarSearchSelectedProduct = similarSearchViewModel.similarSearchSelectedProduct

                similarSearchSelectedProduct.isWishlisted.shouldBe(
                        true,
                        "Selected Product is wishlisted should be true"
                )

                val updateWishlistSelectedProductEventLiveData = similarSearchViewModel.getUpdateWishlistSelectedProductEventLiveData().value

                updateWishlistSelectedProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist selected product event should be null"
                )
            }
        }

        Scenario("Wishlist updated for selected product to false, and selected product isWishlisted already false") {
            val similarSearchSelectedProductNotWishlisted = getSimilarSearchSelectedProductNotWishlisted()
            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel(similarSearchSelectedProductNotWishlisted)
            }

            When("handle view update product wishlist status") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus(similarSearchSelectedProductNotWishlisted.id, false)
            }

            Then("assert selected product isWishlisted is false, and update wishlist selected product event is null") {
                val similarSearchSelectedProduct = similarSearchViewModel.similarSearchSelectedProduct

                similarSearchSelectedProduct.isWishlisted.shouldBe(
                        false,
                        "Selected Product is wishlisted should be false"
                )

                val updateWishlistSelectedProductEventLiveData = similarSearchViewModel.getUpdateWishlistSelectedProductEventLiveData().value

                updateWishlistSelectedProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist selected product event should be null"
                )
            }
        }

        Scenario("Wishlist updated for similar product to true, and similar product isWishlisted already true") {
            val similarProductModel = getSimilarProductModelCommon()
            val wishlistedProduct = similarProductModel.getProductList()[1]
            val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("view already created and has similar search data") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view update product wishlist status") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus(wishlistedProduct.id, true)
            }

            Then("assert chosen similar product isWishlisted is true") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val similarSearchViewModelList = similarSearchLiveData?.data ?: listOf()

                similarSearchViewModelList.shouldHaveSimilarProductWithExpectedWishlistStatus(wishlistedProduct.id, true)
            }

            Then("update wishlist similar product event is null") {
                val updateWishlistSimilarProductEventLiveData = similarSearchViewModel.getUpdateWishlistSimilarProductEventLiveData().value

                updateWishlistSimilarProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist similar product event should be null"
                )
            }
        }

        Scenario("Wishlist updated for similar product to false, and similar product isWishlisted already false") {
            val similarProductModel = getSimilarProductModelCommon()
            val notWishlistedProduct = similarProductModel.getProductList()[0]

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("view already created and has similar search data") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view update product wishlist status") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus(notWishlistedProduct.id, false)
            }

            Then("assert chosen similar product isWishlisted is false") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val similarSearchViewModelList = similarSearchLiveData?.data ?: listOf()

                similarSearchViewModelList.shouldHaveSimilarProductWithExpectedWishlistStatus(notWishlistedProduct.id, false)
            }

            Then("update wishlist similar product event is null") {
                val updateWishlistSimilarProductEventLiveData = similarSearchViewModel.getUpdateWishlistSimilarProductEventLiveData().value

                updateWishlistSimilarProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist similar product event should be null"
                )
            }
        }

        Scenario("Wishlist updated for selected product to true") {
            val similarSearchSelectedProductNotWishlisted = getSimilarSearchSelectedProductNotWishlisted()
            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel(similarSearchSelectedProductNotWishlisted)
            }

            When("handle view update product wishlist status") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus(similarSearchSelectedProductNotWishlisted.id, true)
            }

            Then("assert selected product isWishlisted is true, and update wishlist selected product event is true") {
                val similarSearchSelectedProduct = similarSearchViewModel.similarSearchSelectedProduct

                similarSearchSelectedProduct.isWishlisted.shouldBe(
                        true,
                        "Selected Product is wishlisted should be true"
                )

                val updateWishlistSelectedProductEventLiveData = similarSearchViewModel.getUpdateWishlistSelectedProductEventLiveData().value

                updateWishlistSelectedProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Update wishlist selected product event should be true"
                )
            }
        }

        Scenario("Wishlist updated for selected product to false") {
            val similarSearchSelectedProductWishlisted = getSimilarSearchSelectedProductWishlisted()
            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel(similarSearchSelectedProductWishlisted)
            }

            When("handle view update product wishlist status") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus(similarSearchSelectedProductWishlisted.id, false)
            }

            Then("assert selected product isWishlisted is true, and update wishlist selected product event is true") {
                val similarSearchSelectedProduct = similarSearchViewModel.similarSearchSelectedProduct

                similarSearchSelectedProduct.isWishlisted.shouldBe(
                        false,
                        "Selected Product is wishlisted should be false"
                )

                val updateWishlistSelectedProductEventLiveData = similarSearchViewModel.getUpdateWishlistSelectedProductEventLiveData().value

                updateWishlistSelectedProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        false,
                        "Update wishlist selected product event should be false"
                )
            }
        }

        Scenario("Wishlist updated for similar product to true") {
            val similarProductModel = getSimilarProductModelCommon()
            val wishlistedProduct = similarProductModel.getProductList()[0]

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("view already created and has similar search data") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view update product wishlist status") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus(wishlistedProduct.id, true)
            }

            Then("assert similar search model list has product with updated wishlist status to true") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val similarSearchViewModelList = similarSearchLiveData?.data ?: listOf()

                similarSearchViewModelList.shouldHaveSimilarProductWithExpectedWishlistStatus(wishlistedProduct.id, true)
            }

            Then("update wishlist similar product event is true") {
                val updateWishlistSimilarProductEventLiveData = similarSearchViewModel.getUpdateWishlistSimilarProductEventLiveData().value

                updateWishlistSimilarProductEventLiveData?.getContentIfNotHandled()?.isWishlisted.shouldBe(
                        true,
                        "Update wishlist similar product event should be true"
                )
            }
        }

        Scenario("Wishlist updated for similar product to false") {
            val similarProductModel = getSimilarProductModelCommon()
            val wishlistedProduct = similarProductModel.getProductList()[1]

            lateinit var similarSearchViewModel: SimilarSearchViewModel
            lateinit var similarSearchPreviousViewModelList: List<Any>

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("view already created and has similar search data") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
                similarSearchPreviousViewModelList = similarSearchViewModel.getSimilarSearchLiveData().value?.data ?: listOf()
            }

            When("handle view update product wishlist status") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus(wishlistedProduct.id, false)
            }

            Then("assert similar search model list has product with updated wishlist status to false") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val similarSearchViewModelList = similarSearchLiveData?.data ?: listOf()

                similarSearchViewModelList.shouldHaveSimilarProductWithExpectedWishlistStatus(wishlistedProduct.id, false)
            }

            Then("update wishlist similar product event is false") {
                val updateWishlistSimilarProductEventLiveData = similarSearchViewModel.getUpdateWishlistSimilarProductEventLiveData().value

                updateWishlistSimilarProductEventLiveData?.getContentIfNotHandled()?.isWishlisted.shouldBe(
                        false,
                        "Update wishlist similar product event should be false"
                )
            }
        }
    }
})