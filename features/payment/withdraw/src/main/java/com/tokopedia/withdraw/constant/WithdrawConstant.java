package com.tokopedia.withdraw.constant;

import com.tokopedia.url.TokopediaUrl;

public class WithdrawConstant {
    private static final String WEB_DOMAIN_URL = TokopediaUrl.getInstance().getWEB();
    public static final String SALDOLOCK_PAYNOW_URL = WEB_DOMAIN_URL + "fm/modal-toko/dashboard/pembayaran";
    public static final String SALDOLOCK_INFO = WEB_DOMAIN_URL + "help/article/a-1815";
}
