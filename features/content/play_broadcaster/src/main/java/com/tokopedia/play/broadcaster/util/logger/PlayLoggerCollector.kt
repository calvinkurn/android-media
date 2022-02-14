package com.tokopedia.play.broadcaster.util.logger

import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject


/**
 * Created by mzennis on 25/10/21.
 */
class PlayLoggerCollector @Inject constructor() {

    private val logs = ConcurrentLinkedQueue<Pair<String, String>>()

    fun collect(log: Pair<String, String>) {
        logs.add(log)
    }

    fun getAll(): ConcurrentLinkedQueue<Pair<String, String>> {
        return logs
    }
}