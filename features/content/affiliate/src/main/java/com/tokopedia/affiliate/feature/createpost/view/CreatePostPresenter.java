package com.tokopedia.affiliate.feature.createpost.view;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;

import javax.inject.Inject;

/**
 * @author by milhamj on 9/26/18.
 */
public class CreatePostPresenter extends BaseDaggerPresenter<CreatePostContract.View>
        implements CreatePostContract.Presenter {

    @Inject
    CreatePostPresenter() {
    }

    @Override
    public void fetchContentForm(String productId, String adId) {

    }
}
