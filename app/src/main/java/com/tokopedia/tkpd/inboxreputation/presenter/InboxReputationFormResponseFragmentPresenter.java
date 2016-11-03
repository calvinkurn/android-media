package com.tokopedia.tkpd.inboxreputation.presenter;

/**
 * Created by Nisie on 2/29/16.
 */
public interface InboxReputationFormResponseFragmentPresenter {
    int generateRating(int rating);

    void onPreviewImageClicked();

    void postResponse(String response);

    void onDestroyView();
}
