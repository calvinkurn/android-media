package com.tokopedia.topads.edit.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.topads.edit.data.response.GetKeywordResponse

/**
 * Created by Pika on 14/4/20.
 */

class SharedViewModel : ViewModel() {

    var productId: MutableLiveData<MutableList<String>> = MutableLiveData()
    var groupName: MutableLiveData<String> = MutableLiveData()
    var groupId: MutableLiveData<Int> = MutableLiveData()
    var negKeyword: MutableLiveData<List<GetKeywordResponse.KeywordsItem>> = MutableLiveData()

    fun setProductIds(text: MutableList<String>) {
        productId.value = text
    }

    fun setGroupName(text: String) {
        groupName.value = text
    }

    fun setGroupId(id: Int) {
        groupId.value = id

    }

    fun setNegKeywords(data: List<GetKeywordResponse.KeywordsItem>) {
        negKeyword.value = data

    }

}