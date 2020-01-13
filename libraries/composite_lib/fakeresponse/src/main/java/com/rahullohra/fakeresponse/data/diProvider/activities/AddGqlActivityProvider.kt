package com.rahullohra.fakeresponse.data.diProvider.activities

import androidx.lifecycle.ViewModelProvider
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
        val uiDispatcher = Dispatchers.Main
        val workerDispatcher = Dispatchers.IO

        val gqlDao = getDatabase(t).gqlDao()
        val localRepository = LocalRepository(gqlDao)
        val addToDbUseCase = AddToDbUseCase(localRepository)

        val list = arrayOf(uiDispatcher, workerDispatcher, addToDbUseCase)
        val vmFactory = ViewModelProvider(t,
            VMFactory(t.application, list)
        )
        val vm = vmFactory[AddGqlVM::class.java]
        t.viewModel = vm
    }
}