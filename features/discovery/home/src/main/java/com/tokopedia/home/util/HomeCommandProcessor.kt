package com.tokopedia.home.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import kotlinx.coroutines.*
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

class HomeCommandProcessor (val dispatchers: CoroutineDispatcher): CoroutineScope{
    private val masterJob = SupervisorJob()
    override val coroutineContext: CoroutineContext = dispatchers + masterJob

    fun sendWithQueueMethod(orderCommand: SubmitCommand) {
        launch {
            orderCommand.send()
        }
    }

    fun sendWithQueueMethod(orderCommands: List<SubmitCommand>){
        launch {
            orderCommands.forEach {
                it.send()
            }
        }
    }

    fun onClear(){
        if (isActive && !masterJob.isCancelled) {
            masterJob.children.map { it.cancel() }
        }
    }
}