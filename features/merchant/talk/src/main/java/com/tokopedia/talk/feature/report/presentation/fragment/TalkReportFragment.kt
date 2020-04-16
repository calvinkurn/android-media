package com.tokopedia.talk.feature.report.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.report.data.mapper.TalkReportMapper
import com.tokopedia.talk.feature.report.presentation.uimodel.TalkReportUiModel
import com.tokopedia.talk.feature.report.di.DaggerTalkReportComponent
import com.tokopedia.talk.feature.report.di.TalkReportComponent
import com.tokopedia.talk_old.R
import kotlinx.android.synthetic.main.fragment_talk_report.*


class TalkReportFragment : BaseDaggerFragment(), HasComponent<TalkReportComponent> {

    companion object {

        const val TALK_ID = "talk_id"
        const val COMMENT_ID = "comment_id"

        @JvmStatic
        fun createNewInstance(talkId: Int = 0, commentId: Int = 0): TalkReportFragment =
                TalkReportFragment().apply {
                    arguments = Bundle()
                    arguments?.putInt(TALK_ID, talkId)
                    arguments?.putInt(COMMENT_ID, commentId)
                }
    }

    private val reportUiModelOptions: List<TalkReportUiModel> = listOf()

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

    private fun initReportOptions() {

    }

    private fun showErrorToaster() {

    }

    private fun sendReport() {

    }

    private fun observeSendReportResult() {

    }
}