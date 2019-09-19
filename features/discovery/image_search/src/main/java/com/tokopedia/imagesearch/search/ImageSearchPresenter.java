package com.tokopedia.imagesearch.search;

import android.content.Context;

import com.tokopedia.imagesearch.data.subscriber.DefaultImageSearchSubscriber;
import com.tokopedia.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.usecase.RequestParams;

/**
 * Created by sachinbansal on 4/12/18.
 */

public class ImageSearchPresenter implements ImageSearchContract.Presenter {

    private final GetImageSearchUseCase getImageSearchUseCase;
    private ImageSearchContract.View view;

    public ImageSearchPresenter(GetImageSearchUseCase getImageSearchUseCase) {
        this.getImageSearchUseCase = getImageSearchUseCase;
    }

    @Override
    public void attachView(ImageSearchContract.View view) {
        this.view = view;
    }

    @Override
    public void requestImageSearch(String imagePath) {
        getImageSearchUseCase.setImagePath(imagePath);
        getImageSearchUseCase.execute(
                RequestParams.EMPTY,
                new DefaultImageSearchSubscriber(view)
        );
    }

    @Override
    public void detachView() {
        if(getImageSearchUseCase != null) getImageSearchUseCase.unsubscribe();
    }
}
