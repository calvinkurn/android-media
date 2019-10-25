package com.tokopedia.wallet.ovoactivation.view

import android.os.Bundle
import androidx.core.content.ContextCompat

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.wallet.R

/**
 * Created by nabillasabbaha on 24/09/18.
 */
abstract class BaseOvoActivationActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCloseButton()
    }

    private fun setCloseButton() {
        supportActionBar!!.setHomeAsUpIndicator(ContextCompat.getDrawable(this,
                com.tokopedia.abstraction.R.drawable.ic_close_default))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.wallet_anim_stay, R.anim.wallet_slide_out_up)
    }
}
