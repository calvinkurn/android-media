package com.tokopedia.sellerapp.gmsubscribe.presenter;


import com.tokopedia.sellerapp.gmsubscribe.view.GMSubscribeView;

/**
 * Created by sebastianuskh on 11/11/16.
 */

public class GMSubscribePresenterImpl implements GMSubscribePresenter {

    GMSubscribeView view;

    public GMSubscribePresenterImpl(GMSubscribeView view) {
        this.view = view;
    }
}
