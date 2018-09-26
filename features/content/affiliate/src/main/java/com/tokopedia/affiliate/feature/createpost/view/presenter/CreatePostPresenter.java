package com.tokopedia.affiliate.feature.createpost.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetContentFormUseCase;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;

import javax.inject.Inject;

/**
 * @author by milhamj on 9/26/18.
 */
public class CreatePostPresenter extends BaseDaggerPresenter<CreatePostContract.View>
        implements CreatePostContract.Presenter {

    private GetContentFormUseCase getContentFormUseCase;

    @Inject
    CreatePostPresenter(GetContentFormUseCase getContentFormUseCase) {
        this.getContentFormUseCase = getContentFormUseCase;
    }


    @Override
    public void fetchContentForm(String productId, String adId) {
        getContentFormUseCase.execute(
                GetContentFormUseCase.createRequestParams(productId, adId),
                null
        );
    }
}
