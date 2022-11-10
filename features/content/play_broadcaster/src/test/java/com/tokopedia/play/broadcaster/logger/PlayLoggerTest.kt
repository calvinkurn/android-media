package com.tokopedia.play.broadcaster.logger

import com.tokopedia.broadcaster.revamp.util.error.BroadcasterErrorType
import com.tokopedia.broadcaster.revamp.util.error.BroadcasterException
import com.tokopedia.play.broadcaster.domain.model.Banned
import com.tokopedia.play.broadcaster.domain.model.Freeze
import com.tokopedia.play.broadcaster.domain.model.LiveDuration
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.logger.PlayLoggerCollector
import com.tokopedia.play.broadcaster.util.logger.PlayLoggerImpl
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
        logger.logChannelStatus(ChannelStatus.Live)
        logger.logChannelStatus(ChannelStatus.Pause)
        logger.logChannelStatus(ChannelStatus.Stop)
        logger.logPusherState(PlayBroadcasterState.Started)
        logger.logPusherState(PlayBroadcasterState.Resume(true, true))
        logger.logPusherState(PlayBroadcasterState.Resume(false, false))
        logger.logPusherState(PlayBroadcasterState.Recovered)
        logger.logPusherState(PlayBroadcasterState.Paused)
        logger.logPusherState(PlayBroadcasterState.Stopped)
        logger.logPusherState(PlayBroadcasterState.Error(BroadcasterException(BroadcasterErrorType.StreamFailed)))
        logger.logSocketType(Freeze())
        logger.logSocketType(Banned())
        logger.logSocketType(LiveDuration())
        logger.logChannelStatus(ChannelStatus.Live)
        logger.logChannelStatus(ChannelStatus.Pause)
        logger.logChannelStatus(ChannelStatus.Stop)
        logger.logPusherState(PlayBroadcasterState.Started)
        logger.logPusherState(PlayBroadcasterState.Resume(true, true))
        logger.logPusherState(PlayBroadcasterState.Resume(false, false))
        logger.logPusherState(PlayBroadcasterState.Recovered)
        logger.logPusherState(PlayBroadcasterState.Paused)
        logger.logPusherState(PlayBroadcasterState.Stopped)
        logger.logPusherState(PlayBroadcasterState.Error(BroadcasterException(BroadcasterErrorType.StreamFailed)))
        logger.logSocketType(Freeze())
        logger.logSocketType(Banned())
        logger.logSocketType(LiveDuration())

        val expectedValue = ConcurrentLinkedQueue<Pair<String, String>>().apply {
            add(Pair("channel", ChannelStatus.Live.value))
            add(Pair("channel", ChannelStatus.Pause.value))
            add(Pair("channel", ChannelStatus.Stop.value))
            add(Pair("pusher", PlayBroadcasterState.Started.tag))
            add(Pair("pusher", PlayBroadcasterState.Resume(true, true).tag))
            add(Pair("pusher", PlayBroadcasterState.Resume(false, false).tag))
            add(Pair("pusher", PlayBroadcasterState.Recovered.tag))
            add(Pair("pusher", PlayBroadcasterState.Paused.tag))
            add(Pair("pusher", PlayBroadcasterState.Stopped.tag))
            add(Pair("pusher", PlayBroadcasterState.Error(BroadcasterException(BroadcasterErrorType.StreamFailed)).tag))
            add(Pair("socket", Freeze().type.value))
            add(Pair("socket", Banned().type.value))
            add(Pair("socket", LiveDuration().type.value))
            add(Pair("channel", ChannelStatus.Live.value))
            add(Pair("channel", ChannelStatus.Pause.value))
            add(Pair("channel", ChannelStatus.Stop.value))
            add(Pair("pusher", PlayBroadcasterState.Started.tag))
            add(Pair("pusher", PlayBroadcasterState.Resume(true, true).tag))
            add(Pair("pusher", PlayBroadcasterState.Resume(false, false).tag))
            add(Pair("pusher", PlayBroadcasterState.Recovered.tag))
            add(Pair("pusher", PlayBroadcasterState.Paused.tag))
            add(Pair("pusher", PlayBroadcasterState.Stopped.tag))
            add(Pair("pusher", PlayBroadcasterState.Error(BroadcasterException(BroadcasterErrorType.StreamFailed)).tag))
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