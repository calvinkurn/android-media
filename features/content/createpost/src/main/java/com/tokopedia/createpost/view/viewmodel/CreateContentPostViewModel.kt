package com.tokopedia.createpost.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CreateContentPostViewModel@Inject constructor(
) : BaseViewModel(Dispatchers.Main) {
    val _createPostViewModelData = MutableLiveData<CreatePostViewModel>()

    fun getPostData() : CreatePostViewModel? {
        return _createPostViewModelData.value
    }

    fun setNewContentData(createPostViewModel: CreatePostViewModel) {
        _createPostViewModelData.postValue(createPostViewModel)

    }

}