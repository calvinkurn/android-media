package com.tokopedia.epharmacy.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.epharmacy.databinding.EpharmacyMasterMiniConsultationBottomSheetBinding

class EPharmacyMiniConsultationTransparentActivity : BaseActivity() {

    private val binding: EpharmacyMasterMiniConsultationBottomSheetBinding by lazy {
        EpharmacyMasterMiniConsultationBottomSheetBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
    }
}
