package com.tokopedia.fakeresponse.data.diProvider.fragments

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.fakeresponse.data.diProvider.DiProvider
import com.tokopedia.fakeresponse.data.diProvider.vm.VMFactory
import com.tokopedia.fakeresponse.domain.repository.RemoteSqliteRepository
import com.tokopedia.fakeresponse.domain.usecases.DownloadSqliteUseCase
import com.tokopedia.fakeresponse.presentation.fragments.DownloadFragment
import com.tokopedia.fakeresponse.presentation.viewmodels.DownloadFragmentVM
import kotlinx.coroutines.Dispatchers

class DownloadFragmentProvider :
    DiProvider<DownloadFragment> {

    override fun inject(t: DownloadFragment) {
        t.activity?.let { activity ->
            val uiDispatcher = Dispatchers.Main
            val workerDispatcher = Dispatchers.IO

            val repository = RemoteSqliteRepository()
            val usecase = DownloadSqliteUseCase(repository)
            val list = arrayOf(uiDispatcher, workerDispatcher, usecase)
            val vmFactory = ViewModelProvider(t, VMFactory(activity.application, list))
            val vm = vmFactory[DownloadFragmentVM::class.java]
            t.viewModel = vm
        }
    }
}