package com.tokopedia.home.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext

/**
 * Created by Lukas on 03/09/20.
 */

interface SubmitCommand {
    suspend fun send()
}

interface ResultCommandProcessor{
    suspend fun updateWidget(visitable: Visitable<*>, position: Int)
    suspend fun addWidget(visitable: Visitable<*>, position: Int)
    suspend fun deleteWidget(visitable: Visitable<*>, position: Int)
    suspend fun updateHomeData(homeDataModel: HomeDataModel)
}


class AddWidgetCommand(private val visitable: Visitable<*>, private val position: Int, private val callback: ResultCommandProcessor) : SubmitCommand {
    override suspend fun send() {
        callback.addWidget(visitable, position)
    }
}

class UpdateHomeData(private val homeDataModel: HomeDataModel, private val callback: ResultCommandProcessor) : SubmitCommand {
    override suspend fun send() {
        callback.updateHomeData(homeDataModel)
    }
}

class UpdateWidgetCommand(private val visitable: Visitable<*>, private val position: Int, private val callback: ResultCommandProcessor) : SubmitCommand {
    override suspend fun send() {
        callback.updateWidget(visitable, position)
    }
}

class DeleteWidgetCommand(private val visitable: Visitable<*>?, private val position: Int, private val callback: ResultCommandProcessor) : SubmitCommand {
    override suspend fun send() {
        visitable?.let {
            callback.deleteWidget(visitable, position)
        }
    }
}

class HomeCommandProcessor (dispatchers: CoroutineDispatcher): CoroutineScope{
    companion object{
        private const val CAPACITY_QUEUE = 10
    }
    private val masterJob = SupervisorJob()
    private var channel = Channel<SubmitCommand>(CAPACITY_QUEUE)
    override val coroutineContext: CoroutineContext = dispatchers + masterJob

    init {
        processCommands()
    }

    fun sendWithQueueMethod(orderCommand: SubmitCommand) {
        launchCatchError(coroutineContext, block = {
            if (channel.isClosedForSend) {
                channel = Channel(CAPACITY_QUEUE)
                channel.send(orderCommand)
                processCommands()
            } else {
                channel.send(orderCommand)
            }
        }) {
            channel = Channel(CAPACITY_QUEUE)
            channel.send(orderCommand)
            processCommands()
        }
    }

    fun sendWithQueueMethod(orderCommands: List<SubmitCommand>){
        launchCatchError(coroutineContext, block = {
            if (channel.isClosedForSend) {
                channel = Channel(CAPACITY_QUEUE)
                orderCommands.forEach { channel.send(it) }
                processCommands()
            } else {
                orderCommands.forEach { channel.send(it) }
            }
        }) {
            channel = Channel(CAPACITY_QUEUE)
            orderCommands.forEach { channel.send(it) }
            processCommands()
        }
    }

    private fun processCommands(){
        launchCatchError(coroutineContext, block = {
            channel.consumeAsFlow().collect{
                it.send()
            }
        }){
        }
    }

    fun onClear(){
        if (isActive && !masterJob.isCancelled) {
            masterJob.children.map { it.cancel() }
        }
    }
}