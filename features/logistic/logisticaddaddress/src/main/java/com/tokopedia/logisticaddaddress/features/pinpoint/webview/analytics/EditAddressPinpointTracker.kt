package com.tokopedia.logisticaddaddress.features.pinpoint.webview.analytics

enum class EditAddressPinpointTracker(val trackerId: String) {
    ClickFieldCariLokasi("29692"),
    ClickDropdownSuggestion("29693"),
    ClickGunakanLokasiSaatIniSearch("29694"),
    ClickBackArrowSearch("29695"),
    ImpressBottomSheetAlamatTidakTerdeteksi("29690"),
    ImpressBottomSheetOutOfIndo("29689"),
    ClickCariUlangAlamat("29687"),
    ClickGunakanLokasiSaatIniPinpoint("29686"),
    ClickBackArrowPinpoint("29691"),
    ClickPilihLokasiIni("29688");

    companion object {
        private val values = values()

        @JvmStatic
        fun getById(trackerId: String): EditAddressPinpointTracker? {
            for (tracker in values) {
                if (tracker.trackerId == trackerId) return tracker
            }
            return null
        }
    }
}
