package com.tokopedia.core.newgallery.presenter;

import android.content.ContentResolver;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;

import com.tokopedia.core.myproduct.model.FolderModel;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.core.myproduct.presenter.MultiSelectInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 03/12/2015.
 */
public interface ImageGalleryView extends MultiSelectInterface {

    String TAG = "MNORMANSYAH";
    String messageTAG = "ImageGalleryView : ";

    void fetchImageFromDb();

    void fetchImageFromDb(String folderPath);

    ContentResolver getContentResolver();

    @Deprecated
    void loadData(List<FolderModel> models);

    @Deprecated
    void moveToGallery(List<ImageModel> imageModels, int maxSelection);

    void moveToGallery(int position, int maxSelection);

    void initFragment(String FRAGMENT_TAG);

    void fetchExtras(Intent intent);

    void moveToFragment(Fragment fragment, boolean isAddtoBackStack, String TAG);

    void sendResultImageGallery(String path);

    void sendResultImageGallery(List<String> paths);

    ActionMode showActionMode(ActionMode.Callback callback);

    void retrieveData(ArrayList<com.tokopedia.core.newgallery.model.ImageModel> dataAlbum);

    void retrieveItemData(ArrayList<com.tokopedia.core.newgallery.model.ImageModel> dataAlbum);

    boolean isNeedPermission();
}
