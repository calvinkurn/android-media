/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tokopedia.abstraction.base.data.source

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.coroutineContext

abstract class NetworkBoundResource<ResponseType, ResultType> {

    private val result = MutableLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    suspend fun build(): NetworkBoundResource<ResponseType, ResultType> {
        CoroutineScope(coroutineContext).launch(supervisorJob) {
            try {
                val dbResult = loadFromDb()
                if (shouldFetch(dbResult)) {
                    fetchFromNetwork(dbResult)
                } else {
                    Timber.tag(NetworkBoundResource::class.java.name).d("Return data from local database")
                    setValue(Resource.success(dbResult))
                }
            }catch (e: Exception){
                Timber.tag("NetworkBoundResource").e("An error happened: $e")
                onFetchError()
                setValue(Resource.error(e, loadFromDb()))
            }
        }
        return this
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    private suspend fun fetchFromNetwork(dbResult: ResultType?) {
        Timber.tag(NetworkBoundResource::class.java.name).d("Fetch data from network")
        setValue(Resource.loading(dbResult)) // Dispatch latest value quickly (UX purpose)
        val apiResponse = createCallAsync()
        Timber.tag(NetworkBoundResource::class.java.name).d("Data fetched from network")
        val networkData = processResponse(apiResponse)
        saveCallResults(networkData)
        setValue(Resource.success(networkData))
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        Timber.tag(NetworkBoundResource::class.java.name).d("Resource: %s", newValue)
        if (result.value != newValue) result.value = newValue
    }

    @WorkerThread
    protected abstract fun processResponse(response: ResponseType): ResultType

    @WorkerThread
    protected abstract suspend fun saveCallResults(items: ResultType)

    @WorkerThread
    protected abstract suspend fun onFetchError()

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract suspend fun loadFromDb(): ResultType?

    @MainThread
    protected abstract suspend fun createCallAsync(): ResponseType
}