package com.tokopedia.affiliate.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.AffiliateHeaderItemData
import com.tokopedia.affiliate.model.AffiliateTermsAndConditionData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTermsAndConditionModel
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class AffiliateTermsAndConditionViewModel@Inject constructor(
    private val userSessionInterface: UserSessionInterface)
    :BaseViewModel() {
    private var affiliateTermsAndConditionData = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private val itemList : ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
    fun createListForTermsAndCondition() {
        itemList.add(AffiliateHeaderModel(AffiliateHeaderItemData(isForPortfolio = false)))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData("Akun Affiliate-mu harus terverifikasi untuk bisa tarik penghasilan")))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData("Tidak boleh mempromosikan produk terlarang yang tertera di S&K")))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData("Tidak boleh mempromosikan produk dari toko sendiri")))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData("Akun sosial media & blog yang kamu daftarkan tidak boleh di-private")))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData("Tidak terlibat dalam aktivitas penipuan yang terkait dengan akun Tokopedia")))
        affiliateTermsAndConditionData.value=itemList
    }
    fun getTermsAndConditionList() : LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = affiliateTermsAndConditionData
}