package com.tokopedia.notifications.inApp.ruleEngine

import android.app.Application
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataConsumer
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.InterfaceRuleInterpreter
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class RulesManagerTest {

    private lateinit var rulesManager: RulesManager
    private val ruleInterpreter: InterfaceRuleInterpreter = mockk()
    private val dataConsumer: DataConsumer = mockk()
    private val application: Application = mockk()

    @Before
    @Throws(Exception::class)
    fun setUp() {
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    fun initRuleEngineCheckInstanceReturnedSame() {

        mockkStatic(RepositoryManager::class)
        every { RepositoryManager.initRepository(application) } returns Unit

        RulesManager.initRuleEngine(application, ruleInterpreter, dataConsumer)
        rulesManager = spyk(RulesManager.getInstance())
        RulesManager.initRuleEngine(application, ruleInterpreter, dataConsumer)

        assertEquals(RulesManager.getInstance(), RulesManager.getInstance())
    }

    @Test
    fun initRuleEngineCheckInitialisedOnlyOnce() {

        mockkStatic(RepositoryManager::class)
        every { RepositoryManager.initRepository(application) } returns Unit

        RulesManager.initRuleEngine(application, ruleInterpreter, dataConsumer)
        rulesManager = spyk(RulesManager.getInstance())
        RulesManager.initRuleEngine(application, ruleInterpreter, dataConsumer)

        verify(exactly = 1) { RepositoryManager.initRepository(application) }
    }
}