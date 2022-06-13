package com.tokopedia.talk.feature.reporttalk.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.talk.BuildConfig
import com.tokopedia.talk.R
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.databinding.FragmentReportTalkBinding
import com.tokopedia.talk.feature.reporttalk.analytics.TalkAnalytics
import com.tokopedia.talk.feature.reporttalk.di.DaggerReportTalkComponent
import com.tokopedia.talk.feature.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.talk.feature.reporttalk.view.adapter.ReportTalkAdapter
import com.tokopedia.talk.feature.reporttalk.view.uimodel.TalkReportOptionUiModel
import com.tokopedia.talk.feature.reporttalk.view.viewmodel.ReportTalkViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

/**
 * @author by nisie on 8/30/18.
 */
class ReportTalkFragment : BaseDaggerFragment(), ReportTalkAdapter.OnOptionClickListener {

    companion object {
        fun newInstance(bundle: Bundle): ReportTalkFragment {
            val fragment = ReportTalkFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: ReportTalkViewModel

    @Inject
    lateinit var analytics: TalkAnalytics

    lateinit var reportTalkAdapter: ReportTalkAdapter

    private var binding by autoCleared<FragmentReportTalkBinding>()

    var talkId: String = ""
    var commentId: String = ""

    override fun getScreenName(): String {
        return TalkAnalytics.SCREEN_NAME_REPORT_TALK
    }

    override fun initInjector() {
        val reportTalkComponent = DaggerReportTalkComponent.builder()
                .talkComponent(getComponent(TalkComponent::class.java))
                .build()
        reportTalkComponent.inject(this)

    }

    override fun onStart() {
        super.onStart()
        activity?.run {
            analytics.sendScreen(this, screenName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentReportTalkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(savedInstanceState)
        setupView()
        observeLiveDatas()
    }

    private fun initData(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            talkId = savedInstanceState.getString(ReportTalkActivity.EXTRA_TALK_ID, "")
            commentId = savedInstanceState.getString(ReportTalkActivity.EXTRA_COMMENT_ID, "")

        } ?: arguments?.run {
            talkId = getString(ReportTalkActivity.EXTRA_TALK_ID, "")
            commentId = getString(ReportTalkActivity.EXTRA_COMMENT_ID, "")

        } ?: activity?.run {
            finish()
        }
    }

    override fun onClickOption(talkReportOptionUiModel: TalkReportOptionUiModel) {
        reportTalkAdapter.setChecked(talkReportOptionUiModel)
        checkEnableSendButton()
    }

    private fun setupView() {

        val listOption: ArrayList<TalkReportOptionUiModel> = ArrayList()
        listOption.add(TalkReportOptionUiModel(0, getString(R.string
                .talk_report_option_spam), "", false))
        listOption.add(TalkReportOptionUiModel(1, getString(R.string
                .talk_report_option_sara), "", false))
        listOption.add(TalkReportOptionUiModel(2, getString(R.string
                .talk_report_option_others), "", false))

        reportTalkAdapter = ReportTalkAdapter(this, listOption)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.optionRv.layoutManager = linearLayoutManager
        binding.optionRv.adapter = reportTalkAdapter

        binding.sendButton.setOnClickListener {
            reportTalk(talkId, commentId, binding.reason.editText.text.toString(), reportTalkAdapter.getSelectedOption())
        }
        setupToolbar()
        setupReasonTextField()
    }

    private fun setupToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                binding.headerReportTalk
                binding.headerReportTalk.run {
                    title = getString(R.string.title_report_talk)
                    setNavigationOnClickListener { onBackPressed() }
                }
            }
        }
    }

    private fun setupReasonTextField() {
        binding.reason.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                reportTalkAdapter.setChecked(reportTalkAdapter.getItem(2))
                checkEnableSendButton()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        binding.reason.setOnClickListener {
            reportTalkAdapter.setChecked(reportTalkAdapter.getItem(2))
        }
        binding.reason.editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardHandler.hideSoftKeyboard(activity)
                true
            } else {
                false
            }
        }
        binding.reason.labelText.gone()
    }

    private fun checkEnableSendButton() {
        binding.reason.editText.text?.let {
            if (reportTalkAdapter.getItem(2).isChecked && it.isBlank()) {
                disableSendButton()
            } else {
                enableSendButton()
            }
        }
    }

    private fun disableSendButton() {
        binding.sendButton.isEnabled = false
    }

    private fun enableSendButton() {
        binding.sendButton.isEnabled = true
    }


    private fun showLoadingSendReport() {
        binding.sendButton.isLoading = true
        reportTalkAdapter.disableOptions()
        binding.reason.isEnabled = false
    }

    private fun hideLoadingSendReport() {
        binding.sendButton.isLoading = false
        reportTalkAdapter.enableOptions()
        binding.reason.isEnabled = true
    }

    private fun onErrorReportTalk(throwable: Throwable) {
        logToCrashlytics(throwable)
        val message = ErrorHandler.getErrorMessage(context, throwable)
        NetworkErrorHelper.createSnackbarWithAction(activity, message) {
            reportTalk(talkId, commentId, binding.reason.editText.text.toString(),
                    reportTalkAdapter.getSelectedOption())
        }.showRetrySnackbar()
    }

    private fun reportTalk(talkId: String, commentId: String,
                           otherReason: String, selectedOption: TalkReportOptionUiModel) {

        if (commentId.isBlank()) {
            viewModel.reportTalk(talkId, otherReason, selectedOption.position)
        } else {
            viewModel.reportComment(commentId, otherReason, selectedOption.position)
        }
        showLoadingSendReport()
    }

    private fun onSuccessReportTalk() {
        activity?.run {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putString(ReportTalkActivity.EXTRA_TALK_ID, talkId)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ReportTalkActivity.EXTRA_TALK_ID, talkId)
        outState.putString(ReportTalkActivity.EXTRA_COMMENT_ID, commentId)
    }

    private fun observeLiveDatas() {
        viewModel.reportCommentResult.observe(viewLifecycleOwner, Observer {
            hideLoadingSendReport()
            when(it) {
                is Success -> onSuccessReportTalk()
                is Fail -> onErrorReportTalk(it.throwable)
            }
        })
        viewModel.reportTalkResult.observe(viewLifecycleOwner, Observer {
            hideLoadingSendReport()
            when(it) {
                is Success -> onSuccessReportTalk()
                is Fail -> onErrorReportTalk(it.throwable)
            }
        })
    }

    private fun logToCrashlytics(throwable: Throwable) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        } else {
            throwable.printStackTrace()
        }
    }

}