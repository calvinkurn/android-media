package com.tokopedia.settingnotif.usersetting.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.data.pojo.ChangeSection
import com.tokopedia.settingnotif.usersetting.data.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.data.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.data.pojo.SellerSection
import com.tokopedia.settingnotif.usersetting.state.NotificationItemState
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SettingStateViewModelTest {

    private lateinit var viewModel: SettingStateViewModel
    private var userSession: UserSessionInterface = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = SettingStateViewModel(userSession)
    }

    @Test fun `it should added pinned notification of permission`() {
        val isNotificationEnabled = false

        viewModel.addPinnedPushNotificationItems(
                isNotificationEnabled,
                UserSettingDataView(emptyList())
        )

        assertTrue(viewModel.getPinnedItems()
                .filterIsInstance<NotificationActivation>()
                .isNotEmpty())

        assertTrue(viewModel.getPinnedItems()
                .filterIsInstance<NotificationActivation>()
                .first().type == NotificationItemState.PushNotif)
    }

    @Test fun `it should added pinned of seller section`() {
        val isNotificationEnabled = true

        every { userSession.hasShop() } returns true

        viewModel.addPinnedPushNotificationItems(
                isNotificationEnabled,
                UserSettingDataView(emptyList())
        )

        assertTrue(viewModel.getPinnedItems()
                .filterIsInstance<SellerSection>()
                .isNotEmpty())
    }

    @Test fun `it should added pinned notification of permission and seller section`() {
        val isNotificationEnabled = false

        every { userSession.hasShop() } returns true

        viewModel.addPinnedPushNotificationItems(
                isNotificationEnabled,
                UserSettingDataView(emptyList())
        )

        assertTrue(viewModel.getPinnedItems()
                .filterIsInstance<NotificationActivation>()
                .isNotEmpty())

        assertTrue(viewModel.getPinnedItems()
                .filterIsInstance<SellerSection>()
                .isNotEmpty())
    }

    @Test fun `it should added pinned of adding email`() {
        every { userSession.email } returns ""

        viewModel.addPinnedEmailItems(
                UserSettingDataView(emptyList())
        )

        assertTrue(viewModel.getPinnedItems()
                .filterIsInstance<NotificationActivation>()
                .isNotEmpty())

        assertTrue(viewModel.getPinnedItems()
                .filterIsInstance<NotificationActivation>()
                .first().type == NotificationItemState.Email)
    }

    @Test fun `it should added pinned of change email`() {
        val fakeEmail = "isfhani.ghiath@tokopedia.com"
        every { userSession.email } returns fakeEmail

        viewModel.addPinnedEmailItems(
                UserSettingDataView(emptyList())
        )

        assertTrue(viewModel.getPinnedItems()
                .filterIsInstance<ChangeSection>()
                .first().state == NotificationItemState.Email)
    }

    @Test fun `it should saved the last setting states`() {
        val expectedValue = mutableListOf<Visitable<*>>()
        expectedValue.add(ParentSetting(
                "name",
                "icon",
                "key",
                true
        ))

        viewModel.saveLastStateAll(expectedValue)

        assertTrue(viewModel.getSettingStates().isNotEmpty())
    }

    @Test fun `it should updated the temporary state with a new one`() {
        val expectedStatusValue = false

        viewModel.getSettingStates().add(ParentSetting(
                "name",
                "icon",
                "key",
                true
        ))

        viewModel.updateSettingState(ParentSetting(
                "name",
                "icon",
                "key",
                expectedStatusValue
        ))

        // validate through the status of parentSetting
        val result = viewModel.getSettingStates().first()
        assertTrue(result.status == expectedStatusValue)
    }

    @Test fun `it should not able to updated the temporary state`() {
        viewModel.updateSettingState(null)
        assertTrue(viewModel.getSettingStates().isEmpty())
    }

    @After fun tearDown() {}

}