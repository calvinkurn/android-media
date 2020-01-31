package com.rahullohra.fakeresponse.data.diProvider.fragments

import androidx.lifecycle.ViewModelProvider
import com.rahullohra.fakeresponse.data.diProvider.DiProvider
import com.rahullohra.fakeresponse.data.diProvider.vm.VMFactory
import com.rahullohra.fakeresponse.domain.repository.LocalRepository
import com.rahullohra.fakeresponse.domain.repository.RestRepository
import com.rahullohra.fakeresponse.domain.usecases.ShowRecordsUseCase
import com.rahullohra.fakeresponse.domain.usecases.UpdateGqlUseCase
import com.rahullohra.fakeresponse.presentaiton.fragments.HomeFragment
import com.rahullohra.fakeresponse.presentaiton.viewmodels.FakeResponseModel
import kotlinx.coroutines.Dispatchers

class GqlFragmentProvider : DiProvider<HomeFragment> {

    override fun inject(t: HomeFragment) {
        t.activity?.let { activity ->
            val workerDispatcher = Dispatchers.IO

            val dao = getDatabase(activity).gqlDao()
            val repository = LocalRepository(dao)

            val restDao = getDatabase(activity).restDao()
            val restRepository = RestRepository(restDao)

            val showGqlUseCase = ShowRecordsUseCase(repository, restRepository)
            val usecase = UpdateGqlUseCase(repository, restRepository)
            val list = arrayOf(workerDispatcher, showGqlUseCase, usecase)
            val vmFactory = ViewModelProvider(
                t,
                VMFactory(activity.application, list)
            )
            val vm = vmFactory[FakeResponseModel::class.java]
            t.viewModel = vm
        }
    }
}