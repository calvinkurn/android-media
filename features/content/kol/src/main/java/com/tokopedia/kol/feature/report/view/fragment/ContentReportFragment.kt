package com.tokopedia.kol.feature.report.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kol.R
import com.tokopedia.kol.databinding.FragmentContentReportBinding
import com.tokopedia.kol.feature.report.di.DaggerContentReportComponent
import com.tokopedia.kol.feature.report.view.activity.ContentReportActivity
import com.tokopedia.kol.feature.report.view.adapter.ReportReasonAdapter
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import com.tokopedia.kol.feature.report.view.model.ReportReasonUiModel
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.showLoading
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * @author by milhamj on 08/11/18.
 */
class ContentReportFragment : BaseDaggerFragment(), ContentReportContract.View {

    private var _binding: FragmentContentReportBinding? = null
    private val binding: FragmentContentReportBinding
        get() = _binding!!

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
        activity?.let {
            (it.applicationContext as BaseMainApplication).baseAppComponent
        }.let {
            DaggerContentReportComponent.builder()
                .baseAppComponent(it)
                .build()
                .inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        initVar()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun hideKeyboard() {
        binding.reasonInput.isCursorVisible = false
        KeyboardHandler.DropKeyboard(activity, view)
    }

    override fun enableSendBtn() {
        binding.sendBtn.isEnabled = true
    }

    override fun showLoading() {
        binding.mainView.showLoading()
    }

    override fun hideLoading() {
        binding.mainView.hideLoading()
    }

    override fun onSuccessSendReport() {
        val intent = Intent()
        intent.putExtra(ContentReportActivity.RESULT_SUCCESS, true)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onErrorSendReport(message: String) {
        binding.mainView.let {
            Toaster.make(
                it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                clickListener = { sendReport() }
            )
        }
    }

    override fun onErrorSendReportDuplicate(message: String) {
        val intent = Intent()
        intent.putExtra(ContentReportActivity.RESULT_SUCCESS, false)
        intent.putExtra(ContentReportActivity.RESULT_ERROR_MSG, message)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun initVar() {
        arguments?.run {
            contentId = getInt(ContentReportActivity.PARAM_CONTENT_ID, 0)
        }
        adapter = ReportReasonAdapter(this)
    }

    private fun initView() {
        binding.sendBtn.isEnabled = false
        binding.reasonRv.adapter = adapter
        adapter.addAll(getReasonList())

        binding.reasonInput.afterTextChanged {
            binding.reasonInput.isCursorVisible = true
            adapter.setCustomTypeSelected()
            binding.sendBtn.isEnabled = it.isEmpty().not()
        }

        binding.sendBtn.setOnClickListener { sendReport() }
    }

    private fun getReasonList(): MutableList<ReportReasonUiModel> {
        val reasonList = ArrayList<ReportReasonUiModel>()

        val reasonSpam = ReportReasonUiModel(
            type = getString(R.string.kol_reason_type_spam),
            description = getString(R.string.kol_reason_desc_spam)
        )
        reasonList.add(reasonSpam)

        val reasonAbuse = ReportReasonUiModel(
            type = getString(R.string.kol_reason_type_abuse),
            description = getString(R.string.kol_reason_desc_abuse)
        )
        reasonList.add(reasonAbuse)

        val reasonInappropriate = ReportReasonUiModel(
            type = getString(R.string.kol_reason_type_inappropriate),
            description = getString(R.string.kol_reason_desc_inappropriate)
        )
        reasonList.add(reasonInappropriate)

        val reasonOthers = ReportReasonUiModel(
            type = getString(R.string.kol_reason_type_others),
            description = getString(R.string.kol_reason_desc_others)
        )
        reasonList.add(reasonOthers)

        return reasonList
    }

    private fun sendReport() {
        val isCustomType = adapter.getSelectedItem().type == adapter.getCustomTypeString()
        val reasonMessage = if (isCustomType) binding.reasonInput.text.toString() else adapter.getSelectedItem().description

        presenter.sendReport(
            contentId,
            adapter.getSelectedItem().type,
            reasonMessage,
            "content"
        )
    }
}
