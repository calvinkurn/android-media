package com.tokopedia.sellerhomecommon.presentation.adapter.factory

import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel

/**
 * Created By @ilhamsuaib on 13/06/21
 */

interface PostListAdapterTypeFactory {

    fun type(post: PostItemUiModel.PostImageEmphasizedUiModel): Int

    fun type(post: PostItemUiModel.PostTextEmphasizedUiModel): Int
}