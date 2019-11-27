package com.tokopedia.createpost.view.listener

import com.tokopedia.createpost.view.viewmodel.HeaderViewModel

/**
 * @author by milhamj on 05/03/19.
 */
interface CreatePostActivityListener {
    fun updateHeader(header: HeaderViewModel)

    fun updateShareHeader(text: String)

    fun invalidatePostMenu(isPostEnabled: Boolean)
}