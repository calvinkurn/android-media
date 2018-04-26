package com.tokopedia.imagepicker.editor.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hendry on 25/04/18.
 */

public class ImageEditPreviewPresenter extends BaseDaggerPresenter<ImageEditPreviewPresenter.ImageEditPreviewView> {

    private float expectedPreviewWidth = 1280f;

    private CompositeSubscription compositeSubscription;

    public interface ImageEditPreviewView extends CustomerView {
        Context getContext();
        void onErrorConvertPathToPreviewBitmap(Throwable e);
        void onSuccessConvertPathToPreviewBitmap(BitmapPreviewResult bitmapPreviewResult, float expectedPreviewWidth);
    }

    public void detachView(){
        super.detachView();
        if (compositeSubscription!= null) {
            compositeSubscription.unsubscribe();
        }
    }

    public class BitmapPreviewResult{
        public Bitmap previewBitmap;
        public int originalWidth;
        public int originalHeight;
    }

    public void convertImagePathToPreviewBitmap(String imagePath){
        Subscription subscription =
                Observable.just(imagePath)
                        .map(new Func1<String, BitmapPreviewResult>() {
                            @Override
                            public BitmapPreviewResult call(String path) {
                                Bitmap previewBitmap = BitmapFactory.decodeFile(path);
                                int originalWidth = previewBitmap.getWidth();
                                int originalHeight = previewBitmap.getHeight();

                                int maxBitmapWidthOrHeight = Math.max(originalWidth, originalHeight);

                                if (maxBitmapWidthOrHeight > expectedPreviewWidth){
                                    float scaleToPreview = (float) maxBitmapWidthOrHeight / expectedPreviewWidth;
                                    previewBitmap = Bitmap.createScaledBitmap(previewBitmap, (int) (previewBitmap.getWidth() / scaleToPreview),
                                            (int) (previewBitmap.getHeight() / scaleToPreview), true);
                                }
                                BitmapPreviewResult bitmapPreviewResult = new BitmapPreviewResult();
                                bitmapPreviewResult.previewBitmap = previewBitmap;
                                bitmapPreviewResult.originalWidth = originalWidth;
                                bitmapPreviewResult.originalHeight = originalHeight;
                                return bitmapPreviewResult;
                            }
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BitmapPreviewResult>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                    if (isViewAttached()) {
                                        getView().onErrorConvertPathToPreviewBitmap(e);
                                    }
                               }

                               @Override
                               public void onNext(BitmapPreviewResult bitmapPreviewResult) {
                                   getView().onSuccessConvertPathToPreviewBitmap(bitmapPreviewResult, expectedPreviewWidth);
                               }
                           }
                );
        if (compositeSubscription== null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

}
