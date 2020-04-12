package com.rahullohra.fakeresponse.data.diProvider.activities

import androidx.lifecycle.ViewModelProvider
import com.rahullohra.fakeresponse.chuck.ChuckDBConnector
import com.rahullohra.fakeresponse.chuck.domain.repository.ChuckRepository
import com.rahullohra.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.rahullohra.fakeresponse.data.diProvider.DiProvider
import com.rahullohra.fakeresponse.data.diProvider.vm.VMFactory
import com.rahullohra.fakeresponse.domain.repository.LocalRepository
import com.rahullohra.fakeresponse.domain.usecases.AddToDbUseCase
import com.rahullohra.fakeresponse.presentaiton.activities.AddGqlActivity
import com.rahullohra.fakeresponse.presentaiton.viewmodels.AddGqlVM
import kotlinx.coroutines.Dispatchers

class AddGqlActivityProvider :
    DiProvider<AddGqlActivity> {

    override fun inject(t: AddGqlActivity) {
        val workerDispatcher = Dispatchers.IO

        val gqlDao = getDatabase(t).gqlDao()
        val localRepository = LocalRepository(gqlDao)
        val addToDbUseCase = AddToDbUseCase(localRepository)

        val chuckRepository = ChuckRepository(ChuckDBConnector.getDatabase(t))
        val chuckSearchUseCase = ChuckSearchUseCase(chuckRepository)

        val list = arrayOf(workerDispatcher, addToDbUseCase, chuckSearchUseCase)
        val vmFactory = ViewModelProvider(t, VMFactory(t.application, list))
        val vm = vmFactory[AddGqlVM::class.java]
        t.viewModel = vm
    }
}