package com.tokopedia.purchase_platform.features.promo.presentation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*

fun mockPromoRecommendation(): PromoRecommendationUiModel {
    return PromoRecommendationUiModel(
            uiData = PromoRecommendationUiModel.UiData().apply {
                title = "Title aaaaaa"
                subTitle = "Sub title aaaa"
            },
            uiState = PromoRecommendationUiModel.UiState().apply {
                isButtonSelectEnabled = true
            }
    )
}

fun mockPromoInput(): PromoInputUiModel {
    return PromoInputUiModel(
            uiData = PromoInputUiModel.UiData().apply {
                promoCode = ""
            },
            uiState = PromoInputUiModel.UiState().apply {
                isButtonSelectEnabled = false
            }
    )
}

fun mockEligibleHeader(): PromoEligibilityHeaderUiModel {
    return PromoEligibilityHeaderUiModel(
            uiData = PromoEligibilityHeaderUiModel.UiData().apply {
                title = "Pilih promo"
                subTitle = "Kamu bisa gabungkan promo biar makin hemat"
            },
            uiState = PromoEligibilityHeaderUiModel.UiState().apply {
                isEnabled = true
            }
    )
}

fun mockIneligibleHeader(): PromoEligibilityHeaderUiModel {
    return PromoEligibilityHeaderUiModel(
            uiData = PromoEligibilityHeaderUiModel.UiData().apply {
                title = "Kupon yang belum bisa dipakai"
            },
            uiState = PromoEligibilityHeaderUiModel.UiState().apply {
                isEnabled = false
            }
    )
}

fun mockEligiblePromoGlobalSection(): List<Visitable<*>> {
    val dataList = ArrayList<Visitable<*>>()
    val promoListHeaderUiModel = PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                title = "Kupon saya global"
                subTitle = "Hanya bisa pilih 1"
                promoType = PromoListHeaderUiModel.UiData.PROMO_TYPE_GLOBAL
                identifierId = 1
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isCollapsed = false
                isEnabled = true
            }
    )
    dataList.add(promoListHeaderUiModel)

    val promoListItemUiModel = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                promoId = 0
                parentIdentifierId = 1
                title = "Promo pertama"
                subTitle = "Berakhir 1 jam lagi"
                promoCode = "TOKOPEDIACASHBACK"
            },
            uiState = PromoListItemUiModel.UiState().apply {
                isEnabled = true
            }
    )
    dataList.add(promoListItemUiModel)

    val promoListItemUiModel1 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                promoId = 1
                parentIdentifierId = 1
                title = "Promo kedua"
                subTitle = "Berakhir 2 jam lagi"
                errorMessage = "Kena Error"
            },
            uiState = PromoListItemUiModel.UiState().apply {
                isEnabled = false
            }
    )
    dataList.add(promoListItemUiModel1)

    val promoListItemUiModel2 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                promoId = 2
                parentIdentifierId = 1
                title = "Promo ketiga"
                subTitle = "Berakhir 3 jam lagi"
                imageResourceUrl = "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg"
            },
            uiState = PromoListItemUiModel.UiState().apply {
                isEnabled = true
            }
    )
    dataList.add(promoListItemUiModel2)

    return dataList
}

fun mockEligiblePromoGoldMerchantSection(): List<Visitable<*>> {
    val dataList = ArrayList<Visitable<*>>()
    val promoListItemUiModel3 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                promoId = 3
                parentIdentifierId = 2
                title = "Promo pertama stage 2"
                subTitle = "Berakhir 2 jam lagi"
            },
            uiState = PromoListItemUiModel.UiState().apply {
                isEnabled = true
            }
    )
    val promoListHeaderUiModel1 = PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                identifierId = 2
                title = "Ini promo power merchant"
                subTitle = "Hanya bisa pilih 1"
                promoType = PromoListHeaderUiModel.UiData.PROMO_TYPE_POWER_MERCHANT
                tmpPromoItemList = arrayListOf(promoListItemUiModel3)
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isCollapsed = true
                isEnabled = true
            }
    )
    dataList.add(promoListHeaderUiModel1)

    return dataList
}

