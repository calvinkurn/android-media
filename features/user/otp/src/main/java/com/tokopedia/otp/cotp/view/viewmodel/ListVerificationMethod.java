package com.tokopedia.otp.cotp.view.viewmodel;

import java.util.List;

/**
 * @author by nisie on 1/18/18.
 */

public class ListVerificationMethod {

    List<MethodItem> list;
    int footerLinkType;

    public ListVerificationMethod(List<MethodItem> list, int footerLinkType) {
        this.list = list;
        this.footerLinkType = footerLinkType;
    }

    public List<MethodItem> getList() {
        return list;
    }

    public int getFooterLinkType() {
        return footerLinkType;
    }
}
