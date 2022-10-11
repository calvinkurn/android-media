package com.tokopedia.shopadmin.feature.invitationconfirmation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shopadmin.common.domain.usecase.GetAdminTypeUseCaseCase
import com.tokopedia.shopadmin.common.presentation.uimodel.AdminTypeUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param.InvitationConfirmationParam
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param.InvitationConfirmationParamImpl
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.AdminConfirmationRegUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.GetShopAdminInfoUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.UpdateUserProfileUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.ValidateAdminEmailUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.AdminConfirmationRegUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ShopAdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.UserProfileUpdateUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.viewmodel.InvitationConfirmationViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.After
import org.junit.Before
import org.junit.Rule

@FlowPreview
@ExperimentalCoroutinesApi
abstract class InvitationConfirmationViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getAdminTypeUseCaseCase: Lazy<GetAdminTypeUseCaseCase>

    @RelaxedMockK
    lateinit var getShopAdminInfoUseCase: Lazy<GetShopAdminInfoUseCase>

    @RelaxedMockK
    lateinit var adminConfirmationRegUseCase: Lazy<AdminConfirmationRegUseCase>

    @RelaxedMockK
    lateinit var validateAdminEmailUseCase: Lazy<ValidateAdminEmailUseCase>

    @RelaxedMockK
    lateinit var updateUserProfileUseCase: Lazy<UpdateUserProfileUseCase>

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    lateinit var invitationConfirmationParam: InvitationConfirmationParam

    protected lateinit var viewModel: InvitationConfirmationViewModel

    protected val shopId = "1234"
    protected val userId = "1234"
    protected val email = "shopadmin@tokopedia.com"
    protected val acceptBecomeAdmin = true
    protected val manageID = "567456"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        invitationConfirmationParam = InvitationConfirmationParamImpl()
        every { userSession.shopId } returns shopId
        every { userSession.userId } returns userId
        viewModel = InvitationConfirmationViewModel(
            CoroutineTestDispatchersProvider,
            userSession,
            getAdminTypeUseCaseCase,
            getShopAdminInfoUseCase,
            adminConfirmationRegUseCase,
            validateAdminEmailUseCase,
            updateUserProfileUseCase,
            invitationConfirmationParam
        )
    }

    @After
    fun finish() {
        unmockkAll()
    }

    protected fun onGetAdminTypeUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getAdminTypeUseCaseCase.get().execute() } throws exception
    }

    protected fun verifyGetAdminTypeUseCaseCalled() {
        coVerify { getAdminTypeUseCaseCase.get().execute() }
    }

    protected fun onGetAdminTypeUseCase_thenReturn(
        adminTypeUiModel: AdminTypeUiModel
    ) {
        coEvery { getAdminTypeUseCaseCase.get().execute() } returns adminTypeUiModel
    }

    protected fun onGetShopAdminInfoUseCaseError_thenReturn(shopId: Long, exception: Throwable) {
        coEvery { getShopAdminInfoUseCase.get().execute(shopId) } throws exception
    }

    protected fun verifyGetShopAdminInfoUseCaseCalled(shopId: Long) {
        coVerify { getShopAdminInfoUseCase.get().execute(shopId) }
    }

    protected fun onGetShopAdminInfoUseCase_thenReturn(
        shopId: Long,
        shopAdminInfoUiModel: ShopAdminInfoUiModel
    ) {
        coEvery { getShopAdminInfoUseCase.get().execute(shopId) } returns shopAdminInfoUiModel
    }

    protected fun onAdminConfirmationRegUseCaseError_thenReturn(
        shopId: String,
        userId: String,
        acceptBecomeAdmin: Boolean,
        manageID: String,
        exception: Throwable
    ) {
        coEvery {
            adminConfirmationRegUseCase.get()
                .execute(shopId, userId, acceptBecomeAdmin, manageID)
        } throws exception
    }

    protected fun verifyAdminConfirmationRegUseCaseCalled(
        shopId: String,
        userId: String,
        acceptBecomeAdmin: Boolean,
        manageID: String
    ) {
        coVerify {
            adminConfirmationRegUseCase.get()
                .execute(shopId, userId, acceptBecomeAdmin, manageID)
        }
    }

    protected fun onAdminConfirmationRegUseCase_thenReturn(
        shopId: String,
        userId: String,
        acceptBecomeAdmin: Boolean,
        manageID: String,
        adminConfirmationRegUiModel: AdminConfirmationRegUiModel
    ) {
        coEvery {
            adminConfirmationRegUseCase.get()
                .execute(shopId, userId, acceptBecomeAdmin, manageID)
        } returns adminConfirmationRegUiModel
    }

    protected fun verifyValidateAdminEmailUseCaseCalled(
        shopId: String,
        email: String,
        manageID: String
    ) {
        coVerify {
            validateAdminEmailUseCase.get()
                .execute(shopId, email, manageID)
        }
    }

    protected fun onUpdateUserProfileUseCaseError_thenReturn(email: String, exception: Throwable) {
        coEvery { updateUserProfileUseCase.get().execute(email) } throws exception
    }

    protected fun verifyUpdateUserProfileUseUseCalled(email: String) {
        coVerify { updateUserProfileUseCase.get().execute(email) }
    }

    protected fun onUpdateUserProfileUseUse_thenReturn(
        email: String,
        userProfileUpdateUiModel: UserProfileUpdateUiModel
    ) {
        coEvery { updateUserProfileUseCase.get().execute(email) } returns userProfileUpdateUiModel
    }

}