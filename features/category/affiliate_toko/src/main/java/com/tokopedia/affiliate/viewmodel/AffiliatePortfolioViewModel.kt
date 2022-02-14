package com.tokopedia.affiliate.viewmodel

import android.webkit.URLUtil.isValidUrl
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateHeaderItemData
import com.tokopedia.affiliate.model.pojo.AffiliatePortfolioButtonData
import com.tokopedia.affiliate.model.pojo.AffiliatePortfolioUrlInputData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioButtonModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioUrlModel
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import java.util.ArrayList
import javax.inject.Inject

class AffiliatePortfolioViewModel@Inject constructor(
    private val userSessionInterface: UserSessionInterface)
    :BaseViewModel() {
    var affiliatePortfolioData = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var updateListItem = MutableLiveData<Int>()

    fun createDefaultListForSm() {
        val itemList : ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        itemList.add(AffiliateHeaderModel(AffiliateHeaderItemData(userSessionInterface.name,true)))
        itemList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData(3,"Link Instagram","","Contoh: instagram.com/tokopedia","Link tidak valid.",false)))
        itemList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData(9,"Link Tiktok","","Contoh: tiktok.com/tokopedia","Link tidak valid.",false)))
        itemList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData(13,"Link Youtube","","Contoh: youtube.com/tokopedia","Link tidak valid.",false)))
        itemList.add(AffiliatePortfolioButtonModel(AffiliatePortfolioButtonData("Tambah Sosial Media", UnifyButton.Type.ALTERNATE,UnifyButton.Variant.GHOST)))
        itemList.add(AffiliatePortfolioButtonModel(AffiliatePortfolioButtonData("Selanjutnya", UnifyButton.Type.MAIN,UnifyButton.Variant.FILLED,true)))
        affiliatePortfolioData.value = itemList
    }
    fun updateList(position: Int, text: String) {
        (affiliatePortfolioData.value?.get(position) as? AffiliatePortfolioUrlModel)?.portfolioItm?.text = text
    }
    fun checkData()  : Boolean{
        affiliatePortfolioData.value?.forEachIndexed {i,item->
            if(item is AffiliatePortfolioUrlModel)
            {
                if(item.portfolioItm.text.isNullOrEmpty() && !isValidUrl(item.portfolioItm.text)){
                    item.portfolioItm.isError = true
                    updateListItem.value = i
                    return false
                }

            }
        }
        return true
    }

    fun updateFocus(position: Int,focus : Boolean){
        (affiliatePortfolioData.value?.get(position) as? AffiliatePortfolioUrlModel)?.portfolioItm?.isFocus = focus
        updateListItem.value = position
    }

    fun updateListErrorState(position: Int) {
        (affiliatePortfolioData.value?.get(position) as? AffiliatePortfolioUrlModel)?.portfolioItm?.isError = true
        updateListItem.value = position
    }

    fun updateListSuccess(position: Int) {
        (affiliatePortfolioData.value?.get(position) as? AffiliatePortfolioUrlModel)?.portfolioItm?.isError = false
        updateListItem.value = position
    }

    fun getPortfolioUrlList() : LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = affiliatePortfolioData
    fun getUpdateItemIndex() : LiveData<Int> = updateListItem
}