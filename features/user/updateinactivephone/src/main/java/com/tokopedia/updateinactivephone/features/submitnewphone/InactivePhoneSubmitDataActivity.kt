package com.tokopedia.updateinactivephone.features.submitnewphone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker
import com.tokopedia.updateinactivephone.features.onboarding.regular.InactivePhoneRegularActivity
import com.tokopedia.updateinactivephone.features.submitnewphone.regular.InactivePhoneDataUploadFragment
import com.tokopedia.updateinactivephone.features.submitnewphone.withpin.InactivePhoneSubmitNewPhoneFragment

class InactivePhoneSubmitDataActivity : BaseSimpleActivity() {

    private var tracker: InactivePhoneTracker = InactivePhoneTracker()
    private var inactivePhoneUserDataModel: InactivePhoneUserDataModel? = null

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent?.extras != null) {
            bundle.putAll(intent?.extras)
        }

        val isPinFlow = intent?.getStringExtra(KEY_SOURCE).toString() == InactivePhoneConstant.EXPEDITED
        return if (isPinFlow) {
            InactivePhoneSubmitNewPhoneFragment.create(bundle)
        } else {
            InactivePhoneDataUploadFragment.create(bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.extras?.let {
            inactivePhoneUserDataModel = it.getParcelable(InactivePhoneConstant.PARAM_USER_DATA)
        }
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
        tracker.clickOnBackButtonUploadData()

        DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.text_exit_title))
            setDescription(getString(R.string.text_exit_description))
            setPrimaryCTAText(getString(R.string.text_exit_cta_primary))
            setSecondaryCTAText(getString(R.string.text_exit_cta_secondary))
            setPrimaryCTAClickListener {
                this.dismiss()
                tracker.clickOnExitButtonPopupUploadData()
                gotoOnboardingPage()
            }
            setSecondaryCTAClickListener {
                this.dismiss()
            }
            setCancelable(false)
            setOverlayClose(false)
        }.show()
    }

    private fun gotoOnboardingPage() {
        inactivePhoneUserDataModel?.let {
            startActivity(InactivePhoneRegularActivity.createIntent(this, it))
        }
        finish()
    }

    companion object {
        private const val KEY_SOURCE = "source"

        fun getIntent(context: Context, source: String, inactivePhoneUserDataModel: InactivePhoneUserDataModel?): Intent {
            val bundle = Bundle()
            val intent = Intent(context, InactivePhoneSubmitDataActivity::class.java)
            bundle.putString(KEY_SOURCE, source)
            bundle.putParcelable(InactivePhoneConstant.PARAM_USER_DATA, inactivePhoneUserDataModel)
            intent.putExtras(bundle)
            return intent
        }
    }
}
