package com.tokopedia.gamification.floating.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.gamification.floatingtoken.model.TokenData;

/**
 * Created by hendry on 02/04/18.
 */

public interface FloatingEggView extends CustomerView {
    void onSuccessGetToken(TokenData tokenData);
    void onErrorGetToken(Throwable throwable);
}