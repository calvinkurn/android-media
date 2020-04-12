package com.rahullohra.fakeresponse.data.diProvider.activities

import androidx.lifecycle.ViewModelProvider
import com.rahullohra.fakeresponse.chuck.ChuckDBConnector
import com.rahullohra.fakeresponse.chuck.domain.repository.ChuckRepository
import com.rahullohra.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.rahullohra.fakeresponse.chuck.presentation.activities.SearchActivity
import com.rahullohra.fakeresponse.chuck.presentation.viewmodels.SearchViewModel
import com.rahullohra.fakeresponse.data.diProvider.DiProvider
import com.rahullohra.fakeresponse.data.diProvider.vm.VMFactory
import kotlinx.coroutines.Dispatchers

class SearchActivityProvider : DiProvider<SearchActivity> {

    override fun inject(t: SearchActivity) {
        val workerDispatcher = Dispatchers.IO
        val repository = ChuckRepository(ChuckDBConnector.getDatabase(t))
        val useCase = ChuckSearchUseCase(repository)
        val list = arrayOf(workerDispatcher, useCase)
        val vmFactory = ViewModelProvider(t, VMFactory(t.application, list))
        val vm = vmFactory[SearchViewModel::class.java]
        t.viewModel = vm
    }

}