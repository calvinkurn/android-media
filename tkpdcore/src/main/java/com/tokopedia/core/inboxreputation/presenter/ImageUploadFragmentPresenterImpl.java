package com.tokopedia.core.inboxreputation.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.inboxreputation.listener.ImageUploadPreviewFragmentView;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.inboxreputation.fragment.ImageUploadPreviewFragment;
import com.tokopedia.core.inboxreputation.interactor.CacheInboxReputationInteractor;
import com.tokopedia.core.inboxreputation.interactor.CacheInboxReputationInteractorImpl;
import com.tokopedia.core.inboxreputation.model.ImageUpload;
import com.tokopedia.core.inboxreputation.model.param.ActReviewPass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Nisie on 2/12/16.
 */
public class ImageUploadFragmentPresenterImpl implements ImageUploadFragmentPresenter {

    private static final java.lang.String UPLOAD_URL = "url";

    ImageUploadPreviewFragmentView viewListener;
    ImageUploadHandler imageUploadHandler;
    CacheInboxReputationInteractor cacheInboxReputationInteractor;
    List<ImageUpload> deletedImageUploads;

    public ImageUploadFragmentPresenterImpl(ImageUploadPreviewFragment viewListener) {
        this.viewListener = viewListener;
        this.imageUploadHandler = ImageUploadHandler.createInstance(viewListener);
        this.cacheInboxReputationInteractor = new CacheInboxReputationInteractorImpl();
        this.deletedImageUploads = new ArrayList<>();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == ImageUploadHandler.REQUEST_CODE)
                && (resultCode == Activity.RESULT_OK || resultCode == GalleryBrowser.RESULT_CODE)) {

            int position = viewListener.getAdapter().getList().size();
            ImageUpload image = new ImageUpload();
            image.setPosition(position);
            image.setImageId("image" + UUID.randomUUID().toString());

            switch (resultCode) {
                case GalleryBrowser.RESULT_CODE:
                    image.setFileLoc(data.getStringExtra(ImageGallery.EXTRA_URL));
                    break;
                case Activity.RESULT_OK:
                    image.setFileLoc(imageUploadHandler.getCameraFileloc());
                    break;
                default:
                    break;
            }
            viewListener.getAdapter().addImage(image);
            viewListener.setPreviewImage(image);

        }
    }

    @Override
    public void setImages(final Bundle arguments) {
        if (arguments.getBoolean("is_update", false)) {
            cacheInboxReputationInteractor.getInboxReputationFormCache(new CacheInboxReputationInteractor.GetInboxReputationFormCacheListener() {
                @Override
                public void onSuccess(ActReviewPass review) {
                    for (int i = 0; i < review.getImageUploads().size(); i++) {
                        review.getImageUploads().get(i).setPosition(i);
                    }
                    viewListener.getAdapter().addList(review.getImageUploads());
                    ImageUpload image = review.getImageUploads().get(arguments.getInt("position", 0));
                    viewListener.setCurrentPosition(arguments.getInt("position", 0));
                    viewListener.setDescription(image.getDescription());
                    viewListener.setPreviewImage(image);
                }

                @Override
                public void onError(Throwable e) {

                }
            });
        } else {
            File imgFile = new File(arguments.getString(ImageUploadHandler.FILELOC, ""));

            if (imgFile.exists()) {
                final ImageUpload image = new ImageUpload();
                image.setFileLoc(arguments.getString(ImageUploadHandler.FILELOC, ""));
                image.setImageId("image" + UUID.randomUUID().toString());

                cacheInboxReputationInteractor.getInboxReputationFormCache(new CacheInboxReputationInteractor.GetInboxReputationFormCacheListener() {
                    @Override
                    public void onSuccess(ActReviewPass review) {
                        for (int i = 0; i < review.getImageUploads().size(); i++) {
                            review.getImageUploads().get(i).setPosition(i);
                            viewListener.getAdapter().addImage(review.getImageUploads().get(i));
                        }
                        image.setPosition(viewListener.getAdapter().getList().size());
                        viewListener.getAdapter().addImage(image);

                        viewListener.setPreviewImage(image);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        image.setPosition(viewListener.getAdapter().getList().size());
                        viewListener.getAdapter().addImage(image);
                        viewListener.setPreviewImage(image);
                    }
                });


            }
        }
    }

    @Override
    public void onDeleteImage(final int currentPosition) {

        cacheInboxReputationInteractor.getInboxReputationFormCache(new CacheInboxReputationInteractor.GetInboxReputationFormCacheListener() {
            @Override
            public void onSuccess(ActReviewPass review) {

                if (!viewListener.getAdapter().getList().get(currentPosition).getImageId().startsWith(ActReviewPass.NEW_IMAGE_INDICATOR)) {
                    deletedImageUploads.add(viewListener.getAdapter().getList().get(currentPosition));
                }

                if (hasOnlyOneImage()) {
                    review.getImageUploads().clear();
                    review.getDeletedImageUploads().addAll(deletedImageUploads);
                    cacheInboxReputationInteractor.setInboxReputationFormCache(viewListener.getArguments().getString(InboxReputationFormFragmentPresenterImpl.EXTRA_REVIEW_ID), review);
                    viewListener.getActivity().finish();
                } else if (isLastItem(currentPosition)) {
                    viewListener.getAdapter().removeImage(currentPosition);
                    viewListener.getPagerAdapter().resetAdapter();
                    viewListener.getPagerAdapter().notifyDataSetChanged();
                    viewListener.setCurrentPosition(currentPosition - 1);
                    viewListener.setDescription(viewListener.getAdapter().getList().get(currentPosition - 1).getDescription());
                    viewListener.setPreviewImage(viewListener.getAdapter().getList().get(currentPosition - 1));
                } else {
                    viewListener.getAdapter().removeImage(currentPosition);
                    viewListener.getPagerAdapter().resetAdapter();
                    viewListener.getPagerAdapter().notifyDataSetChanged();
                    viewListener.setPreviewImage(viewListener.getAdapter().getList().get(currentPosition));
                }


            }

            @Override
            public void onError(Throwable e) {
                if (hasOnlyOneImage()) {
                    viewListener.getActivity().finish();
                }
            }
        });


    }

    @Override
    public void onSubmitImageUpload(final ArrayList<ImageUpload> list) {

        String reviewId = viewListener.getArguments().getString(InboxReputationFormFragmentPresenterImpl.EXTRA_REVIEW_ID);
        cacheInboxReputationInteractor.getInboxReputationFormCache(new CacheInboxReputationInteractor.GetInboxReputationFormCacheListener() {
            @Override
            public void onSuccess(ActReviewPass review) {
                for (ImageUpload imageUpload : list) {
                    imageUpload.setIsSelected(false);
                }
                review.setImageUploads(list);
                review.getDeletedImageUploads().addAll(deletedImageUploads);
                cacheInboxReputationInteractor.setInboxReputationFormCache(review.getReviewId(), review);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });

        viewListener.getActivity().finish();


    }

    @Override
    public void openImageGallery() {
        imageUploadHandler.actionImagePicker();
    }

    @Override
    public void openCamera() {
        imageUploadHandler.actionCamera();
    }

    private boolean isLastItem(int currentPosition) {
        return currentPosition == viewListener.getAdapter().getList().size() - 1;
    }

    private boolean hasOnlyOneImage() {
        return viewListener.getAdapter().getList().size() == 1;
    }

}
