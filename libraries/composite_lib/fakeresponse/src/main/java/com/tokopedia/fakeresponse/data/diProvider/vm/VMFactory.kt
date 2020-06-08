package com.tokopedia.fakeresponse.data.diProvider.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.tokopedia.fakeresponse.chuck.presentation.viewmodels.SearchViewModel
import com.tokopedia.fakeresponse.domain.usecases.*
import com.tokopedia.fakeresponse.presentation.viewmodels.*
import kotlinx.coroutines.CoroutineDispatcher

class VMFactory(application: Application, val list: Array<Any>) :
        ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddGqlVM::class.java)) {
            return AddGqlVM(
                    list[0] as CoroutineDispatcher,
                    list[1] as AddToDbUseCase,
                    list[2] as ChuckSearchUseCase,
                    list[3] as ExportUseCase
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
                    list[1] as ShowRecordsUseCase,
                    list[2] as DownloadSqliteUseCase
            ) as T
        } else if (modelClass.isAssignableFrom(FakeResponseModel::class.java)) {
            return FakeResponseModel(
                    list[0] as CoroutineDispatcher,
                    list[1] as ShowRecordsUseCase,
                    list[2] as UpdateGqlUseCase
            ) as T
        } else if (modelClass.isAssignableFrom(AddRestVM::class.java)) {
            return AddRestVM(
                    list[0] as CoroutineDispatcher,
                    list[1] as AddRestDaoUseCase,
                    list[2] as ChuckSearchUseCase,
                    list[3] as ExportUseCase
            ) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(
                    list[0] as CoroutineDispatcher,
                    list[1] as ChuckSearchUseCase,
                    list[2] as ShowRecordsUseCase
            ) as T
        } else if (modelClass.isAssignableFrom(PasteTextViewModel::class.java)) {
            return PasteTextViewModel(
                    list[0] as CoroutineDispatcher,
                    list[1] as ImportUseCase
            ) as T
        }
        return super.create(modelClass)
    }
}