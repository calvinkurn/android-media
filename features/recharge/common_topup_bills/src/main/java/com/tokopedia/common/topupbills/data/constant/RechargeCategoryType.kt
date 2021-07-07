package com.tokopedia.common.topupbills.data.constant

interface RechargeCategory {
    interface ID {
        companion object {
            const val PULSA = 1
            const val PAKET_DATA = 2
            const val ROAMING = 20
            const val PASCABAYAR = 9
        }
    }
    interface Name {
        companion object {
            const val PULSA = "Pulsa"
            const val PAKET_DATA = "Paket Data"
            const val ROAMING = "Roaming"
            const val PASCABAYAR = "Pascabayar"
        }
    }
}