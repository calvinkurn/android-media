package com.tokopedia.imagesearch.search;

import android.content.Context;

import com.tokopedia.imagesearch.domain.viewmodel.ProductViewModel;

/**
 * Created by sachinbansal on 4/12/18.
 */

public class ImageSearchContract {


    public interface View {

        void onHandleImageResponseSearch(ProductViewModel productViewModel);

        void onHandleInvalidImageSearchResponse();

        void showImageNotSupportedError();

        Context getContext();
    }

    public interface Presenter {
        void attachView(View view);
        void requestImageSearch(String filePath);
        void detachView();
    }
}