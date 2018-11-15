package com.tokopedia.kol.feature.report.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kol.R
import com.tokopedia.kol.common.util.hideLoading
import com.tokopedia.kol.common.util.showLoading
import com.tokopedia.kol.feature.report.view.activity.ContentReportActivity
import com.tokopedia.kol.feature.report.view.adapter.ReportReasonAdapter
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import com.tokopedia.kol.feature.report.view.model.ReportReasonViewModel
import kotlinx.android.synthetic.main.fragment_content_report.*

/**
 * @author by milhamj on 08/11/18.
 */
class ContentReportFragment : BaseDaggerFragment(), ContentReportContract.View {

    private var contentId = 0
    private lateinit var adapter: ReportReasonAdapter

    companion object {
        fun createInstance(contentId: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt(ContentReportActivity.PARAM_CONTENT_ID, contentId)

            val fragment = ContentReportFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName() = null

    override fun initInjector() {
        GraphqlClient.init(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_content_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVar()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun hideKeyboard() {
        reasonInput.isCursorVisible = false
        KeyboardHandler.DropKeyboard(activity, view)
    }

    override fun enableSendBtn() {
        sendBtn.isEnabled = true
    }

    override fun showLoading() {
        mainView.showLoading()
    }

    override fun hideLoading() {
        mainView.hideLoading()
    }

    private fun initVar() {
        arguments?.run {
            contentId = getInt(ContentReportActivity.PARAM_CONTENT_ID, 0)
        }
        adapter = ReportReasonAdapter(this)
    }

    private fun initView() {
        reasonRv.adapter = adapter
        adapter.addAll(getReasonList())

        reasonInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                reasonInput.isCursorVisible = true
                adapter.setCustomTypeSelected()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        sendBtn.setOnClickListener {
            //TODO milhamj
            adapter.getSelectedItem()

            if (adapter.getSelectedItem().type == adapter.getCustomTypeString()) {

            }
        }
    }

    private fun getReasonList(): MutableList<ReportReasonViewModel> {
        val reasonList = ArrayList<ReportReasonViewModel>()

        val reasonSpam = ReportReasonViewModel(
                type = getString(R.string.kol_reason_type_spam),
                description = getString(R.string.kol_reason_desc_spam)
        )
        reasonList.add(reasonSpam)

        val reasonAbuse = ReportReasonViewModel(
                type = getString(R.string.kol_reason_type_abuse),
                description = getString(R.string.kol_reason_desc_abuse)
        )
        reasonList.add(reasonAbuse)

        val reasonInappropriate = ReportReasonViewModel(
                type = getString(R.string.kol_reason_type_inappropriate),
                description = getString(R.string.kol_reason_desc_inappropriate)
        )
        reasonList.add(reasonInappropriate)

        val reasonOthers = ReportReasonViewModel(
                type = getString(R.string.kol_reason_type_others),
                description = getString(R.string.kol_reason_desc_others)
        )
        reasonList.add(reasonOthers)

        return reasonList
    }
}