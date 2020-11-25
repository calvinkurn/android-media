package com.tokopedia.talk.feature.reading.data.model

sealed class TalkLastAction
class TalkGoToReply(val questionId: String) : TalkLastAction()
object TalkGoToWrite : TalkLastAction()
