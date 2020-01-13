package com.rahullohra.fakeresponse.data.diProvider.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rahullohra.fakeresponse.domain.usecases.AddToDbUseCase
import com.rahullohra.fakeresponse.domain.usecases.DownloadSqliteUseCase
import com.rahullohra.fakeresponse.domain.usecases.ShowGqlUseCase
import com.rahullohra.fakeresponse.domain.usecases.UpdateGqlUseCase
import com.rahullohra.fakeresponse.presentaiton.viewmodels.AddGqlVM
import com.rahullohra.fakeresponse.presentaiton.viewmodels.DownloadFragmentVM
import com.rahullohra.fakeresponse.presentaiton.viewmodels.FakeResponseModel
import com.rahullohra.fakeresponse.presentaiton.viewmodels.FakeResponseVM
import kotlinx.coroutines.CoroutineDispatcher

class VMFactory(application: Application, val list: Array<Any>) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddGqlVM::class.java)) {
            return AddGqlVM(
                list[0] as CoroutineDispatcher,
                list[1] as CoroutineDispatcher,
                list[2] as AddToDbUseCase
            ) as T
        } else if (modelClass.isAssignableFrom(DownloadFragmentVM::class.java)) {
            return DownloadFragmentVM(
                list[0] as CoroutineDispatcher,
                list[1] as CoroutineDispatcher,
                list[2] as DownloadSqliteUseCase
            ) as T
        } else if (modelClass.isAssignableFrom(FakeResponseVM::class.java)) {
            return FakeResponseVM(
                list[0] as CoroutineDispatcher,
                list[1] as ShowGqlUseCase,
                list[2] as DownloadSqliteUseCase
            ) as T
        } else if (modelClass.isAssignableFrom(FakeResponseModel::class.java)) {
            return FakeResponseModel(
                list[0] as CoroutineDispatcher,
                list[1] as ShowGqlUseCase,
                list[2] as UpdateGqlUseCase
            ) as T
        }
        return super.create(modelClass)
    }
}