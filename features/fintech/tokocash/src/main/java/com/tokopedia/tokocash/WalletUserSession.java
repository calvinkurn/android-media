package com.tokopedia.tokocash;

/**
 * Created by nabillasabbaha on 1/17/18.
 */

public interface WalletUserSession {

    void setTokenWallet(String token);

    String getTokenWallet();

    String getPhoneNumber();

    boolean isMsisdnVerified();

    void setMsisdnVerified(boolean verified);
}
