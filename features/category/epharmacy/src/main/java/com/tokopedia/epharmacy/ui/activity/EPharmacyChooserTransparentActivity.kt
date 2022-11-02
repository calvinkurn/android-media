package com.tokopedia.epharmacy.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.epharmacy.databinding.EpharmacyMiniConsultationTransparentActivityBinding
import com.tokopedia.epharmacy.ui.fragment.EPharmacyChooserBottomSheet
import com.tokopedia.epharmacy.utils.ENABLER_IMAGE_URL
import com.tokopedia.epharmacy.utils.EXTRA_CHECKOUT_ID_STRING

class EPharmacyChooserTransparentActivity : BaseActivity() {

    private var enableImageURL = ""
    private var checkoutId = ""

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
        checkoutId = intent.extras?.getString(EXTRA_CHECKOUT_ID_STRING) ?: ""
    }

    private fun openBottomSheet(){
        EPharmacyChooserBottomSheet.newInstance(enableImageURL,checkoutId).show(supportFragmentManager,"")
    }
}
