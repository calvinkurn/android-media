package com.rahullohra.fakeresponse.data.diProvider.fragments

import androidx.lifecycle.ViewModelProvider
import com.rahullohra.fakeresponse.data.diProvider.DiProvider
import com.rahullohra.fakeresponse.data.diProvider.vm.VMFactory
import com.rahullohra.fakeresponse.domain.repository.RemoteSqliteRepository
import com.rahullohra.fakeresponse.domain.usecases.DownloadSqliteUseCase
import com.rahullohra.fakeresponse.presentaiton.fragments.DownloadFragment
import com.rahullohra.fakeresponse.presentaiton.viewmodels.DownloadFragmentVM
import kotlinx.coroutines.Dispatchers

class DownloadFragmentProvider :
    DiProvider<DownloadFragment> {

    override fun inject(fragment: DownloadFragment) {
        fragment.activity?.let { activity ->
            val uiDispatcher = Dispatchers.Main
            val workerDispatcher = Dispatchers.IO

            val repository = RemoteSqliteRepository()
            val usecase = DownloadSqliteUseCase(repository)
            val list = arrayOf(uiDispatcher, workerDispatcher, usecase)
            val vmFactory = ViewModelProvider(fragment, VMFactory(activity.application, list))
            val vm = vmFactory[DownloadFragmentVM::class.java]
            fragment.viewModel = vm
        }
    }
}