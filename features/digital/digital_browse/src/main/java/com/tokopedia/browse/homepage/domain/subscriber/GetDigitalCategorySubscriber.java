package com.tokopedia.browse.homepage.domain.subscriber;

import android.content.Context;

import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel;

import rx.Subscriber;

/**
 * @author by furqan on 07/09/18.
 */

public class GetDigitalCategorySubscriber extends Subscriber<DigitalBrowseServiceViewModel> {

    private DigitalCategoryActionListener digitalCategoryActionListener;
    private Context context;

    public GetDigitalCategorySubscriber(DigitalCategoryActionListener digitalCategoryActionListener, Context context) {
        this.digitalCategoryActionListener = digitalCategoryActionListener;
        this.context = context;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        digitalCategoryActionListener.onErrorGetDigitalCategory(throwable);
    }

    @Override
    public void onNext(DigitalBrowseServiceViewModel viewModel) {
        digitalCategoryActionListener.onSuccessGetDigitalCategory(viewModel);
    }

    public interface DigitalCategoryActionListener {

        void onErrorGetDigitalCategory(Throwable throwable);

        void onSuccessGetDigitalCategory(DigitalBrowseServiceViewModel digitalBrowseServiceViewModel);

    }
}
