package com.tokopedia.talk.talkdetails.view.viewmodel

/**
 * Created by Hendri on 05/09/18.
 */
data class TalkDetailsViewModel(
        val thread:TalkDetailsThreadItemViewModel,
        val comments:List<TalkDetailsCommentViewModel>
)