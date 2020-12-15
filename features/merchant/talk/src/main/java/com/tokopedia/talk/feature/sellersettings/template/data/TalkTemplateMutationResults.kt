package com.tokopedia.talk.feature.sellersettings.template.data

sealed class TalkTemplateMutationResults {
    object ToggleTemplate : TalkTemplateMutationResults()
    object AddTemplate: TalkTemplateMutationResults()
    object ArrangeTemplate: TalkTemplateMutationResults()
    object DeleteTemplate: TalkTemplateMutationResults()
    object UpdateTemplate: TalkTemplateMutationResults()
    object MutationFailed: TalkTemplateMutationResults()
}