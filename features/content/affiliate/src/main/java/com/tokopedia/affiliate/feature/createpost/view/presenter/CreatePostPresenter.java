package com.tokopedia.affiliate.feature.createpost.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetContentFormUseCase;
import com.tokopedia.affiliate.feature.createpost.domain.usecase.SubmitPostUseCase;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;
import com.tokopedia.affiliate.feature.createpost.view.subscriber.GetContentFormSubscriber;
import com.tokopedia.affiliate.feature.createpost.view.subscriber.SubmitPostSubscriber;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 9/26/18.
 */
public class CreatePostPresenter extends BaseDaggerPresenter<CreatePostContract.View>
        implements CreatePostContract.Presenter {

    private final GetContentFormUseCase getContentFormUseCase;
    private final SubmitPostUseCase submitPostUseCase;

    @Inject
    public CreatePostPresenter(GetContentFormUseCase getContentFormUseCase,
                               SubmitPostUseCase submitPostUseCase) {
        this.getContentFormUseCase = getContentFormUseCase;
        this.submitPostUseCase = submitPostUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        getContentFormUseCase.unsubcribe();
    }

    @Override
    public void fetchContentForm(String productId, String adId) {
        getView().showLoading();
        getContentFormUseCase.execute(
                GetContentFormUseCase.createRequestParams(productId, adId),
                new GetContentFormSubscriber(getView())
        );
    }

    @Override
    public void submitPost(String productId, String adId, String token, List<String> imageList) {
        getView().showLoading();
        submitPostUseCase.execute(
                SubmitPostUseCase.createRequestParams(productId, adId, token, imageList),
                new SubmitPostSubscriber(getView())
        );
    }
}
