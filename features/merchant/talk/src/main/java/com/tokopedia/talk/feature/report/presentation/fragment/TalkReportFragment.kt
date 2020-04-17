package com.tokopedia.talk.feature.report.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.common.TalkConstants.COMMENT_ID
import com.tokopedia.talk.common.TalkConstants.TALK_ID
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.report.data.mapper.TalkReportMapper
import com.tokopedia.talk.feature.report.presentation.uimodel.TalkReportUiModel
import com.tokopedia.talk.feature.report.di.DaggerTalkReportComponent
import com.tokopedia.talk.feature.report.di.TalkReportComponent
import com.tokopedia.talk.feature.report.presentation.viewmodel.TalkReportViewModel
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_talk_report.*
import javax.inject.Inject


class TalkReportFragment : BaseDaggerFragment(), HasComponent<TalkReportComponent> {

    companion object {

        @JvmStatic
        fun createNewInstance(talkId: Int = 0, commentId: Int = 0): TalkReportFragment =
                TalkReportFragment().apply {
                    arguments = Bundle()
                    arguments?.putInt(TALK_ID, talkId)
                    arguments?.putInt(COMMENT_ID, commentId)
                }
    }

    @Inject
    lateinit var viewModel: TalkReportViewModel

    private val reportUiModelOptions: List<TalkReportUiModel> = listOf()
    private var talkId = 0
    private var commentId = 0

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component.inject(this)
    }

    override fun getComponent(): TalkReportComponent {
        return DaggerTalkReportComponent.builder().talkComponent(
                getComponent(TalkComponent::class.java))
                .build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getDataFromArguments()
        initReportOptions()
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        val data = TalkReportMapper.mapTalkReportModelToListUnifyItems(reportUiModelOptions)
        talkReportOptions.setData(data)
        talkReportOptions.setOnItemClickListener { parent, view, position, id ->
            val chosenSortOption = reportUiModelOptions[position]
        }
        talkReportOptions.onLoadFinish {
            data.forEachIndexed { index, listItemUnify ->
                listItemUnify.listRightRadiobtn?.setOnClickListener {

                }

            }
        }
    }

    private fun onSuccessSendReport() {

    }

    private fun onErrorSendReport() {
        showErrorToaster()
    }

    private fun initReportOptions() {

    }

    private fun showErrorToaster() {
        view?.let {
            Toaster.make(it, getString(R.string.toaster_report_fail), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, getString(R.string.talk_ok))
        }
    }

    private fun sendReport() {

    }

    private fun observeSendReportResult() {

    }

    private fun getDataFromArguments() {
        arguments?.let {
            talkId = it.getInt(TALK_ID)
            commentId = it.getInt(COMMENT_ID)
        }
    }
}