package com.tokopedia.fakeresponse.data.diProvider.activities

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.fakeresponse.chuck.ChuckDBConnector
import com.tokopedia.fakeresponse.chuck.domain.repository.ChuckRepository
import com.tokopedia.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.tokopedia.fakeresponse.chuck.presentation.activities.SearchActivity
import com.tokopedia.fakeresponse.chuck.presentation.viewmodels.SearchViewModel
import com.tokopedia.fakeresponse.data.diProvider.DiProvider
import com.tokopedia.fakeresponse.data.diProvider.vm.VMFactory
import com.tokopedia.fakeresponse.domain.repository.GqlRepository
import com.tokopedia.fakeresponse.domain.repository.RestRepository
import com.tokopedia.fakeresponse.domain.usecases.ShowRecordsUseCase
import kotlinx.coroutines.Dispatchers

class SearchActivityProvider : DiProvider<SearchActivity> {

    override fun inject(t: SearchActivity) {
        val workerDispatcher = Dispatchers.IO
        val repository = ChuckRepository(ChuckDBConnector.getDatabase(t))
        val useCase = ChuckSearchUseCase(repository)

        val restDao = getDatabase(t).restDao()
        val restRepository = RestRepository(restDao)
        val gqlRepository = GqlRepository(getDatabase(t).gqlDao())

        val showGqlUseCase = ShowRecordsUseCase(gqlRepository, restRepository)

        val list = arrayOf(workerDispatcher, useCase, showGqlUseCase)
        val vmFactory = ViewModelProvider(t, VMFactory(t.application, list))
        val vm = vmFactory[SearchViewModel::class.java]
        t.viewModel = vm
    }

}