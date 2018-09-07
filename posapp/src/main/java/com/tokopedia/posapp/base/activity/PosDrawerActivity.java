package com.tokopedia.posapp.base.activity;

import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.util.SessionHandler;

/**
 * @author okasurya on 5/24/18.
 */
public abstract class PosDrawerActivity<T> extends DrawerPresenterActivity<T> {
    @Override
    protected void getDrawerUserAttrUseCase(SessionHandler sessionHandler) {
        // no-op
    }
}
