package com.tokopedia.product.edit.listener

import com.tokopedia.product.edit.viewmodel.VideoViewModel

interface VideoChoosenListener {

    fun onVideoChoosenDeleted(videoViewModel : VideoViewModel)

}