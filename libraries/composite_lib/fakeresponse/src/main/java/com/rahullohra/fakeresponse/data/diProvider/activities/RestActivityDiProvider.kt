package com.rahullohra.fakeresponse.data.diProvider.activities

import androidx.lifecycle.ViewModelProvider
import com.rahullohra.fakeresponse.chuck.ChuckDBConnector
import com.rahullohra.fakeresponse.chuck.domain.repository.ChuckRepository
import com.rahullohra.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.rahullohra.fakeresponse.data.diProvider.DiProvider
import com.rahullohra.fakeresponse.data.diProvider.vm.VMFactory
import com.rahullohra.fakeresponse.domain.repository.RestRepository
import com.rahullohra.fakeresponse.domain.usecases.AddRestDaoUseCase
import com.rahullohra.fakeresponse.presentation.activities.AddRestResponseActivity
import com.rahullohra.fakeresponse.presentation.viewmodels.AddRestVM
import kotlinx.coroutines.Dispatchers

class RestActivityDiProvider :
    DiProvider<AddRestResponseActivity> {

    override fun inject(t: AddRestResponseActivity) {
        val workerDispatcher = Dispatchers.IO

        val dao = getDatabase(t).restDao()
        val repository = RestRepository(dao)
        val usecase = AddRestDaoUseCase(repository)

        val chuckRepository = ChuckRepository(ChuckDBConnector.getDatabase(t))
        val chuckSearchUseCase = ChuckSearchUseCase(chuckRepository)

        val list = arrayOf(workerDispatcher, usecase, chuckSearchUseCase)
        val vmFactory = ViewModelProvider(
            t,
            VMFactory(t.application, list)
        )
        val vm = vmFactory[AddRestVM::class.java]
        t.viewModel = vm
    }
}