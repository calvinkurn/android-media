package com.tokopedia.epharmacy.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.epharmacy.databinding.EpharmacyMasterMiniConsultationBottomSheetBinding
import com.tokopedia.epharmacy.databinding.EpharmacyMiniConsultationTransparentActivityBinding
import com.tokopedia.epharmacy.ui.bottomsheet.MiniConsultationMasterBottomSheetInfo

class EPharmacyMiniConsultationTransparentActivity : BaseActivity() {

    private val binding: EpharmacyMiniConsultationTransparentActivityBinding by lazy {
        EpharmacyMiniConsultationTransparentActivityBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        val dataType = intent.getStringExtra(DATA_TYPE)
        val enabler = intent.getStringExtra(ENABLER_NAME)
        if (dataType != null && enabler != null) {
            MiniConsultationMasterBottomSheetInfo.newInstance(dataType, enabler).show(supportFragmentManager,"")
        }else{
            finish()
        }
    }

    companion object{
        private const val DATA_TYPE = "data_type"
        private const val ENABLER_NAME = "enabler_name"
    }
}
