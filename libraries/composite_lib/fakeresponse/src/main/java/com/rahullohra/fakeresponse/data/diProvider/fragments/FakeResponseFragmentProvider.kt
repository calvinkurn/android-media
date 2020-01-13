package com.rahullohra.fakeresponse.data.diProvider.fragments

import androidx.lifecycle.ViewModelProvider
import com.rahullohra.fakeresponse.data.diProvider.DiProvider
import com.rahullohra.fakeresponse.data.diProvider.vm.VMFactory
import com.rahullohra.fakeresponse.domain.repository.LocalRepository
import com.rahullohra.fakeresponse.domain.repository.RemoteSqliteRepository
import com.rahullohra.fakeresponse.domain.usecases.DownloadSqliteUseCase
import com.rahullohra.fakeresponse.domain.usecases.ShowGqlUseCase
import com.rahullohra.fakeresponse.presentaiton.fragments.FakeResponseFragment
import com.rahullohra.fakeresponse.presentaiton.viewmodels.FakeResponseVM
import kotlinx.coroutines.Dispatchers

class FakeResponseFragmentProvider :
    DiProvider<FakeResponseFragment> {
    override fun inject(t: FakeResponseFragment) {
        t.activity?.let { activity ->
            val workerDispatcher = Dispatchers.IO
            val showGqlUseCase = ShowGqlUseCase(LocalRepository(getDatabase(activity).gqlDao()))
            val useCase = DownloadSqliteUseCase(RemoteSqliteRepository())
            val list = arrayOf(workerDispatcher,showGqlUseCase, useCase)
            val vmFactory = ViewModelProvider(
                t,
                VMFactory(activity.application, list)
            )
            val vm = vmFactory[FakeResponseVM::class.java]
            t.viewModel = vm

        }
    }
}