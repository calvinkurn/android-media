package com.tokopedia.talk.reporttalk.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.talk.R
import com.tokopedia.talk.common.analytics.TalkAnalytics
import com.tokopedia.talk.reporttalk.view.listener.ReportTalkContract
import kotlinx.android.synthetic.main.fragment_report_talk.*

/**
 * @author by nisie on 8/30/18.
 */
class ReportTalkFragment : BaseDaggerFragment(), ReportTalkContract.View {


//    @Inject
//    lateinit var presenter : ReportTalkPresenter


    companion object {
        fun newInstance() = ReportTalkFragment()
    }

    override fun getScreenName(): String {
        return TalkAnalytics.SCREEN_NAME_REPORT_TALK
    }

    override fun initInjector() {
//        val inboxTalkComponent = DaggerInboxTalkComponent.builder()
//                .talkComponent(getComponent(TalkComponent::class.java))
//                .build()
//        inboxTalkComponent.inject(this)
//        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_report_talk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()

    }

    private fun setupView() {

        sendButton.setOnClickListener(View.OnClickListener {

        })

//        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
//            checkEnableSendButton()
//        })

        reason.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                radioOther.isChecked = true
                checkEnableSendButton()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        reason.setOnClickListener { radioOther.isChecked = true }


    }

    private fun checkEnableSendButton() {
//        if (radioOther.isChecked && reason.text.isBlank()) {
//            disableSendButton(radioGroup.context)
//        } else {
//            enableSendButton(radioGroup.context)
//        }
    }

    private fun disableSendButton(context: Context) {
        sendButton.isEnabled = false
        sendButton.setTextColor(MethodChecker.getColor(context, R.color.grey_700))
        MethodChecker.setBackground(sendButton, MethodChecker.getDrawable(context, R.drawable.bg_button_disabled))
    }

    private fun enableSendButton(context: Context) {
        sendButton.isEnabled = true
        sendButton.setTextColor(MethodChecker.getColor(context, R.color.white))
        MethodChecker.setBackground(sendButton, MethodChecker.getDrawable(context, R.drawable
                .bg_button_green))
    }


    override fun showLoadingFull() {

    }

}