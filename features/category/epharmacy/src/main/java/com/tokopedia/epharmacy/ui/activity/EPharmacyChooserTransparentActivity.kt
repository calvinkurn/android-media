package com.tokopedia.epharmacy.ui.activity

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.epharmacy.databinding.EpharmacyMiniConsultationTransparentActivityBinding
import com.tokopedia.epharmacy.ui.bottomsheet.EPharmacyChooserBottomSheet
import com.tokopedia.epharmacy.utils.ENABLER_IMAGE_URL
import com.tokopedia.epharmacy.utils.EPHARMACY_CONSULTATION_SOURCE_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_CONS_DURATION
import com.tokopedia.epharmacy.utils.EPHARMACY_CONS_PRICE
import com.tokopedia.epharmacy.utils.EPHARMACY_ENABLER_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_ENABLER_NAME
import com.tokopedia.epharmacy.utils.EPHARMACY_GROUP_ID

class EPharmacyChooserTransparentActivity : BaseActivity() {

    private var enableImageURL = ""
    private var groupId = ""
    private var enablerName = ""
    private var consultationSourceId = 0L
    private var tokoConsultationId = ""
    private var duration = ""
    private var price = ""

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
        consultationSourceId = intent.extras?.getLong(EPHARMACY_ENABLER_ID) ?: 0L
        tokoConsultationId = intent.extras?.getString(EPHARMACY_CONSULTATION_SOURCE_ID) ?: ""
        price = intent.extras?.getString(EPHARMACY_CONS_PRICE) ?: ""
        duration = intent.extras?.getString(EPHARMACY_CONS_DURATION) ?: ""
    }

    private fun openBottomSheet() {
        EPharmacyChooserBottomSheet.newInstance(enableImageURL, groupId, enablerName, consultationSourceId, tokoConsultationId, price, duration).show(supportFragmentManager, EPharmacyChooserBottomSheet::class.simpleName)
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
