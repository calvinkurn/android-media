package com.tokopedia.core.gallery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.myproduct.utils.MetadataUtil;
import com.tokopedia.core.myproduct.utils.VerificationUtils;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.newgallery.presenter.ImageGalleryView;
import com.tokopedia.core.util.Pair;

import java.util.ArrayList;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;
import static com.tokopedia.core.newgallery.presenter.ImageGalleryView.messageTAG;

/**
 * Helper class to get images.
 */
public class ImageGalleryEntry{

    /**
     * get image gallery for certain position without limitation of selection
     * @param appCompatActivity valid appCompat
     * @param position position to return
     */
    public static void moveToImageGallery(AppCompatActivity appCompatActivity, int position){
        if(checkNotNull(appCompatActivity))
            GalleryActivity.moveToImageGallery(appCompatActivity, position);
    }

    /**
     * get image gallery for certain position and having certain selection.
     * @param appCompatActivity valid appCompat
     * @param position position to return
     */
    public static void moveToImageGallery(AppCompatActivity appCompatActivity, int position, int maxSelection){
        if(checkNotNull(appCompatActivity))
            GalleryActivity.moveToImageGallery(appCompatActivity, position, maxSelection);
    }

    /**
     * get image with camera for position 0
     * @param appCompatActivity valid appCompat
     */
    public static void moveToImageGalleryCamera(AppCompatActivity appCompatActivity){
        if(checkNotNull(appCompatActivity))
            GalleryActivity.moveToImageGalleryCamera(appCompatActivity, 0, true, -1);
    }

    /**
     * send condition status get image from gallery or camera.
     * implement this in activity {@code AppCompatActivity}
     * This method include checking for supported image format and resolution of image.
     * @param galleryListener valid listener with context supply.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityForResult(GalleryListener galleryListener, int requestCode, int resultCode, Intent data){
        if ((requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY ||
            requestCode == GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE) && data != null) {
            String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
            if (checkNotNull(imageUrl)) {

                try {
                    Pair<Boolean, String> checkImageResolution = VerificationUtils.checkImageResolution(galleryListener.getContext(), imageUrl);
                    if (imageUrl != null && checkImageResolution.getModel1()) {
                        galleryListener.onSuccess(imageUrl);
                    } else {
                        galleryListener.onFailed(checkImageResolution.getModel2());
                        Log.e(ImageGalleryView.TAG, messageTAG + checkImageResolution.getModel2());
                    }
                } catch (MetadataUtil.UnSupportedimageFormatException e) {
                    e.printStackTrace();
                    galleryListener.onFailed(e.getMessage());
                    Log.e(ImageGalleryView.TAG, messageTAG + e.getMessage());
                }
            } else {
                ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryBrowser.IMAGE_URLS);
                if (checkCollectionNotNull(imageUrls)) {
                    galleryListener.onSuccess(imageUrls);
                }
            }
        }
    }

    /**
     * use this as your listener
     */
    public interface GalleryListener{
        void onSuccess(ArrayList<String> imageUrls);
        void onSuccess(String path);
        void onFailed(String message);

        /**
         * supply this in order to verif the images.
         * @return
         */
        Context getContext();
    }

}