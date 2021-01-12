package com.tokopedia.imagepicker.editor.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.utils.image.ImageProcessingUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hendry on 07/05/18.
 */

public class ImageEditPreviewPresenter extends BaseDaggerPresenter<ImageEditPreviewPresenter.ImageEditPreviewView> {
    private CompositeSubscription compositeSubscription;
    private PublishSubject<Float> brightnessSubject;
    private PublishSubject<Float> contrastSubject;

    public interface ImageEditPreviewView extends CustomerView {
        Context getContext();

        void onSuccessGetBrightnessMatrix(ColorMatrixColorFilter colorMatrixColorFilter);

        void onErrorGetBrightnessMatrix(Throwable e);

        void onSuccessSaveBrightnessImage(String filePath);

        void onErrorSaveBrightnessImage(Throwable e);

        void onSuccessGetContrastMatrix(ColorMatrixColorFilter colorMatrixColorFilter);

        void onErrorGetContrastMatrix(Throwable e);

        void onSuccessSaveContrastImage(String filePath);

        void onErrorSaveContrastImage(Throwable e);
    }

    public ImageEditPreviewPresenter() {
        brightnessSubject = PublishSubject.create();
        Subscription brightnessSubscription = brightnessSubject
                .debounce(0, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .flatMap(new Func1<Float, Observable<ColorMatrixColorFilter>>() {
                    @Override
                    public Observable<ColorMatrixColorFilter> call(Float value) {
                        return Observable.just(getBrightnessMatrix(value));
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ColorMatrixColorFilter>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onErrorGetBrightnessMatrix(e);
                        }
                    }

                    @Override
                    public void onNext(ColorMatrixColorFilter colorMatrixColorFilter) {
                        if (isViewAttached()) {
                            getView().onSuccessGetBrightnessMatrix(colorMatrixColorFilter);
                        }
                    }
                });
        addToComposite(brightnessSubscription);

        contrastSubject = PublishSubject.create();
        Subscription contrastSubscription = contrastSubject
                .debounce(0, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .flatMap(new Func1<Float, Observable<ColorMatrixColorFilter>>() {
                    @Override
                    public Observable<ColorMatrixColorFilter> call(Float value) {
                        return Observable.just(getContrastMatrix(value));
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ColorMatrixColorFilter>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onErrorGetContrastMatrix(e);
                        }
                    }

                    @Override
                    public void onNext(ColorMatrixColorFilter colorMatrixColorFilter) {
                        if (isViewAttached()) {
                            getView().onSuccessGetContrastMatrix(colorMatrixColorFilter);
                        }
                    }
                });
        addToComposite(contrastSubscription);
    }

    private void addToComposite(Subscription subscription) {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    public void saveBrightnessImage(Bitmap bitmap, final float brightnessValue, final Bitmap.CompressFormat compressFormat) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        Subscription subscription =
                Observable.just(bitmap).flatMap(new Func1<Bitmap, Observable<String>>() {
                    @Override
                    public Observable<String> call(Bitmap bitmap) {
                        Bitmap resultBitmap = ImageProcessingUtil.brightBitmap(bitmap, brightnessValue);
                        File file = ImageProcessingUtil.writeImageToTkpdPath(resultBitmap, compressFormat);
                        return Observable.just(file.getAbsolutePath());
                    }
                })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().onErrorSaveBrightnessImage(e);
                                }
                            }

                            @Override
                            public void onNext(String filePath) {
                                if (isViewAttached()) {
                                    getView().onSuccessSaveBrightnessImage(filePath);
                                }
                            }
                        });
        addToComposite(subscription);
    }

    public void saveContrastImage(Bitmap bitmap, final float contrastValue, final Bitmap.CompressFormat compressFormat) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        Subscription subscription =
                Observable.just(bitmap).flatMap(new Func1<Bitmap, Observable<String>>() {
                    @Override
                    public Observable<String> call(Bitmap bitmap) {
                        Bitmap resultBitmap = ImageProcessingUtil.contrastBitmap(bitmap, contrastValue);
                        File file = ImageProcessingUtil.writeImageToTkpdPath(resultBitmap, compressFormat);
                        return Observable.just(file.getAbsolutePath());
                    }
                })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().onErrorSaveContrastImage(e);
                                }
                            }

                            @Override
                            public void onNext(String filePath) {
                                if (isViewAttached()) {
                                    getView().onSuccessSaveContrastImage(filePath);
                                }
                            }
                        });
        addToComposite(subscription);
    }

    public void rotateImage(Bitmap bitmap, final float angle, final Bitmap.CompressFormat compressFormat) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        Subscription subscription =
                Observable.just(bitmap).flatMap(new Func1<Bitmap, Observable<String>>() {
                    @Override
                    public Observable<String> call(Bitmap bitmap) {
                        Bitmap resultBitmap = ImageProcessingUtil.rotateBitmapByDegree(bitmap, angle);
                        File file = ImageProcessingUtil.writeImageToTkpdPath(resultBitmap, compressFormat);
                        return Observable.just(file.getAbsolutePath());
                    }
                })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().onErrorSaveContrastImage(e);
                                }
                            }

                            @Override
                            public void onNext(String filePath) {
                                if (isViewAttached()) {
                                    if (filePath != null) {
                                        getView().onSuccessSaveContrastImage(filePath);
                                    } else {
                                        getView().onErrorSaveContrastImage(null);
                                    }
                                }
                            }
                        });
        addToComposite(subscription);
    }

    public void getDebounceBrightnessMatrix(float brightnessValue) {
        brightnessSubject.onNext(brightnessValue);
    }

    public void getDebounceContrastMatrix(float contrastValue) {
        contrastSubject.onNext(contrastValue);
    }

    public ColorMatrixColorFilter getBrightnessMatrix(float brightnessValuePer100) {
        ColorMatrix cmB = new ColorMatrix();
        cmB.set(new float[]{
                1, 0, 0, 0, brightnessValuePer100,
                0, 1, 0, 0, brightnessValuePer100,
                0, 0, 1, 0, brightnessValuePer100,
                0, 0, 0, 1, 0});
        return new ColorMatrixColorFilter(cmB);
    }

    public ColorMatrixColorFilter getContrastMatrix(float contrastValue) {
        float[] array = new float[]{
                contrastValue, 0, 0, 0, 0,
                0, contrastValue, 0, 0, 0,
                0, 0, contrastValue, 0, 0,
                0, 0, 0, 1, 0};
        ColorMatrix matrix = new ColorMatrix(array);
        return new ColorMatrixColorFilter(matrix);
    }

    public void detachView() {
        super.detachView();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }
}
