package com.tokopedia.shop.product.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cartcommon.data.response.deletecart.Data
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.mvcwidget.ResultStatus
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.shop.common.data.model.ShopPageAtcTracker
import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.graphql.data.shopsort.ShopProductSort
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.domain.interactor.GetMembershipUseCaseNew
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import rx.Observable

@ExperimentalCoroutinesApi
class ShopPageProductListViewModelTest : ShopPageProductListViewModelTestFixture() {

    @Test
    fun `check userSession userId return same value with mock value`() {
        every { userSessionInterface.userId } returns "123"
        Assert.assertEquals("123", viewModelShopPageProductListViewModel.userId)
    }

    @Test
    fun `check userSession isLoggedIn return same value with mock value`() {
        every { userSessionInterface.isLoggedIn } returns true
        Assert.assertTrue(viewModelShopPageProductListViewModel.isLogin)
    }

    @Test
    fun `check userSession deviceId return same value with mock value`() {
        every { userSessionInterface.deviceId } returns "abc123"
        Assert.assertEquals("abc123", viewModelShopPageProductListViewModel.userDeviceId)
    }

    @Test
    fun `check whether response claim new membership success is not null`() {
        runBlocking {
            coEvery { claimBenefitMembershipUseCase.executeOnBackground() } returns MembershipClaimBenefitResponse()
            viewModelShopPageProductListViewModel.claimMembershipBenefit(anyInt())
            verifyGetClaimBenefitMembershipUseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.claimMembershipResp.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.claimMembershipResp.value)
        }
    }

    @Test
    fun `check whether response claim new membership error is null`() {
        runBlocking {
            coEvery { claimBenefitMembershipUseCase.executeOnBackground() } throws Exception()
            viewModelShopPageProductListViewModel.claimMembershipBenefit(anyInt())
            verifyGetClaimBenefitMembershipUseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.claimMembershipResp.value is Fail)
        }
    }

    @Test
    fun `check whether response get product list data success is not null with direct purchase true`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct(), ShopProduct())
            )
            viewModelShopPageProductListViewModel.getProductListData(
                anyString(),
                anyInt(),
                anyInt(),
                anyString(),
                ShopProductFilterParameter(),
                addressWidgetData,
                mockIsDirectPurchaseTrue
            )

            verify { GqlGetShopProductUseCase.createParams(anyString(), any()) }

            verifyGetShopProductUseCaseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.productListData.value)
            assert(
                (viewModelShopPageProductListViewModel.productListData.value as? Success)?.data?.listShopProductUiModel?.all {
                    it.isEnableDirectPurchase
                } == mockIsDirectPurchaseTrue
            )
        }
    }

    @Test
    fun `check whether response get product list data success is not null with direct purchase false`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct(), ShopProduct())
            )
            viewModelShopPageProductListViewModel.getProductListData(
                anyString(),
                anyInt(),
                anyInt(),
                anyString(),
                ShopProductFilterParameter(),
                addressWidgetData,
                mockIsDirectPurchaseFalse
            )

            verify { GqlGetShopProductUseCase.createParams(anyString(), any()) }

            verifyGetShopProductUseCaseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.productListData.value)
            assert(
                (viewModelShopPageProductListViewModel.productListData.value as? Success)?.data?.listShopProductUiModel?.all {
                    it.isEnableDirectPurchase
                } == mockIsDirectPurchaseFalse
            )
        }
    }

    @Test
    fun `check whether response get product list data success is not null and has next page`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct(
                totalData = 20
            )
            viewModelShopPageProductListViewModel.getProductListData(
                anyString(),
                anyInt(),
                anyInt(),
                anyString(),
                ShopProductFilterParameter(),
                addressWidgetData,
                mockIsDirectPurchaseTrue
            )

            verify { GqlGetShopProductUseCase.createParams(anyString(), any()) }

            verifyGetShopProductUseCaseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.productListData.value)
        }
    }

    @Test
    fun `check whether response  get product list data error is null`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()
            viewModelShopPageProductListViewModel.getProductListData(anyString(), anyInt(), anyInt(), anyString(), ShopProductFilterParameter(), addressWidgetData, mockIsDirectPurchaseTrue)

            verify { GqlGetShopProductUseCase.createParams(anyString(), any()) }

            verifyGetShopProductUseCaseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Fail)
        }
    }

    @Test
    fun `check whether response get new membership data success is not null`() {
        runBlocking {
            coEvery { getMembershipUseCase.executeOnBackground() } returns MembershipStampProgress()

            viewModelShopPageProductListViewModel.getNewMembershipData(anyString())

            verify { GetMembershipUseCaseNew.createRequestParams(anyInt()) }

            verifyGetMemberShipUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.newMembershipData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.newMembershipData.value)
        }
    }

    @Test
    fun `check whether response get new membership data error`() {
        runBlocking {
            coEvery { getMembershipUseCase.executeOnBackground() } throws Exception()
            viewModelShopPageProductListViewModel.getNewMembershipData(anyString())

            verify { GetMembershipUseCaseNew.createRequestParams(anyInt()) }

            verifyGetMemberShipUseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.newMembershipData.value is Fail)
        }
    }

    @Test
    fun `check getNewMerchantVoucher is Success`() {
        runBlocking {
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(ResultStatus(code = "200", listOf("success"), null, null), true, null, null))

            viewModelShopPageProductListViewModel.getNewMerchantVoucher("123", context)

            verifyGetMerchantVoucerUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.newMerchantVoucherData.value is Success<ShopMerchantVoucherUiModel>)
        }
    }

    @Test
    fun `check getNewMerchantVoucher is Fail is exception thrown`() {
        runBlocking {
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(ResultStatus(code = "200", listOf("success"), null, null), true, null, null))
            val observer = mockk<Observer<Result<ShopMerchantVoucherUiModel>>>(relaxed = true)
            viewModelShopPageProductListViewModel.newMerchantVoucherData.observeForever(observer)
            every { observer.onChanged(any<Success<ShopMerchantVoucherUiModel>>()) } throws Exception()
            viewModelShopPageProductListViewModel.getNewMerchantVoucher("123", context)

            verifyGetMerchantVoucerUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.newMerchantVoucherData.value is Fail)
        }
    }

    @Test
    fun `check getNewMerchantVoucher is null`() {
        runBlocking {
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(ResultStatus(code = "123", listOf("not success"), null, null), false, null, null))

            viewModelShopPageProductListViewModel.getNewMerchantVoucher("123", context)

            verifyGetMerchantVoucerUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.newMerchantVoucherData.value == null)

            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(null)

            viewModelShopPageProductListViewModel.getNewMerchantVoucher("123", context)

            verifyGetMerchantVoucerUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.newMerchantVoucherData.value == null)

            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } throws Exception()

            viewModelShopPageProductListViewModel.getNewMerchantVoucher("123", context)

            verifyGetMerchantVoucerUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.newMerchantVoucherData.value == null)
        }
    }

    @Test
    fun `check isMyShop is return valid logged in shop id`() {
        every { userSessionInterface.shopId } returns "123"
        val expectedResult = viewModelShopPageProductListViewModel.isMyShop("123")
        Assert.assertTrue(expectedResult)
        Assert.assertFalse(!expectedResult)
    }

    @Test
    fun `check clearCache for UseCase is called`() {
        runBlocking {
            // membership use case
            coEvery {
                getMembershipUseCase.executeOnBackground()
            } returns MembershipStampProgress()

            // merchant voucher use case
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(null, true, null, null))

            // shop featured product use case
            coEvery {
                getShopFeaturedProductUseCase.executeOnBackground()
            } returns listOf(ShopFeaturedProduct())

            // gql get shop product use case
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct())
            )

            every {
                getShopHighlightProductUseCase.get()
            } returns getShopProductUseCase

            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                shopId = "123",
                etalaseList = listOf(
                    ShopEtalaseItemDataModel(
                        etalaseId = "sold",
                        alias = "",
                        etalaseName = "test",
                        type = 2,
                        etalaseBadge = "",
                        etalaseCount = 10,
                        highlighted = true,
                        etalaseRules = listOf()
                    )
                ),
                isShopWidgetAlreadyShown = false,
                widgetUserAddressLocalData = addressWidgetData,
                context,
                mockIsDirectPurchaseTrue
            )

            verifyGetMemberShipUseCaseCalled()
            verifyGetMerchantVoucerUseCaseCalled()
            verifyGetShopFeaturedProductUseCaseCalled()
            verifyGetShopProductUseCaseCaseCalled()
        }

        viewModelShopPageProductListViewModel.clearCache()
        verify {
            getShopEtalaseByShopUseCase.clearCache()
            getShopFeaturedProductUseCase.clearCache()
            getShopProductUseCase.clearCache()
        }
    }

    @Test
    fun `check getShopFilterData is Success if isMyShop true`() {
        runBlocking {
            every { userSessionInterface.shopId } returns "123"
            viewModelShopPageProductListViewModel.isMyShop("123")

            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns listOf(ShopProductSort())

            viewModelShopPageProductListViewModel.getShopFilterData("123")

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            Assert.assertNotNull(viewModelShopPageProductListViewModel.shopSortFilterData.value)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopSortFilterData.value is Success<ShopStickySortFilter>)
        }
    }

    @Test
    fun `check getShopFilterData is Success if isMyShop false`() {
        runBlocking {
            every {
                userSessionInterface.userId
            } returns "321"
            viewModelShopPageProductListViewModel.isMyShop("321")

            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns listOf(ShopProductSort())

            viewModelShopPageProductListViewModel.getShopFilterData("123")

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            Assert.assertNotNull(viewModelShopPageProductListViewModel.shopSortFilterData.value)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopSortFilterData.value is Success<ShopStickySortFilter>)
        }
    }

    @Test
    fun `check getShopFilterData is Fail when mapper throw exception`() {
        runBlocking {
            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns listOf(ShopProductSort())
            val observer = mockk<Observer<Result<ShopStickySortFilter>>>(relaxed = true)
            viewModelShopPageProductListViewModel.shopSortFilterData.observeForever(observer)
//            val slot = slot<Result<ShopStickySortFilter>>()
            every { observer.onChanged(any<Success<ShopStickySortFilter>>()) } throws Exception()
            viewModelShopPageProductListViewModel.getShopFilterData("123")

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            Assert.assertNotNull(viewModelShopPageProductListViewModel.shopSortFilterData.value)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopSortFilterData.value is Fail)
        }
    }

    @Test
    fun `check getShopFilterData shopSortFilterData value is fail if both shopEtalaseData response is error`() {
        runBlocking {
            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } throws Exception()

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns listOf()
            viewModelShopPageProductListViewModel.getShopFilterData("123")

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            Assert.assertNotNull(viewModelShopPageProductListViewModel.shopSortFilterData.value)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopSortFilterData.value is Fail)
        }
    }

    @Test
    fun `check getShopFilterData shopSortFilterData value is fail if both shopSortData response is error`() {
        runBlocking {
            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } throws Exception()
            viewModelShopPageProductListViewModel.getShopFilterData("123")

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            Assert.assertNotNull(viewModelShopPageProductListViewModel.shopSortFilterData.value)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopSortFilterData.value is Fail)
        }
    }

    @Test
    fun `check setInitialProductList is Success`() {
        viewModelShopPageProductListViewModel.setInitialProductList(
            "123",
            productPerPage = mockProductPerPage,
            ShopProduct.GetShopProduct(
                data = listOf(ShopProduct())
            ),
            mockIsDirectPurchaseTrue
        )
        Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Success<GetShopProductUiModel>)
    }

    @Test
    fun `check getBottomSheetFilterData is Success`() {
        runBlocking {
            coEvery {
                getShopFilterBottomSheetDataUseCase.executeOnBackground()
            } returns DynamicFilterModel(
                data = DataValue(
                    listOf(Filter(title = "pengiriman"), Filter(title = "Rating"))
                )
            )
            viewModelShopPageProductListViewModel.getBottomSheetFilterData()

            verifyGetShopFilterBottomSheetDataUseCaseCalled()

            Assert.assertNotNull(viewModelShopPageProductListViewModel.bottomSheetFilterLiveData.value)
            Assert.assertTrue(viewModelShopPageProductListViewModel.bottomSheetFilterLiveData.value is Success<DynamicFilterModel>)
        }
    }

    @Test
    fun `check getBottomSheetFilterData bottomSheetFilterLiveData value is null if response error`() {
        runBlocking {
            coEvery {
                getShopFilterBottomSheetDataUseCase.executeOnBackground()
            } throws Exception()

            viewModelShopPageProductListViewModel.getBottomSheetFilterData()

            verifyGetShopFilterBottomSheetDataUseCaseCalled()

            Assert.assertNull(viewModelShopPageProductListViewModel.bottomSheetFilterLiveData.value)
        }
    }

    @Test
    fun `check getFilterResultCount is Success`() {
        runBlocking {
            coEvery {
                getShopFilterProductCountUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct().totalData

            viewModelShopPageProductListViewModel.getFilterResultCount(
                shopId = "123",
                productPerPage = mockProductPerPage,
                tempShopProductFilterParameter = ShopProductFilterParameter(),
                widgetUserAddressLocalData = addressWidgetData
            )

            verifyGetShopFilterProductCountUseCaseCalled()

            Assert.assertNotNull(viewModelShopPageProductListViewModel.shopProductFilterCountLiveData.value)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductFilterCountLiveData.value is Success<Int>)
        }
    }

    @Test
    fun `check getFilterResultCount is Fail`() {
        runBlocking {
            coEvery {
                getShopFilterProductCountUseCase.executeOnBackground()
            } throws Exception()

            viewModelShopPageProductListViewModel.getFilterResultCount(
                shopId = "123",
                productPerPage = mockProductPerPage,
                tempShopProductFilterParameter = ShopProductFilterParameter(),
                widgetUserAddressLocalData = addressWidgetData
            )

            verifyGetShopFilterProductCountUseCaseCalled()

            Assert.assertNull(viewModelShopPageProductListViewModel.shopProductFilterCountLiveData.value)
        }
    }

    @Test
    fun `check getBuyerViewContentData is Success with sort by SOLD_ETALASE`() {
        runBlocking {
            // membership use case
            coEvery {
                getMembershipUseCase.executeOnBackground()
            } returns MembershipStampProgress()

            // merchant voucher use case
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(null, true, null, null))

            // shop featured product use case
            coEvery {
                getShopFeaturedProductUseCase.executeOnBackground()
            } returns listOf(ShopFeaturedProduct())

            // gql get shop product use case
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct())
            )

            every {
                getShopHighlightProductUseCase.get()
            } returns getShopProductUseCase

            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                shopId = "123",
                etalaseList = listOf(
                    ShopEtalaseItemDataModel(
                        etalaseId = "sold",
                        alias = "",
                        etalaseName = "test",
                        type = 2,
                        etalaseBadge = "",
                        etalaseCount = 10,
                        highlighted = true,
                        etalaseRules = listOf()
                    )
                ),
                isShopWidgetAlreadyShown = false,
                widgetUserAddressLocalData = addressWidgetData,
                context,
                mockIsDirectPurchaseTrue
            )

            verifyGetMemberShipUseCaseCalled()
            verifyGetMerchantVoucerUseCaseCalled()
            verifyGetShopFeaturedProductUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.membershipData.value is Success<MembershipStampProgressUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.merchantVoucherData.value is Success<ShopMerchantVoucherUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductFeaturedData.value is Success<ShopProductFeaturedUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductEtalaseHighlightData.value is Success<ShopProductEtalaseHighlightUiModel>)
        }
    }

    @Test
    fun `check getBuyerViewContentData is Success with sort by DISCOUNT_ETALASE`() {
        runBlocking {
            // membership use case
            coEvery {
                getMembershipUseCase.executeOnBackground()
            } returns MembershipStampProgress()

            // merchant voucher use case
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(null, true, null, null))

            // shop featured product use case
            coEvery {
                getShopFeaturedProductUseCase.executeOnBackground()
            } returns listOf(ShopFeaturedProduct())

            // gql get shop product use case
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct())
            )

            every {
                getShopHighlightProductUseCase.get()
            } returns getShopProductUseCase

            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                shopId = "123",
                etalaseList = listOf(
                    ShopEtalaseItemDataModel(
                        etalaseId = "discount",
                        alias = "",
                        etalaseName = "test",
                        type = 2,
                        etalaseBadge = "",
                        etalaseCount = 10,
                        highlighted = true,
                        etalaseRules = listOf()
                    )
                ),
                isShopWidgetAlreadyShown = false,
                widgetUserAddressLocalData = addressWidgetData,
                context,
                mockIsDirectPurchaseTrue
            )

            verifyGetMemberShipUseCaseCalled()
            verifyGetMerchantVoucerUseCaseCalled()
            verifyGetShopFeaturedProductUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.membershipData.value is Success<MembershipStampProgressUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.merchantVoucherData.value is Success<ShopMerchantVoucherUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductFeaturedData.value is Success<ShopProductFeaturedUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductEtalaseHighlightData.value is Success<ShopProductEtalaseHighlightUiModel>)
        }
    }

    @Test
    fun `check getBuyerViewContentData is Success with no sort`() {
        runBlocking {
            // membership use case
            coEvery {
                getMembershipUseCase.executeOnBackground()
            } returns MembershipStampProgress()

            // merchant voucher use case
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(null, true, null, null))

            // shop featured product use case
            coEvery {
                getShopFeaturedProductUseCase.executeOnBackground()
            } returns listOf(ShopFeaturedProduct())

            // gql get shop product use case
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct())
            )

            every {
                getShopHighlightProductUseCase.get()
            } returns getShopProductUseCase

            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                shopId = "123",
                etalaseList = listOf(
                    ShopEtalaseItemDataModel(
                        etalaseId = "123",
                        alias = "",
                        etalaseName = "test",
                        type = 2,
                        etalaseBadge = "",
                        etalaseCount = 10,
                        highlighted = true,
                        etalaseRules = listOf()
                    )
                ),
                isShopWidgetAlreadyShown = false,
                widgetUserAddressLocalData = addressWidgetData,
                context,
                mockIsDirectPurchaseTrue
            )

            verifyGetMemberShipUseCaseCalled()
            verifyGetMerchantVoucerUseCaseCalled()
            verifyGetShopFeaturedProductUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.membershipData.value is Success<MembershipStampProgressUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.merchantVoucherData.value is Success<ShopMerchantVoucherUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductFeaturedData.value is Success<ShopProductFeaturedUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductEtalaseHighlightData.value is Success<ShopProductEtalaseHighlightUiModel>)
        }
    }

    @Test
    fun `check getBuyerViewContentData is Success with no highlighted etalase`() {
        runBlocking {
            // membership use case
            coEvery {
                getMembershipUseCase.executeOnBackground()
            } returns MembershipStampProgress()

            // merchant voucher use case
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(null, true, null, null))

            // shop featured product use case
            coEvery {
                getShopFeaturedProductUseCase.executeOnBackground()
            } returns listOf(ShopFeaturedProduct())

            // gql get shop product use case
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct()

            every {
                getShopHighlightProductUseCase.get()
            } returns getShopProductUseCase

            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                shopId = "123",
                etalaseList = listOf(
                    ShopEtalaseItemDataModel(
                        etalaseId = "123",
                        alias = "",
                        etalaseName = "test",
                        type = 2,
                        etalaseBadge = "",
                        etalaseCount = 10,
                        highlighted = false,
                        etalaseRules = listOf()
                    )
                ),
                isShopWidgetAlreadyShown = false,
                widgetUserAddressLocalData = addressWidgetData,
                context,
                mockIsDirectPurchaseTrue
            )

            verifyGetMemberShipUseCaseCalled()
            verifyGetMerchantVoucerUseCaseCalled()
            verifyGetShopFeaturedProductUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.membershipData.value is Success<MembershipStampProgressUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.merchantVoucherData.value is Success<ShopMerchantVoucherUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductFeaturedData.value is Success<ShopProductFeaturedUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductEtalaseHighlightData.value is Success<ShopProductEtalaseHighlightUiModel>)
        }
    }

    @Test
    fun `check getBuyerViewContentData is Success with no product on highlighted etalase`() {
        runBlocking {
            // membership use case
            coEvery {
                getMembershipUseCase.executeOnBackground()
            } returns MembershipStampProgress()

            // merchant voucher use case
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(null, true, null, null))

            // shop featured product use case
            coEvery {
                getShopFeaturedProductUseCase.executeOnBackground()
            } returns listOf(ShopFeaturedProduct())

            // gql get shop product use case
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                listOf()
            )

            every {
                getShopHighlightProductUseCase.get()
            } returns getShopProductUseCase

            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                shopId = "123",
                etalaseList = listOf(
                    ShopEtalaseItemDataModel(
                        etalaseId = "123",
                        alias = "",
                        etalaseName = "test",
                        type = 2,
                        etalaseBadge = "",
                        etalaseCount = 10,
                        highlighted = true,
                        etalaseRules = listOf()
                    )
                ),
                isShopWidgetAlreadyShown = false,
                widgetUserAddressLocalData = addressWidgetData,
                context,
                mockIsDirectPurchaseFalse
            )

            verifyGetMemberShipUseCaseCalled()
            verifyGetMerchantVoucerUseCaseCalled()
            verifyGetShopFeaturedProductUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.membershipData.value is Success<MembershipStampProgressUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.merchantVoucherData.value is Success<ShopMerchantVoucherUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductFeaturedData.value is Success<ShopProductFeaturedUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductEtalaseHighlightData.value is Success<ShopProductEtalaseHighlightUiModel>)
        }
    }

    @Test
    fun `check getBuyerViewContentData with getHighlightEtalaseData throw an exception`() {
        runBlocking {
            // membership use case
            coEvery {
                getMembershipUseCase.executeOnBackground()
            } returns MembershipStampProgress()

            // merchant voucher use case
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(null, true, null, null))

            // shop featured product use case
            coEvery {
                getShopFeaturedProductUseCase.executeOnBackground()
            } returns listOf(ShopFeaturedProduct())

            // gql get shop product use case
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct())
            )

            every {
                getShopHighlightProductUseCase.get()
            } throws Exception()

            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                shopId = "123",
                etalaseList = listOf(
                    ShopEtalaseItemDataModel(
                        etalaseId = "123",
                        alias = "",
                        etalaseName = "test",
                        type = 2,
                        etalaseBadge = "",
                        etalaseCount = 10,
                        highlighted = true,
                        etalaseRules = listOf()
                    )
                ),
                isShopWidgetAlreadyShown = false,
                widgetUserAddressLocalData = addressWidgetData,
                context,
                mockIsDirectPurchaseTrue
            )

            verifyGetMemberShipUseCaseCalled()
            verifyGetMerchantVoucerUseCaseCalled()
            verifyGetShopFeaturedProductUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.membershipData.value is Success<MembershipStampProgressUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.merchantVoucherData.value is Success<ShopMerchantVoucherUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductFeaturedData.value is Success<ShopProductFeaturedUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductEtalaseHighlightData.value == null)
        }
    }

    @Test
    fun `check getBuyerViewContentData with featured product throw an exception`() {
        runBlocking {
            // membership use case
            coEvery {
                getMembershipUseCase.executeOnBackground()
            } returns MembershipStampProgress()

            // merchant voucher use case
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(null, true, null, null))

            // shop featured product use case
            coEvery {
                getShopFeaturedProductUseCase.executeOnBackground()
            } throws Exception()

            // gql get shop product use case
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct())
            )

            every {
                getShopHighlightProductUseCase.get()
            } returns getShopProductUseCase

            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                shopId = "123",
                etalaseList = listOf(
                    ShopEtalaseItemDataModel(
                        etalaseId = "sold",
                        alias = "",
                        etalaseName = "test",
                        type = 2,
                        etalaseBadge = "",
                        etalaseCount = 10,
                        highlighted = true,
                        etalaseRules = listOf()
                    )
                ),
                isShopWidgetAlreadyShown = false,
                widgetUserAddressLocalData = addressWidgetData,
                context,
                mockIsDirectPurchaseTrue
            )

            verifyGetMemberShipUseCaseCalled()
            verifyGetMerchantVoucerUseCaseCalled()
            verifyGetShopFeaturedProductUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.membershipData.value is Success<MembershipStampProgressUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.merchantVoucherData.value is Success<ShopMerchantVoucherUiModel>)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductFeaturedData.value == null)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductEtalaseHighlightData.value is Success<ShopProductEtalaseHighlightUiModel>)
        }
    }

    @Test
    fun `check getBuyerViewContentData membershipData value is null if getMembershipData throw exception`() {
        runBlocking {
            // membership use case
            coEvery {
                getMembershipUseCase.executeOnBackground()
            } throws Exception()

            // merchant voucher use case
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(null, true, null, null))

            // shop featured product use case
            coEvery {
                getShopFeaturedProductUseCase.executeOnBackground()
            } returns listOf()

            // gql get shop product use case
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct())
            )

            every {
                getShopHighlightProductUseCase.get()
            } returns getShopProductUseCase
            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                shopId = "123",
                etalaseList = listOf(
                    ShopEtalaseItemDataModel(
                        etalaseId = "sold",
                        alias = "",
                        etalaseName = "test",
                        type = 2,
                        etalaseBadge = "",
                        etalaseCount = 10,
                        highlighted = true,
                        etalaseRules = listOf()
                    )
                ),
                isShopWidgetAlreadyShown = false,
                widgetUserAddressLocalData = addressWidgetData,
                context,
                mockIsDirectPurchaseTrue
            )
            assert(viewModelShopPageProductListViewModel.membershipData.value == null)
        }
    }

    @Test
    fun `check getBuyerViewContentData merchantVoucherData value is null if getMerchantVoucherCoupon throw exception`() {
        runBlocking {
            // membership use case
            coEvery {
                getMembershipUseCase.executeOnBackground()
            } returns MembershipStampProgress()

            // merchant voucher use case
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } throws Exception()

            // shop featured product use case
            coEvery {
                getShopFeaturedProductUseCase.executeOnBackground()
            } returns listOf()

            // gql get shop product use case
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct())
            )

            every {
                getShopHighlightProductUseCase.get()
            } returns getShopProductUseCase
            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                shopId = "123",
                etalaseList = listOf(
                    ShopEtalaseItemDataModel(
                        etalaseId = "sold",
                        alias = "",
                        etalaseName = "test",
                        type = 2,
                        etalaseBadge = "",
                        etalaseCount = 10,
                        highlighted = true,
                        etalaseRules = listOf()
                    )
                ),
                isShopWidgetAlreadyShown = false,
                widgetUserAddressLocalData = addressWidgetData,
                context,
                mockIsDirectPurchaseTrue
            )
            assert(viewModelShopPageProductListViewModel.merchantVoucherData.value == null)
        }
    }

    @Test
    fun `check getBuyerViewContentData if home tab is shown`() {
        runBlocking {
            // membership use case
            coEvery {
                getMembershipUseCase.executeOnBackground()
            } returns MembershipStampProgress()

            // merchant voucher use case
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(null, true, null, null))

            // shop featured product use case
            coEvery {
                getShopFeaturedProductUseCase.executeOnBackground()
            } returns listOf()

            // gql get shop product use case
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct())
            )

            every {
                getShopHighlightProductUseCase.get()
            } returns getShopProductUseCase
            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                shopId = "123",
                etalaseList = listOf(
                    ShopEtalaseItemDataModel(
                        etalaseId = "sold",
                        alias = "",
                        etalaseName = "test",
                        type = 2,
                        etalaseBadge = "",
                        etalaseCount = 10,
                        highlighted = true,
                        etalaseRules = listOf()
                    )
                ),
                isShopWidgetAlreadyShown = true,
                widgetUserAddressLocalData = addressWidgetData,
                context,
                mockIsDirectPurchaseTrue
            )
            assert(viewModelShopPageProductListViewModel.merchantVoucherData.value == null)
            assert(viewModelShopPageProductListViewModel.shopProductFeaturedData.value == null)
            assert(viewModelShopPageProductListViewModel.shopProductEtalaseHighlightData.value == null)
        }
    }

    @Test
    fun `check getBuyerViewContentData productListData value is Fail if exception is thrown`() {
        runBlocking {
            // membership use case
            coEvery {
                getMembershipUseCase.executeOnBackground()
            } returns MembershipStampProgress()

            // merchant voucher use case
            coEvery {
                mvcSummaryUseCase.getResponse(any())
            } returns TokopointsCatalogMVCSummaryResponse(TokopointsCatalogMVCSummary(null, true, null, null))

            // shop featured product use case
            coEvery {
                getShopFeaturedProductUseCase.executeOnBackground()
            } throws Exception()

            // gql get shop product use case
            coEvery {
                getShopProductUseCase.executeOnBackground()
            } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct())
            )

            every {
                getShopHighlightProductUseCase.get()
            } returns getShopProductUseCase
            val observer = mockk<Observer<Result<MembershipStampProgressUiModel>>>(relaxed = true)
            viewModelShopPageProductListViewModel.membershipData.observeForever(observer)
            every { observer.onChanged(any<Success<MembershipStampProgressUiModel>>()) } throws Exception()
            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                shopId = "123",
                etalaseList = listOf(
                    ShopEtalaseItemDataModel(
                        etalaseId = "sold",
                        alias = "",
                        etalaseName = "test",
                        type = 2,
                        etalaseBadge = "",
                        etalaseCount = 10,
                        highlighted = true,
                        etalaseRules = listOf()
                    )
                ),
                isShopWidgetAlreadyShown = false,
                widgetUserAddressLocalData = addressWidgetData,
                context,
                mockIsDirectPurchaseTrue
            )
            assert(viewModelShopPageProductListViewModel.productListData.value is Fail)
        }
    }

    @Test
    fun `check getSortNameById is return same value with mock value`() {
        runBlocking {
            val sortList = mutableListOf<ShopProductSort>().apply {
                add(
                    ShopProductSort().apply {
                        name = "sortExample"
                        value = "123"
                        inputType = "inputType"
                        key = "key"
                    }
                )
            }

            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns sortList

            coEvery {
                shopProductSortMapper.convertSort(any())
            } returns convertToUiModel(sortList)

            viewModelShopPageProductListViewModel.getShopFilterData("123")

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            val actualResult: String = viewModelShopPageProductListViewModel.getSortNameById("123")
            Assert.assertNotNull(actualResult)
            Assert.assertTrue(actualResult == "sortExample")
        }
    }

    @Test
    fun `check getSortNameById is return empty because no sortId is matched`() {
        runBlocking {
            val sortList = mutableListOf<ShopProductSort>().apply {
                add(
                    ShopProductSort().apply {
                        name = "sortExample"
                        value = "321"
                        inputType = "inputType"
                        key = "key"
                    }
                )
            }

            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns sortList

            coEvery {
                shopProductSortMapper.convertSort(any())
            } returns convertToUiModel(sortList)

            viewModelShopPageProductListViewModel.getShopFilterData("123")

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            val actualResult: String = viewModelShopPageProductListViewModel.getSortNameById("123")
            Assert.assertNotNull(actualResult)
            Assert.assertTrue(actualResult == "")
        }
    }

    @Test
    fun `check getSortNameById return empty because list is empty`() {
        runBlocking {
            coEvery {
                getShopEtalaseByShopUseCase.createObservable(any())
            } returns Observable.just(arrayListOf(ShopEtalaseModel()))

            coEvery {
                gqlGetShopSortUseCase.executeOnBackground()
            } returns listOf()

            coEvery {
                shopProductSortMapper.convertSort(any())
            } returns convertToUiModel(listOf())

            viewModelShopPageProductListViewModel.getShopFilterData("123")

            verifyGetShopEtalaseByShopUseCaseCalled()
            verifyGetShopSortUseCaseCalled()

            val actualResult: String = viewModelShopPageProductListViewModel.getSortNameById("123")
            Assert.assertNotNull(actualResult)
            Assert.assertTrue(actualResult == "")
        }
    }

    private fun convertToUiModel(list: List<ShopProductSort>): List<ShopProductSortModel> {
        return mutableListOf<ShopProductSortModel>().apply {
            list.forEach {
                add(
                    ShopProductSortModel().apply {
                        key = it.key
                        value = it.value
                        inputType = it.inputType
                        name = it.name
                    }
                )
            }
        }
    }

    private fun verifyGetShopProductUseCaseCaseCalled() {
        coVerify { getShopProductUseCase.executeOnBackground() }
    }

    private fun verifyGetShopFeaturedProductUseCaseCalled() {
        coVerify { getShopFeaturedProductUseCase.executeOnBackground() }
    }

    private fun verifyGetClaimBenefitMembershipUseCaseCalled() {
        coVerify { claimBenefitMembershipUseCase.executeOnBackground() }
    }

    private fun verifyGetMemberShipUseCaseCalled() {
        coVerify { getMembershipUseCase.executeOnBackground() }
    }

    private fun verifyGetShopEtalaseByShopUseCaseCalled() {
        coVerify { getShopEtalaseByShopUseCase.createObservable(any()) }
    }

    private fun verifyGetShopSortUseCaseCalled() {
        coVerify { gqlGetShopSortUseCase.executeOnBackground() }
    }

    private fun verifyGetShopFilterBottomSheetDataUseCaseCalled() {
        coVerify { getShopFilterBottomSheetDataUseCase.executeOnBackground() }
    }

    private fun verifyGetShopFilterProductCountUseCaseCalled() {
        coVerify { getShopFilterProductCountUseCase.executeOnBackground() }
    }

    private fun verifyGetMerchantVoucerUseCaseCalled() {
        coVerify { mvcSummaryUseCase.getResponse(any()) }
    }

    @Test
    fun `when call getShopProductDataWithUpdatedQuantity with mocked mini cart data, then product in cart should match with mini cart data`() {
        val mockMiniCartSimplifiedData = getMockMiniCartSimplifiedData()
        val mockShopProductListData = getMockShopProductData()
        viewModelShopPageProductListViewModel.setMiniCartData(mockMiniCartSimplifiedData)
        viewModelShopPageProductListViewModel.getShopProductDataWithUpdatedQuantity(
            mockShopProductListData
        )
        viewModelShopPageProductListViewModel.updatedShopProductListQuantityData.value?.onEach { shopProductUiModel ->
            when (shopProductUiModel) {
                is ShopProductUiModel -> {
                    checkProductListDataShouldMatchWithMatchedMiniCartData(
                        shopProductUiModel,
                        mockMiniCartSimplifiedData
                    )
                }
            }
        }
    }

    private fun getMatchedMiniCartItem(
        shopProductUiModel: ShopProductUiModel,
        miniCartData: MiniCartSimplifiedData
    ): List<MiniCartItem.MiniCartItemProduct> {
        return miniCartData.let { miniCartSimplifiedData ->
            val isVariant = shopProductUiModel.isVariant
            val listMatchedMiniCartItemProduct = if (isVariant) {
                miniCartSimplifiedData.miniCartItems.values.filterIsInstance<MiniCartItem.MiniCartItemProduct>()
                    .filter { it.productParentId == shopProductUiModel.parentId }
            } else {
                val childProductId = shopProductUiModel.id
                miniCartSimplifiedData.miniCartItems.getMiniCartItemProduct(childProductId)?.let {
                    listOf(it)
                }.orEmpty()
            }
            listMatchedMiniCartItemProduct.filter { !it.isError }
        }
    }

    private fun checkProductListDataShouldMatchWithMatchedMiniCartData(
        shopProductUiModel: ShopProductUiModel,
        miniCartSimplifiedData: MiniCartSimplifiedData
    ) {
        val matchedMiniCartItem = getMatchedMiniCartItem(
            shopProductUiModel,
            miniCartSimplifiedData
        )
        if (matchedMiniCartItem.isNotEmpty()) {
            val quantityOnMiniCart = matchedMiniCartItem.sumOf { it.quantity }
            assert(quantityOnMiniCart == shopProductUiModel.productInCart)
        } else {
            assert(shopProductUiModel.productInCart.isZero())
        }
    }

    @Test
    fun `when call getShopProductDataWithUpdatedQuantity without mocked mini cart data, then product in cart should be zero`() {
        val mockShopProductListData = getMockShopProductData()
        viewModelShopPageProductListViewModel.getShopProductDataWithUpdatedQuantity(mockShopProductListData)
        viewModelShopPageProductListViewModel.updatedShopProductListQuantityData.value?.onEach { shopProductUiModel ->
            when (shopProductUiModel) {
                is ShopProductUiModel -> {
                    assert(shopProductUiModel.productInCart.isZero())
                }
            }
        }
    }

    private fun getMockShopProductData(): MutableList<Visitable<*>> {
        return mutableListOf(
            ShopProductUiModel().apply {
                id = "1"
                productInCart = 3
            },
            ShopProductUiModel().apply {
                id = "8"
            },
            ShopProductUiModel().apply {
                parentId = "12"
                isVariant = true
            },
            ShopProductUiModel().apply {
                parentId = "13"
                isVariant = true
            },
            ShopProductUiModel().apply {
                parentId = "14"
                isVariant = true
            },
            ShopProductUiModel(),
            ShopProductEtalaseTitleUiModel("", "")
        )
    }

    private fun getMockMiniCartSimplifiedData(): MiniCartSimplifiedData {
        return MiniCartSimplifiedData(
            miniCartItems = hashMapOf(
                MiniCartItemKey("1") to MiniCartItem.MiniCartItemProduct(
                    productId = "1",
                    quantity = 3
                ),
                MiniCartItemKey("2") to MiniCartItem.MiniCartItemProduct(
                    productId = "2",
                    productParentId = "12",
                    quantity = 5
                ),
                MiniCartItemKey("3") to MiniCartItem.MiniCartItemProduct(
                    productId = "3",
                    productParentId = "12",
                    quantity = 5
                ),
                MiniCartItemKey("4") to MiniCartItem.MiniCartItemProduct(
                    productId = "4",
                    productParentId = "13",
                    quantity = 5,
                    isError = true
                ),
                MiniCartItemKey("5") to MiniCartItem.MiniCartItemProduct(
                    productId = "5",
                    productParentId = "13",
                    quantity = 5
                ),
                MiniCartItemKey("6") to MiniCartItem.MiniCartItemProduct(
                    productId = "6",
                    productParentId = "14",
                    quantity = 5,
                    isError = true
                ),
                MiniCartItemKey("7") to MiniCartItem.MiniCartItemProduct(
                    productId = "7",
                    productParentId = "14",
                    quantity = 5,
                    isError = true
                ),
                MiniCartItemKey("8") to MiniCartItem.MiniCartItemProduct(
                    productId = "8",
                    isError = true
                ),
                MiniCartItemKey("9") to MiniCartItem.MiniCartItemParentProduct()
            )
        )
    }

    @Test
    fun `when call handleAtcFlow on add item to cart state is success, then miniCartAdd value should be success`() {
        val mockQuantity = 5
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "33"
            name = "product name"
            displayedPrice = "100"
        }
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(
                AddToCartDataModel(
                    data = DataModel(
                        success = 1,
                        productId = "33"
                    )
                )
            )
        }
        viewModelShopPageProductListViewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModelShopPageProductListViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(viewModelShopPageProductListViewModel.miniCartAdd.value is Success)
        assert(viewModelShopPageProductListViewModel.shopPageAtcTracker.value?.atcType == ShopPageAtcTracker.AtcType.ADD)
        assert(viewModelShopPageProductListViewModel.createAffiliateCookieAtcProduct.value != null)
    }

    @Test
    fun `when call handleAtcFlow on add item to cart state is error, then miniCartAdd value should be fail`() {
        val mockQuantity = 5
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "33"
            name = "product name"
            displayedPrice = "100"
        }
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Exception())
        }
        viewModelShopPageProductListViewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModelShopPageProductListViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(viewModelShopPageProductListViewModel.miniCartAdd.value is Fail)
    }

    @Test
    fun `when call handleAtcFlow on remove item from cart state is success, then miniCartRemove value should be success`() {
        val mockQuantity = 0
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "1"
            name = "product name"
            displayedPrice = "100"
        }
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(
                RemoveFromCartData(
                    data = Data(
                        success = 1
                    )
                )
            )
        }
        viewModelShopPageProductListViewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModelShopPageProductListViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(viewModelShopPageProductListViewModel.miniCartRemove.value is Success)
        assert(viewModelShopPageProductListViewModel.shopPageAtcTracker.value?.atcType == ShopPageAtcTracker.AtcType.REMOVE)
        assert(viewModelShopPageProductListViewModel.createAffiliateCookieAtcProduct.value == null)
    }

    @Test
    fun `when call handleAtcFlow on remove item from cart state is error, then miniCartRemove value should be fail`() {
        val mockQuantity = 0
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "1"
            name = "product name"
            displayedPrice = "100"
        }
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Exception())
        }
        viewModelShopPageProductListViewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModelShopPageProductListViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(viewModelShopPageProductListViewModel.miniCartRemove.value is Fail)
    }

    @Test
    fun `when call handleAtcFlow on update add item from cart state is success, then miniCartRemove value should be success`() {
        val mockQuantity = 10
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "1"
            productInCart = 5
            name = "product name"
            displayedPrice = "100"
        }
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(
                UpdateCartV2Data(
                    data = com.tokopedia.cartcommon.data.response.updatecart.Data(
                        status = true
                    )
                )
            )
        }
        viewModelShopPageProductListViewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModelShopPageProductListViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(viewModelShopPageProductListViewModel.miniCartUpdate.value is Success)
        assert(viewModelShopPageProductListViewModel.shopPageAtcTracker.value?.atcType == ShopPageAtcTracker.AtcType.UPDATE_ADD)
        assert(viewModelShopPageProductListViewModel.createAffiliateCookieAtcProduct.value != null)
    }

    @Test
    fun `when call handleAtcFlow on update remove item from cart state is success, then miniCartRemove value should be success`() {
        val mockQuantity = 2
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "1"
            productInCart = 5
            name = "product name"
            displayedPrice = "100"
        }
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(
                UpdateCartV2Data(
                    data = com.tokopedia.cartcommon.data.response.updatecart.Data(
                        status = true
                    )
                )
            )
        }
        viewModelShopPageProductListViewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModelShopPageProductListViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(viewModelShopPageProductListViewModel.miniCartUpdate.value is Success)
        assert(viewModelShopPageProductListViewModel.shopPageAtcTracker.value?.atcType == ShopPageAtcTracker.AtcType.UPDATE_REMOVE)
        assert(viewModelShopPageProductListViewModel.createAffiliateCookieAtcProduct.value == null)
    }

    @Test
    fun `when call handleAtcFlow on update item from cart state is success, then miniCartRemove value should be fail`() {
        val mockQuantity = 2
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "1"
            productInCart = 5
            name = "product name"
            displayedPrice = "100"
        }
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Exception())
        }
        viewModelShopPageProductListViewModel.setMiniCartData(getMockMiniCartSimplifiedData())
        viewModelShopPageProductListViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(viewModelShopPageProductListViewModel.miniCartUpdate.value is Fail)
    }

    @Test
    fun `when call handleAtcFlow without set mini cart data, then miniCartAdd, miniCartRemove ,miniCartUpdate and shopPageAtcTracker value should be null`() {
        val mockQuantity = 2
        val mockComponentName = "Product"
        val mockShopProductUiModel = ShopProductUiModel().apply {
            id = "1"
            productInCart = 5
            name = "product name"
            displayedPrice = "100"
        }
        viewModelShopPageProductListViewModel.handleAtcFlow(
            mockQuantity,
            mockShopId,
            mockComponentName,
            mockShopProductUiModel
        )
        assert(viewModelShopPageProductListViewModel.miniCartAdd.value == null)
        assert(viewModelShopPageProductListViewModel.miniCartUpdate.value == null)
        assert(viewModelShopPageProductListViewModel.miniCartUpdate.value == null)
        assert(viewModelShopPageProductListViewModel.shopPageAtcTracker.value == null)
    }

    @Test
    fun `check whether shopPageTickerData and shopPageShopShareData post success value`() {
        val mockShopId = "123"
        val mockShopDomain = "mock domain"
        coEvery {
            gqlGetShopInfoForHeaderUseCase.get().executeOnBackground()
        } returns ShopInfo()

        shopPageProductListResultViewModel.getShopShareData(mockShopId, mockShopDomain)
        assert(shopPageProductListResultViewModel.shopPageShopShareData.value is Success)
    }

    @Test
    fun `check whether shopPageShopShareData value is null if error when get shopInfo data`() {
        val mockShopId = "123"
        val mockShopDomain = "mock domain"
        coEvery {
            gqlGetShopInfoForHeaderUseCase.get().executeOnBackground()
        } throws Exception()

        shopPageProductListResultViewModel.getShopShareData(mockShopId, mockShopDomain)
        assert(shopPageProductListResultViewModel.shopPageShopShareData.value == null)
    }
}
