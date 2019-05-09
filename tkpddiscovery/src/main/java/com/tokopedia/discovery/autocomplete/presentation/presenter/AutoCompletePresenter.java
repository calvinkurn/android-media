package com.tokopedia.discovery.autocomplete.presentation.presenter;

import android.content.Context;

import com.tokopedia.discovery.autocomplete.presentation.AutoCompleteContract;
import com.tokopedia.discovery.autocomplete.presentation.activity.AutoCompleteActivity;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryPresenter;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;

public class AutoCompletePresenter extends DiscoveryPresenter<AutoCompleteContract.View, AutoCompleteActivity>
        implements AutoCompleteContract.Presenter {

    public AutoCompletePresenter(Context context, GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        super(context, getProductUseCase, getImageSearchUseCase);
    }
}