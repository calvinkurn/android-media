package com.rahullohra.fakeresponse.data.diProvider.fragments

import androidx.lifecycle.ViewModelProvider
import com.rahullohra.fakeresponse.data.diProvider.DiProvider
import com.rahullohra.fakeresponse.data.diProvider.vm.VMFactory
import com.rahullohra.fakeresponse.domain.repository.LocalRepository
import com.rahullohra.fakeresponse.domain.usecases.ShowGqlUseCase
import com.rahullohra.fakeresponse.domain.usecases.UpdateGqlUseCase
import com.rahullohra.fakeresponse.presentaiton.fragments.GqlFragment
import com.rahullohra.fakeresponse.presentaiton.viewmodels.FakeResponseModel
import kotlinx.coroutines.Dispatchers

class GqlFragmentProvider : DiProvider<GqlFragment> {

    override fun inject(t: GqlFragment) {
        t.activity?.let { activity ->
            val workerDispatcher = Dispatchers.IO

            val dao = getDatabase(activity).gqlDao()
            val repository = LocalRepository(dao)
            val showGqlUseCase = ShowGqlUseCase(repository)
            val usecase = UpdateGqlUseCase(repository)
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