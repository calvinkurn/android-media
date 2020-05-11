package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel
import com.tokopedia.productcard.options.testutils.InstantTaskExecutorRuleSpek
import com.tokopedia.productcard.options.testutils.shouldHaveSize
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class CreateProductCardOptionsViewModelTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Create Product Card Options View Model") {
        createTestInstance()

        Scenario("Null Options") {
            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            When("Create product Card Options View Model with null options") {
                productCardOptionsViewModel = createProductCardOptionsViewModel(null)
            }

            Then("Assert product card options item model list is empty") {
                val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

                productCardOptionsItemModelList shouldHaveSize 0
            }
        }

        Scenario("Options Has Similar Search") {
            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            When("Create Product Card Options View Model with hasSimilarSearch = true") {
                productCardOptionsViewModel = createProductCardOptionsViewModel(ProductCardOptionsModel(
                        hasSimilarSearch = true
                ))
            }

            Then("Assert product card options item model list contains option to see similar products") {
                val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

                productCardOptionsItemModelList.shouldContain {
                    it is ProductCardOptionsItemModel && it.title == SEE_SIMILAR_PRODUCTS
                }
            }
        }

        Scenario("Options Does not Have Similar Search") {
            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            When("Create Product Card Options View Model with hasSimilarSearch = false") {
                productCardOptionsViewModel = createProductCardOptionsViewModel(ProductCardOptionsModel(
                        hasSimilarSearch = false
                ))
            }

            Then("Assert product card options item model list does not contain option to see similar products") {
                val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

                productCardOptionsItemModelList.shouldNotContain {
                    it is ProductCardOptionsItemModel && it.title == SEE_SIMILAR_PRODUCTS
                }
            }
        }

        Scenario("Options Has Wishlist and Is not Wishlisted") {
            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            When("Product Card Options View Model with hasWishlist = true, and isWishlisted = false") {
                productCardOptionsViewModel = createProductCardOptionsViewModel(ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = false
                ))
            }

            Then("Assert product card options item model list contains option to add to wishlist") {
                val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

                productCardOptionsItemModelList.shouldContain {
                    it is ProductCardOptionsItemModel && it.title == SAVE_TO_WISHLIST
                }
            }
        }

        Scenario("Options Has Wishlist and Is Wishlisted") {
            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            When("Product Card Options View Model with hasWishlist = true, and isWishlisted = true") {
                productCardOptionsViewModel = createProductCardOptionsViewModel(ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = true
                ))
            }

            Then("Assert product card options item model list contains option to delete from wishlist") {
                val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

                productCardOptionsItemModelList.shouldContain {
                    it is ProductCardOptionsItemModel && it.title == DELETE_FROM_WISHLIST
                }
            }
        }

        Scenario("Options Does not Have Wishlist") {
            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            When("Product Card Options View Model with hasWishlist = false") {
                productCardOptionsViewModel = createProductCardOptionsViewModel(ProductCardOptionsModel(
                        hasWishlist = false
                ))
            }

            Then("Assert product card options item model list does not contain option to save or remove wishlist") {
                val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

                productCardOptionsItemModelList.shouldNotContain {
                    it is ProductCardOptionsItemModel
                            && (it.title == SAVE_TO_WISHLIST || it.title == DELETE_FROM_WISHLIST)
                }
            }
        }

        Scenario("Has Multiple Options") {
            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            When("Product Card Options View Model with hasSimilarSearch = true, and hasWishlist = true") {
                productCardOptionsViewModel = createProductCardOptionsViewModel(ProductCardOptionsModel(
                        hasSimilarSearch = true,
                        hasWishlist = true
                ))
            }

            Then("Assert product card options item model list has divider item model in between options") {
                val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

                productCardOptionsItemModelList!!.forEachIndexed { index, item ->
                    if (index % 2 == 0) item.shouldBeInstanceOf<ProductCardOptionsItemModel>()
                    else item.shouldBeInstanceOf<ProductCardOptionsItemDivider>()
                }
            }
        }
    }
})