package com.tokopedia.content.common.comment

import androidx.lifecycle.ViewModel
import com.tokopedia.content.common.comment.repository.ContentCommentRepository
import javax.inject.Inject

/**
 * @author by astidhiyaa on 09/02/23
 */
class ContentCommentViewModel @Inject constructor(private val repo: ContentCommentRepository) :
    ViewModel() { //use AssistedVal for [PageSource]
}
