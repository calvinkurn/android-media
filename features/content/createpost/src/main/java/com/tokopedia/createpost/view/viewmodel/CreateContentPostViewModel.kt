package com.tokopedia.createpost.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CreateContentPostViewModel@Inject constructor(
) : BaseViewModel(Dispatchers.Main) {
     val _createPostViewModelData = MutableLiveData<CreatePostViewModel>()

     fun setNewContentData(createPostViewModel: CreatePostViewModel) {
       _createPostViewModelData.postValue(createPostViewModel)

    }

}