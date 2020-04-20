package com.tokopedia.fakeresponse.data.diProvider.activities

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.fakeresponse.data.diProvider.DiProvider
import com.tokopedia.fakeresponse.data.diProvider.vm.VMFactory
import com.tokopedia.fakeresponse.domain.repository.GqlRepository
import com.tokopedia.fakeresponse.domain.repository.RestRepository
import com.tokopedia.fakeresponse.domain.usecases.ImportUseCase
import com.tokopedia.fakeresponse.presentation.activities.PasteTextActivity
import com.tokopedia.fakeresponse.presentation.viewmodels.PasteTextViewModel
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