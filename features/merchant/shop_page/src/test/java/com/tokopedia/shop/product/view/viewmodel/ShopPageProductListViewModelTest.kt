package com.tokopedia.shop.product.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.mvcwidget.ResultStatus
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.shop.product.domain.interactor.GetMembershipUseCaseNew
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort
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
    fun `check whether response get product list data success is not null`() {
        runBlocking {

            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
            viewModelShopPageProductListViewModel.getProductListData(
                    anyString(),
                    anyInt(),
                    anyInt(),
                    anyString(),
                    ShopProductFilterParameter(),
                    addressWidgetData
            )

            verify { GqlGetShopProductUseCase.createParams(anyString(), any()) }

            verifyGetShopProductUseCaseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.productListData.value)
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
                    addressWidgetData
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
            viewModelShopPageProductListViewModel.getProductListData(anyString(), anyInt(), anyInt(), anyString(), ShopProductFilterParameter(), addressWidgetData)

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
                    etalaseList = listOf(ShopEtalaseItemDataModel(
                            etalaseId = "sold",
                            alias = "",
                            etalaseName = "test",
                            type = 2,
                            etalaseBadge = "",
                            etalaseCount = 10,
                            highlighted = true,
                            etalaseRules = listOf()
                    )),
                    isShowNewShopHomeTab = false,
                    widgetUserAddressLocalData = addressWidgetData,
                    context
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
                ShopProduct.GetShopProduct(data = listOf(ShopProduct())
        ))
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
                    etalaseList = listOf(ShopEtalaseItemDataModel(
                            etalaseId = "sold",
                            alias = "",
                            etalaseName = "test",
                            type = 2,
                            etalaseBadge = "",
                            etalaseCount = 10,
                            highlighted = true,
                            etalaseRules = listOf()
                    )),
                    isShowNewShopHomeTab = false,
                    widgetUserAddressLocalData = addressWidgetData,
                    context
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
                    etalaseList = listOf(ShopEtalaseItemDataModel(
                            etalaseId = "discount",
                            alias = "",
                            etalaseName = "test",
                            type = 2,
                            etalaseBadge = "",
                            etalaseCount = 10,
                            highlighted = true,
                            etalaseRules = listOf()
                    )),
                    isShowNewShopHomeTab = false,
                    widgetUserAddressLocalData = addressWidgetData,
                    context
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
                    etalaseList = listOf(ShopEtalaseItemDataModel(
                            etalaseId = "123",
                            alias = "",
                            etalaseName = "test",
                            type = 2,
                            etalaseBadge = "",
                            etalaseCount = 10,
                            highlighted = true,
                            etalaseRules = listOf()
                    )),
                    isShowNewShopHomeTab = false,
                    widgetUserAddressLocalData = addressWidgetData,
                    context
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
                    etalaseList = listOf(ShopEtalaseItemDataModel(
                            etalaseId = "123",
                            alias = "",
                            etalaseName = "test",
                            type = 2,
                            etalaseBadge = "",
                            etalaseCount = 10,
                            highlighted = false,
                            etalaseRules = listOf()
                    )),
                    isShowNewShopHomeTab = false,
                    widgetUserAddressLocalData = addressWidgetData,
                    context
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
                    etalaseList = listOf(ShopEtalaseItemDataModel(
                            etalaseId = "123",
                            alias = "",
                            etalaseName = "test",
                            type = 2,
                            etalaseBadge = "",
                            etalaseCount = 10,
                            highlighted = true,
                            etalaseRules = listOf()
                    )),
                    isShowNewShopHomeTab = false,
                    widgetUserAddressLocalData = addressWidgetData,
                    context
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
                    etalaseList = listOf(ShopEtalaseItemDataModel(
                            etalaseId = "123",
                            alias = "",
                            etalaseName = "test",
                            type = 2,
                            etalaseBadge = "",
                            etalaseCount = 10,
                            highlighted = true,
                            etalaseRules = listOf()
                    )),
                    isShowNewShopHomeTab = false,
                    widgetUserAddressLocalData = addressWidgetData,
                    context
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
                    etalaseList = listOf(ShopEtalaseItemDataModel(
                            etalaseId = "sold",
                            alias = "",
                            etalaseName = "test",
                            type = 2,
                            etalaseBadge = "",
                            etalaseCount = 10,
                            highlighted = true,
                            etalaseRules = listOf()
                    )),
                    isShowNewShopHomeTab = false,
                    widgetUserAddressLocalData = addressWidgetData,
                    context
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
                    etalaseList = listOf(ShopEtalaseItemDataModel(
                            etalaseId = "sold",
                            alias = "",
                            etalaseName = "test",
                            type = 2,
                            etalaseBadge = "",
                            etalaseCount = 10,
                            highlighted = true,
                            etalaseRules = listOf()
                    )),
                    isShowNewShopHomeTab = false,
                    widgetUserAddressLocalData = addressWidgetData,
                    context
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
                    etalaseList = listOf(ShopEtalaseItemDataModel(
                            etalaseId = "sold",
                            alias = "",
                            etalaseName = "test",
                            type = 2,
                            etalaseBadge = "",
                            etalaseCount = 10,
                            highlighted = true,
                            etalaseRules = listOf()
                    )),
                    isShowNewShopHomeTab = false,
                    widgetUserAddressLocalData = addressWidgetData,
                    context
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
                    etalaseList = listOf(ShopEtalaseItemDataModel(
                            etalaseId = "sold",
                            alias = "",
                            etalaseName = "test",
                            type = 2,
                            etalaseBadge = "",
                            etalaseCount = 10,
                            highlighted = true,
                            etalaseRules = listOf()
                    )),
                    isShowNewShopHomeTab = true,
                    widgetUserAddressLocalData = addressWidgetData,
                    context
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
            val slot = slot<Result<MembershipStampProgressUiModel>>()
            every { observer.onChanged(any<Success<MembershipStampProgressUiModel>>()) } throws Exception()
            viewModelShopPageProductListViewModel.getBuyerViewContentData(
                    shopId = "123",
                    etalaseList = listOf(ShopEtalaseItemDataModel(
                            etalaseId = "sold",
                            alias = "",
                            etalaseName = "test",
                            type = 2,
                            etalaseBadge = "",
                            etalaseCount = 10,
                            highlighted = true,
                            etalaseRules = listOf()
                    )),
                    isShowNewShopHomeTab = false,
                    widgetUserAddressLocalData = addressWidgetData,
                    context
            )
            assert(viewModelShopPageProductListViewModel.productListData.value is Fail)
        }
    }

    @Test
    fun `check getSortNameById is return same value with mock value`() {
        runBlocking {
            val sortList = mutableListOf<ShopProductSort>().apply {
                add(ShopProductSort().apply {
                    name = "sortExample"
                    value = "123"
                    inputType = "inputType"
                    key = "key"
                })
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

            val actualResult : String = viewModelShopPageProductListViewModel.getSortNameById("123")
            Assert.assertNotNull(actualResult)
            Assert.assertTrue(actualResult == "sortExample")
        }
    }

    @Test
    fun `check getSortNameById is return empty because no sortId is matched`() {
        runBlocking {
            val sortList = mutableListOf<ShopProductSort>().apply {
                add(ShopProductSort().apply {
                    name = "sortExample"
                    value = "321"
                    inputType = "inputType"
                    key = "key"
                })
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

            val actualResult : String = viewModelShopPageProductListViewModel.getSortNameById("123")
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

            val actualResult : String = viewModelShopPageProductListViewModel.getSortNameById("123")
            Assert.assertNotNull(actualResult)
            Assert.assertTrue(actualResult == "")
        }
    }

    private fun convertToUiModel(list: List<ShopProductSort>): List<ShopProductSortModel> {
        return mutableListOf<ShopProductSortModel>().apply {
            list.forEach {
                add(ShopProductSortModel().apply {
                    key = it.key
                    value = it.value
                    inputType = it.inputType
                    name = it.name
                })
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
        coVerify { mvcSummaryUseCase.getResponse(any())}
    }
}