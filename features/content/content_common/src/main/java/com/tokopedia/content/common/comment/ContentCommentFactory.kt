package com.tokopedia.content.common.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 09/02/23
 */
class ContentCommentFactory @AssistedInject constructor(
    @Assisted private val source: PageSource,
    private val factory: ContentCommentViewModel.Factory,
) : ViewModelProvider.Factory {

    @AssistedFactory
    interface Creator {
        fun create(
            @Assisted source: PageSource,
        ): ContentCommentFactory
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T = factory.create(source) as T
}
