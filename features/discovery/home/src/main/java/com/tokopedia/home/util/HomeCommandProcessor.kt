package com.tokopedia.home.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    fun send()
}

interface ResultCommandProcessor{
    fun updateWidget(visitable: Visitable<*>, position: Int)
    fun addWidget(visitable: Visitable<*>, position: Int)
    fun deleteWidget(visitable: Visitable<*>, position: Int)
    fun updateHomeData(homeDataModel: HomeDataModel)
}


class AddWidgetCommand(private val visitable: Visitable<*>, private val position: Int, private val callback: ResultCommandProcessor) : SubmitCommand {
    override fun send() {
        callback.addWidget(visitable, position)
    }
}

class UpdateHomeData(private val homeDataModel: HomeDataModel, private val callback: ResultCommandProcessor) : SubmitCommand {
    override fun send() {
        callback.updateHomeData(homeDataModel)
    }
}

class UpdateWidgetCommand(private val visitable: Visitable<*>, private val position: Int, private val callback: ResultCommandProcessor) : SubmitCommand {
    override fun send() {
        callback.updateWidget(visitable, position)
    }
}

class DeleteWidgetCommand(private val visitable: Visitable<*>?, private val position: Int, private val callback: ResultCommandProcessor) : SubmitCommand {
    override fun send() {
        visitable?.let {
            callback.deleteWidget(visitable, position)
        }
    }
}

class HomeCommandProcessor : CoroutineScope{
    private val masterJob = SupervisorJob()
    private var channel = Channel<SubmitCommand>(10)
    override val coroutineContext: CoroutineContext = Dispatchers.Main + masterJob

    init {
        processCommands()
    }

    fun sendWithQueueMethod(orderCommand: SubmitCommand) {
        launchCatchError(coroutineContext, block = {
            if (channel.isClosedForSend) {
                channel = Channel(10)
                channel.send(orderCommand)
                processCommands()
            } else {
                channel.send(orderCommand)
            }
        }) {
        }
    }

    fun sendWithQueueMethod(orderCommands: List<SubmitCommand>){
        launchCatchError(coroutineContext, block = {
            if (channel.isClosedForSend) {
                channel = Channel(10)
                orderCommands.forEach { channel.send(it) }
                processCommands()
            } else {
                orderCommands.forEach { channel.send(it) }
            }
        }) {
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