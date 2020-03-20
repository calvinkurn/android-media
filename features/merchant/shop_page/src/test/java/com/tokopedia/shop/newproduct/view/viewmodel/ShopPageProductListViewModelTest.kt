package com.tokopedia.shop.newproduct.view.viewmodel

import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import java.lang.Exception

@ExperimentalCoroutinesApi
class ShopPageProductListViewModelTest : ShopPageProductListViewModelTestFixture() {

    @Test
    fun `check whether response get buyer shop page product success is not null`() {
        runBlocking {
            val shopFeaturedProduct = ShopFeaturedProduct()
            val shopFeaturedProducts = listOf(shopFeaturedProduct)

            coEvery { getMembershipUseCase.executeOnBackground() } returns MembershipStampProgress()
            coEvery { getShopFeaturedProductUseCase.executeOnBackground() } returns shopFeaturedProducts
            viewModelShopPageProductListViewModel.getBuyerShopPageProductTabData(anyString(), any(), anyBoolean())
            verifyGetMemberShipUseCaseCalled()
            verifyGetShopFeaturedProductUseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.membershipData.value is Success)
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductFeaturedData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.membershipData.value)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.shopProductFeaturedData.value)
        }
    }

    @Test
    fun `check whether response shop featured product error is null`() {
        runBlocking {
            coEvery { getShopFeaturedProductUseCase.executeOnBackground() } throws Exception()
            viewModelShopPageProductListViewModel.getBuyerShopPageProductTabData(anyString(), any(), anyBoolean())
            verifyGetShopFeaturedProductUseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductFeaturedData.value is Fail)
        }
    }

    @Test
    fun `check whether response get seller shop page product tab data success is not null`() {
        runBlocking {
            val shopFeaturedProduct = ShopFeaturedProduct()
            val shopFeaturedProducts = listOf(shopFeaturedProduct)

            coEvery { getShopFeaturedProductUseCase.executeOnBackground() } returns shopFeaturedProducts
            viewModelShopPageProductListViewModel.getSellerShopPageProductTabData(anyString(), any())
            verifyGetShopFeaturedProductUseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductFeaturedData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.shopProductFeaturedData.value)
        }
    }

    @Test
    fun `check whether response get seller shop page product tab data error is null`() {
        runBlocking {
            coEvery { getShopFeaturedProductUseCase.executeOnBackground() } throws Exception()
            viewModelShopPageProductListViewModel.getSellerShopPageProductTabData(anyString(), any())
            verifyGetShopFeaturedProductUseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.shopProductFeaturedData.value is Fail)
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
            Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.productListData.value)
        }
    }

    @Test
    fun `check whether response get next product list data error is null`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()
            viewModelShopPageProductListViewModel.getNextProductListData(anyString(), anyString(), anyInt())
            verifyGetShopProductUseCaseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.productListData.value is Fail)
        }
    }

    @Test
    fun `check whether response get new membership data success is not null`() {
        val mockShopId = "7307343"
        runBlocking {
            coEvery { getMembershipUseCase.executeOnBackground() } returns MembershipStampProgress()
            viewModelShopPageProductListViewModel.getNewMembershipData(mockShopId)
            verifyGetMemberShipUseCaseCalled()
            Assert.assertTrue(viewModelShopPageProductListViewModel.newMembershipData.value is Success)
            Assert.assertNotNull(viewModelShopPageProductListViewModel.newMembershipData.value)
        }
    }

    @Test
    fun `check whether response get new membership data error`() {
        runBlocking {
            val mockShopId = "7307343"
            coEvery { getMembershipUseCase.executeOnBackground() } throws Exception()
            viewModelShopPageProductListViewModel.getNewMembershipData(mockShopId)
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
}
