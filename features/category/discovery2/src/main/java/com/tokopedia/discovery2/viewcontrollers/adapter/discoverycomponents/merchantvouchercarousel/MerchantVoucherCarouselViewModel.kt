package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class MerchantVoucherCarouselViewModel(application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private val _couponList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    val couponList: LiveData<ArrayList<ComponentsItem>> = _couponList

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
//        Todo:: Get Actual Data fro API
        _couponList.value = getFakeData()
    }

    fun getFakeData():ArrayList<ComponentsItem>{
        val data = Gson().fromJson(RESPONSE_MVC_CAROUSEL_ITEM,DataItem::class.java)
        return DiscoveryDataMapper().mapDataItemToMerchantVoucherComponent(listOf(data,data.copy()),ComponentNames.MerchantVoucherCarouselItem.componentName,components.name,position)
    }

}

const val RESPONSE_MVC_CAROUSEL_ITEM = "{\"shopInfo\":{\"id\":\"8218465\",\"name\":\"Whitelab Official Store\",\"iconUrl\":\"https://ecs7-p.tokopedia.net/img/cache/750/attachment/2020/6/26/99411207/99411207_2bb372ad-0094-4eef-a164-3c38de41fa80.jpg\",\"url\":\"https://www.tokopedia.com/whitelab\",\"appLink\":\"tokopedia://shop/8218465\",\"shopStatusIconURL\":\"\"},\"title\":\"Cashback hingga\",\"maximumBenefitAmountStr\":\"500\",\"subtitle\":\"+1 Kupon Lainnya\",\"products\":[{\"id\":\"1513192567\",\"name\":\"Whitelab Brightening Face Serum & Face Toner\",\"imageURL\":\"https://ecs7-p.tokopedia.net/img/cache/200-square/VqbcmM/2021/1/11/31ea6db4-ab3e-4e96-986a-104097427545.jpg\",\"redirectURL\":\"https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEopHFo_rFoAeDUMVj9RzNrc1i6sHDUSC5rfB7q3YXUsthbm-7q3OBUsthosJRopUO6AJFbm-xgVYpyfYagZUEHsyObsHOoAyRosKRHs1N6AKDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_1o-r7BW_sCsQABE3BPc8ujagfBvq1BN_7H7oJYJgpzvzcr7_7zSoJYJgpzvzVB2_JoGrB2yQAoiH7203Ao6qMUpZMh-qMY2_1o-r7BWo9giQ1NDZ325q1NJe_VozOjp3jPuPVB93MOv1pnF_jz6qjh9ZMxvu7u7_S20oJNE39x68B2k_92qQ1B2PfBiH72F3s-DPuKa1MhszsBF3jo-ojBke3BHe72fyfODQMV9o3gsZJ2f3jx3qMzc69Cqe7jfZ32Cq1hAZSgsQ3hXyurOgMHaqIgszsBF3jo-ojBke3BHe72dgVP383u36_CgufzEZ325q1B2PfBsHjNfyfO3gBBXZSgjH7NDZ325q1Ok3_VHqj7h_jzgH7NkgpooqB1O_7zSq1hAZS2gHsBN3ByN8B29zSBgHMP2_fB-81Nke9uvuJ77_92gHJNEypuo8juR_M2oq3gzv_7ibm-Orfua9fBjUstiHZUDUMVDgaUEUMzBgiUDUMNOQ3-BrBY5gBYxgIHi6sJDUMNOQ3-BrBYxgIowrMuhUsthbm-X9foxQMz2gcV7guYxgIHi6sUdbm-xyBY7g9o7Usti_iUDUSgBrSo2Qfdi6i-fHi-Y?t=ios&uid=1&template_id_used=3&r=https%3A%2F%2Fwww.tokopedia.com%2Fwhitelab%2Fwhitelab-brightening-face-serum-face-toner&click_ad_type=2&src=rewards&page=1&cpm_template_req=3&product_id=1513192567\",\"redirectAppLink\":\"tokopedia://product/1513192567\",\"benefitLabel\":\"42\",\"category\":{\"rootID\":\"61\",\"rootName\":\"Kecantikan\"}},{\"id\":\"772700669\",\"name\":\"Whitelab Brightening Face Serum\",\"imageURL\":\"https://ecs7-p.tokopedia.net/img/cache/200-square/VqbcmM/2021/4/5/10514516-1c42-46f0-9849-6e71b218db1a.png\",\"redirectURL\":\"https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEopHFo_rFoAeDUMVj9RzNrc1i6sHDUSC5rfB7q3YXUsthbm-7q3OBUsthosJRopUO6AJFbm-xgVYpyfYagZUEHsyObsHOoAyRosKRHs1N6AKDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_1o-r7BW_sCsQABE3BPc8ujagfBvq1BN_7H7oJYJgpzvzcr7_7zSoJYJgpzvzVB2_JoGrB2yQAoiH7203Ao6qMUpZMh-qMY2_1o-r7BWo9giQ1NDZ325q1NJe_VozOjp3jPuPVB93MOv1pnF_jz6qjh9ZMxvu7u7_S20oJNE39x68B2k_92qQ1B2PfBiH72F3s-DPuKa1MhszsBF3jo-ojBke3BHe72fyfODQMV9o3gsZJ2f3jx3qMzc69Cqe7jfZ32Cq1hAZSgsQ3hXyurOgMHaqIgszsBF3jo-ojBke3BHe72dgVP383u36_CgufzEZ325q1B2PfBsHjNfyfO3gBBXZSgjH7NDZ325q1Ok3_VHqj7h_jzgH7NkgpooqB1O_7zSq1hAZS2gHsBN3ByN8B29zSBgHMP2_fB-81Nke9uvuJ77_92gHJNEypuo8juR_M2oq3gzv_7ibm-Orfua9fBjUstiHZUDUMVDgaUEUMzBgiUDUMNOQ3-BrBY5gBYxgIHi6sJDUMNOQ3-BrBYxgIowrMuhUsthbm-X9foxQMz2gcV7guYxgIHi6sUdbm-xyBY7g9o7Usti_iUDUSgBrSo2Qfdi6i-fHi-Y?click_ad_type=2&src=rewards&page=1&template_id_used=3&t=ios&uid=1&r=https%3A%2F%2Fwww.tokopedia.com%2Fwhitelab%2Fwhitelab-brightening-face-serum&cpm_template_req=3&product_id=772700669\",\"redirectAppLink\":\"tokopedia://product/772700669\",\"benefitLabel\":\"42\",\"category\":{\"rootID\":\"61\",\"rootName\":\"Kecantikan\"}},{\"id\":\"1439367147\",\"name\":\"Whitelab Acne Calming Serum\",\"imageURL\":\"https://ecs7-p.tokopedia.net/img/cache/200-square/VqbcmM/2020/12/21/4f4904d7-00af-4ae4-81de-edb7844166a5.jpg\",\"redirectURL\":\"https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEopHFo_rFoAeDUMVj9RzNrc1i6sHDUSC5rfB7q3YXUsthbm-7q3OBUsthosJRopUO6AJFbm-xgVYpyfYagZUEHsyObsHOoAyRosKRHs1N6AKDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_1o-r7BW_sCsQABE3BPc8ujagfBvq1BN_7H7oJYJgpzvzcr7_7zSoJYJgpzvzVB2_JoGrB2yQAoiH7203Ao6qMUpZMh-qMY2_1o-r7BWo9giQ1NDZ325q1NJe_VozOjp3jPuPVB93MOv1pnF_jz6qjh9ZMxvu7u7_S20oJNE39x68B2k_92qQ1B2PfBiH72F3s-DPuKa1MhszsBF3jo-ojBke3BHe72fyfODQMV9o3gsZJ2f3jx3qMzc69Cqe7jfZ32Cq1hAZSgsQ3hXyurOgMHaqIgszsBF3jo-ojBke3BHe72dgVP383u36_CgufzEZ325q1B2PfBsHjNfyfO3gBBXZSgjH7NDZ325q1Ok3_VHqj7h_jzgH7NkgpooqB1O_7zSq1hAZS2gHsBN3ByN8B29zSBgHMP2_fB-81Nke9uvuJ77_92gHJNEypuo8juR_M2oq3gzv_7ibm-Orfua9fBjUstiHZUDUMVDgaUEUMzBgiUDUMNOQ3-BrBY5gBYxgIHi6sJDUMNOQ3-BrBYxgIowrMuhUsthbm-X9foxQMz2gcV7guYxgIHi6sUdbm-xyBY7g9o7Usti_iUDUSgBrSo2Qfdi6i-fHi-Y?r=https%3A%2F%2Fwww.tokopedia.com%2Fwhitelab%2Fwhitelab-acne-calming-serum&src=rewards&page=1&product_id=1439367147&t=ios&click_ad_type=2&uid=1&cpm_template_req=3&template_id_used=3\",\"redirectAppLink\":\"tokopedia://product/1439367147\",\"benefitLabel\":\"42\",\"category\":{\"rootID\":\"61\",\"rootName\":\"Kecantikan\"}}]}"