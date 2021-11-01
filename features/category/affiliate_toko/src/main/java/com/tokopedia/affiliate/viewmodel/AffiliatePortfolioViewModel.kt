package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
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

    fun createDefaultListForSm() {
        val tempList : ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        tempList.add(AffiliateHeaderModel(AffiliateHeaderItemData(userSessionInterface.name,true)))
        tempList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData("Link Instagram","",false)))
        tempList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData("Link Tiktok","",false)))
        tempList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData("Link Youtube","",false)))
        tempList.add(AffiliatePortfolioButtonModel(AffiliatePortfolioButtonData("Tambah Sosial Media")))
        affiliatePortfolioData.value=tempList
    }
    fun getPortfolioUrlList() : LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = affiliatePortfolioData
}