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
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.review.feature.inbox.buyerreview.analytics.AppScreen
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking
import com.tokopedia.review.feature.inbox.buyerreview.di.DaggerReputationComponent
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationReportActivity
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.report.InboxReputationReportViewModel
import com.tokopedia.review.inbox.R
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by nisie on 9/13/17.
 */
class InboxReputationReportFragment : BaseDaggerFragment() {

    private var sendButton: UnifyButton? = null
    private var reportRadioGroup: RadioGroup? = null
    private var otherRadioButton: RadioButton? = null
    private var spamRadioButton: RadioButton? = null
    private var saraRadioButton: RadioButton? = null
    private var otherReason: TextFieldUnify? = null
    private var progressDialog: ProgressDialog? = null
    private var feedbackId = ""

    @Inject
    lateinit var tracking: ReputationTracking

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: InboxReputationReportViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InboxReputationReportViewModel::class.java)
    }

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
        sendButton = parentView.findViewById<View>(R.id.send_button) as UnifyButton?
        reportRadioGroup = parentView.findViewById<View>(R.id.radio_group) as RadioGroup?
        otherReason = parentView.findViewById<View>(R.id.reason) as TextFieldUnify?
        otherRadioButton = parentView.findViewById<View>(R.id.report_other) as RadioButton?
        spamRadioButton = parentView.findViewById(R.id.report_spam)
        saraRadioButton = parentView.findViewById(R.id.report_sara)
        prepareView()
        feedbackId = arguments?.getString(InboxReputationReportActivity.ARGS_REVIEW_ID) ?: ""
        return parentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeReportReviewResult()
    }

    private fun prepareView() {
        reportRadioGroup?.setOnCheckedChangeListener { _: RadioGroup?, id: Int ->
            if (id != otherRadioButton?.id) {
                context?.let { context ->
                    KeyboardHandler.DropKeyboard(context, otherReason)
                }
            }
            setSendButton()
        }
        otherReason?.textFieldInput?.addTextChangedListener(object : TextWatcher {
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
            showLoadingProgress()
            context?.let { context ->
                KeyboardHandler.DropKeyboard(context, otherReason)
            }
            viewModel.reportReview(
                feedbackId = arguments?.getString(
                    InboxReputationReportActivity.ARGS_REVIEW_ID,
                    ""
                ) ?: "",
                checkedRadioId = reportRadioGroup?.checkedRadioButtonId ?: 0,
                reasonText = otherReason?.getEditableValue().toString()
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
        val sendButtonEnabled = (reportRadioGroup?.checkedRadioButtonId == R.id.report_spam
                    ) || (reportRadioGroup?.checkedRadioButtonId == R.id.report_sara
                    ) || ((reportRadioGroup?.checkedRadioButtonId == R.id.report_other
                    && !TextUtils.isEmpty(otherReason?.getEditableValue().toString().trim { it <= ' ' })))
        sendButton?.isEnabled = sendButtonEnabled
    }

    private fun initProgressDialog() {
        if (getContext() != null) {
            progressDialog = ProgressDialog(getContext())
            progressDialog?.setTitle("")
            progressDialog?.setMessage(getContext()?.getString(R.string.progress_dialog_loading))
            progressDialog?.setCancelable(false)
        }
    }

    private fun showLoadingProgress() {
        progressDialog?.let {
            if (it.isShowing) {
                return
            }
            it.show()
        }
    }

    private fun onErrorReportReview(errorMessage: String?) {
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    private fun onSuccessReportReview() {
        activity?.apply {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun removeLoadingProgress() {
        progressDialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }

    private fun observeReportReviewResult() {
        viewModel.reportReviewResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    removeLoadingProgress()
                    if (it.data.success) {
                        onSuccessReportReview()
                    } else {
                        val message = ErrorHandler.getErrorMessage(
                            context,
                            MessageErrorException(getString(R.string.review_report_unknown_error))
                        )
                        onErrorReportReview(
                            message
                        )
                    }
                }
                is Fail -> {
                    removeLoadingProgress()
                    onErrorReportReview(ErrorHandler.getErrorMessage(context, it.throwable))
                }
            }
        }
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