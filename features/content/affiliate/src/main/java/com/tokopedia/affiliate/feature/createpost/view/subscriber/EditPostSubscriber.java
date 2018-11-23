package com.tokopedia.affiliate.feature.createpost.view.subscriber;

import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;

/**
 * @author by milhamj on 10/19/18.
 */
public class EditPostSubscriber extends SubmitPostSubscriber {
    private final CreatePostContract.View view;

    public EditPostSubscriber(CreatePostContract.View view) {
        super(view);
        this.view = view;
    }

    @Override
    protected void showError(String message) {
        view.onErrorEditPost(message);
    }
}
