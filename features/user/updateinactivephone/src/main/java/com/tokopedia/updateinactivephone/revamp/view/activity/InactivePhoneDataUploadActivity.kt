package com.tokopedia.updateinactivephone.revamp.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.view.fragment.InactivePhoneDataUploadFragment

class InactivePhoneDataUploadActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return InactivePhoneDataUploadFragment()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        updateTitle(getString(R.string.text_title))
        toolbar.setTitleTextAppearance(this, R.style.BoldToolbar)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_inactive_phone)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            elevation = 0f
        }
    }

    @SuppressLint("DialogUnifyUsage")
    override fun onBackPressed() {
        DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.text_exit_title))
            setDescription(getString(R.string.text_exit_description))
            setPrimaryCTAText(getString(R.string.text_exit_cta_primary))
            setSecondaryCTAText(getString(R.string.text_exit_cta_secondary))
            setPrimaryCTAClickListener {
                this.dismiss()
                gotoOnboardingPage()
                finish()
            }
            setSecondaryCTAClickListener {
                this.dismiss()
            }
            setCancelable(false)
            setOverlayClose(false)
        }.show()
    }

    private fun gotoOnboardingPage() {
        startActivity(InactivePhoneActivity.getIntent(this))
    }

    companion object {
        private const val KEY_SOURCE = "source"

        fun getIntent(context: Context, source: String): Intent {
            val bundle = Bundle()
            val intent = Intent(context, InactivePhoneDataUploadActivity::class.java)
            bundle.putString(KEY_SOURCE, source)
            intent.putExtras(bundle)
            return intent
        }
    }
}
