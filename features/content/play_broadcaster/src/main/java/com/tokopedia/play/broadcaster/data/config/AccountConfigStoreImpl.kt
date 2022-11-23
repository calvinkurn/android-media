package com.tokopedia.play.broadcaster.data.config

import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_NAME_SELLER
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_NAME_USER
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import javax.inject.Inject

/**
 * Created by fachrizalmrsln on 22/09/22
 */
class AccountConfigStoreImpl @Inject constructor(): AccountConfigStore {

    private var mAuthor: ContentAccountUiModel = ContentAccountUiModel.Empty

    override fun setAuthor(author: ContentAccountUiModel) {
        mAuthor = author
    }

    override fun getAuthor(): ContentAccountUiModel {
        return mAuthor
    }

    override fun getAuthorId(): String {
        return mAuthor.id
    }

    override fun getAuthorType(): String {
        return mAuthor.type
    }

    override fun getAuthorTypeName(): String {
        if (mAuthor == ContentAccountUiModel.Empty) return ""
        return if (mAuthor.type == TYPE_SHOP) TYPE_NAME_SELLER else TYPE_NAME_USER
    }

}