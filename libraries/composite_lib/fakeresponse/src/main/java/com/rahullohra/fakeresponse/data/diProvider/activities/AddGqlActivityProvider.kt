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

    override fun inject(activity: AddGqlActivity) {
        val workerDispatcher = Dispatchers.IO

        val gqlDao = getDatabase(activity).gqlDao()
        val localRepository = LocalRepository(gqlDao)
        val addToDbUseCase = AddToDbUseCase(localRepository)

        val list = arrayOf(workerDispatcher, addToDbUseCase)
        val vmFactory = ViewModelProvider(activity, VMFactory(activity.application, list))
        val vm = vmFactory[AddGqlVM::class.java]
        activity.viewModel = vm
    }
}