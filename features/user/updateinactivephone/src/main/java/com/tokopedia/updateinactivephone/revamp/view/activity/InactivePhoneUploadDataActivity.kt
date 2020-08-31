package com.tokopedia.updateinactivephone.revamp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.FragmentTransactionInterface
import com.tokopedia.updateinactivephone.revamp.common.IOnBackPressed
import com.tokopedia.updateinactivephone.revamp.common.replaceFragment
import com.tokopedia.updateinactivephone.revamp.view.fragment.InactivePhoneOnboardingFragment
import com.tokopedia.updateinactivephone.revamp.view.fragment.InactivePhoneUploadDataFragment

class InactivePhoneUploadDataActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val source = intent?.extras?.getString(KEY_SOURCE) ?: ""
        return InactivePhoneUploadDataFragment.instance(source)
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        updateTitle(getString(R.string.text_title))
        toolbar.setTitleTextAppearance(this, R.style.BoldToolbar)
    }

    override fun onBackPressed() {
        DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.text_exit_title))
            setDescription(getString(R.string.text_exit_description))
            setPrimaryCTAText(getString(R.string.text_exit_cta_primary))
            setSecondaryCTAText(getString(R.string.text_exit_cta_secondary))
            setPrimaryCTAClickListener {
                this.dismiss()
                super.onBackPressed()
            }
            setSecondaryCTAClickListener {
                this.dismiss()
            }
            setCancelable(false)
            setOverlayClose(false)
        }.show()
    }

    companion object {
        private const val KEY_SOURCE = "source"

        fun getIntent(context: Context, source: String): Intent {
            val bundle = Bundle()
            val intent = Intent(context, InactivePhoneUploadDataActivity::class.java)
            bundle.putString(KEY_SOURCE, source)
            intent.putExtras(bundle)
            return intent
        }
    }
}
