package com.tokopedia.prescription.ui.fragment

import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.prescription.viewmodel.UploadPrescriptionViewModel

class UploadPrescriptionFragment : BaseViewModelFragment<UploadPrescriptionViewModel>() {

    private lateinit var uploadPrescriptionViewModel : UploadPrescriptionViewModel

    override fun getViewModelType(): Class<UploadPrescriptionViewModel> {
        return UploadPrescriptionViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        uploadPrescriptionViewModel = viewModel as UploadPrescriptionViewModel
    }

}