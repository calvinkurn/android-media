package com.tokopedia.fakeresponse.data.diProvider.fragments

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.fakeresponse.data.diProvider.DiProvider
import com.tokopedia.fakeresponse.data.diProvider.vm.VMFactory
import com.tokopedia.fakeresponse.domain.repository.GqlRepository
import com.tokopedia.fakeresponse.domain.repository.RemoteSqliteRepository
import com.tokopedia.fakeresponse.domain.repository.RestRepository
import com.tokopedia.fakeresponse.domain.usecases.DownloadSqliteUseCase
import com.tokopedia.fakeresponse.domain.usecases.ShowRecordsUseCase
import com.tokopedia.fakeresponse.presentation.fragments.FakeResponseFragment
import com.tokopedia.fakeresponse.presentation.viewmodels.FakeResponseVM
import kotlinx.coroutines.Dispatchers

class FakeResponseFragmentProvider :
    DiProvider<FakeResponseFragment> {
    override fun inject(t: FakeResponseFragment) {
        t.activity?.let { activity ->
            val workerDispatcher = Dispatchers.IO

            val restDao = getDatabase(activity).restDao()
            val restRepository = RestRepository(restDao)

            val localRepository = GqlRepository(getDatabase(activity).gqlDao())

            val showGqlUseCase = ShowRecordsUseCase(localRepository, restRepository)
            val useCase = DownloadSqliteUseCase(RemoteSqliteRepository())
            val list = arrayOf(workerDispatcher, showGqlUseCase, useCase)
            val vmFactory = ViewModelProvider(
                t,
                VMFactory(activity.application, list)
            )
            val vm = vmFactory[FakeResponseVM::class.java]
            t.viewModel = vm

        }
    }
}