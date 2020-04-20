package com.tokopedia.topads.edit.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Pika on 14/4/20.
 */

class SharedViewModel : ViewModel() {

    var productId: MutableLiveData<MutableList<String>> = MutableLiveData()
    var groupName: MutableLiveData<String> = MutableLiveData()
    var groupId:MutableLiveData<Int> = MutableLiveData()

    fun setProductIds(text: MutableList<String>) {
        productId.value = text
    }

    fun setGroupName(text: String) {
        groupName.value = text
    }
    fun setGroupId(id:Int){
        groupId.value = id

    }

}