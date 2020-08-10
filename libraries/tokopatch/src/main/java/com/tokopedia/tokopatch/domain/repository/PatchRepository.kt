package com.tokopedia.tokopatch.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.tokopedia.tokopatch.domain.api.PatchApiService
import com.tokopedia.tokopatch.domain.api.RetrofitClient
import com.tokopedia.tokopatch.domain.data.DataDao
import com.tokopedia.tokopatch.domain.data.DataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Author errysuprayogi on 09,February,2020
 */
class PatchRepository(private val dataDao: DataDao, versionCode: String) {

    private var TAG : String = PatchRepository::class.java.simpleName
    private var client: PatchApiService = RetrofitClient.webservice

    val allData: LiveData<List<DataResponse.Result>> = getDistinctAllResult(versionCode)

    private fun getDistinctAllResult(versionCode: String): LiveData<List<DataResponse.Result>>
            = dataDao.getAllResult(versionCode).getDistinct()

    private fun <T> LiveData<T>.getDistinct(): LiveData<T> {
        val distinctLiveData = MediatorLiveData<T>()
        distinctLiveData.addSource(this, object : Observer<T> {
            private var initialized = false
            private var lastObj: T? = null
            override fun onChanged(obj: T?) {
                if (!initialized) {
                    initialized = true
                    lastObj = obj
                    distinctLiveData.postValue(lastObj)
                } else if ((obj == null && lastObj != null)
                        || obj != lastObj) {
                    lastObj = obj
                    distinctLiveData.postValue(lastObj)
                }
            }
        })
        return distinctLiveData
    }

    suspend fun flush(){
        dataDao.deleteAll()
    }

    suspend fun insert(result: DataResponse.Result) {
        dataDao.insertAll(result)
    }

    fun getPatch(applicationId: String, versionName: String, buildNumber: String, onSuccess: ((DataResponse) -> Unit),
                 onError: ((Throwable) -> Unit)) {
        client.getPatch(applicationId, versionName, buildNumber).enqueue(object : Callback<DataResponse>{
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if(response.isSuccessful){
                    response.body()?.let(onSuccess)
                }
            }
            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                t.printStackTrace()
                onError(t)
            }
        })
    }

}
