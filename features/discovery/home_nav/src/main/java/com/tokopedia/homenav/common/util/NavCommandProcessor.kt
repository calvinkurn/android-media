package com.tokopedia.homenav.common.util

import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext

interface SubmitCommand {
    suspend fun send()
}

class AddWidgetCommand(private val visitable: HomeNavVisitable, private val position: Int, private val onAddWidget: suspend (HomeNavVisitable, Int)-> Unit) : SubmitCommand {
    override suspend fun send() {
        onAddWidget.invoke(visitable, position)
    }
}

class AddWidgetListCommand(private val visitables: List<HomeNavVisitable>, private val position: Int, private val onAddWidgetList: suspend (List<HomeNavVisitable>, Int)-> Unit) : SubmitCommand {
    override suspend fun send() {
        onAddWidgetList.invoke(visitables, position)
    }
}

class UpdateNavigationData(private val navigationDataModel: MainNavigationDataModel, private val onUpdateNavigationData: suspend (MainNavigationDataModel)-> Unit) : SubmitCommand {
    override suspend fun send() {
        onUpdateNavigationData.invoke(navigationDataModel)
    }
}

class UpdateWidgetCommand(private val visitable: HomeNavVisitable, private val position: Int, private val onUpdateWidget: suspend (HomeNavVisitable, Int)-> Unit) : SubmitCommand {
    override suspend fun send() {
        onUpdateWidget.invoke(visitable, position)
    }
}

class DeleteWidgetCommand(private val visitable: HomeNavVisitable?, private val position: Int, private val onDeleteWidget: suspend (HomeNavVisitable, Int)-> Unit) : SubmitCommand {
    override suspend fun send() {
        visitable?.let {
            onDeleteWidget.invoke(visitable, position)
        }
    }
}

class NavCommandProcessor (dispatchers: CoroutineDispatcher): CoroutineScope {
    companion object{
        private const val CAPACITY_QUEUE = 15
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