package com.tokopedia.prescription.ui.activity

import androidx.fragment.app.Fragment
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.prescription.ui.fragment.UploadPrescriptionFragment
import com.tokopedia.prescription.viewmodel.EPharmacyViewModel

class EPharmacyActivity : BaseViewModelActivity<EPharmacyViewModel>() {

    private lateinit var ePharmacyViewModel : EPharmacyViewModel

    override fun getViewModelType(): Class<EPharmacyViewModel> {
        return EPharmacyViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        ePharmacyViewModel = viewModel as EPharmacyViewModel
    }

    override fun getNewFragment(): Fragment {
        return UploadPrescriptionFragment()
    }

}