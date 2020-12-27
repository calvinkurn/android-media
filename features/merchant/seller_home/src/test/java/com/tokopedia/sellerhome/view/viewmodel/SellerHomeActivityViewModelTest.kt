package com.tokopedia.sellerhome.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.usecase.GetNotificationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoUseCase
import com.tokopedia.sellerhome.domain.usecase.SellerAdminUseCase
import com.tokopedia.sellerhome.view.model.NotificationSellerOrderStatusUiModel
import com.tokopedia.sellerhome.view.model.NotificationUiModel
import com.tokopedia.sellerhome.view.model.ShopInfoUiModel
import com.tokopedia.sessioncommon.data.admin.AdminData
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminDetailInformation
import com.tokopedia.sessioncommon.data.admin.AdminRoleType
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt

/**
 * Created By @ilhamsuaib on 24/03/20
 */

@ExperimentalCoroutinesApi
class SellerHomeActivityViewModelTest {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getNotificationUseCase: GetNotificationUseCase

    @RelaxedMockK
    lateinit var getShopInfoUseCase: GetShopInfoUseCase

    @RelaxedMockK
    lateinit var sellerAdminUseCase: SellerAdminUseCase

    @RelaxedMockK
    lateinit var authorizeAccessUseCase: AuthorizeAccessUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var mViewModel: SellerHomeActivityViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mViewModel = createViewModel()
    }

    private fun createViewModel() =
        SellerHomeActivityViewModel(userSession, getNotificationUseCase, getShopInfoUseCase, sellerAdminUseCase, authorizeAccessUseCase, coroutineTestRule.dispatchers)

    @Test
    fun `get notifications then returns success result`() {

        val notifications = NotificationUiModel(0, 0,
            NotificationSellerOrderStatusUiModel())

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } returns notifications

        val viewModel = createViewModel()
        runBlocking {
            viewModel.getNotifications()
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            coVerify {
                getNotificationUseCase.executeOnBackground()
            }
            assertEquals(Success(notifications), viewModel.notifications.value)
        }
    }

    @Test
    fun `get notifications then returns failed result`() = runBlocking {

        val throwable = MessageErrorException()

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } throws throwable

        val viewModel = createViewModel()
        viewModel.getNotifications()

        coVerify {
            getNotificationUseCase.executeOnBackground()
        }

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        assert(viewModel.notifications.value is Fail)
    }

    @Test
    fun `get shop info then returns success result`() {
        val userId = "123456"
        val shopInfo = ShopInfoUiModel()

        getShopInfoUseCase.params = GetShopInfoUseCase.getRequestParam(userId)

        every {
            userSession.userId
        } returns userId

        coEvery {
            getShopInfoUseCase.executeOnBackground()
        } returns shopInfo

        val viewModel = createViewModel()
        runBlocking {
            viewModel.getShopInfo()
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            coVerify {
                userSession.userId
            }
            coVerify {
                getShopInfoUseCase.executeOnBackground()
            }
        }

        assertEquals(Success(shopInfo), viewModel.shopInfo.value)
    }

    @Test
    fun `get shop info then returns failed result`() = coroutineTestRule.runBlockingTest {
        val userId = "123456"
        val throwable = MessageErrorException()

        getShopInfoUseCase.params = GetShopInfoUseCase.getRequestParam(userId)

        every {
            userSession.userId
        } returns userId

        coEvery {
            getShopInfoUseCase.executeOnBackground()
        } throws throwable

        val viewModel = createViewModel()
        viewModel.getShopInfo()

        verify {
            userSession.userId
        }

        coVerify {
            getShopInfoUseCase.executeOnBackground()
        }

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        assert(viewModel.shopInfo.value is Fail)
    }

    @Test
    fun `if user is shop owner, user is eligible`() = coroutineTestRule.runBlockingTest {
        coEvery {
            userSession.isShopOwner
        } returns true

        mViewModel.getAdminInfo()

        assert((mViewModel.isRoleEligible.value as? Success)?.data == true)
    }

    @Test
    fun `if user is not shop owner but is location admin, user is not eligible`() = coroutineTestRule.runBlockingTest {
        coEvery {
            userSession.isShopOwner
        } returns false
        coEvery {
            userSession.isLocationAdmin
        } returns true

        mViewModel.getAdminInfo()

        assert((mViewModel.isRoleEligible.value as? Success)?.data == false)
    }

    @Test
    fun `if user is shop owner, we dont need to check for role permission`() = coroutineTestRule.runBlockingTest {
        coEvery {
            userSession.isShopOwner
        } returns true

        mViewModel.getAdminInfo()

        verifyGetAdminTypeIsNotCalled()
        verifyCheckAdminOrderPermissionIsNotCalled()
    }

    @Test
    fun `if user is shop owner but is location admin, we dont need to check for role permission`() = coroutineTestRule.runBlockingTest {
        coEvery {
            userSession.isShopOwner
        } returns true
        coEvery {
            userSession.isLocationAdmin
        } returns true

        mViewModel.getAdminInfo()

        verifyGetAdminTypeIsNotCalled()
        verifyCheckAdminOrderPermissionIsNotCalled()
    }

    @Test
    fun `get admin info then success return isEligible`() = coroutineTestRule.runBlockingTest {
        val adminData = AdminDataResponse()

        everyGetAdminTypeThenReturn(adminData)
        everyCheckOrderRolePermissionThenReturn(true)
        userIsNotShopOwnerOrLocationAdmin()

        mViewModel.getAdminInfo()

        verifyGetAdminTypeIsCalled()

        assert(mViewModel.isRoleEligible.value is Success)
    }

    @Test
    fun `get admin info then return failed result`() = coroutineTestRule.runBlockingTest {
        val throwable = MessageErrorException("")

        everyGetAdminTypeThenThrow(throwable)
        everyCheckOrderRolePermissionThenReturn(true)
        userIsNotShopOwnerOrLocationAdmin()

        mViewModel.getAdminInfo()

        verifyGetAdminTypeIsCalled()

        assert(mViewModel.isRoleEligible.value is Fail)
    }

    @Test
    fun `when admin info is shop admin, get admin info will return isEligible true result`() = coroutineTestRule.runBlockingTest {
        val isShopAdmin = true
        val adminData = AdminDataResponse(
                data = AdminData(
                        detail = AdminDetailInformation(
                                roleType = AdminRoleType(
                                        isShopAdmin = isShopAdmin
                                )
                        )
                )
        )

        everyGetAdminTypeThenReturn(adminData)
        everyCheckOrderRolePermissionThenReturn(true)
        userIsNotShopOwnerOrLocationAdmin()

        mViewModel.getAdminInfo()

        verifyGetAdminTypeIsCalled()

        assert((mViewModel.isRoleEligible.value as? Success)?.data == true)
    }

    @Test
    fun `when admin info is not shop admin but is location admin, get admin info will return isEligible false result`() = coroutineTestRule.runBlockingTest {
        val isShopAdmin = false
        val isLocationAdmin = true
        val adminData = AdminDataResponse(
                data = AdminData(
                        detail = AdminDetailInformation(
                                roleType = AdminRoleType(
                                        isShopAdmin = isShopAdmin,
                                        isLocationAdmin = isLocationAdmin
                                )
                        )
                )
        )

        everyGetAdminTypeThenReturn(adminData)
        everyCheckOrderRolePermissionThenReturn(true)
        userIsNotShopOwnerOrLocationAdmin()

        mViewModel.getAdminInfo()

        verifyGetAdminTypeIsCalled()

        assert((mViewModel.isRoleEligible.value as? Success)?.data == false)
    }

    @Test
    fun `when admin info is not shop admin and not is location admin, get admin info will return isEligible true result`() = coroutineTestRule.runBlockingTest {
        val isShopAdmin = false
        val isLocationAdmin = false
        val adminData = AdminDataResponse(
                data = AdminData(
                        detail = AdminDetailInformation(
                                roleType = AdminRoleType(
                                        isShopAdmin = isShopAdmin,
                                        isLocationAdmin = isLocationAdmin
                                )
                        )
                )
        )

        everyGetAdminTypeThenReturn(adminData)
        everyCheckOrderRolePermissionThenReturn(true)
        userIsNotShopOwnerOrLocationAdmin()

        mViewModel.getAdminInfo()

        verifyGetAdminTypeIsCalled()

        assert((mViewModel.isRoleEligible.value as? Success)?.data == true)
    }

    @Test
    fun `when get admin type is success, will update user session values`() {
        val isShopOwner = false
        val isShopAdmin = true
        val isLocationAdmin = false
        val roleType = AdminRoleType(isShopAdmin, isLocationAdmin, isShopOwner)
        val isMultiLocationShop = true
        val adminData = AdminDataResponse(
                isMultiLocationShop = isMultiLocationShop,
                data = AdminData(
                        detail = AdminDetailInformation(
                                roleType = roleType
                        )
                )
        )

        everyGetAdminTypeThenReturn(adminData)
        everyCheckOrderRolePermissionThenReturn(true)
        userIsNotShopOwnerOrLocationAdmin()

        mViewModel.getAdminInfo()

        verifyGetAdminTypeIsCalled()
        verifyUserSessionValueIsUpdated(roleType, isMultiLocationShop)
    }

    @Test
    fun `when get admin type is fail, will not update user session value`() {
        everyGetAdminTypeThenThrow(MessageErrorException())
        everyCheckOrderRolePermissionThenReturn(true)
        userIsNotShopOwnerOrLocationAdmin()

        mViewModel.getAdminInfo()

        verifyGetAdminTypeIsCalled()
        verifyUserSessionValueIsNotUpdated()
    }

    @Test
    fun `success check permission will change order eligibility value`() {
        val isRoleEligible = true
        everyGetAdminTypeThenReturn(AdminDataResponse())
        everyCheckOrderRolePermissionThenReturn(isRoleEligible)
        userIsNotShopOwnerOrLocationAdmin()

        mViewModel.getAdminInfo()

        verifyCheckAdminOrderPermissionIsCalled()

        assert(mViewModel.isOrderShopAdmin.value == isRoleEligible)
    }


    @Test
    fun `fail check permission will not change order eligibility value`() {
        everyGetAdminTypeThenReturn(AdminDataResponse())
        everyCheckOrderRolePermissionThenThrow(MessageErrorException())
        userIsNotShopOwnerOrLocationAdmin()

        val isRoleEligible = mViewModel.isOrderShopAdmin.value

        mViewModel.getAdminInfo()

        verifyCheckAdminOrderPermissionIsCalled()

        assert(mViewModel.isOrderShopAdmin.value == isRoleEligible)
    }

    @Test
    fun `execute launch in custom base view model with custom onError block without custom context`() = runBlocking {
        val customOnErrorViewModel = CustomOnErrorViewModel(Job())
        customOnErrorViewModel.noCustomContext()
        customOnErrorViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        assert(customOnErrorViewModel.mockLiveData.value is Fail)
    }

    @Test
    fun `execute launch in custom base view model with custom onError block with custom context`() = runBlocking {
        val job = Job()
        val customOnErrorViewModel = CustomOnErrorViewModel(job)
        customOnErrorViewModel.withCustomContext()
        job.children.forEach { it.join() }
        assert(customOnErrorViewModel.mockLiveData.value is Fail)
    }

    inner class CustomOnErrorViewModel(private val job: Job) : CustomBaseViewModel(coroutineTestRule.dispatchers) {

        private val _mockLiveData = MutableLiveData<Result<Any>>()
        val mockLiveData: LiveData<Result<Any>>
            get() = _mockLiveData

        fun noCustomContext() = executeCall(_mockLiveData, onError = {
            _mockLiveData.value = Fail(it)
        }, block = {
            getResult()
        })

        fun withCustomContext() = executeCall(_mockLiveData, job, onError = {
            _mockLiveData.value = Fail(it)
        }, block = {
            getResult()
        })

        private fun getResult(): Any {
            throw RuntimeException()
        }
    }

    private fun everyGetAdminTypeThenReturn(adminDataResponse: AdminDataResponse) {
        coEvery {
            sellerAdminUseCase.executeOnBackground()
        } returns adminDataResponse
    }

    private fun everyGetAdminTypeThenThrow(exception: Exception) {
        coEvery {
            sellerAdminUseCase.executeOnBackground()
        } throws exception
    }

    private fun everyCheckOrderRolePermissionThenReturn(isRoleEligible: Boolean) {
        coEvery {
            authorizeAccessUseCase.execute(any())
        } returns isRoleEligible
    }

    private fun everyCheckOrderRolePermissionThenThrow(exception: Exception) {
        coEvery {
            authorizeAccessUseCase.execute(any())
        } throws exception
    }

    private fun userIsNotShopOwnerOrLocationAdmin() {
        coEvery {
            userSession.isShopOwner
        } returns false
        coEvery {
            userSession.isLocationAdmin
        } returns false
    }

    private fun verifyGetAdminTypeIsCalled() {
        coVerify {
            sellerAdminUseCase.executeOnBackground()
        }
    }

    private fun verifyCheckAdminOrderPermissionIsCalled() {
        coVerify {
            authorizeAccessUseCase.execute(any())
        }
    }

    private fun verifyGetAdminTypeIsNotCalled() {
        coVerify(exactly = 0) {
            sellerAdminUseCase.executeOnBackground()
        }
    }

    private fun verifyCheckAdminOrderPermissionIsNotCalled() {
        coVerify(exactly = 0) {
            authorizeAccessUseCase.execute(any())
        }
    }

    private fun verifyUserSessionValueIsUpdated(roleType: AdminRoleType, isMultiLocationShop: Boolean) {
        coVerify {
            with(userSession) {
                setIsShopOwner(roleType.isShopOwner)
                setIsLocationAdmin(roleType.isLocationAdmin)
                setIsShopAdmin(roleType.isShopAdmin)
                setIsMultiLocationShop(isMultiLocationShop)
            }
        }
    }

    private fun verifyUserSessionValueIsNotUpdated() {
        coVerify(exactly = 0) {
            with(userSession) {
                setIsShopOwner(anyBoolean())
                setIsLocationAdmin(anyBoolean())
                setIsShopAdmin(anyBoolean())
                setIsMultiLocationShop(anyBoolean())
            }
        }
    }
}