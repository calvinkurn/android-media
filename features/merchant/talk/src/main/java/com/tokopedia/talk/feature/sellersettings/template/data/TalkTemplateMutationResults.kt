package com.tokopedia.talk.feature.sellersettings.template.data

import com.tokopedia.network.exception.MessageErrorException

sealed class TalkTemplateMutationResults {
    object TemplateMutationSuccess: TalkTemplateMutationResults()
    object DeleteTemplate: TalkTemplateMutationResults()
    object TemplateActivateSuccess: TalkTemplateMutationResults()
    object TemplateDeactivateSuccess: TalkTemplateMutationResults()
    data class DeleteTemplateFailed(val throwable: Throwable = MessageErrorException()): TalkTemplateMutationResults()
    data class RearrangeTemplateFailed(val throwable: Throwable = MessageErrorException()): TalkTemplateMutationResults()
    data class MutationFailed(val throwable: Throwable = MessageErrorException()): TalkTemplateMutationResults()
}