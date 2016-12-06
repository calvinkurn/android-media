package com.tokopedia.core.myproduct.presenter;

import android.content.Context;

import com.tokopedia.core.myproduct.model.FolderModel;

import java.util.List;

/**
 * Created by m.normansyah on 03/12/2015.
 */
public interface ImageGallery {
    String TAG = "MNORMANSYAH";
    String messageTAG = "ImageGallery : ";

    List<FolderModel> fetchImageUsingDb(Context context, String folderName);

    List<FolderModel> fetchImageUsingDb(Context context);
}
