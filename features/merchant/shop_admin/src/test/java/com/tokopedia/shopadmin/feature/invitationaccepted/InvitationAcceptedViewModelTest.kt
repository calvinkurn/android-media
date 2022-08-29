package com.tokopedia.shopadmin.feature.invitationaccepted

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.AdminPermissionUiModel
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.ArticleDetailUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class InvitationAcceptedViewModelTest : InvitationAcceptedViewModelTestFixture() {

    @Test
    fun `when fetchAdminPermission should set live data success`() {
        runBlocking {
            //given
            val adminPermissionList = listOf(
                AdminPermissionUiModel(
                    permissionName = "Kelola Toko",
                    iconUrl = "https://images.tokopedia.net/img/kelolaToko.png"
                ),
                AdminPermissionUiModel(
                    permissionName = "Atur Produk",
                    iconUrl = "https://images.tokopedia.net/img/aturProduk.png"
                ),
                AdminPermissionUiModel(
                    permissionName = "Ubah Stok Produk",
                    iconUrl = "https://images.tokopedia.net/img/ubahStokProduk.png"
                )
            )
            onGetPermissionListUseCase_thenReturn(shopId, adminPermissionList)

            //when
            viewModel.fetchAdminPermission(shopId)

            //then
            verifyGetPermissionListUseCaseCalled(shopId)
            val actualResult = (viewModel.adminPermission.value as Success).data
            Assert.assertEquals(adminPermissionList, actualResult)
            adminPermissionList.forEachIndexed { index, item ->
                Assert.assertEquals(adminPermissionList[index].iconUrl, item.iconUrl)
                Assert.assertEquals(adminPermissionList[index].permissionName, item.permissionName)
            }
        }
    }

    @Test
    fun `when fetchAdminPermission should set live data error`() {
        runBlocking {
            //given
            val errorException = MessageErrorException()
            onGetPermissionListUseCaseError_thenReturn(shopId, errorException)

            //when
            viewModel.fetchAdminPermission(shopId)

            //then
            verifyGetPermissionListUseCaseCalled(shopId)
            val actualResult = (viewModel.adminPermission.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            Assert.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when fetchArticleDetail should set live data success`() {
        runBlocking {
            //given
            val articleDetailUiModel =
                ArticleDetailUiModel("Kini, Skor Performa Toko jadi satu-satunya skema untuk menilai kualitas pelayananmu kepada pembeli")
            onGetArticleDetailUseCase_thenReturn(articleDetailUiModel)

            //when
            viewModel.fetchArticleDetail()

            //then
            verifyGetArticleDetailUseCaseCalled()
            val actualResult = (viewModel.articleDetail.value as Success).data
            Assert.assertEquals(articleDetailUiModel, actualResult)
            Assert.assertEquals(articleDetailUiModel.htmlContent, actualResult.htmlContent)
        }
    }

    @Test
    fun `when fetchArticleDetail should set live data error`() {
        runBlocking {
            //given
            val errorException = MessageErrorException()
            onGetArticleDetailUseCaseError_thenReturn(errorException)

            //when
            viewModel.fetchArticleDetail()

            //then
            verifyGetArticleDetailUseCaseCalled()
            val actualResult = (viewModel.articleDetail.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            Assert.assertEquals(expectedResult, actualResult)
        }
    }
}