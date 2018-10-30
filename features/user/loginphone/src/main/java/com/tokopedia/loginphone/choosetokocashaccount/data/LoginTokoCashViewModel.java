package com.tokopedia.loginphone.choosetokocashaccount.data;

import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;

/**
 * @author by nisie on 12/5/17.
 */

public class LoginTokoCashViewModel extends LoginEmailDomain {
    private GetCodeTokoCashPojo tokoCashCode;

    public void setTokoCashCode(GetCodeTokoCashPojo tokoCashCode) {
        this.tokoCashCode = tokoCashCode;
    }

    public GetCodeTokoCashPojo getTokoCashCode() {
        return tokoCashCode;
    }

}
