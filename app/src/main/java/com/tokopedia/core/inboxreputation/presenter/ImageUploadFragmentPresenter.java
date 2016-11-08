package com.tokopedia.core.inboxreputation.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.inboxreputation.model.ImageUpload;

import java.util.ArrayList;

/**
 * Created by Nisie on 2/12/16.
 */
public interface ImageUploadFragmentPresenter {

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void setImages(Bundle arguments);

    void onDeleteImage(int currentPosition);

    void onSubmitImageUpload(ArrayList<ImageUpload> list);

    void openImageGallery();

    void openCamera();
}
