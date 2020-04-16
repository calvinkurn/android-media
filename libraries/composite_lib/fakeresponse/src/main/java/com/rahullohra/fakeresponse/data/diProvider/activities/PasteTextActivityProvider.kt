package com.rahullohra.fakeresponse.data.diProvider.activities

import androidx.lifecycle.ViewModelProvider
import com.rahullohra.fakeresponse.data.diProvider.DiProvider
import com.rahullohra.fakeresponse.data.diProvider.vm.VMFactory
import com.rahullohra.fakeresponse.domain.repository.GqlRepository
import com.rahullohra.fakeresponse.domain.repository.RestRepository
import com.rahullohra.fakeresponse.domain.usecases.ImportUseCase
import com.rahullohra.fakeresponse.presentation.activities.PasteTextActivity
import com.rahullohra.fakeresponse.presentation.viewmodels.PasteTextViewModel
import kotlinx.coroutines.Dispatchers

class PasteTextActivityProvider : DiProvider<PasteTextActivity> {

    override fun inject(t: PasteTextActivity) {
        val workerDispatcher = Dispatchers.IO

        val db = getDatabase(t)
        val restRepository = RestRepository(db.restDao())
        val gqlRepository = GqlRepository(db.gqlDao())

        val importUseCase = ImportUseCase(restRepository, gqlRepository)

        val list = arrayOf(workerDispatcher, importUseCase)
        val vmFactory = ViewModelProvider(t, VMFactory(t.application, list))
        val vm = vmFactory[PasteTextViewModel::class.java]
        t.viewModel = vm
    }
}