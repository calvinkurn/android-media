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
import com.tokopedia.epharmacy.utils.EPHARMACY_IS_OUTSIDE_WORKING_HOURS
import com.tokopedia.epharmacy.utils.EPHARMACY_NOTE
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY

class EPharmacyChooserTransparentActivity : BaseActivity() {

    private var enableImageURL = String.EMPTY
    private var groupId = String.EMPTY
    private var enablerName = String.EMPTY
    private var duration = String.EMPTY
    private var price = String.EMPTY
    private var note = String.EMPTY
    private var isOutsideWorkingHours = false
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
        enableImageURL = intent.extras?.getString(ENABLER_IMAGE_URL).orEmpty()
        groupId = intent.data?.getQueryParameter(EPHARMACY_GROUP_ID).orEmpty()
        enablerName = intent.data?.getQueryParameter(EPHARMACY_ENABLER_NAME).orEmpty()
        price = intent.data?.getQueryParameter(EPHARMACY_CONS_PRICE).orEmpty()
        duration = intent.data?.getQueryParameter(EPHARMACY_CONS_DURATION).orEmpty()
        note = intent.data?.getQueryParameter(EPHARMACY_NOTE).orEmpty()
        isOutsideWorkingHours = intent.data?.getBooleanQueryParameter(EPHARMACY_IS_OUTSIDE_WORKING_HOURS, false).orFalse()
        isOnlyConsultation = intent.data?.getBooleanQueryParameter(EPHARMACY_IS_ONLY_CONSULT, false).orFalse()
    }

    private fun openBottomSheet() {
        EPharmacyChooserBottomSheet.newInstance(enableImageURL, groupId, enablerName, price, duration, note, isOutsideWorkingHours, isOnlyConsultation).show(supportFragmentManager, EPharmacyChooserBottomSheet::class.simpleName)
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
