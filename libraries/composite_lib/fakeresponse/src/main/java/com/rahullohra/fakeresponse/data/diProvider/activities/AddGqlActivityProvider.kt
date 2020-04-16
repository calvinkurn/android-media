package com.rahullohra.fakeresponse.data.diProvider.activities

import androidx.lifecycle.ViewModelProvider
import com.rahullohra.fakeresponse.chuck.ChuckDBConnector
import com.rahullohra.fakeresponse.chuck.domain.repository.ChuckRepository
import com.rahullohra.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.rahullohra.fakeresponse.data.diProvider.DiProvider
import com.rahullohra.fakeresponse.data.diProvider.vm.VMFactory
import com.rahullohra.fakeresponse.domain.repository.GqlRepository
import com.rahullohra.fakeresponse.domain.repository.RestRepository
import com.rahullohra.fakeresponse.domain.usecases.AddToDbUseCase
import com.rahullohra.fakeresponse.domain.usecases.ExportUseCase
import com.rahullohra.fakeresponse.presentation.activities.AddGqlActivity
import com.rahullohra.fakeresponse.presentation.viewmodels.AddGqlVM
import kotlinx.coroutines.Dispatchers

class AddGqlActivityProvider :
    DiProvider<AddGqlActivity> {

    override fun inject(t: AddGqlActivity) {
        val workerDispatcher = Dispatchers.IO

        val gqlDao = getDatabase(t).gqlDao()
        val gqlRepository = GqlRepository(gqlDao)

        val restDao = getDatabase(t).restDao()
        val restRepository = RestRepository(restDao)
        val addToDbUseCase = AddToDbUseCase(gqlRepository)

        val chuckRepository = ChuckRepository(ChuckDBConnector.getDatabase(t))
        val chuckSearchUseCase = ChuckSearchUseCase(chuckRepository)
        val exportUseCase = ExportUseCase(restRepository,gqlRepository)

        val list = arrayOf(workerDispatcher, addToDbUseCase, chuckSearchUseCase, exportUseCase)
        val vmFactory = ViewModelProvider(t, VMFactory(t.application, list))
        val vm = vmFactory[AddGqlVM::class.java]
        t.viewModel = vm
    }
}