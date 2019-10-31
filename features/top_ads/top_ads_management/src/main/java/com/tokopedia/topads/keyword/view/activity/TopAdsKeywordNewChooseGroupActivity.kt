package com.tokopedia.topads.keyword.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.model.StepperModel
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.R
import com.tokopedia.topads.TopAdsComponentInstance
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordNewAddFragment
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordNewChooseGroupFragment
import com.tokopedia.topads.keyword.view.model.TopAdsKeywordNewStepperModel

class TopAdsKeywordNewChooseGroupActivity : BaseStepperActivity(), HasComponent<TopAdsComponent>,
        TopAdsKeywordNewAddFragment.OnSuccessSaveKeywordListener {

    var fragmentList: MutableList<Fragment>? = null

    companion object {
        private val RESULT_WORDS = "rslt_wrds"
        private val TAG = TopAdsKeywordNewChooseGroupActivity::class.java.simpleName
        private val EXTRA_IS_POSITIVE = "is_pos"
        private val EXTRA_CHOOSEN_GROUP = "EXTRA_CHOOSEN_GROUP"
        private val START_PAGE_POSITION = 1
        private val SAVED_POSITION = "SVD_POS"

        fun createIntent(context: Context, isPositive: Boolean, groupId: String?): Intent {
            val intent = Intent(context, TopAdsKeywordNewChooseGroupActivity::class.java)
            val bundle = Bundle().apply {
                putBoolean(EXTRA_IS_POSITIVE, isPositive)
                putString(EXTRA_CHOOSEN_GROUP, groupId)
            }
            return intent.putExtras(bundle)
        }

        fun start(activity: Activity, requestCode: Int, isPositive: Boolean,
                  groupId: String? = null){
            activity.startActivityForResult(createIntent(activity, isPositive, groupId), requestCode)
        }

        fun start(fragment: Fragment, context: Context, requestCode: Int,
                  isPositive: Boolean, groupId: String? = null){
            fragment.startActivityForResult(createIntent(context, isPositive, groupId), requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        stepperModel = savedInstanceState?.getParcelable(STEPPER_MODEL_EXTRA) ?: createNewStepperModel()
        currentPosition = savedInstanceState?.getInt(SAVED_POSITION) ?: START_PAGE_POSITION
        super.onCreate(savedInstanceState)
    }

    private fun createNewStepperModel(): StepperModel {
        return TopAdsKeywordNewStepperModel().apply {

            if (intent != null) {
                if(intent.data != null){
                    this.groupId = intent?.data?.getQueryParameter(EXTRA_CHOOSEN_GROUP)
                    this.isPositive = intent?.data?.getBooleanQueryParameter(EXTRA_IS_POSITIVE, true) ?: false
                } else if (intent.extras != null){
                    this.groupId = intent?.extras?.getString(EXTRA_CHOOSEN_GROUP)
                    this.isPositive = intent?.extras?.getBoolean(EXTRA_IS_POSITIVE, true) ?: false
                }
            }
        }
    }

    override fun getListFragment(): MutableList<Fragment> {
        fragmentList = fragmentList ?: mutableListOf(TopAdsKeywordNewChooseGroupFragment.newInstance(),
                TopAdsKeywordNewAddFragment.newInstance())
        return fragmentList!!
    }

    override fun getComponent(): TopAdsComponent {
        return TopAdsComponentInstance.getComponent(application)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(STEPPER_MODEL_EXTRA, stepperModel)
        outState?.putInt(SAVED_POSITION, currentPosition)
    }

    private fun setResultAdSaved() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
    }

    override fun finishPage() {
        setResultAdSaved()
        super.finishPage()
    }

    private fun exitConfirmation(): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.parent_view)
        if (fragment != null && fragment is TopAdsKeywordNewAddFragment) {
            if (fragment.isButtonSaveEnabled()) {
                val dialog = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                        .setMessage(getString(R.string.topads_keyword_add_cancel_dialog))
                        .setPositiveButton(getString(R.string.yes)) { dialogInterface, i ->
                            fragment.clearKeywords()
                            super@TopAdsKeywordNewChooseGroupActivity.onBackPressed() }
                        .setNegativeButton(getString(R.string.No)) { arg0, arg1 -> }.create()
                dialog.show()
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        if (!exitConfirmation()) {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == android.R.id.home) {
            onBackPressed()
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSuccessSave(keywords: List<AddKeywordDomainModelDatum>) {
        val intent = Intent()
        intent.putParcelableArrayListExtra(RESULT_WORDS, ArrayList(keywords))
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}