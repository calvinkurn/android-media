package com.tokopedia.checkout.revamp.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckoutViewModel @Inject constructor(private val dispatchers: CoroutineDispatchers) : ViewModel() {

    var currentJob: Job? = null
    var counter: Int = 0

    var ms = MutableStateFlow(true)

    var sm = flow<Boolean> { }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    fun test() {
        currentJob = viewModelScope.launch {
            Log.i("qwertyuiop", "start launch")
            doSomething(++counter)
            Log.i("qwertyuiop", "done launch")
        }
    }

    private suspend fun doSomething(count: Int) {
        currentJob?.join()
        withContext(dispatchers.io) {
            Log.i("qwertyuiop", "start delay $count")
            delay(5_000)
            Log.i("qwertyuiop", "done delay $count")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("qwertyuiop", "done clear")
        Log.i("qwertyuiop", "job: ${currentJob?.isCompleted}")
        Log.i("qwertyuiop", "parent job: ${viewModelScope.coroutineContext.job?.children?.find { !it.isCompleted }}")
    }
}
