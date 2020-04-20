package com.tokopedia.fakeresponse.data.diProvider.activities

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.fakeresponse.chuck.ChuckDBConnector
import com.tokopedia.fakeresponse.chuck.domain.repository.ChuckRepository
import com.tokopedia.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.tokopedia.fakeresponse.data.diProvider.DiProvider
import com.tokopedia.fakeresponse.data.diProvider.vm.VMFactory
import com.tokopedia.fakeresponse.domain.repository.GqlRepository
import com.tokopedia.fakeresponse.domain.repository.RestRepository
import com.tokopedia.fakeresponse.domain.usecases.AddRestDaoUseCase
import com.tokopedia.fakeresponse.domain.usecases.ExportUseCase
import com.tokopedia.fakeresponse.presentation.activities.AddRestResponseActivity
import com.tokopedia.fakeresponse.presentation.viewmodels.AddRestVM
import kotlinx.coroutines.Dispatchers

class RestActivityDiProvider :
        DiProvider<AddRestResponseActivity> {

    override fun inject(t: AddRestResponseActivity) {
        val workerDispatcher = Dispatchers.IO

        val gqlDao = getDatabase(t).gqlDao()
        val gqlRepository = GqlRepository(gqlDao)

        val dao = getDatabase(t).restDao()
        val restRepository = RestRepository(dao)
        val usecase = AddRestDaoUseCase(restRepository)

        val chuckRepository = ChuckRepository(ChuckDBConnector.getDatabase(t))
        val chuckSearchUseCase = ChuckSearchUseCase(chuckRepository)
        val exportUseCase = ExportUseCase(restRepository, gqlRepository)

        val list = arrayOf(workerDispatcher, usecase, chuckSearchUseCase, exportUseCase)
        val vmFactory = ViewModelProvider(
                t,
                VMFactory(t.application, list)
        )
        val vm = vmFactory[AddRestVM::class.java]
        t.viewModel = vm
    }
}