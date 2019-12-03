package com.tokopedia.withdraw.constant;

import com.tokopedia.url.TokopediaUrl;

public class WithdrawConstant {
    private static final String WEB_DOMAIN_URL = TokopediaUrl.getInstance().getWEB();
    public static final String SALDOLOCK_PAYNOW_URL = WEB_DOMAIN_URL + "fm/modal-toko/dashboard/pembayaran";
    public static final String SALDOLOCK_INFO = WEB_DOMAIN_URL + "help/article/a-2086";

    public static final String KEY_PREMIUM_ACCOUNT_NEW_TAG = "swd_new_tag";
    public static final String WEB_REKENING_PREMIUM_URL= "https://m.tokopedia.com/payment/rekening-premium";
    public static final String WEB_TNC_URL= "https://m.tokopedia.com/terms/withdrawal-sla";
    public static final int MAX_LENGTH = 14;

    public interface Keys{
        String BANK_ACCOUNT = "bank_account";
        String MESSAGE = "message";
        String AMOUNT = "amount";
    }

    public interface ResultCode{
        int GOTO_SALDO_DETAIL_PAGE = 101;
        int GOTO_TOKOPEDIA_HOME_PAGE = 102;
    }

}
