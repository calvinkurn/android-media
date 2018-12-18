package com.tokopedia.feedcomponent.view.adapter.post

import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel

/**
 * @author by milhamj on 03/12/18.
 */
interface DynamicPostTypeFactory {
    fun type(dynamicPostViewModel: DynamicPostViewModel): Int
}