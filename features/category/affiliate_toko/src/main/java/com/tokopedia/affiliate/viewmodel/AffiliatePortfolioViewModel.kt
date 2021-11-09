package com.tokopedia.affiliate.viewmodel

import android.webkit.URLUtil.isValidUrl
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.INSTAGRAM
import com.tokopedia.affiliate.TIKTOK
import com.tokopedia.affiliate.YOUTUBE
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.AffiliateHeaderItemData
import com.tokopedia.affiliate.model.AffiliatePortfolioButtonData
import com.tokopedia.affiliate.model.AffiliatePortfolioUrlInputData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioButtonModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioUrlModel
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.user.session.UserSessionInterface
import java.util.ArrayList
import javax.inject.Inject

class AffiliatePortfolioViewModel@Inject constructor(
    private val userSessionInterface: UserSessionInterface)
    :BaseViewModel() {
    private var affiliatePortfolioData = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var updateListItem = MutableLiveData<Int>()
    private val itemList : ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()

    fun createDefaultListForSm() {
        itemList.add(AffiliateHeaderModel(AffiliateHeaderItemData(userSessionInterface.name,true)))
        itemList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData("Link Instagram","","Contoh: instagram.com/tokopedia","Link tidak valid.",false,type = INSTAGRAM)))
        itemList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData("Link Tiktok","","Contoh: tiktok.com/tokopedia","Link tidak valid.",false,type = TIKTOK)))
        itemList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData("Link Youtube","","Contoh: youtube.com/tokopedia","Link tidak valid.",false,type = YOUTUBE)))
        itemList.add(AffiliatePortfolioButtonModel(AffiliatePortfolioButtonData("Tambah Sosial Media")))
        affiliatePortfolioData.value = itemList
    }
    fun updateList(position: Int, text: String) {
        (itemList[position] as? AffiliatePortfolioUrlModel)?.portfolioItm?.text=text
    }
    fun checkDataAndMakeApiCall() {
        itemList.forEachIndexed {i,item->
            if(item is AffiliatePortfolioUrlModel)
            {
                if(!item.portfolioItm.text.isNullOrEmpty() && !isValidUrl(item.portfolioItm.text)){
                    item.portfolioItm.isError=true
                    updateListItem.value = i
                    return
                }

            }
        }
    }
    fun updateListErrorState(position: Int) {
        (itemList[position] as? AffiliatePortfolioUrlModel)?.portfolioItm?.isError = true
        updateListItem.value=position
    }
    fun updateListSuccess(position: Int) {
        (itemList[position] as? AffiliatePortfolioUrlModel)?.portfolioItm?.isError = false
        updateListItem.value=position
    }

    fun getPortfolioUrlList() : LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = affiliatePortfolioData
    fun getUpdateItemIndex() : LiveData<Int> = updateListItem
}