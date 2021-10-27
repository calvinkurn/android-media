package com.tokopedia.review.feature.inbox.buyerreview.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.review.common.util.ReviewErrorHandler
import com.tokopedia.review.feature.inbox.buyerreview.analytics.AppScreen
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking
import com.tokopedia.review.feature.inbox.buyerreview.di.DaggerReputationComponent
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationReportActivity
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationReport
import com.tokopedia.review.feature.inbox.buyerreview.view.presenter.InboxReputationReportPresenter
import com.tokopedia.review.inbox.R
import javax.inject.Inject

/**
 * @author by nisie on 9/13/17.
 */
class InboxReputationReportFragment : BaseDaggerFragment(),
    InboxReputationReport.View {

    private var sendButton: Button? = null
    private var reportRadioGroup: RadioGroup? = null
    private var otherRadioButton: RadioButton? = null
    private var spamRadioButton: RadioButton? = null
    private var saraRadioButton: RadioButton? = null
    private var otherReason: EditText? = null
    private var progressDialog: ProgressDialog? = null
    private var feedbackId = ""

    @Inject
    lateinit var presenter: InboxReputationReportPresenter

    @Inject
    lateinit var tracking: ReputationTracking

    override fun getScreenName(): String {
        return AppScreen.SCREEN_INBOX_REPUTATION_REPORT
    }

    override fun initInjector() {
        val baseAppComponent: BaseAppComponent =
            (requireContext().applicationContext as BaseMainApplication).baseAppComponent
        val reputationComponent: DaggerReputationComponent = DaggerReputationComponent
            .builder()
            .baseAppComponent(baseAppComponent)
            .build() as DaggerReputationComponent
        reputationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        val parentView: View = inflater.inflate(
            R.layout.fragment_inbox_reputation_report, container,
            false
        )
        sendButton = parentView.findViewById<View>(R.id.send_button) as Button?
        reportRadioGroup = parentView.findViewById<View>(R.id.radio_group) as RadioGroup?
        otherReason = parentView.findViewById<View>(R.id.reason) as EditText?
        otherRadioButton = parentView.findViewById<View>(R.id.report_other) as RadioButton?
        spamRadioButton = parentView.findViewById(R.id.report_spam)
        saraRadioButton = parentView.findViewById(R.id.report_sara)
        prepareView()
        presenter.attachView(this)
        feedbackId = arguments?.getString(InboxReputationReportActivity.ARGS_REVIEW_ID) ?: ""
        return parentView
    }

    private fun prepareView() {
        reportRadioGroup?.setOnCheckedChangeListener { _: RadioGroup?, _: Int -> setSendButton() }
        otherReason?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                otherRadioButton?.isChecked = true
                setSendButton()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        otherReason?.setOnClickListener {
            otherRadioButton?.isChecked = true
        }
        sendButton?.setOnClickListener { v: View? ->
            tracking.onSubmitReportAbuse(feedbackId)
            presenter.reportReview(
                arguments?.getString(
                    InboxReputationReportActivity.ARGS_REVIEW_ID,
                    ""
                ) ?: "",
                arguments?.getString(InboxReputationReportActivity.ARGS_SHOP_ID) ?: "",
                reportRadioGroup?.checkedRadioButtonId ?: 0,
                otherReason?.text.toString()
            )
        }
        initProgressDialog()
    }

    private fun setSendButton() {
        when (reportRadioGroup?.checkedRadioButtonId) {
            R.id.report_spam -> {
                tracking.onClickRadioButtonReportAbuse(spamRadioButton?.text.toString())
            }
            R.id.report_sara -> {
                tracking.onClickRadioButtonReportAbuse(saraRadioButton?.text.toString())
            }
            R.id.report_other -> {
                tracking.onClickRadioButtonReportAbuse(otherRadioButton?.text.toString())
            }
        }
        if ((reportRadioGroup?.checkedRadioButtonId == R.id.report_spam
                    ) || (reportRadioGroup?.checkedRadioButtonId == R.id.report_sara
                    ) || ((reportRadioGroup?.checkedRadioButtonId == R.id.report_other
                    && !TextUtils.isEmpty(otherReason?.text.toString().trim { it <= ' ' })))
        ) {
            sendButton?.isEnabled = true
            sendButton?.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
            MethodChecker.setBackground(
                sendButton,
                resources.getDrawable(com.tokopedia.design.R.drawable.green_button_rounded)
            )
        } else {
            sendButton?.isEnabled = false
            sendButton?.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N500))
            MethodChecker.setBackground(
                sendButton,
                resources.getDrawable(com.tokopedia.design.R.drawable.bg_button_disabled)
            )
        }
    }

    private fun initProgressDialog() {
        if (getContext() != null) {
            progressDialog = ProgressDialog(getContext())
            progressDialog?.setTitle("")
            progressDialog?.setMessage(getContext()?.getString(R.string.progress_dialog_loading))
            progressDialog?.setCancelable(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showLoadingProgress() {
        progressDialog?.let {
            if (it.isShowing) {
                return
            }
            it.show()
        }
    }

    override fun onErrorReportReview(errorMessage: String?) {
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessReportReview() {
        activity?.apply {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun removeLoadingProgress() {
        progressDialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }

    override fun getErrorMessage(throwable: Throwable): String {
        return context?.let { ReviewErrorHandler.getErrorMessage(it, throwable) } ?: ""
    }

    companion object {
        fun createInstance(reviewId: String, shopId: String): Fragment {
            val fragment: Fragment = InboxReputationReportFragment()
            val bundle = Bundle()
            bundle.putString(InboxReputationReportActivity.ARGS_SHOP_ID, shopId)
            bundle.putString(InboxReputationReportActivity.ARGS_REVIEW_ID, reviewId)
            fragment.arguments = bundle
            return fragment
        }
    }
}