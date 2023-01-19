package com.tokopedia.kyc_centralized.ui.tokoKyc.form

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.BulletSpan
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.model.StepperModel
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_KYC_TYPE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.analytics.UserIdentificationCommonAnalytics
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KYCConstant.LIVENESS_TAG
import com.tokopedia.kyc_centralized.common.KycStatus
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.UserIdentificationCommonComponent
import com.tokopedia.kyc_centralized.util.KycCleanupStorageWorker
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.stepper.UserIdentificationStepperModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.BODY_2
import com.tokopedia.utils.file.FileUtil
import timber.log.Timber

/**
 * @author by alvinatin on 02/11/18.
 */
class UserIdentificationFormActivity : BaseStepperActivity(),
    HasComponent<UserIdentificationCommonComponent> {
    private var fragmentList: ArrayList<Fragment> = arrayListOf()
    private var snackbar: SnackbarRetry? = null
    private var projectId = -1
    private var kycType = ""
    private var analytics: UserIdentificationCommonAnalytics? = null

    interface Listener {
        fun trackOnBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        intent?.data?.let {
            projectId = it.getQueryParameter(PARAM_PROJECT_ID)?.toIntOrZero() ?: KycStatus.DEFAULT.code
            kycType = it.getQueryParameter(PARAM_KYC_TYPE).orEmpty()
            intent.putExtra(PARAM_PROJECT_ID, projectId)
        }

        if (kycType.isEmpty()) {
            kycType = intent?.extras?.getString(PARAM_KYC_TYPE).orEmpty()
        }

        analytics = UserIdentificationCommonAnalytics.createInstance(projectId)
        stepperModel = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(STEPPER_MODEL_EXTRA)
        } else {
            createNewStepperModel()
        }
        super.onCreate(savedInstanceState)
        toolbar.setTitleTextColor(
            MethodChecker.getColor(
                this,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_96
            )
        )
        KycCleanupStorageWorker.scheduleWorker(this, externalCacheDir?.absolutePath + FILE_NAME_KYC)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STEPPER_MODEL_EXTRA, stepperModel)
    }

    private fun createNewStepperModel(): StepperModel {
        return UserIdentificationStepperModel()
    }

    override fun getListFragment(): List<Fragment> {
        return if (projectId == KycStatus.DEFAULT.code) {
           listOf(NotFoundFragment.createInstance())
        } else {
            if (fragmentList.isEmpty()) {
                fragmentList.add(UserIdentificationFormKtpFragment.createInstance(kycType))
                fragmentList.add(UserIdentificationFormFaceFragment.createInstance(kycType))
                fragmentList.add(UserIdentificationFormFinalFragment.createInstance(projectId, kycType))
            }
            fragmentList
        }
    }

    /**
     * Hacky solution to mitigate Fragment initialization issue in BaseStepperActivity
     */
    override fun setupFragment(savedinstancestate: Bundle?) {
        val actualPosition = currentPosition - 1
        if (listFragment.size >= currentPosition && actualPosition >= 0) {
            val fragment = when (listFragment[actualPosition]) {
                is UserIdentificationFormKtpFragment -> {
                    UserIdentificationFormKtpFragment.createInstance(kycType)
                }
                is UserIdentificationFormFaceFragment -> {
                    UserIdentificationFormFaceFragment.createInstance(kycType)
                }
                is UserIdentificationFormFinalFragment -> {
                    UserIdentificationFormFinalFragment.createInstance(projectId, kycType)
                }
                is NotFoundFragment -> {
                    NotFoundFragment.createInstance()
                }
                else -> {
                    throw Exception()
                }
            }

            if (fragmentList.isNotEmpty()) {
                fragmentList[actualPosition] = fragment
            }

            val stepperBundle = Bundle().apply {
                putParcelable(STEPPER_MODEL_EXTRA, stepperModel)
            }
            fragment.arguments?.putAll(stepperBundle)

            if (savedinstancestate == null) {
                supportFragmentManager.beginTransaction()
                    .replace(parentView, fragment, fragment.javaClass.simpleName)
                    .commit()
            }
        }
    }

    fun showError(error: String?, retryClickedListener: NetworkErrorHelper.RetryClickedListener?) {
        snackbar = NetworkErrorHelper.createSnackbarWithAction(this, error, retryClickedListener)
        snackbar?.showRetrySnackbar()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (snackbar?.isShown == true) {
            snackbar?.hideRetrySnackbar()
        }
    }

    override fun onBackEvent() {
        if (listFragment.size == currentPosition) {
            val fragmentId = listFragment[currentPosition - 1].id
            val fragment = supportFragmentManager.findFragmentById(fragmentId)
            if (fragment is UserIdentificationFormFinalFragment) {
                fragment.clickBackAction()
                showDocumentAlertDialog(fragment)
            } else {
                backToPreviousFragment()
            }
        } else {
            backToPreviousFragment()
        }
    }

    private fun showDocumentAlertDialog(fragment: UserIdentificationFormFinalFragment?) {
        val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.kyc_dialog_title))
        dialog.setDescription(getString(R.string.kyc_dialog_description))
        dialog.setPrimaryCTAText(getString(R.string.kyc_dialog_primary_button))
        dialog.setSecondaryCTAText(getString(R.string.kyc_dialog_secondary_button))
        dialog.setPrimaryCTAClickListener {
            analytics?.eventClickDialogStay()
            dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            analytics?.eventClickDialogExit()
            fragment?.deleteTmpFile(deleteKtp = true, deleteFace = true)
            dialog.dismiss()
            setResult(KYCConstant.USER_EXIT)
            finish()
        }
        dialog.show()
    }

    private fun backToPreviousFragment() {
        if (listFragment.isNotEmpty() && listFragment[currentPosition - 1] is Listener) {
            (listFragment[currentPosition - 1] as Listener).trackOnBackPressed()
        }
        super.onBackEvent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (snackbar?.isShown == true) {
                    snackbar?.hideRetrySnackbar()
                }
                onBackEvent()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getComponent(): UserIdentificationCommonComponent {
        return ActivityComponentFactory.instance.createActivityComponent(this)
    }

    fun setTextViewWithBullet(text: String, context: Context, layout: LinearLayout) {
        val tv = Typography(context)
        val span = SpannableString(text)
        val radius = dpToPx(RADIUS_FOUR)
        val gapWidth = dpToPx(GAP_WIDTH)
        val color = ResourcesCompat.getColor(
            resources,
            com.tokopedia.unifyprinciples.R.color.Unify_N100,
            null
        )
        val bulletSpan: BulletSpan
        bulletSpan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            BulletSpan(gapWidth, color, radius)
        } else {
            BulletSpan(gapWidth, color)
        }
        span.setSpan(bulletSpan, 0, text.length, 0)
        tv.setType(BODY_2)
        tv.text = span
        tv.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val margin = dpToPx(MARGIN_EIGHT)
        setMargins(tv, 0, 0, 0, margin)
        layout.addView(tv)
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //Delete KYC folder immediately, if onDestroy is not called, we rely on worker
        if (isFinishing) {
            Timber.d("$LIVENESS_TAG: onDestroy Delete")
            FileUtil.deleteFolder(externalCacheDir?.absolutePath + FILE_NAME_KYC)
        }
    }

    companion object {
        @JvmField
        var isSupportedLiveness = true
        const val FILE_NAME_KYC = "/KYC"
        fun getIntent(context: Context?): Intent {
            val intent = Intent(context, UserIdentificationFormActivity::class.java)
            val bundle = Bundle()
            intent.putExtras(bundle)
            return intent
        }

        private const val RADIUS_FOUR = 4
        private const val GAP_WIDTH = 12
        private const val MARGIN_EIGHT = 8
    }
}
