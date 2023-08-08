package com.tokopedia.epharmacy.ui.activity

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.epharmacy.databinding.EpharmacyMiniConsultationTransparentActivityBinding
import com.tokopedia.epharmacy.ui.bottomsheet.EPharmacyChooserBottomSheet
import com.tokopedia.epharmacy.utils.ENABLER_IMAGE_URL
import com.tokopedia.epharmacy.utils.EPHARMACY_CONS_DURATION
import com.tokopedia.epharmacy.utils.EPHARMACY_CONS_PRICE
import com.tokopedia.epharmacy.utils.EPHARMACY_ENABLER_NAME
import com.tokopedia.epharmacy.utils.EPHARMACY_GROUP_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_IS_ONLY_CONSULT
import com.tokopedia.epharmacy.utils.EPHARMACY_NOTE

class EPharmacyChooserTransparentActivity : BaseActivity() {

    private var enableImageURL = ""
    private var groupId = ""
    private var enablerName = ""
    private var duration = ""
    private var price = ""
    private var note = ""
    private var isOnlyConsultation = false

    private val binding: EpharmacyMiniConsultationTransparentActivityBinding by lazy {
        EpharmacyMiniConsultationTransparentActivityBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        adjustOrientation()
        extractParameters()
        openBottomSheet()
    }

    private fun extractParameters() {
        enableImageURL = intent.extras?.getString(ENABLER_IMAGE_URL) ?: ""
        groupId = intent.extras?.getString(EPHARMACY_GROUP_ID) ?: ""
        enablerName = intent.extras?.getString(EPHARMACY_ENABLER_NAME) ?: ""
        price = intent.extras?.getString(EPHARMACY_CONS_PRICE) ?: ""
        duration = intent.extras?.getString(EPHARMACY_CONS_DURATION) ?: ""
        note = intent.extras?.getString(EPHARMACY_NOTE) ?: ""
        isOnlyConsultation = intent.extras?.getBoolean(EPHARMACY_IS_ONLY_CONSULT) ?: false
    }

    private fun openBottomSheet() {
        EPharmacyChooserBottomSheet.newInstance(enableImageURL, groupId, enablerName, price, duration, note, isOnlyConsultation).show(supportFragmentManager, EPharmacyChooserBottomSheet::class.simpleName)
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
