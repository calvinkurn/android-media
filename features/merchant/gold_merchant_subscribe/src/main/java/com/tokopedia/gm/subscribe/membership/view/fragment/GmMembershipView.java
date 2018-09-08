package com.tokopedia.gm.subscribe.membership.view.fragment;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

public interface GmMembershipView extends CustomerView {

    void showProgressDialog();

    void dismissProgressDialog();

    void showMessageError(String string);

}
