package com.rahullohra.fakeresponse.data.diProvider.activities

import androidx.lifecycle.ViewModelProvider
import com.rahullohra.fakeresponse.data.diProvider.DiProvider
import com.rahullohra.fakeresponse.data.diProvider.vm.VMFactory
import com.rahullohra.fakeresponse.domain.repository.LocalRepository
import com.rahullohra.fakeresponse.domain.repository.RestRepository
import com.rahullohra.fakeresponse.domain.usecases.AddRestDaoUseCase
import com.rahullohra.fakeresponse.domain.usecases.AddToDbUseCase
import com.rahullohra.fakeresponse.presentaiton.activities.AddRestResponseActivity
import com.rahullohra.fakeresponse.presentaiton.viewmodels.AddRestVM
import kotlinx.coroutines.Dispatchers

class RestActivityDiProvider :
    DiProvider<AddRestResponseActivity> {

    override fun inject(t: AddRestResponseActivity) {
        val workerDispatcher = Dispatchers.IO

        val dao = getDatabase(t).restDao()
        val repository = RestRepository(dao)
        val usecase = AddRestDaoUseCase(repository )

        val list = arrayOf(workerDispatcher, usecase)
        val vmFactory = ViewModelProvider(
            t,
            VMFactory(t.application, list)
        )
        val vm = vmFactory[AddRestVM::class.java]
        t.viewModel = vm
    }
}