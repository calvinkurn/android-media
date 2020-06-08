package com.tokopedia.fakeresponse.data.diProvider.fragments

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.fakeresponse.data.diProvider.DiProvider
import com.tokopedia.fakeresponse.data.diProvider.vm.VMFactory
import com.tokopedia.fakeresponse.domain.repository.GqlRepository
import com.tokopedia.fakeresponse.domain.repository.RestRepository
import com.tokopedia.fakeresponse.domain.usecases.ShowRecordsUseCase
import com.tokopedia.fakeresponse.domain.usecases.UpdateGqlUseCase
import com.tokopedia.fakeresponse.presentation.fragments.HomeFragment
import com.tokopedia.fakeresponse.presentation.viewmodels.FakeResponseModel
import kotlinx.coroutines.Dispatchers

class GqlFragmentProvider : DiProvider<HomeFragment> {

    override fun inject(t: HomeFragment) {
        t.activity?.let { activity ->
            val workerDispatcher = Dispatchers.IO

            val dao = getDatabase(activity).gqlDao()
            val repository = GqlRepository(dao)

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