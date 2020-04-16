package com.rahullohra.fakeresponse.data.diProvider.activities

import androidx.lifecycle.ViewModelProvider
import com.rahullohra.fakeresponse.chuck.ChuckDBConnector
import com.rahullohra.fakeresponse.chuck.domain.repository.ChuckRepository
import com.rahullohra.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.rahullohra.fakeresponse.chuck.presentation.activities.SearchActivity
import com.rahullohra.fakeresponse.chuck.presentation.viewmodels.SearchViewModel
import com.rahullohra.fakeresponse.data.diProvider.DiProvider
import com.rahullohra.fakeresponse.data.diProvider.vm.VMFactory
import com.rahullohra.fakeresponse.domain.repository.GqlRepository
import com.rahullohra.fakeresponse.domain.repository.RestRepository
import com.rahullohra.fakeresponse.domain.usecases.ShowRecordsUseCase
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