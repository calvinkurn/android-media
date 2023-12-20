package com.tokopedia.shareexperience.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.shareexperience.R

class ShareExLoadingActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shareexperience_loading_activity)
        removeActivityAnimation() // Remove open activity animation
        initBottomSheet()
    }

    private fun initBottomSheet() {
        val bottomSheet = ShareExBottomSheet()
        bottomSheet.setOnDismissListener {
            finishActivityWithoutAnimation()
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            bottomSheet.show(supportFragmentManager, "")
            findViewById<View>(R.id.shareex_dim_overlay).hide()
        }, 1000)
    }

    private fun finishActivityWithoutAnimation() {
        finish()
        removeActivityAnimation() // Remove finish activity animation
    }

    private fun removeActivityAnimation() {
        overridePendingTransition(
            R.anim.shareexperience_no_animation_transition,
            R.anim.shareexperience_no_animation_transition
        )
    }
}
