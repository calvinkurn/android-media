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

package com.tokopedia.v2.home.base

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import timber.log.Timber
import com.tokopedia.v2.home.model.vo.Resource
import kotlin.coroutines.coroutineContext

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result = MutableLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    suspend fun build(): NetworkBoundResource<ResultType, RequestType> {
        CoroutineScope(coroutineContext).launch(supervisorJob) {
            val dbResult = loadFromDb()
            if (shouldFetch(dbResult)) {
                try {
                    fetchFromNetwork(dbResult)
                } catch (e: Exception) {
                    Timber.tag("NetworkBoundResource").e("An error happened: $e")
                    setValue(Resource.error(e, loadFromDb()))
                }
            } else {
                Timber.tag(NetworkBoundResource::class.java.name).d("Return data from local database")
                setValue(Resource.success(dbResult))
            }
        }
        return this
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    private suspend fun fetchFromNetwork(dbResult: ResultType) {
        Timber.tag(NetworkBoundResource::class.java.name).d("Fetch data from network")
        setValue(Resource.cache(dbResult)) // Dispatch latest value quickly (UX purpose)
        val apiResponse = createCallAsync()
        Timber.tag(NetworkBoundResource::class.java.name).d("Data fetched from network")
        saveCallResults(processResponse(apiResponse))
        setValue(Resource.success(loadFromDb()))
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        Timber.tag(NetworkBoundResource::class.java.name).d("Resource: %s", newValue)
        if (result.value != newValue) result.postValue(newValue)
    }

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType

    @WorkerThread
    protected abstract suspend fun saveCallResults(items: ResultType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract suspend fun loadFromDb(): ResultType

    @MainThread
    protected abstract suspend fun createCallAsync(): RequestType
}