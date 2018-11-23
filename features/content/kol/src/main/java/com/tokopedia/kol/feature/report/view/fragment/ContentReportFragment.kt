package com.tokopedia.kol.feature.report.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kol.R
import com.tokopedia.kol.common.util.afterTextChanged
import com.tokopedia.kol.common.util.hideLoading
import com.tokopedia.kol.common.util.showLoading
import com.tokopedia.kol.feature.report.di.DaggerContentReportComponent
import com.tokopedia.kol.feature.report.view.activity.ContentReportActivity
import com.tokopedia.kol.feature.report.view.adapter.ReportReasonAdapter
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import com.tokopedia.kol.feature.report.view.model.ReportReasonViewModel
import kotlinx.android.synthetic.main.fragment_content_report.*
import javax.inject.Inject

/**
 * @author by milhamj on 08/11/18.
 */
class ContentReportFragment : BaseDaggerFragment(), ContentReportContract.View {

    private var contentId = 0
    private lateinit var adapter: ReportReasonAdapter

    @Inject
    lateinit var presenter: ContentReportContract.Presenter

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
        activity?.let {
            (it.applicationContext as BaseMainApplication).baseAppComponent
        }.let {
            DaggerContentReportComponent.builder()
                    .baseAppComponent(it)
                    .build()
                    .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_content_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        initVar()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
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

    override fun onSuccessSendReport() {
        activity?.setResult(Activity.RESULT_OK, Intent())
        activity?.finish()
    }

    override fun onErrorSendReport(message: String) {
        NetworkErrorHelper.showEmptyState(context!!, mainView, message) { sendReport() }
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

        reasonInput.afterTextChanged {
            reasonInput.isCursorVisible = true
            adapter.setCustomTypeSelected()
            sendBtn.isEnabled = it.isEmpty().not()
        }

        sendBtn.setOnClickListener { sendReport() }
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

    private fun sendReport() {
        val isCustomType = adapter.getSelectedItem().type == adapter.getCustomTypeString()
        val reasonMessage = if (isCustomType) reasonInput.text.toString() else adapter.getSelectedItem().description

        presenter.sendReport(
                contentId,
                adapter.getSelectedItem().type,
                reasonMessage
        )
    }
}