package com.tokopedia.play.broadcaster.logger

import com.tokopedia.play.broadcaster.domain.model.Banned
import com.tokopedia.play.broadcaster.domain.model.Freeze
import com.tokopedia.play.broadcaster.domain.model.LiveDuration
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorState
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.error.PlayLivePusherException
import com.tokopedia.play.broadcaster.util.logger.PlayLoggerCollector
import com.tokopedia.play.broadcaster.util.logger.PlayLoggerImpl
import com.tokopedia.play_common.types.PlayChannelStatusType
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.concurrent.ConcurrentLinkedQueue


/**
 * Created by mzennis on 22/10/21.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class PlayLoggerTest {

    private val collector = PlayLoggerCollector()
    private val logger = PlayLoggerImpl(collector)

    @Test
    fun testCollectLogs() {
        logger.logChannelStatus(PlayChannelStatusType.Live)
        logger.logChannelStatus(PlayChannelStatusType.Pause)
        logger.logChannelStatus(PlayChannelStatusType.Stop)
        logger.logPusherState(PlayLivePusherMediatorState.Idle)
        logger.logPusherState(PlayLivePusherMediatorState.Connecting)
        logger.logPusherState(PlayLivePusherMediatorState.Started)
        logger.logPusherState(PlayLivePusherMediatorState.Resume(true))
        logger.logPusherState(PlayLivePusherMediatorState.Resume(false))
        logger.logPusherState(PlayLivePusherMediatorState.Recovered)
        logger.logPusherState(PlayLivePusherMediatorState.Paused)
        logger.logPusherState(PlayLivePusherMediatorState.Stopped)
        logger.logPusherState(PlayLivePusherMediatorState.Error(PlayLivePusherException("unknown")))
        logger.logSocketType(Freeze())
        logger.logSocketType(Banned())
        logger.logSocketType(LiveDuration())
        logger.logChannelStatus(PlayChannelStatusType.Live)
        logger.logChannelStatus(PlayChannelStatusType.Pause)
        logger.logChannelStatus(PlayChannelStatusType.Stop)
        logger.logPusherState(PlayLivePusherMediatorState.Idle)
        logger.logPusherState(PlayLivePusherMediatorState.Connecting)
        logger.logPusherState(PlayLivePusherMediatorState.Started)
        logger.logPusherState(PlayLivePusherMediatorState.Resume(true))
        logger.logPusherState(PlayLivePusherMediatorState.Resume(false))
        logger.logPusherState(PlayLivePusherMediatorState.Recovered)
        logger.logPusherState(PlayLivePusherMediatorState.Paused)
        logger.logPusherState(PlayLivePusherMediatorState.Stopped)
        logger.logPusherState(PlayLivePusherMediatorState.Error(PlayLivePusherException("unknown")))
        logger.logSocketType(Freeze())
        logger.logSocketType(Banned())
        logger.logSocketType(LiveDuration())

        val expectedValue = ConcurrentLinkedQueue<Pair<String, String>>().apply {
            add(Pair("channel", PlayChannelStatusType.Live.value))
            add(Pair("channel", PlayChannelStatusType.Pause.value))
            add(Pair("channel", PlayChannelStatusType.Stop.value))
            add(Pair("pusher", PlayLivePusherMediatorState.Idle.tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Connecting.tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Started.tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Resume(true).tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Resume(false).tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Recovered.tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Paused.tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Stopped.tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Error(PlayLivePusherException("unknown")).tag))
            add(Pair("socket", Freeze().type.value))
            add(Pair("socket", Banned().type.value))
            add(Pair("socket", LiveDuration().type.value))
            add(Pair("channel", PlayChannelStatusType.Live.value))
            add(Pair("channel", PlayChannelStatusType.Pause.value))
            add(Pair("channel", PlayChannelStatusType.Stop.value))
            add(Pair("pusher", PlayLivePusherMediatorState.Idle.tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Connecting.tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Started.tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Resume(true).tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Resume(false).tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Recovered.tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Paused.tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Stopped.tag))
            add(Pair("pusher", PlayLivePusherMediatorState.Error(PlayLivePusherException("unknown")).tag))
            add(Pair("socket", Freeze().type.value))
            add(Pair("socket", Banned().type.value))
            add(Pair("socket", LiveDuration().type.value))
        }.toString()

        val actualValue = collector.getAll().toString()

        println("ActualValue: $actualValue")
        println("ExpectedValue: $expectedValue")

        actualValue.assertEqualTo(expectedValue)
    }

    @Test
    fun testSendLogs() {
        runBlocking {
            launch { logger.sendAll("1") }
            launch { logger.sendAll("1") }

            val actualValue = collector.getAll().toString()
            val expectedValue = emptyList<Pair<String, String>>().toString()

            println(actualValue)
            println(expectedValue)

            actualValue.assertEqualTo(expectedValue)
        }
    }
}