package com.tokopedia.core.network.entity.tokocash;

/**
 * Created by nabillasabbaha on 11/17/17.
 */

@Deprecated
public class RefreshTokenEntity {

   private WalletTokenEntity data;

    public WalletTokenEntity getData() {
        return data;
    }

    public void setData(WalletTokenEntity data) {
        this.data = data;
    }
}
