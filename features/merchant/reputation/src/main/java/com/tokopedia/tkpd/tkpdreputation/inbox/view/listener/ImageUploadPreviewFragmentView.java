package com.tokopedia.tkpd.tkpdreputation.inbox.view.listener;

import android.app.Activity;
import android.os.Bundle;

import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.ImageUploadPreviewFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;

/**
 * Created by Nisie on 6/7/16.
 */
public interface ImageUploadPreviewFragmentView {
    ImageUploadAdapter getAdapter();

    void setPreviewImage(ImageUpload image);

    void setCurrentPosition(int position);

    void setDescription(String description);

    Activity getActivity();

    ImageUploadPreviewFragment.PreviewImageViewPagerAdapter getPagerAdapter();

    Bundle getArguments();
}
