package com.tokopedia.topads.edit.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.response.TopAdsBidSettingsModel

/**
 * Created by Pika on 14/4/20.
 */

class SharedViewModel : ViewModel() {

    private var productId: MutableLiveData<MutableList<String>> = MutableLiveData()
    private var groupId: MutableLiveData<Int> = MutableLiveData()
    private var negKeyword: MutableLiveData<List<GetKeywordResponse.KeywordsItem>> = MutableLiveData()
    private var bidForGroup: MutableLiveData<Int> = MutableLiveData()
    private var dailyBudget: MutableLiveData<Int> = MutableLiveData()
    private var rekomendedBudget: MutableLiveData<Int> = MutableLiveData()
    private var autoBidStatus: MutableLiveData<String> = MutableLiveData()
    private var isWhiteListedUser: Boolean = false
    private var bidSettings: MutableLiveData<List<TopAdsBidSettingsModel>> = MutableLiveData()

    fun setProductIds(text: MutableList<String>) {
        productId.value = text
    }

    fun setGroupId(id: Int) {
        groupId.value = id
    }

    fun setBudget(budget: Int) {
        bidForGroup.value = budget
    }

    fun setNegKeywords(data: List<GetKeywordResponse.KeywordsItem>) {
        negKeyword.value = data
    }

    fun setDailyBudget(budget:Int){
        dailyBudget.value = budget*40

    }

    fun setRekomendedBudget(budget:Int){
        rekomendedBudget.value = budget*40

    }

    fun getProuductIds(): MutableLiveData<MutableList<String>> {
        return productId
    }

    fun getGroupId(): MutableLiveData<Int> {
        return groupId
    }

    fun getnegKeywords(): MutableLiveData<List<GetKeywordResponse.KeywordsItem>> {
        return negKeyword
    }

    fun getBudget(): MutableLiveData<Int> {
        return bidForGroup
    }

    fun getDailyBudget(): MutableLiveData<Int>{
        return dailyBudget
    }


    fun getRekomendedBudget() : MutableLiveData<Int> {
        return rekomendedBudget
    }

    fun getAutoBidStatus() : MutableLiveData<String> {
        return autoBidStatus
    }

    fun setAutoBidStatus(status: String) {
        autoBidStatus.value = status
    }

    fun setIsWhiteListedUser(isWhiteListedUser: Boolean) {
        this.isWhiteListedUser = isWhiteListedUser
    }

    fun getIsWhiteListedUser() : Boolean {
        return isWhiteListedUser
    }

    fun setBidSettings(bidSettingsModel: List<TopAdsBidSettingsModel>) {
        this.bidSettings.value = bidSettingsModel
    }

    fun getBidSettings() : MutableLiveData<List<TopAdsBidSettingsModel>> {
        return bidSettings
    }

}