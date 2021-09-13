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
import android.widget.*
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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
class InboxReputationReportFragment constructor() : BaseDaggerFragment(),
    InboxReputationReport.View {
    private var sendButton: Button? = null
    private var reportRadioGroup: RadioGroup? = null
    private var otherRadioButton: RadioButton? = null
    private var spamRadioButton: RadioButton? = null
    private var saraRadioButton: RadioButton? = null
    private var otherReason: EditText? = null
    private var progressDialog: ProgressDialog? = null
    private var feedbackId: String? = null

    @kotlin.jvm.JvmField
    @Inject
    var presenter: InboxReputationReportPresenter? = null

    @kotlin.jvm.JvmField
    @Inject
    var tracking: ReputationTracking? = null
    override fun getScreenName(): String {
        return AppScreen.SCREEN_INBOX_REPUTATION_REPORT
    }

    override fun initInjector() {
        val baseAppComponent: BaseAppComponent =
            (requireContext().getApplicationContext() as BaseMainApplication).getBaseAppComponent()
        val reputationComponent: DaggerReputationComponent = DaggerReputationComponent
            .builder()
            .baseAppComponent(baseAppComponent)
            .build() as DaggerReputationComponent
        reputationComponent.inject(this)
    }

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setRetainInstance(true)
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
        presenter!!.attachView(this)
        if (getArguments() != null) {
            feedbackId =
                getArguments()!!.getString(InboxReputationReportActivity.Companion.ARGS_REVIEW_ID)
        }
        return parentView
    }

    private fun prepareView() {
        reportRadioGroup!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener({ group: RadioGroup?, checkedId: Int -> setSendButton() }))
        otherReason!!.addTextChangedListener(object : TextWatcher {
            public override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            public override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                otherRadioButton!!.setChecked(true)
                setSendButton()
            }

            public override fun afterTextChanged(s: Editable) {}
        })
        otherReason!!.setOnClickListener(View.OnClickListener({ view: View? ->
            otherRadioButton!!.setChecked(
                true
            )
        }))
        sendButton!!.setOnClickListener(View.OnClickListener({ v: View? ->
            tracking!!.onSubmitReportAbuse(feedbackId)
            if (getArguments() != null) {
                presenter!!.reportReview(
                    getArguments()!!.getString(
                        InboxReputationReportActivity.Companion.ARGS_REVIEW_ID,
                        ""
                    ),
                    getArguments()!!.getString(InboxReputationReportActivity.Companion.ARGS_SHOP_ID),
                    reportRadioGroup!!.getCheckedRadioButtonId(),
                    otherReason!!.getText().toString()
                )
            }
        }))
        initProgressDialog()
    }

    private fun setSendButton() {
        if (reportRadioGroup!!.getCheckedRadioButtonId() == R.id.report_spam) {
            tracking!!.onClickRadioButtonReportAbuse(spamRadioButton!!.getText().toString())
        } else if (reportRadioGroup!!.getCheckedRadioButtonId() == R.id.report_sara) {
            tracking!!.onClickRadioButtonReportAbuse(saraRadioButton!!.getText().toString())
        } else if (reportRadioGroup!!.getCheckedRadioButtonId() == R.id.report_other) {
            tracking!!.onClickRadioButtonReportAbuse(otherRadioButton!!.getText().toString())
        }
        if ((reportRadioGroup!!.getCheckedRadioButtonId() == R.id.report_spam
                    ) || (reportRadioGroup!!.getCheckedRadioButtonId() == R.id.report_sara
                    ) || ((reportRadioGroup!!.getCheckedRadioButtonId() == R.id.report_other
                    && !TextUtils.isEmpty(otherReason!!.getText().toString().trim({ it <= ' ' }))))
        ) {
            sendButton!!.setEnabled(true)
            sendButton!!.setTextColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
            MethodChecker.setBackground(
                sendButton,
                getResources().getDrawable(com.tokopedia.design.R.drawable.green_button_rounded)
            )
        } else {
            sendButton!!.setEnabled(false)
            sendButton!!.setTextColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N500))
            MethodChecker.setBackground(
                sendButton,
                getResources().getDrawable(com.tokopedia.design.R.drawable.bg_button_disabled)
            )
        }
    }

    private fun initProgressDialog() {
        if (getContext() != null) {
            progressDialog = ProgressDialog(getContext())
            progressDialog!!.setTitle("")
            progressDialog!!.setMessage(getContext()!!.getString(R.string.progress_dialog_loading))
            progressDialog!!.setCancelable(false)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (presenter != null) presenter!!.detachView()
    }

    public override fun showLoadingProgress() {
        if (!progressDialog!!.isShowing() && (getContext() != null) && (progressDialog != null)) progressDialog!!.show()
    }

    public override fun onErrorReportReview(errorMessage: String?) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage)
    }

    public override fun onSuccessReportReview() {
        getActivity()!!.setResult(Activity.RESULT_OK)
        getActivity()!!.finish()
    }

    public override fun removeLoadingProgress() {
        if (progressDialog!!.isShowing() && (getContext() != null) && (progressDialog != null)) progressDialog!!.dismiss()
    }

    companion object {
        fun createInstance(reviewId: String?, shopId: String?): Fragment {
            val fragment: Fragment = InboxReputationReportFragment()
            val bundle: Bundle = Bundle()
            bundle.putString(InboxReputationReportActivity.Companion.ARGS_SHOP_ID, shopId)
            bundle.putString(InboxReputationReportActivity.Companion.ARGS_REVIEW_ID, reviewId)
            fragment.setArguments(bundle)
            return fragment
        }
    }
}