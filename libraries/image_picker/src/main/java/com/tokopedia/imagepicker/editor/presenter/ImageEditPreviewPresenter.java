package com.tokopedia.imagepicker.editor.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.editor.converter.BitmapConverter;
import com.tokopedia.imagepicker.editor.domain.SetRemoveBackgroundUseCase;
import com.tokopedia.imagepicker.editor.watermark.WatermarkBuilder;
import com.tokopedia.utils.image.ImageProcessingUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.ResponseBody;
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

    private SetRemoveBackgroundUseCase removeBackgroundUseCase;
    private Subscription requestRemoveBackground;

    public interface ImageEditPreviewView extends CustomerView {
        Context getContext();

        // brightness

        void onSuccessGetBrightnessMatrix(ColorMatrixColorFilter colorMatrixColorFilter);

        void onErrorGetBrightnessMatrix(Throwable e);

        void onSuccessSaveBrightnessImage(String filePath);

        void onErrorSaveBrightnessImage(Throwable e);

        // contrast

        void onSuccessGetContrastMatrix(ColorMatrixColorFilter colorMatrixColorFilter);

        void onErrorGetContrastMatrix(Throwable e);

        void onSuccessSaveContrastImage(String filePath);

        void onErrorSaveContrastImage(Throwable e);

        // watermark

        void onSuccessGetWatermarkImage(Bitmap[] bitmap);

        void onSuccessSaveWatermarkImage(String filePath);

        void onErrorWatermarkImage(Throwable e);

        // remove background

        void onSuccessGetRemoveBackground(String filePath);

        void onErrorGetRemoveBackground(Throwable e);

        void onSuccessSaveRemoveBackground(String filePath);

        void onErrorSaveRemoveBackground(Throwable e);
    }

    @Inject
    public ImageEditPreviewPresenter(SetRemoveBackgroundUseCase removeBackgroundUseCase) {
        this.removeBackgroundUseCase = removeBackgroundUseCase;
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
                    public Observable<String> call(Bitmap result) {
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

    public void saveCurrentBitmapImage(Bitmap bitmap, final Bitmap.CompressFormat compressFormat) {
        if (bitmap == null) return;

        Subscription subscription =
                Observable.just(bitmap).flatMap(new Func1<Bitmap, Observable<String>>() {
                    @Override
                    public Observable<String> call(Bitmap bitmap) {
                        File file = ImageProcessingUtil.writeImageToTkpdPath(bitmap, compressFormat);
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
                                    getView().onErrorWatermarkImage(e);
                                }
                            }

                            @Override
                            public void onNext(String filePath) {
                                if (isViewAttached()) {
                                    if (filePath != null) {
                                        getView().onSuccessSaveWatermarkImage(filePath);
                                    } else {
                                        getView().onErrorWatermarkImage(null);
                                    }
                                }
                            }
                        });
        addToComposite(subscription);
    }

    public void setTokopediaWatermark(String userInfoName, Bitmap mainBitmap) {
        if (mainBitmap == null || mainBitmap.isRecycled()) return;
        Bitmap watermarkLogo = BitmapFactory.decodeResource(
                getView().getContext().getResources(),
                R.drawable.watermark_logo_tokopedia
        );
        Subscription subscription = Observable.just(watermarkLogo).flatMap((Func1<Bitmap, Observable<Bitmap[]>>) tokopediaLogoBitmap -> {
            // create watermark with transparent container (empty) bitmap
            Bitmap[] watermark = WatermarkBuilder
                    .create(getView().getContext(), mainBitmap)
                    .loadOnlyWatermarkTextImage(userInfoName, tokopediaLogoBitmap)
                    .getOutputImages();

            return Observable.just(watermark);
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmaps -> {
                    watermarkLogo.recycle();
                    getView().onSuccessGetWatermarkImage(bitmaps);
                });

        addToComposite(subscription);
    }

    public void saveRemoveBackground(View view, final Bitmap.CompressFormat compressFormat, int backgroundColor) {
        if (view == null) return;

        Subscription subscription = Observable.just(view)
                .flatMap(new Func1<View, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(View view) {
                        return Observable.just(BitmapConverter.toBitmapWithBackgroundColor(view, backgroundColor));
                    }
                })
                .flatMap(new Func1<Bitmap, Observable<String>>() {
                    @Override
                    public Observable<String> call(Bitmap bitmap) {
                        File file = ImageProcessingUtil.writeImageToTkpdPath(bitmap, compressFormat);
                        return Observable.just(file.getAbsolutePath());
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onErrorSaveRemoveBackground(e);
                        }
                    }

                    @Override
                    public void onNext(String filePath) {
                        if (isViewAttached()) {
                            if (filePath != null) {
                                getView().onSuccessSaveRemoveBackground(filePath);
                            } else {
                                getView().onErrorSaveRemoveBackground(null);
                            }
                        }
                    }
                });
        addToComposite(subscription);
    }

    public void setRemoveBackground(Bitmap bitmap, final Bitmap.CompressFormat compressFormat) {
        if (bitmap == null || bitmap.isRecycled()) return;

        requestRemoveBackground =
                Observable.just(bitmap).flatMap(new Func1<Bitmap, Observable<File>>() {
                    @Override
                    public Observable<File> call(Bitmap bitmap) {
                        File file = ImageProcessingUtil.writeImageToTkpdPath(bitmap, compressFormat);
                        return Observable.just(file);
                    }
                }).flatMap(new Func1<File, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> call(File file) {
                        return removeBackgroundUseCase.invoke(file);
                    }
                }).flatMap(new Func1<ResponseBody, Observable<String>>() {
                    @Override
                    public Observable<String> call(ResponseBody result) {
                        File file = ImageProcessingUtil.writeImageToTkpdPath(result.byteStream(), compressFormat);
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
                                    getView().onErrorGetRemoveBackground(e);
                                }
                            }

                            @Override
                            public void onNext(String filePath) {
                                if (isViewAttached()) {
                                    if (filePath != null) {
                                        getView().onSuccessGetRemoveBackground(filePath);
                                    } else {
                                        getView().onErrorGetRemoveBackground(null);
                                    }
                                }
                            }
                        });

        addToComposite(requestRemoveBackground);
    }

    public void cancelRequestRemoveBackground() {
        if (requestRemoveBackground == null) return;

        if (!requestRemoveBackground.isUnsubscribed()) {
            requestRemoveBackground.unsubscribe();
        }
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

    public void unsubscribe() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }
}
