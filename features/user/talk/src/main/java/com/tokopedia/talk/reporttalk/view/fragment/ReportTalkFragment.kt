package com.tokopedia.talk.reporttalk.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.talk.R
import com.tokopedia.talk.common.analytics.TalkAnalytics
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.reporttalk.di.DaggerReportTalkComponent
import com.tokopedia.talk.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.talk.reporttalk.view.adapter.ReportTalkAdapter
import com.tokopedia.talk.reporttalk.view.listener.ReportTalkContract
import com.tokopedia.talk.reporttalk.view.presenter.ReportTalkPresenter
import com.tokopedia.talk.reporttalk.view.viewmodel.TalkReportOptionViewModel
import kotlinx.android.synthetic.main.fragment_report_talk.*
import javax.inject.Inject

/**
 * @author by nisie on 8/30/18.
 */
class ReportTalkFragment : BaseDaggerFragment(), ReportTalkContract.View, ReportTalkAdapter.OnOptionClickListener {

    companion object {
        fun newInstance(bundle: Bundle): ReportTalkFragment {
            val fragment = ReportTalkFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter: ReportTalkPresenter

    @Inject
    lateinit var analytics: TalkAnalytics

    lateinit var reportTalkAdapter: ReportTalkAdapter

    var talkId: String = ""
    var shopId: String = ""
    var productId: String = ""
    var commentId: String = ""

    override fun getScreenName(): String {
        return TalkAnalytics.SCREEN_NAME_REPORT_TALK
    }

    override fun initInjector() {
        val reportTalkComponent = DaggerReportTalkComponent.builder()
                .talkComponent(getComponent(TalkComponent::class.java))
                .build()
        reportTalkComponent.inject(this)
        presenter.attachView(this)

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
    }

    private fun initData(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            talkId = savedInstanceState.getString(ReportTalkActivity.EXTRA_TALK_ID, "")
            shopId = savedInstanceState.getString(ReportTalkActivity.EXTRA_SHOP_ID, "")
            productId = savedInstanceState.getString(ReportTalkActivity.EXTRA_PRODUCT_ID, "")
            commentId = savedInstanceState.getString(ReportTalkActivity.EXTRA_COMMENT_ID, "")

        } ?: arguments?.run {
            talkId = getString(ReportTalkActivity.EXTRA_TALK_ID, "")
            shopId = getString(ReportTalkActivity.EXTRA_SHOP_ID, "")
            productId = getString(ReportTalkActivity.EXTRA_PRODUCT_ID, "")
            commentId = getString(ReportTalkActivity.EXTRA_COMMENT_ID, "")

        } ?: activity?.run {
            finish()
        }
    }

    override fun onClickOption(talkReportOptionViewModel: TalkReportOptionViewModel) {
        reportTalkAdapter.setChecked(talkReportOptionViewModel)
        checkEnableSendButton()

    }

    private fun setupView() {

        val listOption: ArrayList<TalkReportOptionViewModel> = ArrayList()
        listOption.add(TalkReportOptionViewModel(0, getString(R.string
                .talk_report_option_spam), "", false))
        listOption.add(TalkReportOptionViewModel(1, getString(R.string
                .talk_report_option_sara), "", false))
        listOption.add(TalkReportOptionViewModel(2, getString(R.string
                .talk_report_option_others), "", false))

        reportTalkAdapter = ReportTalkAdapter(this, listOption)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        optionRv.layoutManager = linearLayoutManager
        optionRv.adapter = reportTalkAdapter

        sendButton.setOnClickListener {

            reportTalk(talkId, shopId, productId, commentId, reason.text.toString(),
                    reportTalkAdapter.getSelectedOption())

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
        if (reportTalkAdapter.getItem(2).isChecked && reason.text.isBlank()) {
            disableSendButton(sendButton.context)
        } else {
            enableSendButton(sendButton.context)
        }
    }

    private fun disableSendButton(context: Context) {
        sendButton.isEnabled = false
        sendButton.setTextColor(MethodChecker.getColor(context, R.color.black_12))
        MethodChecker.setBackground(sendButton, MethodChecker.getDrawable(context, R.drawable.bg_button_disabled))
    }

    private fun enableSendButton(context: Context) {
        sendButton.isEnabled = true
        sendButton.setTextColor(MethodChecker.getColor(context, R.color.white))
        MethodChecker.setBackground(sendButton, MethodChecker.getDrawable(context, R.drawable
                .bg_button_green))
    }


    override fun showLoadingFull() {
        progressBar.visibility = View.VISIBLE
        mainView.visibility = View.GONE
        sendButton.visibility = View.GONE
    }

    override fun hideLoadingFull() {
        progressBar.visibility = View.GONE
        mainView.visibility = View.VISIBLE
        sendButton.visibility = View.VISIBLE
    }

    override fun onErrorReportTalk(errorMessage: String) {
        NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
            reportTalk(talkId, shopId, productId, commentId, reason.text.toString(),
                    reportTalkAdapter.getSelectedOption())
        }.showRetrySnackbar()
    }

    private fun reportTalk(talkId: String, shopId: String, productId: String, commentId: String,
                           otherReason: String, selectedOption: TalkReportOptionViewModel) {

        if (commentId.isBlank()) {
            presenter.reportTalk(talkId, shopId, productId, otherReason, selectedOption)
        } else {
            presenter.reportCommentTalk(talkId, shopId, productId, commentId, otherReason,
                    selectedOption)
        }
    }

    override fun onSuccessReportTalk() {
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
        outState.putString(ReportTalkActivity.EXTRA_PRODUCT_ID, productId)
        outState.putString(ReportTalkActivity.EXTRA_SHOP_ID, shopId)
        outState.putString(ReportTalkActivity.EXTRA_TALK_ID, talkId)
        outState.putString(ReportTalkActivity.EXTRA_COMMENT_ID, commentId)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onDestroyView() {
        context?.run {
            KeyboardHandler.DropKeyboard(this, reason)
        }
        super.onDestroyView()
    }

}