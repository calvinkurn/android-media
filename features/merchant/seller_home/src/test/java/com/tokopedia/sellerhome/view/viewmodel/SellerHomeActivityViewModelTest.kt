package com.tokopedia.sellerhome.view.viewmodel

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.NodeClient
import com.google.common.util.concurrent.ListenableFuture
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.device.info.DeviceInfo.await
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.usecase.GetNotificationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopStateInfoUseCase
import com.tokopedia.sellerhome.domain.usecase.SellerAdminUseCase
import com.tokopedia.sellerhome.view.model.NotificationSellerOrderStatusUiModel
import com.tokopedia.sellerhome.view.model.NotificationUiModel
import com.tokopedia.sellerhome.view.model.ShopInfoUiModel
import com.tokopedia.sellerhome.view.model.ShopStateInfoUiModel
import com.tokopedia.sessioncommon.data.admin.AdminData
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminDetailInformation
import com.tokopedia.sessioncommon.data.admin.AdminRoleType
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.ArgumentMatchers.anyBoolean

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
    lateinit var authorizeChatAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var authorizeOrderAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var getShopStateInfoUseCase: GetShopStateInfoUseCase

    @RelaxedMockK
    lateinit var capabilityClient: CapabilityClient

    @RelaxedMockK
    lateinit var nodeClient: NodeClient

    @RelaxedMockK
    lateinit var remoteActivityHelper: RemoteActivityHelper

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
        SellerHomeActivityViewModel(
            userSession,
            getNotificationUseCase,
            getShopInfoUseCase,
            sellerAdminUseCase,
            authorizeChatAccessUseCase,
            authorizeOrderAccessUseCase,
            getShopStateInfoUseCase,
            capabilityClient,
            nodeClient,
            remoteActivityHelper,
            coroutineTestRule.dispatchers
        )

    @Test
    fun `get notifications then returns success result`() {

        val notifications = NotificationUiModel(0, 0,
            NotificationSellerOrderStatusUiModel())

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } returns notifications
        everyCheckChatRolePermissionThenReturn(true)
        everyCheckOrderRolePermissionThenReturn(true)

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
    fun `get notifications but chat admin role is ineligible, should make the chat notif count 0`() = coroutineTestRule.runBlockingTest {
        val notifications = NotificationUiModel(5, 5,
                NotificationSellerOrderStatusUiModel(5, 5))

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } returns notifications
        userIsShopAdmin()
        everyShopIdIsExist()
        everyCheckChatRolePermissionThenReturn(false)
        everyCheckOrderRolePermissionThenReturn(true)

        val viewModel = createViewModel()
        viewModel.getNotifications()

        coVerify {
            getNotificationUseCase.executeOnBackground()
        }
        verifyCheckAdminOrderPermissionIsCalled()

        val expectedNotification = notifications.copy(chat = 0)
        assertNotificationDataEquals(Success(expectedNotification), viewModel)
    }

    @Test
    fun `get notifications but order admin role is ineligible, should make the chat notif count 0`() = coroutineTestRule.runBlockingTest {
        val notifications = NotificationUiModel(5, 5,
                NotificationSellerOrderStatusUiModel(5, 5))

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } returns notifications
        userIsShopAdmin()
        everyShopIdIsExist()
        everyCheckChatRolePermissionThenReturn(false)
        everyCheckOrderRolePermissionThenReturn(true)

        val viewModel = createViewModel()
        viewModel.getNotifications()

        coVerify {
            getNotificationUseCase.executeOnBackground()
        }
        verifyCheckAdminOrderPermissionIsCalled()

        val expectedNotification = notifications.copy(
                chat = 0
        )
        assertNotificationDataEquals(Success(expectedNotification), viewModel)
    }

    @Test
    fun `get notifications and both chat and order admin role is eligible, should not change the notif counts`() = coroutineTestRule.runBlockingTest {
        val notifications = NotificationUiModel(5, 5,
                NotificationSellerOrderStatusUiModel(5, 5))

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } returns notifications
        userIsShopAdmin()
        everyShopIdIsExist()
        everyCheckChatRolePermissionThenReturn(true)
        everyCheckOrderRolePermissionThenReturn(true)

        val viewModel = createViewModel()
        viewModel.getNotifications()

        coVerify {
            getNotificationUseCase.executeOnBackground()
        }

        verifyCheckAdminOrderPermissionIsCalled()
        assertNotificationDataEquals(Success(notifications), viewModel)
    }

    @Test
    fun `get notifications success but check role is fail, should still make the notifications result success`() = coroutineTestRule.runBlockingTest {
        val notifications = NotificationUiModel(5, 5,
                NotificationSellerOrderStatusUiModel(5, 5))
        val throwable = MessageErrorException()

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } returns notifications
        userIsShopAdmin()
        everyShopIdIsExist()
        everyCheckOrderRolePermissionThenThrow(throwable)

        val viewModel = createViewModel()
        viewModel.getNotifications()

        coVerify {
            getNotificationUseCase.executeOnBackground()
        }
        verifyCheckAdminOrderPermissionIsCalled()
        assert(viewModel.notifications.value is Success)
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
    fun `if user is shop owner and is not location admin then mark as user is eligible`() = coroutineTestRule.runBlockingTest {
        val isEligible = true
        val response = AdminDataResponse(
            data = AdminData(
                detail = AdminDetailInformation(
                    roleType = AdminRoleType(
                        isLocationAdmin = false,
                        isShopOwner = true
                    )
                )
            )
        )
        every {
            userSession.isShopOwner
        } returns false

        coEvery {
            sellerAdminUseCase.executeOnBackground()
        } returns response

        mViewModel.getAdminInfo()

        assert((mViewModel.isRoleEligible.value as? Success)?.data == isEligible)
    }

    @Test
    fun `if user is not shop owner and is not location admin then mark as user is eligible`() = coroutineTestRule.runBlockingTest {
        val isEligible = true
        val response = AdminDataResponse(
            data = AdminData(
                detail = AdminDetailInformation(
                    roleType = AdminRoleType(
                        isLocationAdmin = false,
                        isShopOwner = false
                    )
                )
            )
        )
        every {
            userSession.isShopOwner
        } returns false

        coEvery {
            sellerAdminUseCase.executeOnBackground()
        } returns response

        mViewModel.getAdminInfo()

        assert((mViewModel.isRoleEligible.value as? Success)?.data == isEligible)
    }

    @Test
    fun `if user is not shop owner and is location admin then mark as user is not eligible`() = coroutineTestRule.runBlockingTest {
        val isEligible = false
        val response = AdminDataResponse(
            data = AdminData(
                detail = AdminDetailInformation(
                    roleType = AdminRoleType(
                        isLocationAdmin = true,
                        isShopOwner = false
                    )
                )
            )
        )
        every {
            userSession.isShopOwner
        } returns false

        coEvery {
            sellerAdminUseCase.executeOnBackground()
        } returns response

        mViewModel.getAdminInfo()

        assert((mViewModel.isRoleEligible.value as? Success)?.data == isEligible)
    }

    @Test
    fun `if user is shop owner and is location admin then mark as user is eligible`() = coroutineTestRule.runBlockingTest {
        val isEligible = true
        val response = AdminDataResponse(
            data = AdminData(
                detail = AdminDetailInformation(
                    roleType = AdminRoleType(
                        isLocationAdmin = true,
                        isShopOwner = true
                    )
                )
            )
        )
        every {
            userSession.isShopOwner
        } returns false

        coEvery {
            sellerAdminUseCase.executeOnBackground()
        } returns response

        mViewModel.getAdminInfo()

        assert((mViewModel.isRoleEligible.value as? Success)?.data == isEligible)
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
        userIsShopAdmin()

        mViewModel.getAdminInfo()

        verifyGetAdminTypeIsCalled()

        assert(mViewModel.isRoleEligible.value is Success)
    }

    @Test
    fun `get admin info then return failed result`() = coroutineTestRule.runBlockingTest {
        val throwable = MessageErrorException("")

        everyGetAdminTypeThenThrow(throwable)
        userIsShopAdmin()

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
        userIsShopAdmin()

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
        userIsShopAdmin()

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
        userIsShopAdmin()

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
        userIsShopAdmin()

        mViewModel.getAdminInfo()

        verifyGetAdminTypeIsCalled()
        verifyUserSessionValueIsUpdated(roleType, isMultiLocationShop)
    }

    @Test
    fun `when get admin type is fail, will not update user session value`() {
        everyGetAdminTypeThenThrow(MessageErrorException())
        userIsShopAdmin()

        mViewModel.getAdminInfo()

        verifyGetAdminTypeIsCalled()
        verifyUserSessionValueIsNotUpdated()
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

    @Test
    fun `when calling checkIfWearHasCompanionApp and both node is not matched, then shouldAskInstallCompanionApp value should be true`() = runBlocking {
        mockkObject(DeviceInfo)
        val mockTaskListNode = mockk<Task<List<Node>>>()
        every {
            nodeClient.connectedNodes
        } returns mockTaskListNode
        val mockConnectedNode = mockk<Node>()
        coEvery {
            mockConnectedNode.isNearby
        } returns true
        coEvery {
            mockTaskListNode.await()
        } returns listOf(mockConnectedNode)
        val mockTaskCapabilityInfo = mockk<Task<CapabilityInfo>>()
        every {
            capabilityClient.getCapability(any(), any())
        } returns mockTaskCapabilityInfo
        val mockCapabilityInfoNode = mockk<Node>()
        coEvery {
            mockCapabilityInfoNode.isNearby
        } returns true
        val mockSetNode = HashSet<Node>(3).apply {
            add(mockCapabilityInfoNode)
        }
        val mockCapabilityInfo = mockk<CapabilityInfo>()
        coEvery {
            mockTaskCapabilityInfo.await()
        } returns mockCapabilityInfo
        every { mockCapabilityInfo.nodes } returns mockSetNode
        mViewModel.checkIfWearHasCompanionApp()
        assert(mViewModel.shouldAskInstallCompanionApp.value == true)
    }

    @Test
    fun `when calling checkIfWearHasCompanionApp and all node on CapabilityInfo is false, then shouldAskInstallCompanionApp value should be true`() = runBlocking {
        mockkObject(DeviceInfo)
        val mockTaskListNode = mockk<Task<List<Node>>>()
        every {
            nodeClient.connectedNodes
        } returns mockTaskListNode
        val mockConnectedNode = mockk<Node>()
        coEvery {
            mockConnectedNode.isNearby
        } returns true
        coEvery {
            mockTaskListNode.await()
        } returns listOf(mockConnectedNode)
        val mockTaskCapabilityInfo = mockk<Task<CapabilityInfo>>()
        every {
            capabilityClient.getCapability(any(), any())
        } returns mockTaskCapabilityInfo
        val mockCapabilityInfoNode = mockk<Node>()
        coEvery {
            mockCapabilityInfoNode.isNearby
        } returns false
        val mockSetNode = HashSet<Node>(3).apply {
            add(mockCapabilityInfoNode)
        }
        val mockCapabilityInfo = mockk<CapabilityInfo>()
        coEvery {
            mockTaskCapabilityInfo.await()
        } returns mockCapabilityInfo
        every { mockCapabilityInfo.nodes } returns mockSetNode
        mViewModel.checkIfWearHasCompanionApp()
        assert(mViewModel.shouldAskInstallCompanionApp.value == true)
    }

    @Test
    fun `when calling checkIfWearHasCompanionApp and both node is matched, then shouldAskInstallCompanionApp value should be false`() = runBlocking {
        mockkObject(DeviceInfo)
        val mockTaskListNode = mockk<Task<List<Node>>>()
        every {
            nodeClient.connectedNodes
        } returns mockTaskListNode
        val mockConnectedNode = mockk<Node>()
        coEvery {
            mockConnectedNode.isNearby
        } returns true
        coEvery {
            mockTaskListNode.await()
        } returns listOf(mockConnectedNode)
        val mockTaskCapabilityInfo = mockk<Task<CapabilityInfo>>()
        every {
            capabilityClient.getCapability(any(), any())
        } returns mockTaskCapabilityInfo
        val mockSetNode = HashSet<Node>(3).apply {
            add(mockConnectedNode)
        }
        val mockCapabilityInfo = mockk<CapabilityInfo>()
        coEvery {
            mockTaskCapabilityInfo.await()
        } returns mockCapabilityInfo
        every { mockCapabilityInfo.nodes } returns mockSetNode
        mViewModel.checkIfWearHasCompanionApp()
        assert(mViewModel.shouldAskInstallCompanionApp.value == false)
    }

    @Test
    fun `when calling checkIfWearHasCompanionApp and there are no CapabilityInfo node, then shouldAskInstallCompanionApp value should be true`() = runBlocking {
        mockkObject(DeviceInfo)
        val mockTaskListNode = mockk<Task<List<Node>>>()
        every {
            nodeClient.connectedNodes
        } returns mockTaskListNode
        val mockConnectedNode = mockk<Node>()
        coEvery {
            mockConnectedNode.isNearby
        } returns true
        coEvery {
            mockTaskListNode.await()
        } returns listOf(mockConnectedNode)
        val mockTaskCapabilityInfo = mockk<Task<CapabilityInfo>>()
        every {
            capabilityClient.getCapability(any(), any())
        } returns mockTaskCapabilityInfo
        coEvery {
            mockTaskCapabilityInfo.await()
        } returns null
        mViewModel.checkIfWearHasCompanionApp()
        assert(mViewModel.shouldAskInstallCompanionApp.value == true)
    }

    @Test
    fun `when calling checkIfWearHasCompanionApp and nodeClient connectedNodes return null, then shouldAskInstallCompanionApp value should be null`() = runBlocking {
        mockkObject(DeviceInfo)
        val mockTaskListNode = mockk<Task<List<Node>>>()
        every {
            nodeClient.connectedNodes
        } returns mockTaskListNode
        coEvery {
            mockTaskListNode.await()
        } returns null
        mViewModel.checkIfWearHasCompanionApp()
        assert(mViewModel.shouldAskInstallCompanionApp.value == null)
    }

    @Test
    fun `when calling checkIfWearHasCompanionApp but capabilityInfo await throw CancellationException, then shouldAskInstallCompanionApp value should be null`() = runBlocking {
        mockkObject(DeviceInfo)
        val mockTaskListNode = mockk<Task<List<Node>>>()
        every {
            nodeClient.connectedNodes
        } returns mockTaskListNode
        val mockConnectedNode = mockk<Node>()
        coEvery {
            mockConnectedNode.isNearby
        } returns true
        coEvery {
            mockTaskListNode.await()
        } returns listOf(mockConnectedNode)
        val mockTaskCapabilityInfo = mockk<Task<CapabilityInfo>>()
        every {
            capabilityClient.getCapability(any(), any())
        } returns mockTaskCapabilityInfo
        val mockCapabilityInfoNode = mockk<Node>()
        coEvery {
            mockCapabilityInfoNode.isNearby
        } returns true
        coEvery {
            mockTaskCapabilityInfo.await()
        } throws CancellationException()
        mViewModel.checkIfWearHasCompanionApp()
        assert(mViewModel.shouldAskInstallCompanionApp.value == null)
    }

    @Test
    fun `when calling checkIfWearHasCompanionApp but capabilityInfo await throw Exception, then shouldAskInstallCompanionApp value should be null`() = runBlocking {
        mockkObject(DeviceInfo)
        val mockTaskListNode = mockk<Task<List<Node>>>()
        every {
            nodeClient.connectedNodes
        } returns mockTaskListNode
        val mockConnectedNode = mockk<Node>()
        coEvery {
            mockConnectedNode.isNearby
        } returns true
        coEvery {
            mockTaskListNode.await()
        } returns listOf(mockConnectedNode)
        val mockTaskCapabilityInfo = mockk<Task<CapabilityInfo>>()
        every {
            capabilityClient.getCapability(any(), any())
        } returns mockTaskCapabilityInfo
        val mockCapabilityInfoNode = mockk<Node>()
        coEvery {
            mockCapabilityInfoNode.isNearby
        } returns true
        coEvery {
            mockTaskCapabilityInfo.await()
        } throws Exception()
        mViewModel.checkIfWearHasCompanionApp()
        assert(mViewModel.shouldAskInstallCompanionApp.value == null)
    }

    @Test
    fun `when calling checkIfWearHasCompanionApp but connectedNodes nearby is false, then shouldAskInstallCompanionApp value should be null`() = runBlocking {
        mockkObject(DeviceInfo)
        val mockTaskListNode = mockk<Task<List<Node>>>()
        every {
            nodeClient.connectedNodes
        } returns mockTaskListNode
        val mockConnectedNode = mockk<Node>()
        coEvery {
            mockConnectedNode.isNearby
        } returns false
        coEvery {
            mockTaskListNode.await()
        } returns listOf(mockConnectedNode)
        mViewModel.checkIfWearHasCompanionApp()
        assert(mViewModel.shouldAskInstallCompanionApp.value == null)
    }

    @Test
    fun `when calling launchMarket success, then remoteActivityHelper should call startRemoteActivity`() = runBlocking {
        val mockIntent = mockk<Intent>()
        mockkStatic("kotlinx.coroutines.guava.ListenableFutureKt")
        coEvery {
            any<ListenableFuture<Void>>().await()
        } returns mockk()
        mViewModel.launchMarket(mockIntent)
        verify {
            remoteActivityHelper.startRemoteActivity(any())
        }
    }

    @Test
    fun `when calling launchMarket throw exception, then remoteActivityHelper should call startRemoteActivity`()= runBlocking  {
        every {
            remoteActivityHelper.startRemoteActivity(any())
        }throws Exception()
        val mockIntent = mockk<Intent>()
        mViewModel.launchMarket(mockIntent)
        verify {
            remoteActivityHelper.startRemoteActivity(any())
        }
    }

    @Test
    fun `when calling launchMarket throw CancellationException, then remoteActivityHelper should call startRemoteActivity`()= runBlocking {
        every {
            remoteActivityHelper.startRemoteActivity(any())
        }throws CancellationException()
        val mockIntent = mockk<Intent>()
        mViewModel.launchMarket(mockIntent)
        verify {
            remoteActivityHelper.startRemoteActivity(any())
        }
    }

    @Test
    fun `when get shop state info should return success result`() {
        coroutineTestRule.runBlockingTest {
            val shopId = "123"
            val dataKeys = "shopStateChanged"
            val pageSource = "seller-home"

            val response = ShopStateInfoUiModel()

            coEvery {
                userSession.shopId
            } returns shopId

            coEvery {
                getShopStateInfoUseCase.executeInBackground(any(), any(), any())
            } returns response

            mViewModel.getShopStateInfo()

            coVerify {
                getShopStateInfoUseCase.executeInBackground(shopId, dataKeys, pageSource)
            }

            val expected = Success(response)
            mViewModel.shopStateInfo.verifySuccessEquals(expected)
        }
    }

    @Test
    fun `when get shop state info should return error result`() {
        coroutineTestRule.runBlockingTest {
            val shopId = "123"
            val dataKeys = "shopStateChanged"
            val pageSource = "seller-home"

            val throwable = RuntimeException()

            coEvery {
                userSession.shopId
            } returns shopId

            coEvery {
                getShopStateInfoUseCase.executeInBackground(any(), any(), any())
            } throws throwable

            mViewModel.getShopStateInfo()

            coVerify {
                getShopStateInfoUseCase.executeInBackground(shopId, dataKeys, pageSource)
            }

            val expected = Fail(throwable)
            mViewModel.shopStateInfo.verifyErrorEquals(expected)
        }
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

    private fun everyCheckChatRolePermissionThenReturn(isRoleEligible: Boolean) {
        coEvery {
            authorizeChatAccessUseCase.execute(any())
        } returns isRoleEligible
    }

    private fun everyCheckOrderRolePermissionThenReturn(isRoleEligible: Boolean) {
        coEvery {
            authorizeOrderAccessUseCase.execute(any())
        } returns isRoleEligible
    }

    private fun everyCheckOrderRolePermissionThenThrow(exception: Exception) {
        coEvery {
            authorizeOrderAccessUseCase.execute(any())
        } throws exception
    }

    private fun userIsShopAdmin() {
        coEvery {
            userSession.isShopOwner
        } returns false
        coEvery {
            userSession.isShopAdmin
        } returns true
        coEvery {
            userSession.isLocationAdmin
        } returns false
    }

    private fun everyShopIdIsExist() {
        coEvery {
            userSession.shopId
        } returns "1"
    }

    private fun verifyGetAdminTypeIsCalled() {
        coVerify {
            sellerAdminUseCase.executeOnBackground()
        }
    }

    private fun verifyCheckAdminOrderPermissionIsCalled() {
        coVerify {
            authorizeChatAccessUseCase.execute(any())
        }
    }

    private fun verifyGetAdminTypeIsNotCalled() {
        coVerify(exactly = 0) {
            sellerAdminUseCase.executeOnBackground()
        }
    }

    private fun verifyCheckAdminOrderPermissionIsNotCalled() {
        coVerify(exactly = 0) {
            authorizeChatAccessUseCase.execute(any())
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

    private fun assertNotificationDataEquals(expectedNotification: Result<NotificationUiModel>, viewModel: SellerHomeActivityViewModel) {
        assertEquals(expectedNotification, viewModel.notifications.value)
    }
}
