package com.tokopedia.tkpd.myproduct.presenter;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;

import com.tokopedia.tkpd.myproduct.model.FolderModel;
import com.tokopedia.tkpd.myproduct.model.ImageModel;

import java.util.List;

/**
 * Created by m.normansyah on 03/12/2015.
 */
public interface ImageGalleryView extends MultiSelectInterface {

    String TAG = "MNORMANSYAH";
    String messageTAG = "ImageGalleryView : ";

    void fetchImageFromDb();

    void loadData(List<FolderModel> models);

    void moveToGallery(List<ImageModel> imageModels, int maxSelection);

    void initFragment(String FRAGMENT_TAG);

    void fetchExtras(Intent intent);

    void moveToFragment(Fragment fragment, boolean isAddtoBackStack, String TAG);

    void sendResultImageGallery(String path);

    void sendResultImageGallery(List<String> paths);

    ActionMode showActionMode(ActionMode.Callback callback);
}
