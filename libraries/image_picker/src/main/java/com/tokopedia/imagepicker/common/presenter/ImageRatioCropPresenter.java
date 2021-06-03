package com.tokopedia.imagepicker.common.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ExifInterface;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.imagepicker.common.ImageRatioType;
import com.tokopedia.imagepicker.editor.watermark.WatermarkBuilder;
import com.tokopedia.imagepicker.editor.watermark.uimodel.WatermarkText;
import com.tokopedia.utils.image.ImageProcessingUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.Pair;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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

    public void cropBitmapToExpectedRatio(final ArrayList<String> localImagePaths, final ArrayList<ImageRatioType> imageRatioList,
                                          final boolean needCheckRotate, boolean convertToWebp) {
        Subscription subscription =
                Observable.zip(
                        Observable.from(localImagePaths),
                        Observable.from(imageRatioList),
                        new Func2<String, ImageRatioType, String>() {
                            @Override
                            public String call(String imagePath, ImageRatioType imageRatioTypeDef) {
                                System.gc();

                                int ratioX = imageRatioTypeDef.getRatioX();
                                int ratioY = imageRatioTypeDef.getRatioY();

                                if (ratioX <= 0 || ratioY <= 0) {
                                    return imagePath;
                                }
                                // if the dimension is not expected dimension, crop it
                                float expectedRatio = (float) ratioX / ratioY;
                                Pair<Integer, Integer> widthHeight = ImageProcessingUtil.getWidthAndHeight(imagePath);
                                int defaultOrientation;
                                if (needCheckRotate) {
                                    try {
                                        defaultOrientation = ImageProcessingUtil.getOrientation(imagePath);
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
                                    width = widthHeight.getSecond();
                                    height = widthHeight.getFirst();
                                } else {
                                    width = widthHeight.getFirst();
                                    height = widthHeight.getSecond();
                                }
                                float currentRatio = (float) width / height;
                                if (expectedRatio == currentRatio) {
                                    return imagePath;
                                } else {
                                    String outputPath;
                                    if (convertToWebp)
                                        outputPath = ImageProcessingUtil.trimBitmap(imagePath, expectedRatio, currentRatio, needCheckRotate, Bitmap.CompressFormat.WEBP);
                                    else {
                                        outputPath = ImageProcessingUtil.trimBitmap(imagePath, expectedRatio, currentRatio, needCheckRotate);
                                    }
                                    return outputPath;
                                }
                            }
                        })
                        .flatMap(new Func1<String, Observable<Bitmap>>() {
                            @Override
                            public Observable<Bitmap> call(String path) {
                                Bitmap bitmap = ImageProcessingUtil.getBitmapFromPath(path);
                                return Observable.just(bitmap);
                            }
                        })
                        .flatMap(new Func1<Bitmap, Observable<Bitmap>>() {
                            @Override
                            public Observable<Bitmap> call(Bitmap bitmap) {
                                WatermarkText watermarkText = new WatermarkText()
                                        .setContentText("Tokopedia")
                                        .positionX(0.5)
                                        .positionY(0.5)
                                        .textAlpha(255)
                                        .textColor(Color.WHITE);


                                return Observable.just(WatermarkBuilder
                                        .create(getView().getContext(), bitmap)
                                        .loadWatermarkText(watermarkText)
                                        .setTileMode(true)
                                        .getWatermark()
                                        .getOutputImage()
                                );
                            }
                        })
                        .flatMap(new Func1<Bitmap, Observable<String>>() {
                            @Override
                            public Observable<String> call(Bitmap bitmap) {
                                File filePath;

                                if (convertToWebp) {
                                    filePath = ImageProcessingUtil.writeImageToTkpdPath(bitmap, Bitmap.CompressFormat.WEBP);
                                } else {
                                    filePath = ImageProcessingUtil.writeImageToTkpdPath(bitmap, Bitmap.CompressFormat.PNG);
                                }

                                return Observable.just(Objects.requireNonNull(filePath).getAbsolutePath());
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
