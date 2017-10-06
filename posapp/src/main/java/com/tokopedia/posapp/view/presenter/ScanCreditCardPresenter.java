package com.tokopedia.posapp.view.presenter;

import com.tokopedia.posapp.view.ScanCreditCard;

/**
 * Created by okasurya on 10/6/17.
 */

public class ScanCreditCardPresenter implements ScanCreditCard.Presenter {
    ScanCreditCard.View viewListener;

    public ScanCreditCardPresenter(ScanCreditCard.View viewListener) {
        this.viewListener = viewListener;
    }
}
