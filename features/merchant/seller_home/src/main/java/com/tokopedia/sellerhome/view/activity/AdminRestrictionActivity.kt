package com.tokopedia.sellerhome.view.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.bottomsheet.AdminRestrictionBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify

class AdminRestrictionActivity: BaseSimpleActivity() {

    private val bottomSheet by lazy {
        AdminRestrictionBottomSheet.createInstance(this)
    }

    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sah_transparent_base)
        setOrientation()
    }

    override fun onStart() {
        super.onStart()
        showBottomSheet()
    }

    private fun showBottomSheet() {
        (bottomSheet as? BottomSheetUnify)?.setOnDismissListener {
            finish()
        }
        bottomSheet.show(supportFragmentManager)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

}