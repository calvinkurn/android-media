package com.tokopedia.shareexperience.ui

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.shareexperience.R
import com.tokopedia.unifycomponents.LoaderUnify

class ShareExLoadingActivity: BaseActivity() {

    private var bottomSheet: ShareExBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shareexperience_loading_activity)
        removeActivityAnimation() // Remove open activity animation
        initBottomSheet()
    }

    private fun initBottomSheet() {
        if (bottomSheet == null) {
            bottomSheet = ShareExBottomSheet()
            bottomSheet?.setOnDismissListener {
                finishActivityWithoutAnimation()
            }
            //TODO: Add viewmodel, hit BE, then open BS after get data
            openBottomSheet()
        }
    }

    private fun openBottomSheet() {
        findViewById<LoaderUnify>(R.id.shareex_loader).hide()
        findViewById<View>(R.id.shareex_dim_overlay).hide()
        bottomSheet?.show(supportFragmentManager, "")
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
