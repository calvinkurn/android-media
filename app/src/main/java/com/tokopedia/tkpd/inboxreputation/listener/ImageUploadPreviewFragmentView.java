package com.tokopedia.tkpd.inboxreputation.listener;

import android.app.Activity;
import android.os.Bundle;

import com.tokopedia.tkpd.inboxreputation.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.inboxreputation.fragment.ImageUploadPreviewFragment;
import com.tokopedia.tkpd.inboxreputation.model.ImageUpload;

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
