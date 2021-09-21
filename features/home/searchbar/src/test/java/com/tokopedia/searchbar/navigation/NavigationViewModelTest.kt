package com.tokopedia.searchbar.navigation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.viewModel.NavigationViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class NavigationViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockTotalGlobalNotif = 11
    private val mockTotalNewInbox = 12
    private val mockTotalInbox = 13
    private val mockTotalNotif = 14
    private val mockTotalCart = 15

    @Test
    fun `Test navigationViewModel with supported icon when getAllNotification success then livedata value updated`() {
        val mockGetNotificationUseCase = mockk<GetNotificationUseCase>()

        coEvery {
            mockGetNotificationUseCase.executeOnBackground()
        } returns TopNavNotificationModel(
                totalGlobalNavNotif = mockTotalGlobalNotif,
                totalNewInbox = mockTotalNewInbox,
                totalInbox = mockTotalInbox,
                totalNotif = mockTotalNotif,
                totalCart = mockTotalCart
        )

        val navigationViewModel = NavigationViewModel(
                dispatcher = CoroutineTestDispatchersProvider,
                userSession = mockk(relaxed = true),
                getNotificationUseCase = mockGetNotificationUseCase
        )
        val mockIconBuilderWithSupportedIcon = IconBuilder().addIcon(IconList.ID_CART){}.build()

        navigationViewModel.setRegisteredIconList(mockIconBuilderWithSupportedIcon)
        navigationViewModel.getNotification()

        val topNavigationValue = navigationViewModel.navNotificationLiveData.value!!
        Assert.assertEquals(mockTotalNotif, topNavigationValue.totalNotif)
        Assert.assertEquals(mockTotalCart, topNavigationValue.totalCart)
        Assert.assertEquals(mockTotalGlobalNotif, topNavigationValue.totalGlobalNavNotif)
        Assert.assertEquals(mockTotalInbox, topNavigationValue.totalInbox)
        Assert.assertEquals(mockTotalNewInbox, topNavigationValue.totalNewInbox)
    }

    @Test
    fun `Test navigationViewModel with supported icon when getAllNotification failed then livedata value is empty`() {
        val mockGetNotificationUseCase = mockk<GetNotificationUseCase>()

        coEvery {
            mockGetNotificationUseCase.executeOnBackground()
        } throws Exception("Mocking error")

        val navigationViewModel = NavigationViewModel(
                dispatcher = CoroutineTestDispatchersProvider,
                userSession = mockk(relaxed = true),
                getNotificationUseCase = mockGetNotificationUseCase
        )
        val mockIconBuilderWithSupportedIcon = IconBuilder().addIcon(IconList.ID_CART){}.build()

        navigationViewModel.setRegisteredIconList(mockIconBuilderWithSupportedIcon)
        navigationViewModel.getNotification()

        val topNavigationValue = navigationViewModel.navNotificationLiveData.value!!
        Assert.assertEquals(0, topNavigationValue.totalNotif)
        Assert.assertEquals(0, topNavigationValue.totalCart)
        Assert.assertEquals(0, topNavigationValue.totalGlobalNavNotif)
        Assert.assertEquals(0, topNavigationValue.totalInbox)
        Assert.assertEquals(0, topNavigationValue.totalNewInbox)
    }

    @Test
    fun `Test navigationViewModel with no supported icon when getAllNotification then not doing any process`() {
        val mockGetNotificationUseCase = mockk<GetNotificationUseCase>()

        coEvery {
            mockGetNotificationUseCase.executeOnBackground()
        } throws Exception("Mocking error")

        val navigationViewModel = NavigationViewModel(
                dispatcher = CoroutineTestDispatchersProvider,
                userSession = mockk(relaxed = true),
                getNotificationUseCase = mockGetNotificationUseCase
        )
        val mockIconBuilderWithNoSupportedIcon = IconBuilder().addIcon(IconList.ID_SETTING){}.build()

        navigationViewModel.setRegisteredIconList(mockIconBuilderWithNoSupportedIcon)
        navigationViewModel.getNotification()

        coVerify { mockGetNotificationUseCase wasNot Called }

        val topNavigationValue = navigationViewModel.navNotificationLiveData.value!!
        Assert.assertEquals(0, topNavigationValue.totalNotif)
        Assert.assertEquals(0, topNavigationValue.totalCart)
        Assert.assertEquals(0, topNavigationValue.totalGlobalNavNotif)
        Assert.assertEquals(0, topNavigationValue.totalInbox)
        Assert.assertEquals(0, topNavigationValue.totalNewInbox)
    }

    @Test
    fun `Test navigationViewModel with supported icon when applyNotification then keep live data value updated`() {
        val mockGetNotificationUseCase = mockk<GetNotificationUseCase>()

        coEvery {
            mockGetNotificationUseCase.executeOnBackground()
        } returns TopNavNotificationModel(
                totalGlobalNavNotif = mockTotalGlobalNotif,
                totalNewInbox = mockTotalNewInbox,
                totalInbox = mockTotalInbox,
                totalNotif = mockTotalNotif,
                totalCart = mockTotalCart
        )

        val navigationViewModel = NavigationViewModel(
                dispatcher = CoroutineTestDispatchersProvider,
                userSession = mockk(relaxed = true),
                getNotificationUseCase = mockGetNotificationUseCase
        )
        val mockIconBuilderWithSupportedIcon = IconBuilder().addIcon(IconList.ID_CART){}.build()

        navigationViewModel.setRegisteredIconList(mockIconBuilderWithSupportedIcon)
        navigationViewModel.getNotification()
        navigationViewModel.applyNotification()

        val topNavigationValue = navigationViewModel.navNotificationLiveData.value!!
        Assert.assertEquals(mockTotalNotif, topNavigationValue.totalNotif)
        Assert.assertEquals(mockTotalCart, topNavigationValue.totalCart)
        Assert.assertEquals(mockTotalGlobalNotif, topNavigationValue.totalGlobalNavNotif)
        Assert.assertEquals(mockTotalInbox, topNavigationValue.totalInbox)
        Assert.assertEquals(mockTotalNewInbox, topNavigationValue.totalNewInbox)
    }

    @Test
    fun `Test navigationViewModel with supported icon when applyNotification then not doing any process`() {
        val mockGetNotificationUseCase = mockk<GetNotificationUseCase>()

        coEvery {
            mockGetNotificationUseCase.executeOnBackground()
        } returns TopNavNotificationModel(
                totalGlobalNavNotif = mockTotalGlobalNotif,
                totalNewInbox = mockTotalNewInbox,
                totalInbox = mockTotalInbox,
                totalNotif = mockTotalNotif,
                totalCart = mockTotalCart
        )

        val navigationViewModel = NavigationViewModel(
                dispatcher = CoroutineTestDispatchersProvider,
                userSession = mockk(relaxed = true),
                getNotificationUseCase = mockGetNotificationUseCase
        )
        val mockIconBuilderWithSupportedIcon = IconBuilder().addIcon(IconList.ID_CART){}.build()

        navigationViewModel.setRegisteredIconList(mockIconBuilderWithSupportedIcon)
        navigationViewModel.applyNotification()
        coVerify { mockGetNotificationUseCase wasNot Called }

        val topNavigationValue = navigationViewModel.navNotificationLiveData.value!!
        Assert.assertEquals(0, topNavigationValue.totalNotif)
        Assert.assertEquals(0, topNavigationValue.totalCart)
        Assert.assertEquals(0, topNavigationValue.totalGlobalNavNotif)
        Assert.assertEquals(0, topNavigationValue.totalInbox)
        Assert.assertEquals(0, topNavigationValue.totalNewInbox)
    }
}