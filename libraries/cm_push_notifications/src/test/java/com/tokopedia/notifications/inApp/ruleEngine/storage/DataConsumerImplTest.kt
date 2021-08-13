package com.tokopedia.notifications.inApp.ruleEngine.storage

import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import io.mockk.*
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class DataConsumerImplTest {

    private val dataConsumerImpl = spyk<DataConsumerImpl>()

    @Before
    fun setUp() {
        mockkStatic(RepositoryManager::class)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun dataShownWithId() {
        every { RepositoryManager.getInstance().storageProvider.updateInAppDataFreq(any()).subscribe() } returns mockk()

        dataConsumerImpl.dataShown(123)

        verify { RepositoryManager.getInstance().storageProvider.updateInAppDataFreq(any()).subscribe() }
    }

    @Test
    fun viewDismissed() {
        every { RepositoryManager.getInstance().storageProvider.viewDismissed(any()).subscribe() } returns mockk()

        dataConsumerImpl.viewDismissed(123)

        verify { RepositoryManager.getInstance().storageProvider.viewDismissed(any()).subscribe() }
    }

    @Test
    fun inflationError() {
        every { RepositoryManager.getInstance().storageProvider.deleteRecord(any()).subscribe() } returns mockk()

        dataConsumerImpl.inflationError(123)

        verify { RepositoryManager.getInstance().storageProvider.deleteRecord(any()).subscribe() }
    }
}