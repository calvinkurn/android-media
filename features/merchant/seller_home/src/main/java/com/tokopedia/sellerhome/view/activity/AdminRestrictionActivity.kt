package com.tokopedia.sellerhome.view.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.bottomsheet.AdminRestrictionBottomSheet

class AdminRestrictionActivity: BaseSimpleActivity() {

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
        getBottomSheet().run {
            setOnDismissListener {
                finish()
            }
            show(supportFragmentManager)
        }
    }

    private fun getBottomSheet(): AdminRestrictionBottomSheet {
        val articleUrl =
            intent.data?.getQueryParameter(ApplinkConstInternalSellerapp.PARAM_ARTICLE_URL)
                .orEmpty()
        return AdminRestrictionBottomSheet.createInstance(this, articleUrl)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

}