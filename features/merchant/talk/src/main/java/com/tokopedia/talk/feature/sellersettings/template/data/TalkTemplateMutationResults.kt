package com.tokopedia.talk.feature.sellersettings.template.data

sealed class TalkTemplateMutationResults {
    object TemplateMutationSuccess: TalkTemplateMutationResults()
    object DeleteTemplate: TalkTemplateMutationResults()
    object DeleteTemplateFailed: TalkTemplateMutationResults()
    object MutationFailed: TalkTemplateMutationResults()
    object RearrangeTemplateFailed: TalkTemplateMutationResults()
}