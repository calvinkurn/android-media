package com.tokopedia.common_digital.common.util

object BranchProductGroup {
    private const val AIR_PDAM = "5"
    private const val ANGSURAN_KREDIT = "7"
    private const val BPJS = "4"
    private const val DONASI = "12"
    private const val FOOD_AND_VOUCHER = "24"
    private const val E_MONEY = "34"
    private const val LISTRIK_PLN = "3"
    private const val MTIX_XXI = "31"
    private const val PAJAK_PBB = "22"
    private const val PAKET_DATA = "2"
    private const val PASCA_BAYAR = "9"
    private const val PULSA = "1"
    private const val ROAMING = "20"
    private const val TELKOM = "10"
    private const val TV_KABEL = "8"

    fun getGroupWiseProductID(categoryId: String): String {
        return when (categoryId) {
            AIR_PDAM            -> "{29,399,453}"
            ANGSURAN_KREDIT     -> "{2076,2097,798}"
            BPJS                -> "{798,279,280}"
            DONASI              -> "{470,468,487}"
            FOOD_AND_VOUCHER    -> "{16050,16051,18685}"
            E_MONEY             -> "{2069,2070,2071,765}"
            LISTRIK_PLN         -> "{172,173,291}"
            MTIX_XXI            -> "{1585,1586,1588}"
            PAJAK_PBB           -> "{1569,2077,2104}"
            PAKET_DATA          -> "{1951,2699,1408}"
            PASCA_BAYAR         -> "{559,563,560}"
            PULSA               -> "{69,31,84}"
            ROAMING             -> "{1078,1911,1341}"
            TELKOM              -> "{689}"
            TV_KABEL            -> "{577,1249,1600}"
            else                -> categoryId
        }
    }
}
