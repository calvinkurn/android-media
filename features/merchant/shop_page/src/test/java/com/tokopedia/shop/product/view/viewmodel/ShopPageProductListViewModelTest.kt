package com.tokopedia.shop.product.view.viewmodel

import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetMembershipUseCaseNew
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductFeaturedViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.*

@ExperimentalCoroutinesApi
class ShopPageProductListViewModelTest : ShopPageProductListViewModelTestFixture() {

    @Test
    fun `check whether response get buyer shop page product success is not null`() {
        runBlocking {

            coEvery { getMembershipUseCase.executeOnBackground() } returns MembershipStampProgress()

            viewModelShopPageProductListViewModel.getBuyerShopPageProductTabData(anyString(),
                    ShopProductSortFilterUiModel(), false)

            verify { GetMembershipUseCaseNew.createRequestParams(anyInt()) }

            verifyGetMemberShipUseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.membershipData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.membershipData.value)
        }
    }

    @Test
    fun `check whether response shop featured product error is null`() {
        runBlocking {
            coEvery { getMembershipUseCase.executeOnBackground() } throws Exception()
            viewModelShopPageProductListViewModel.getBuyerShopPageProductTabData(anyString(), ShopProductSortFilterUiModel(), anyBoolean())

            verify { GetMembershipUseCaseNew.createRequestParams(anyInt()) }

            verifyGetMemberShipUseCaseCalled()

            Assert.assertNull(viewModelShopPageProductListViewModel.membershipData.value)
        }
    }

    @Test
    fun `check whether response get seller shop page product tab data success is not null`() {
        runBlocking {

            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
            viewModelShopPageProductListViewModel.getSellerShopPageProductTabData(anyString(), ShopProductSortFilterUiModel(), sortId)

            verify { GqlGetShopProductUseCase.createParams(anyString(), any()) }

            verifyGetShopProductUseCaseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.productListData.value)
        }
    }

    @Test
    fun `check whether response get seller shop page product tab data error is null`() {
        runBlocking {

            coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()
            viewModelShopPageProductListViewModel.getSellerShopPageProductTabData(anyString(), ShopProductSortFilterUiModel(), sortId)

            verify { GqlGetShopProductUseCase.createParams(anyString(), any()) }

            verifyGetShopProductUseCaseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Fail)
        }
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
    fun `check whether response get new product list data success is not null`() {
        runBlocking {

            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
            viewModelShopPageProductListViewModel.getNewProductListData(anyString(), anyString())

            verify { GqlGetShopProductUseCase.createParams(anyString(), any()) }

            verifyGetShopProductUseCaseCaseCalled()

            Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.productListData.value)
        }
    }

    @Test
    fun `check whether response  get new product list data error is null`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()
            viewModelShopPageProductListViewModel.getNewProductListData(anyString(), anyString())

            verify { GqlGetShopProductUseCase.createParams(anyString(), any()) }

            verifyGetShopProductUseCaseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Fail)
        }
    }

    @Test
    fun `check whether response get next product list data success is not null`() {
        runBlocking {

            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
            viewModelShopPageProductListViewModel.getNextProductListData(anyString(), anyString(), anyInt())
            verifyGetShopProductUseCaseCaseCalled()

            verify { GqlGetShopProductUseCase.createParams(anyString(), any()) }

            Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.productListData.value)
        }
    }

    @Test
    fun `check whether response get next product list data error is null`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()
            viewModelShopPageProductListViewModel.getNextProductListData(anyString(), anyString(), anyInt())

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

    private fun verifyGetShopProductUseCaseCaseCalled() {
        coVerify { getShopProductUseCase.executeOnBackground() }
    }

    private fun verifyGetClaimBenefitMembershipUseCaseCalled() {
        coVerify { claimBenefitMembershipUseCase.executeOnBackground() }
    }

    private fun verifyGetMemberShipUseCaseCalled() {
        coVerify { getMembershipUseCase.executeOnBackground() }
    }

    private fun verifyGetShopFeaturedProductUseCaseCalled() {
        coVerify { getShopFeaturedProductUseCase.executeOnBackground() }
    }

    private fun List<ShopFeaturedProduct>.toViewModel(): ShopProductFeaturedViewModel {
        return ShopProductFeaturedViewModel(
               this.map { shopFeaturedProduct ->
                    ShopPageProductListMapper.mapShopFeaturedProductToProductViewModel(
                            shopFeaturedProduct,
                            anyBoolean()
                    )
                }
        )
    }
}
