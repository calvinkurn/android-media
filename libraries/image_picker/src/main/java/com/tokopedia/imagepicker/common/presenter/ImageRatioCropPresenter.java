package com.tokopedia.imagepicker.common.presenter;

import android.content.Context;
import android.media.ExifInterface;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hendry on 12/06/18.
 */

public class ImageRatioCropPresenter extends BaseDaggerPresenter<ImageRatioCropPresenter.ImageRatioCropView> {
    private CompositeSubscription compositeSubscription;

    public interface ImageRatioCropView extends CustomerView {
        Context getContext();

        void onErrorCropImageToRatio(ArrayList<String> localImagePaths, Throwable e);

        void onSuccessCropImageToRatio(ArrayList<String> cropppedImagePaths, ArrayList<Boolean> isEditted);

    }

    public void detachView() {
        super.detachView();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    public void cropBitmapToExpectedRatio(final ArrayList<String> localImagePaths, final ArrayList<ImageRatioTypeDef> imageRatioList,
                                          final boolean needCheckRotate, final @ImageUtils.DirectoryDef String targetDirectory) {
        Subscription subscription =
                Observable.zip(
                        Observable.from(localImagePaths),
                        Observable.from(imageRatioList),
                        new Func2<String, ImageRatioTypeDef, String>() {
                            @Override
                            public String call(String imagePath, ImageRatioTypeDef imageRatioTypeDef) {
                                System.gc();

                                int ratioX = imageRatioTypeDef.getRatioX();
                                int ratioY = imageRatioTypeDef.getRatioY();

                                if (ratioX <= 0 || ratioY <= 0) {
                                    return imagePath;
                                }
                                // if the dimension is not expected dimension, crop it
                                float expectedRatio = (float) ratioX / ratioY;
                                int[] widthHeight = ImageUtils.getWidthAndHeight(imagePath);
                                int defaultOrientation;
                                if (needCheckRotate) {
                                    try {
                                        defaultOrientation = ImageUtils.getOrientation(imagePath);
                                    } catch (Throwable e) {
                                        defaultOrientation = ExifInterface.ORIENTATION_NORMAL;
                                    }
                                } else {
                                    defaultOrientation = ExifInterface.ORIENTATION_NORMAL;
                                }
                                int width;
                                int height;
                                if (defaultOrientation == ExifInterface.ORIENTATION_ROTATE_90 ||
                                        defaultOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
                                    width = widthHeight[1];
                                    height = widthHeight[0];
                                } else {
                                    width = widthHeight[0];
                                    height = widthHeight[1];
                                }
                                float currentRatio = (float) width / height;
                                if (expectedRatio == currentRatio) {
                                    return imagePath;
                                } else {
                                    String outputPath;
                                    outputPath = ImageUtils.trimBitmap(imagePath, expectedRatio, currentRatio, needCheckRotate,
                                            targetDirectory);
                                    return outputPath;
                                }
                            }
                        })
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<List<String>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().onErrorCropImageToRatio(localImagePaths, e);
                                }
                            }

                            @Override
                            public void onNext(List<String> croppedImagedPath) {
                                if (isViewAttached()) {
                                    ArrayList<String> resultLocalPaths = new ArrayList<>();
                                    ArrayList<Boolean> isEdittedList = new ArrayList<>();
                                    for (int i = 0, sizei = croppedImagedPath.size(); i < sizei; i++) {
                                        String result = croppedImagedPath.get(i);
                                        resultLocalPaths.add(result);
                                        if (result.equals(localImagePaths.get(i))) {
                                            isEdittedList.add(false);
                                        } else {
                                            isEdittedList.add(true);
                                        }
                                    }
                                    getView().onSuccessCropImageToRatio(resultLocalPaths, isEdittedList);
                                }
                            }
                        });

        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

}
