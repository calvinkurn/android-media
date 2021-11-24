package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailData
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateCommissionDetailsUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(transactionID:String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_TRANSACTION_ID] = transactionID
        return request
    }

    suspend fun affiliateCommissionDetails(transactionID:String): AffiliateCommissionDetailData {
//        return repository.getGQLData(
//                GQL_Affiliate_Commission,
//                AffiliateCommissionDetailData::class.java,
//                createRequestParams(transactionID)
//        )
        return  mockData()
    }

    private fun mockData(): AffiliateCommissionDetailData {
        val list = ArrayList<AffiliateCommissionDetailData.GetAffiliateCommissionDetail.Data.Detail>()
        list.add(AffiliateCommissionDetailData.GetAffiliateCommissionDetail.Data.Detail("","","Rincian Komisi",""))
        list.add(AffiliateCommissionDetailData.GetAffiliateCommissionDetail.Data.Detail("Rp5.000.000","","Total Harga (5 barang)",""))
        list.add(AffiliateCommissionDetailData.GetAffiliateCommissionDetail.Data.Detail("5%","","Komisi","Maksimal komisi yang bisa kamu dapat dari setiap produk yang terjual adalah Rp50.000"))
        list.add(AffiliateCommissionDetailData.GetAffiliateCommissionDetail.Data.Detail("Rp50.0000","","Total Diterima",""))
        return AffiliateCommissionDetailData(
            AffiliateCommissionDetailData.GetAffiliateCommissionDetail(
                AffiliateCommissionDetailData.GetAffiliateCommissionDetail.Data(
                AffiliateCommissionDetailData.GetAffiliateCommissionDetail.Data.CardDetail(
null,"Rp1.000.000","Jordan 1 Retro High Black Satin Gym Red - Black Red",null,null,null,null,"NCR Sport"
                ),"","22 Des 2020, 10:30 WIB",list,null,null,null,null,null,null
            )))
    }

    companion object {
        private const val PARAM_TRANSACTION_ID = "transactionID"
    }
}