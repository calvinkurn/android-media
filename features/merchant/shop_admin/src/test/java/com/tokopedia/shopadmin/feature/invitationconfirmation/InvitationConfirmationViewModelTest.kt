package com.tokopedia.shopadmin.feature.invitationconfirmation

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shopadmin.common.presentation.uimodel.AdminTypeUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.AdminConfirmationRegUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ShopAdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.UserProfileUpdateUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ValidateAdminEmailUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
@FlowPreview
class InvitationConfirmationViewModelTest : InvitationConfirmationViewModelTestFixture() {

    @Test
    fun `when fetchAdminType should set live data success`() {
        runBlocking {
            //given
            val adminTypeUiModel =
                AdminTypeUiModel(status = "2", "1234", isShopAdmin = true)
            onGetAdminTypeUseCase_thenReturn(adminTypeUiModel)

            //when
            viewModel.fetchAdminType()

            //then
            verifyGetAdminTypeUseCaseCalled()
            val actualResult = (viewModel.adminType.value as Success).data
            Assert.assertEquals(adminTypeUiModel, actualResult)
            Assert.assertEquals(adminTypeUiModel.shopID, actualResult.shopID)
            Assert.assertEquals(adminTypeUiModel.status, actualResult.status)
        }
    }

    @Test
    fun `when fetchAdminType should set live data error`() {
        runBlocking {
            //given
            val errorException = MessageErrorException()
            onGetAdminTypeUseCaseError_thenReturn(errorException)

            //when
            viewModel.fetchAdminType()

            //then
            verifyGetAdminTypeUseCaseCalled()
            val actualResult = (viewModel.adminType.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            Assert.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when updateUserProfile should set live data success`() {
        runBlocking {
            //given
            val updateUserProfileUiModel =
                UserProfileUpdateUiModel(isSuccess = true, "")
            onUpdateUserProfileUseUse_thenReturn(email, updateUserProfileUiModel)

            //when
            viewModel.updateUserProfile(email)

            //then
            verifyUpdateUserProfileUseUseCalled(email)
            val actualResult = (viewModel.updateUserProfile.value as Success).data
            Assert.assertEquals(updateUserProfileUiModel, actualResult)
            Assert.assertEquals(updateUserProfileUiModel.isSuccess, actualResult.isSuccess)
            Assert.assertEquals(updateUserProfileUiModel.errorMessage, actualResult.errorMessage)
        }
    }

    @Test
    fun `when updateUserProfile should set live data error`() {
        runBlocking {
            //given
            val errorException = MessageErrorException()
            onUpdateUserProfileUseCaseError_thenReturn(email, errorException)

            //when
            viewModel.updateUserProfile(email)

            //then
            verifyUpdateUserProfileUseUseCalled(email)
            val actualResult = (viewModel.updateUserProfile.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            Assert.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when fetchShopAdminInfo should set live data success`() {
        runBlocking {
            //given
            val shopId = 1234L
            val shopAdminInfoUiModel =
                ShopAdminInfoUiModel(
                    shopName = "Toko Jaya",
                    iconUrl = "https://images.tokopedia.net/img/kelolaToko.png",
                    shopManageId = "2"
                )
            onGetShopAdminInfoUseCase_thenReturn(shopId, shopAdminInfoUiModel)

            //when
            viewModel.fetchShopAdminInfo()

            //then
            verifyGetShopAdminInfoUseCaseCalled(shopId)
            val actualResult = (viewModel.shopAdminInfo.value as Success).data
            Assert.assertEquals(shopAdminInfoUiModel, actualResult)
            Assert.assertEquals(shopAdminInfoUiModel.shopName, actualResult.shopName)
            Assert.assertEquals(shopAdminInfoUiModel.iconUrl, actualResult.iconUrl)
            Assert.assertEquals(shopAdminInfoUiModel.shopManageId, actualResult.shopManageId)
        }
    }

    @Test
    fun `when fetchShopAdminInfo should set live data error`() {
        runBlocking {
            //given
            val shopId = 1234L
            val errorException = MessageErrorException()
            onGetShopAdminInfoUseCaseError_thenReturn(shopId, errorException)

            //when
            viewModel.fetchShopAdminInfo()

            //then
            verifyGetShopAdminInfoUseCaseCalled(shopId)
            val actualResult = (viewModel.shopAdminInfo.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            Assert.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when adminConfirmationReg should set live data success`() {
        runBlocking {
            //given
            val adminConfirmationRegUiModel =
                AdminConfirmationRegUiModel(
                    isSuccess = true,
                    message = "success",
                    acceptBecomeAdmin = acceptBecomeAdmin
                )
            invitationConfirmationParam.setShopManageId(manageID)
            onAdminConfirmationRegUseCase_thenReturn(
                shopId,
                userId,
                acceptBecomeAdmin,
                manageID,
                adminConfirmationRegUiModel
            )

            //when
            viewModel.adminConfirmationReg(acceptBecomeAdmin)

            //then
            verifyAdminConfirmationRegUseCaseCalled(
                shopId, userId, acceptBecomeAdmin, manageID
            )

            val actualResult = (viewModel.confirmationReg.value as Success).data
            Assert.assertEquals(adminConfirmationRegUiModel, actualResult)
            Assert.assertEquals(adminConfirmationRegUiModel.isSuccess, actualResult.isSuccess)
            Assert.assertEquals(adminConfirmationRegUiModel.message, actualResult.message)
            Assert.assertEquals(
                adminConfirmationRegUiModel.acceptBecomeAdmin,
                actualResult.acceptBecomeAdmin
            )
        }
    }

    @Test
    fun `when adminConfirmationReg should set live data error`() {
        runBlocking {
            //given
            val errorException = MessageErrorException()
            invitationConfirmationParam.setShopManageId(manageID)
            onAdminConfirmationRegUseCaseError_thenReturn(
                shopId,
                userId,
                acceptBecomeAdmin,
                manageID,
                errorException
            )

            //when
            viewModel.adminConfirmationReg(acceptBecomeAdmin)

            //then
            verifyAdminConfirmationRegUseCaseCalled(
                shopId, userId, acceptBecomeAdmin, manageID
            )
            val actualResult = (viewModel.confirmationReg.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            Assert.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when validateAdminEmail should set live data success`() =
        runBlocking {
            //given
            invitationConfirmationParam.setShopManageId(manageID)

            val validateAdminEmailUiModel = ValidateAdminEmailUiModel(
                isSuccess = true,
                message = "success",
                existsUser = true
            )

            val result = async {
                viewModel.validateEmail.first()
            }

            coEvery {
                validateAdminEmailUseCase.get().execute(shopId, email, manageID)
            } returns validateAdminEmailUiModel

            //when
            viewModel.validateAdminEmail(email)

            //then
            val actualResult = (result.await() as Success).data
            assert(validateAdminEmailUiModel == actualResult)
            Assert.assertEquals(validateAdminEmailUiModel.isSuccess, actualResult.isSuccess)
            Assert.assertEquals(validateAdminEmailUiModel.message, actualResult.message)
            Assert.assertEquals(
                validateAdminEmailUiModel.existsUser,
                actualResult.existsUser
            )

            verifyValidateAdminEmailUseCaseCalled(shopId, email, manageID)

            result.cancel()
        }

    @Test
    fun `when validateAdminEmail should set live data fail`() =
        runBlocking {
            //given
            val errorException = MessageErrorException()
            invitationConfirmationParam.setShopManageId(manageID)

            val result = async {
                viewModel.validateEmail.first()
            }

            coEvery {
                validateAdminEmailUseCase.get().execute(shopId, email, manageID)
            } throws errorException

            //when
            viewModel.validateAdminEmail(email)

            //then
            val actualResult = (result.await() as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            Assert.assertEquals(expectedResult, actualResult)

            verifyValidateAdminEmailUseCaseCalled(shopId, email, manageID)

            result.cancel()
        }
}