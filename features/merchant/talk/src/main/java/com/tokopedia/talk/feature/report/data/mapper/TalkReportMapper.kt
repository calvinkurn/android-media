package com.tokopedia.talk.feature.report.data.mapper

import com.tokopedia.talk.feature.report.presentation.uimodel.TalkReportUiModel
import com.tokopedia.unifycomponents.list.ListItemUnify

object TalkReportMapper {

    fun mapTalkReportModelToListUnifyItems(talkReportUiModels: List<TalkReportUiModel>) : ArrayList<ListItemUnify> {
        val result = arrayListOf<ListItemUnify>()
        talkReportUiModels.forEach {
            result.add(ListItemUnify(title = it.displayName, description = ""))
        }
        return result
    }

}