fun mockEligiblePromoOfficialStoreSection(): List<Visitable<*>> {
    val dataList = ArrayList<Visitable<*>>()
    val promoListItemUiModel4 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                promoId = 4
                parentIdentifierId = 3
                title = "Promo kedua"
                subTitle = "Berakhir 2 jam lagi"
                errorMessage = "Kena Error"
            },
            uiState = PromoListItemUiModel.UiState().apply {
                isEnabled = false
            }
    )
    val promoListHeaderUiModel2 = PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                identifierId = 3
                title = "Ini promo official store"
                subTitle = "Hanya bisa pilih 1"
                promoType = PromoListHeaderUiModel.UiData.PROMO_TYPE_MERCHANT_OFFICIAL
                tmpPromoItemList = arrayListOf(promoListItemUiModel4)
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isCollapsed = true
                isEnabled = true
            }
    )
    dataList.add(promoListHeaderUiModel2)

    return dataList
}

fun mockIneligiblePromoGlobalSection(): List<Visitable<*>> {
    val dataList = ArrayList<Visitable<*>>()
    val promoListHeaderUiModel = PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                title = "Kupon saya global"
                subTitle = "Hanya bisa pilih 1"
                promoType = PromoListHeaderUiModel.UiData.PROMO_TYPE_GLOBAL
                identifierId = 11
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isCollapsed = false
                isEnabled = false
            }
    )
    dataList.add(promoListHeaderUiModel)

    val promoListItemUiModel = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                promoId = 100
                parentIdentifierId = 11
                title = "Promo pertama"
                subTitle = "Berakhir 1 jam lagi"
                errorMessage = "Tambah Rp27.500 untuk pakai promo."
                imageResourceUrl = "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg"
            },
            uiState = PromoListItemUiModel.UiState().apply {
                isEnabled = false
            }
    )
    dataList.add(promoListItemUiModel)

    val promoListItemUiModel1 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                promoId = 101
                parentIdentifierId = 11
                title = "Promo kedua"
                subTitle = "Berakhir 2 jam lagi"
                errorMessage = "Tambah Rp10.000 untuk pakai promo."
            },
            uiState = PromoListItemUiModel.UiState().apply {
                isEnabled = false
            }
    )
    dataList.add(promoListItemUiModel1)

    val promoListItemUiModel2 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                promoId = 102
                parentIdentifierId = 11
                title = "Promo ketiga"
                subTitle = "Berakhir 3 jam lagi"
                errorMessage = "Belanja di kategori fashion wanita, kecantikan atau fashion pria untuk pakai promo ini. "
            },
            uiState = PromoListItemUiModel.UiState().apply {
                isEnabled = false
            }
    )
    dataList.add(promoListItemUiModel2)

    return dataList
}

fun mockIneligiblePromoGoldMerchantSection(): List<Visitable<*>> {
    val dataList = ArrayList<Visitable<*>>()
    val promoListHeaderUiModel1 = PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                identifierId = 22
                title = "Ini promo power merchant"
                subTitle = "Hanya bisa pilih 1"
                promoType = PromoListHeaderUiModel.UiData.PROMO_TYPE_POWER_MERCHANT
                tmpPromoItemList = emptyList()
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isCollapsed = false
                isEnabled = false
            }
    )
    dataList.add(promoListHeaderUiModel1)

    val promoListItemUiModel3 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                promoId = 201
                parentIdentifierId = 22
                title = "Promo pertama stage 2"
                subTitle = "Berakhir 2 jam lagi"
                errorMessage = "Tambah Rp27.500 untuk pakai promo."
            },
            uiState = PromoListItemUiModel.UiState().apply {
                isEnabled = false
            }
    )
    dataList.add(promoListItemUiModel3)

    return dataList
}

fun mockIneligiblePromoOfficialStoreSection(): List<Visitable<*>> {
    val dataList = ArrayList<Visitable<*>>()
    val promoListHeaderUiModel2 = PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                identifierId = 33
                title = "Ini promo official store"
                subTitle = "Hanya bisa pilih 1"
                promoType = PromoListHeaderUiModel.UiData.PROMO_TYPE_MERCHANT_OFFICIAL
                tmpPromoItemList = emptyList()
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isCollapsed = false
                isEnabled = false
            }
    )
    dataList.add(promoListHeaderUiModel2)

    val promoListItemUiModel4 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                promoId = 301
                parentIdentifierId = 33
                title = "Promo kedua"
                subTitle = "Berakhir 2 jam lagi"
                errorMessage = "Tambah Rp27.500 untuk pakai promo."
            },
            uiState = PromoListItemUiModel.UiState().apply {
                isEnabled = false
            }
    )
    dataList.add(promoListItemUiModel4)

    return dataList
}