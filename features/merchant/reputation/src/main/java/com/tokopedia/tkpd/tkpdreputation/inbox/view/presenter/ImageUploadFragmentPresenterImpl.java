package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.tkpd.tkpdreputation.constant.Constant;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.GetSendReviewFormUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SetReviewFormCacheUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.ImageUploadPreviewActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.ImageUploadPreviewFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.ImageUploadPreviewFragmentView;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.sendreview.SendReviewPass;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import rx.Subscriber;

import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

/**
 * Created by Nisie on 2/12/16.
 */
public class ImageUploadFragmentPresenterImpl implements ImageUploadFragmentPresenter {

    private static final String TAG = ImageUploadFragmentPresenterImpl.class.getSimpleName();

    ImageUploadPreviewFragmentView viewListener;
    GetSendReviewFormUseCase getSendReviewFormUseCase;
    SetReviewFormCacheUseCase setReviewFormCacheUseCase;
    List<ImageUpload> deletedImageUploads;
    String cameraFileLoc;

    public ImageUploadFragmentPresenterImpl(ImageUploadPreviewFragmentView viewListener, Context context) {
        this.viewListener = viewListener;
        this.deletedImageUploads = new ArrayList<>();
        PersistentCacheManager persistentCacheManager = new PersistentCacheManager(context);
        this.getSendReviewFormUseCase = new GetSendReviewFormUseCase(persistentCacheManager);
        this.setReviewFormCacheUseCase = new SetReviewFormCacheUseCase(persistentCacheManager);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageUploadPreviewFragment.REQUEST_CODE_IMAGE_REVIEW && resultCode == Activity.RESULT_OK && data!= null) {
            ImageUpload image;
            ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
            if (imageUrlOrPathList!= null && imageUrlOrPathList.size() > 0) {
                for(String url : imageUrlOrPathList){
                    int position = viewListener.getAdapter().getList().size();
                    image = new ImageUpload();
                    image.setPosition(position);
                    image.setImageId(SendReviewUseCase.IMAGE + UUID.randomUUID().toString());
                    image.setFileLoc(url);
                    viewListener.getAdapter().addImage(image);
                    viewListener.setPreviewImage(image);
                }
            }
        }
    }

    @Override
    public void setImages(final Bundle arguments) {
        if (arguments.getBoolean(ImageUploadPreviewActivity.IS_UPDATE, false)) {

            getSendReviewFormUseCase.execute(RequestParams.EMPTY, new Subscriber<SendReviewPass>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, e.getMessage());
                }

                @Override
                public void onNext(SendReviewPass sendReviewPass) {

                    for (int i = 0; i < sendReviewPass.getListImage().size(); i++) {
                        sendReviewPass.getListImage().get(i).setPosition(i);
                    }
                    viewListener.getAdapter().addList(sendReviewPass.getListImage());
                    ImageUpload image = sendReviewPass.getListImage().get(arguments.getInt
                            (ImageUploadPreviewActivity.ARGS_POSITION, 0));
                    viewListener.setCurrentPosition(arguments.getInt(
                            ImageUploadPreviewActivity.ARGS_POSITION, 0));
                    viewListener.setDescription(image.getDescription());
                    viewListener.setPreviewImage(image);

                }
            });

        } else {

            for(String url : Objects.requireNonNull(arguments.getStringArrayList(Constant.ImageUpload.FILELOC))) {
                int position = viewListener.getAdapter().getList().size();
                final ImageUpload image = new ImageUpload();
                image.setPosition(position);
                image.setFileLoc(url);
                image.setImageId(SendReviewUseCase.IMAGE + UUID.randomUUID().toString());
                viewListener.getAdapter().addImage(image);
                viewListener.setPreviewImage(image);
            }
        }
    }

    @Override
    public void onDeleteImage(final int currentPosition) {

        getSendReviewFormUseCase.execute(RequestParams.EMPTY, new Subscriber<SendReviewPass>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (hasOnlyOneImage()) {
                    viewListener.getActivity().finish();
                }
            }

            @Override
            public void onNext(SendReviewPass sendReviewPass) {
                if (!viewListener.getAdapter().getList().get(currentPosition).getImageId()
                        .startsWith(SendReviewUseCase.IMAGE)) {
                    deletedImageUploads.add(viewListener.getAdapter().getList().get(currentPosition));
                }

                if (hasOnlyOneImage()) {
                    sendReviewPass.getListImage().clear();
                    sendReviewPass.getListDeleted().addAll(deletedImageUploads);
                    setReviewFormCacheUseCase.executeSync(SetReviewFormCacheUseCase.getParam
                            (sendReviewPass));
                    viewListener.getActivity().setResult(Activity.RESULT_OK);
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
        });

    }

    @Override
    public void onSubmitImageUpload(final ArrayList<ImageUpload> list) {

        getSendReviewFormUseCase.execute(RequestParams.EMPTY, new Subscriber<SendReviewPass>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onNext(SendReviewPass sendReviewPass) {
                Log.i(TAG, "Get The Cache!! " + sendReviewPass.toString());

                for (ImageUpload imageUpload : list) {
                    imageUpload.setIsSelected(false);
                }
                sendReviewPass.setListImage(list);
                sendReviewPass.getListDeleted().addAll(deletedImageUploads);
                setReviewFormCacheUseCase.executeSync(SetReviewFormCacheUseCase.getParam
                        (sendReviewPass));

            }
        });

        viewListener.getActivity().setResult(Activity.RESULT_OK);
        viewListener.getActivity().finish();


    }

    @Override
    public String getCameraFileLoc() {
        return cameraFileLoc;
    }

    @Override
    public void setCameraFileLoc(String cameraFileLoc) {
        this.cameraFileLoc = cameraFileLoc;
    }

    private boolean isLastItem(int currentPosition) {
        return currentPosition == viewListener.getAdapter().getList().size() - 1;
    }

    private boolean hasOnlyOneImage() {
        return viewListener.getAdapter().getList().size() == 1;
    }

}
