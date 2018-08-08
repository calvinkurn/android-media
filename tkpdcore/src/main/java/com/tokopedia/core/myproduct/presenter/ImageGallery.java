package com.tokopedia.core.myproduct.presenter;

import com.tokopedia.core.myproduct.model.FolderModel;

import java.util.List;

/**
 * Created by m.normansyah on 03/12/2015.
 *
 * this class is abstract presenter
 */
public interface ImageGallery {
    String TAG = "MNORMANSYAH";
    String messageTAG = "ImageGallery : ";

    @Deprecated
    List<FolderModel> fetchImageUsingDb(String folderName);

    @Deprecated
    List<FolderModel> fetchImageUsingDb();

    void getItemAlbum();

    String getFolderPath(int position);

    void getItemListAlbum(String folderPath);

    void detach();
}
