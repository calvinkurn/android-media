package com.tokopedia.notifications.common;

/**
 * @author lalit.singh
 */
public enum CMScreenEventMap {

    Beli("Beli","tokopedia://jump/kategori"), Bayar("Bayar","tokopedia://jump/tagihan"),
    Pesan("Pesan","tokopedia://jump/tiket"),Ajukan("Ajukan","tokopedia://jump/keuangan"),
    Jual("Bayar","tokopedia://jump/jual"),Cari("Cari","tokopedia://search"),
    Top_Up("Top-Up","tokopedia://jump/tagihan"),Promo("Promo","tokopedia://promo"),
    Feed("Feed","tokopedia://feed"),
    Share("Share","tokopedia://referral"),
    Pesawat("Pesawat","tokopedia://pesawat"),
    TokoPoints("TokoPoints","tokopedia://tokopoints"),
    Hotlist("Hotlist","tokopedia://hot"),
    Tutup("Tutup","Dismiss");

    private final String screenName;
    private final String appLink;

    CMScreenEventMap(String screenName, String appLink) {
        this.screenName = screenName;
        this.appLink= appLink;
    }

    public static String getScreenNameByApplink(String appLink) {
        for(CMScreenEventMap map :  CMScreenEventMap.values()){
            if(map.appLink.equalsIgnoreCase(appLink))
                return map.screenName;
        }
        return "unknown_screen";
    }
}
