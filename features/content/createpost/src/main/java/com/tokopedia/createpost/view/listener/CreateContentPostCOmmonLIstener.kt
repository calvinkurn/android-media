package com.tokopedia.createpost.view.listener

import android.net.Uri
import com.tokopedia.createpost.view.viewmodel.HeaderViewModel

interface CreateContentPostCOmmonLIstener {
    fun deleteItemFromProductTagList(position: Int)
    fun updateHeader(header: HeaderViewModel)
    fun launchProductTagFragment(data: ArrayList<Uri>?)

}