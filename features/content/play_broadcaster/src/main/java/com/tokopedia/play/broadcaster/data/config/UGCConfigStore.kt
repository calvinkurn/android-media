package com.tokopedia.play.broadcaster.data.config

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created by fachrizalmrsln on 22/09/22
 */
interface UGCConfigStore {
    fun setAuthor(author: ContentAccountUiModel)
    fun getAuthor(): ContentAccountUiModel
    fun getAuthorId(): String
    fun getAuthorType(): String
    fun getAuthorTypeName(): String
}