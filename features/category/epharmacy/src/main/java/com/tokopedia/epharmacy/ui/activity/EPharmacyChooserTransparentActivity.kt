package com.tokopedia.epharmacy.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.epharmacy.databinding.EpharmacyMiniConsultationTransparentActivityBinding
import com.tokopedia.epharmacy.ui.fragment.EPharmacyChooserBottomSheet
import com.tokopedia.epharmacy.utils.ENABLER_IMAGE_URL
import com.tokopedia.epharmacy.utils.EPHARMACY_GROUP_ID

class EPharmacyChooserTransparentActivity : BaseActivity() {

    private var enableImageURL = ""
    private var groupId = ""

    private val binding: EpharmacyMiniConsultationTransparentActivityBinding by lazy {
        EpharmacyMiniConsultationTransparentActivityBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        extractParameters()
        openBottomSheet()
    }

    private fun extractParameters() {
        enableImageURL = intent.extras?.getString(ENABLER_IMAGE_URL) ?: ""
        groupId = intent.extras?.getString(EPHARMACY_GROUP_ID) ?: ""
    }

    private fun openBottomSheet() {
        EPharmacyChooserBottomSheet.newInstance(enableImageURL, groupId).show(supportFragmentManager, EPharmacyChooserBottomSheet::class.simpleName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
