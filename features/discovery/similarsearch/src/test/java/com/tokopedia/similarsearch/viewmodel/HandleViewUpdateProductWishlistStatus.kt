package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.similarsearch.*
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelCommon
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelOriginalProductWishlisted
import com.tokopedia.similarsearch.getsimilarproducts.GetSimilarProductsUseCase
import com.tokopedia.similarsearch.testutils.InstantTaskExecutorRuleSpek
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.testutils.stubExecute
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
                similarSearchViewModel.onViewUpdateProductWishlistStatus(null, true)
                similarSearchViewModel.onViewUpdateProductWishlistStatus(null, false)
                similarSearchViewModel.onViewUpdateProductWishlistStatus("", true)
                similarSearchViewModel.onViewUpdateProductWishlistStatus("", false)
                similarSearchViewModel.onViewUpdateProductWishlistStatus("randomproductid", true)
                similarSearchViewModel.onViewUpdateProductWishlistStatus("randomproductid", false)
            }

            Then("update wishlist similar product event is null") {
                val updateWishlistSimilarProductEventLiveData = similarSearchViewModel.getUpdateWishlistSimilarProductEventLiveData().value

                updateWishlistSimilarProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist similar product event should be null"
                )
            }

            Then("update wishlist selected product event is null") {
                val updateWishlistSelectedProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

                updateWishlistSelectedProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist selected product event should be null"
                )
            }
        }

        Scenario("Wishlist updated for original product to true, and original product isWishlisted already true") {
            val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
            val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()
            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("view already created and has similar search data with original product wishlisted") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelOriginalProductWishlisted)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view update product wishlist status") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus(originalProduct.id, true)
            }

            Then("assert original product isWishlisted is true, and update wishlist original product event is null") {
                val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

                similarSearchOriginalProduct?.isWishlisted.shouldBe(
                        true,
                        "Original Product is wishlisted should be true"
                )

                val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

                updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist Original product event should be null"
                )
            }
        }

        Scenario("Wishlist updated for Original product to false, and Original product isWishlisted already false") {
            val similarProductModelCommon = getSimilarProductModelCommon()
            val originalProduct = similarProductModelCommon.getOriginalProduct()
            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("view already created and has similar search data with original product wishlisted") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view update product wishlist status") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus(originalProduct.id, false)
            }

            Then("assert Original product isWishlisted is false, and update wishlist Original product event is null") {
                val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

                similarSearchOriginalProduct?.isWishlisted.shouldBe(
                        false,
                        "Original Product is wishlisted should be false"
                )

                val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

                updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist Original product event should be null"
                )
            }
        }

        Scenario("Wishlist updated for similar product to true, and similar product isWishlisted already true") {
            val similarProductModel = getSimilarProductModelCommon()
            val wishlistedProduct = similarProductModel.getSimilarProductList()[1]
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
            val notWishlistedProduct = similarProductModel.getSimilarProductList()[0]

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

        Scenario("Wishlist updated for Original product to true") {
            val similarProductModelCommon = getSimilarProductModelCommon()
            val originalProduct = similarProductModelCommon.getOriginalProduct()
            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("view already created and has similar search data with original product wishlisted") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view update product wishlist status") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus(originalProduct.id, true)
            }

            Then("assert Original product isWishlisted is true, and update wishlist Original product event is true") {
                val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

                similarSearchOriginalProduct?.isWishlisted.shouldBe(
                        true,
                        "Original Product is wishlisted should be true"
                )

                val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

                updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Update wishlist Original product event should be true"
                )
            }
        }

        Scenario("Wishlist updated for Original product to false") {
            val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
            val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()
            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("view already created and has similar search data with original product wishlisted") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelOriginalProductWishlisted)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view update product wishlist status") {
                similarSearchViewModel.onViewUpdateProductWishlistStatus(originalProduct.id, false)
            }

            Then("assert Original product isWishlisted is true, and update wishlist Original product event is true") {
                val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

                similarSearchOriginalProduct?.isWishlisted.shouldBe(
                        false,
                        "Original Product is wishlisted should be false"
                )

                val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

                updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        false,
                        "Update wishlist Original product event should be false"
                )
            }
        }

        Scenario("Wishlist updated for similar product to true") {
            val similarProductModel = getSimilarProductModelCommon()
            val wishlistedProduct = similarProductModel.getSimilarProductList()[0]

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
            val wishlistedProduct = similarProductModel.getSimilarProductList()[1]

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