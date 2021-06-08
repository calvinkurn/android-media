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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.talk.R
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reporttalk.analytics.TalkAnalytics
import com.tokopedia.talk.feature.reporttalk.di.DaggerReportTalkComponent
import com.tokopedia.talk.feature.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.talk.feature.reporttalk.view.adapter.ReportTalkAdapter
import com.tokopedia.talk.feature.reporttalk.view.uimodel.TalkReportOptionUiModel
import com.tokopedia.talk.feature.reporttalk.view.viewmodel.ReportTalkViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_report_talk.*
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_report_talk, container, false)
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

        optionRv.layoutManager = linearLayoutManager
        optionRv.adapter = reportTalkAdapter

        sendButton.setOnClickListener {
            reportTalk(talkId, commentId, reason.text.toString(), reportTalkAdapter.getSelectedOption())
        }

        reason.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                reportTalkAdapter.setChecked(reportTalkAdapter.getItem(2))
                checkEnableSendButton()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        reason.setOnClickListener {
            reportTalkAdapter.setChecked(reportTalkAdapter.getItem(2))
        }

        reason.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardHandler.hideSoftKeyboard(activity)
                true
            } else {
                false
            }
        }


    }

    private fun checkEnableSendButton() {
        reason.text?.let {
            if (reportTalkAdapter.getItem(2).isChecked && it.isBlank()) {
                disableSendButton()
            } else {
                enableSendButton()
            }
        }
    }

    private fun disableSendButton() {
        sendButton.isEnabled = false
    }

    private fun enableSendButton() {
        sendButton.isEnabled = true
    }


    private fun showLoadingFull() {
        progressBar.visibility = View.VISIBLE
        mainView.visibility = View.GONE
        sendButton.visibility = View.GONE
    }

    private fun hideLoadingFull() {
        progressBar.visibility = View.GONE
        mainView.visibility = View.VISIBLE
        sendButton.visibility = View.VISIBLE
    }

    private fun onErrorReportTalk(errorMessage: String) {
        NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
            reportTalk(talkId, commentId, reason.text.toString(),
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
        showLoadingFull()
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

    override fun onDestroyView() {
        context?.run {
            KeyboardHandler.DropKeyboard(this, reason)
        }
        super.onDestroyView()
    }

    private fun observeLiveDatas() {
        viewModel.reportCommentResult.observe(viewLifecycleOwner, Observer {
            hideLoadingFull()
            when(it) {
                is Success -> onSuccessReportTalk()
                is Fail -> onErrorReportTalk(it.throwable.message ?: "")
            }
        })
        viewModel.reportTalkResult.observe(viewLifecycleOwner, Observer {
            hideLoadingFull()
            when(it) {
                is Success -> onSuccessReportTalk()
                is Fail -> onErrorReportTalk(it.throwable.message ?: "")
            }
        })
    }

}