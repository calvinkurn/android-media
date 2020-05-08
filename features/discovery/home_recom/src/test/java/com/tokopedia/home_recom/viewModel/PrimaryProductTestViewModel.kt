package com.tokopedia.home_recom.viewModel

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_recom.domain.usecases.GetPrimaryProductUseCase
import com.tokopedia.home_recom.model.entity.Data
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.home_recom.model.entity.ProductRecommendationProductDetail
import com.tokopedia.home_recom.rules.InstantTaskExecutorRuleSpek
import com.tokopedia.home_recom.util.Status
import com.tokopedia.home_recom.util.createInstance
import com.tokopedia.home_recom.util.createPrimaryProductViewModel
import com.tokopedia.home_recom.viewmodel.PrimaryProductViewModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 2019-07-08
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(PrimaryProductViewModel::class)
@ExperimentalCoroutinesApi
class PrimaryProductTestViewModel : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Test Primary"){
        lateinit var viewModel: PrimaryProductViewModel

        createInstance()
        val getPrimaryProductUseCase by memoized<GetPrimaryProductUseCase>()
        val productId = "316960043"

        Scenario("Success get product info"){
            val data = ProductRecommendationProductDetail(
                    data = listOf(Data(
                            recommendation = listOf(ProductDetailData())
                    ))
            )

            Given("data product info"){
                coEvery {
                    getPrimaryProductUseCase.executeOnBackground()
                } returns PrimaryProductEntity(data)
            }

            Given("view model"){
                viewModel = createPrimaryProductViewModel()
            }

            When("Get data primary"){
                viewModel.getPrimaryProduct(productId, "")
            }

            Then("Check result must be have data"){
                assert(viewModel.productInfoDataModel.value?.status == Status.SUCCESS)
                assert(viewModel.productInfoDataModel.value?.data != null)
            }
        }

        Scenario("Success get product info but empty"){
            val data = ProductRecommendationProductDetail(
                    data = listOf(Data(
                            recommendation = listOf()
                    ))
            )

            Given("data product info"){
                coEvery {
                    getPrimaryProductUseCase.executeOnBackground()
                } returns PrimaryProductEntity(data)
            }

            Given("view model"){
                viewModel = createPrimaryProductViewModel()
            }

            When("Get data primary"){
                viewModel.getPrimaryProduct(productId, "")
            }

            Then("Check result must be have data"){
                assert(viewModel.productInfoDataModel.value?.status == Status.EMPTY)
            }
        }

        Scenario("Error get product info"){

            Given("data product info"){
                coEvery {
                    getPrimaryProductUseCase.executeOnBackground()
                } throws TimeoutException()
            }

            Given("view model"){
                viewModel = createPrimaryProductViewModel()
            }

            When("Get data primary"){
                viewModel.getPrimaryProduct(productId, "")
            }

            Then("Check result must be have data"){
                assert(viewModel.productInfoDataModel.value?.status == Status.ERROR)
            }
        }
    }

    Feature("Test Atc"){
        lateinit var viewModel: PrimaryProductViewModel

        createInstance()
        val addToCartUseCase by memoized<AddToCartUseCase>()

        Scenario("Success atc"){
            Given("atc"){
                every {
                    addToCartUseCase.createObservable(any())
                } returns Observable.just(AddToCartDataModel(
                        status = AddToCartDataModel.STATUS_OK,
                        data = DataModel(
                                success = 1
                        )
                ))
            }

            Given("view model"){
                viewModel = createPrimaryProductViewModel()
            }

            When("Get data primary"){
                viewModel.addToCart(AddToCartRequestParams())
            }

            Then("Check result must be have data"){
                assert(viewModel.addToCartLiveData.value?.status == Status.SUCCESS)
                assert(viewModel.productInfoDataModel.value?.data != null)
            }
        }

        Scenario("Error atc"){
            Given("atc"){
                every {
                    addToCartUseCase.createObservable(any())
                } returns Observable.just(AddToCartDataModel(
                        status = AddToCartDataModel.STATUS_ERROR,
                        data = DataModel(
                                success = 0
                        )
                ))
            }
            Given("view model"){
                viewModel = createPrimaryProductViewModel()
            }

            When("Get data primary"){
                viewModel.addToCart(AddToCartRequestParams())
            }

            Then("Check result must be have data"){
                assert(viewModel.addToCartLiveData.value?.status == Status.ERROR)
                assert(viewModel.productInfoDataModel.value?.data == null)
            }
        }

        Scenario("Throw Error atc"){
            Given("atc"){
                every {
                    addToCartUseCase.createObservable(any())
                } returns Observable.error(TimeoutException())
            }
            Given("view model"){
                viewModel = createPrimaryProductViewModel()
            }

            When("Get data primary"){
                viewModel.addToCart(AddToCartRequestParams())
            }

            Then("Check result must be have data"){
                assert(viewModel.addToCartLiveData.value?.status == Status.ERROR)
                assert(viewModel.productInfoDataModel.value?.data == null)
            }
        }
    }

    Feature("Test Wishlist"){
        lateinit var viewModel: PrimaryProductViewModel

        createInstance()
        val addWishListUseCase by memoized<AddWishListUseCase>()
        val removeWishListUseCase by memoized<RemoveWishListUseCase>()

        val slot = slot<WishListActionListener>()
        Scenario("Success add wishlist"){

            Given("add wishlist"){
                every { addWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
                    slot.captured.onSuccessAddWishlist("123")
                }
            }

            Given("view model"){
                viewModel = createPrimaryProductViewModel()
            }

            When("Get data primary"){
                viewModel.addWishList("123")
            }

            Then("Check result must be have data"){
                assert(viewModel.addWishlistLiveData.value?.status == Status.SUCCESS)
                assert(viewModel.addWishlistLiveData.value?.data != null)
            }
        }

        Scenario("Error add wishlist"){

            Given("add wishlist"){
                every { addWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
                    slot.captured.onErrorAddWishList("123", "123")
                }
            }

            Given("view model"){
                viewModel = createPrimaryProductViewModel()
            }

            When("Get data primary"){
                viewModel.addWishList("123")
            }

            Then("Check result must be have data"){
                assert(viewModel.addWishlistLiveData.value?.status == Status.ERROR)
                assert(viewModel.addWishlistLiveData.value?.data == null)
            }
        }

        Scenario("Success remove wishlist"){

            Given("add wishlist"){
                every { removeWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
                    slot.captured.onSuccessRemoveWishlist("123")
                }
            }

            Given("view model"){
                viewModel = createPrimaryProductViewModel()
            }

            When("Get data primary"){
                viewModel.removeWishList("123")
            }

            Then("Check result must be have data"){
                assert(viewModel.addWishlistLiveData.value?.status == Status.SUCCESS)
                assert(viewModel.addWishlistLiveData.value?.data != null)
            }
        }

        Scenario("Error remove wishlist"){

            Given("add wishlist"){
                every { removeWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
                    slot.captured.onErrorRemoveWishlist("123", "123")
                }
            }

            Given("view model"){
                viewModel = createPrimaryProductViewModel()
            }

            When("Get data primary"){
                viewModel.removeWishList("123")
            }

            Then("Check result must be have data"){
                assert(viewModel.addWishlistLiveData.value?.status == Status.ERROR)
                assert(viewModel.addWishlistLiveData.value?.data == null)
            }
        }
    }

    Feature("Test is loggin"){
        lateinit var viewModel: PrimaryProductViewModel
        createInstance()
        val userSessionInterface by memoized<UserSessionInterface>()
        Scenario("Is login true"){
            Given("set data true"){
                every { userSessionInterface.isLoggedIn } returns true
            }

            Given("view model"){
                viewModel = createPrimaryProductViewModel()
            }

            When("Check true"){
                assert(viewModel.isLoggedIn())
            }
        }

        Scenario("Is login false"){
            Given("set data false"){
                every { userSessionInterface.isLoggedIn } returns false
            }

            Given("view model"){
                viewModel = createPrimaryProductViewModel()
            }

            When("Check true"){
                assert(!viewModel.isLoggedIn())
            }
        }
    }
})