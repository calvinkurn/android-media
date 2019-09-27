package com.tokopedia.withdraw.constant;

import com.tokopedia.url.TokopediaUrl;

public class WithdrawConstant {
    private static final String WEB_DOMAIN_URL = TokopediaUrl.getInstance().getWEB();
    public static final String SALDOLOCK_PAYNOW_URL = WEB_DOMAIN_URL + "fm/modal-toko/dashboard/pembayaran";
    public static final String SALDOLOCK_INFO = WEB_DOMAIN_URL + "help/article/a-2086";

    public static final String KEY_PREMIUM_ACCOUNT_NEW_TAG = "swd_new_tag";
    public static final String WEB_REKENING_PREMIUM_URL= "https://m.tokopedia.com/payment/rekening-premium";
    public static final String WEB_TNC_URL= "https://m.tokopedia.com/terms/withdrawal-sla";
}
