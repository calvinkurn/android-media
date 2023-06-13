package com.tokopedia.logisticaddaddress.features.pinpoint.webview.analytics

enum class AddAddressPinpointTracker(val trackerId: String) {
    ClickFieldCariLokasi("11045"),
    ClickDropdownSuggestion("11046"),
    ClickGunakanLokasiSaatIniSearch("11047"),
    ClickAllowLocationSearch("11050"),
    ClickDontAllowLocationSearch("11051"),
    ClickAktifkanLayananLokasiSearch("11052"),
    ClickXOnBlockGpsSearch("11053"),
    ClickBackArrowSearch("11055"),
    ImpressBottomSheetAlamatTidakTerdeteksi("11056"),
    ClickIsiAlamatManualUndetectedLocation("11057"),
    ImpressBottomSheetOutOfIndo("11058"),
    ClickIsiAlamatOutOfIndo("11059"),
    ClickCariUlangAlamat("11062"),
    ClickGunakanLokasiSaatIniPinpoint("11063"),
    ClickAllowLocationPinpoint("11064"),
    ClickDontAllowLocationPinpoint("11065"),
    ClickAktifkanLayananLokasiPinpoint("11066"),
    ClickXOnBlockGpsPinpoint("11067"),
    ClickIconQuestion("11068"),
    ClickPilihLokasiPositive("11069"),
    ClickPilihLokasiNegative("11083"),
    ClickBackArrowPinpoint("11061"),
    ViewToasterPinpointTidakSesuai("11084");

    companion object {
        private val values = values()

        @JvmStatic
        fun getById(trackerId: String): AddAddressPinpointTracker? {
            for (tracker in values) {
                if (tracker.trackerId == trackerId) return tracker
            }
            return null
        }
    }
}
