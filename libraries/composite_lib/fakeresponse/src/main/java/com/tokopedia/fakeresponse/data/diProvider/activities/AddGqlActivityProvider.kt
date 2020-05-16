package com.tokopedia.fakeresponse.data.diProvider.activities

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.fakeresponse.chuck.ChuckDBConnector
import com.tokopedia.fakeresponse.chuck.domain.repository.ChuckRepository
import com.tokopedia.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.tokopedia.fakeresponse.data.diProvider.DiProvider
import com.tokopedia.fakeresponse.data.diProvider.vm.VMFactory
import com.tokopedia.fakeresponse.domain.repository.GqlRepository
import com.tokopedia.fakeresponse.domain.repository.RestRepository
import com.tokopedia.fakeresponse.domain.usecases.AddToDbUseCase
import com.tokopedia.fakeresponse.domain.usecases.ExportUseCase
import com.tokopedia.fakeresponse.presentation.activities.AddGqlActivity
import com.tokopedia.fakeresponse.presentation.viewmodels.AddGqlVM
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