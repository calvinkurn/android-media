package com.tokopedia.epharmacy.ui.activity

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.epharmacy.databinding.EpharmacyMiniConsultationTransparentActivityBinding
import com.tokopedia.epharmacy.ui.bottomsheet.MiniConsultationMasterBottomSheetInfo
import com.tokopedia.kotlin.extensions.view.EMPTY

class EPharmacyMiniConsultationTransparentActivity : BaseActivity() {

    private var enablerName = String.EMPTY
    private var dataType = String.EMPTY

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
        val pathSegments = Uri.parse(intent.data?.path.orEmpty()).pathSegments
        enablerName = if (pathSegments.size > 1) pathSegments[1].orEmpty() else String.EMPTY
        dataType = if (pathSegments.size > 2) pathSegments[2].orEmpty() else String.EMPTY
    }

    private fun openBottomSheet() {
        if (enablerName.isNotBlank() && dataType.isNotBlank()) {
            MiniConsultationMasterBottomSheetInfo.newInstance(dataType, enablerName).show(supportFragmentManager, "")
        } else {
            finish()
        }
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
