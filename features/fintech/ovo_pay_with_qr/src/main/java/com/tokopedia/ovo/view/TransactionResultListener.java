package com.tokopedia.ovo.view;

public interface TransactionResultListener {
    void finish();

    void goToSuccessFragment();

    void goToFailFragment();

    void setResult(int result);
}
