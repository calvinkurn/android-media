package com.tokopedia.kyc_centralized.view.activity

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
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationFormFaceFragment
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationFormFinalFragment
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationFormKtpFragment
import com.tokopedia.kyc_centralized.view.model.UserIdentificationStepperModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.BODY_2
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.analytics.UserIdentificationCommonAnalytics
import com.tokopedia.kyc_centralized.view.customview.fragment.NotFoundFragment.Companion.createInstance
import kotlin.collections.ArrayList

/**
 * @author by alvinatin on 02/11/18.
 */
class UserIdentificationFormActivity : BaseStepperActivity() {
    private var fragmentList: ArrayList<Fragment> = arrayListOf()
    private var snackbar: SnackbarRetry? = null
    private var projectId = -1
    private var analytics: UserIdentificationCommonAnalytics? = null

    interface Listener {
        fun trackOnBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            projectId = intent.data?.getQueryParameter(ApplinkConstInternalGlobal.PARAM_PROJECT_ID).toIntOrZero()
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId)
        } catch (e: NumberFormatException) {
            projectId = KYCConstant.STATUS_DEFAULT
        } catch (e: NullPointerException) {
            projectId = KYCConstant.STATUS_DEFAULT
        } catch (e: Exception) {
            e.printStackTrace()
        }
        analytics = UserIdentificationCommonAnalytics.createInstance(projectId)
        stepperModel = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(STEPPER_MODEL_EXTRA)
        } else {
            createNewStepperModel()
        }
        super.onCreate(savedInstanceState)
        toolbar.setTitleTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STEPPER_MODEL_EXTRA, stepperModel)
    }

    private fun createNewStepperModel(): StepperModel {
        return UserIdentificationStepperModel()
    }

    override fun getListFragment(): List<Fragment> {
        return if (projectId == KYCConstant.STATUS_DEFAULT) {
            val notFoundList = ArrayList<Fragment>()
            notFoundList.add(createInstance())
            notFoundList
        } else {
            if(fragmentList.isEmpty()) {
                fragmentList.add(UserIdentificationFormKtpFragment.createInstance())
                fragmentList.add(UserIdentificationFormFaceFragment.createInstance())
                fragmentList.add(UserIdentificationFormFinalFragment.createInstance(projectId))
            }
            fragmentList
        }
    }

    override fun setupFragment(savedinstancestate: Bundle?) {
        if (listFragment.size >= currentPosition) {
            val fragment = listFragment[currentPosition - 1]
            val fragmentArguments = fragment.arguments
            val bundle: Bundle
            bundle = fragmentArguments ?: Bundle()
            bundle.putParcelable(STEPPER_MODEL_EXTRA, stepperModel)
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                    .replace(parentView, fragment, fragment.javaClass.simpleName)
                    .commit()
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
            val fragment = supportFragmentManager.findFragmentById(fragmentId) as UserIdentificationFormFinalFragment?
            fragment?.clickBackAction()
            showDocumentAlertDialog(fragment)
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
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setTextViewWithBullet(text: String, context: Context, layout: LinearLayout) {
        val tv = Typography(context)
        val span = SpannableString(text)
        val radius = dpToPx(4)
        val gapWidth = dpToPx(12)
        val color = ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_N100, null)
        val bulletSpan: BulletSpan
        bulletSpan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            BulletSpan(gapWidth, color, radius)
        } else {
            BulletSpan(gapWidth, color)
        }
        span.setSpan(bulletSpan, 0, text.length, 0)
        tv.setType(BODY_2)
        tv.text = span
        tv.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val margin = dpToPx(8)
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

    companion object {
        @JvmField
        var isSupportedLiveness = true
        fun getIntent(context: Context?): Intent {
            val intent = Intent(context, UserIdentificationFormActivity::class.java)
            val bundle = Bundle()
            intent.putExtras(bundle)
            return intent
        }
    }
}