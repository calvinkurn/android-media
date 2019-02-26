package com.tokopedia.affiliate.feature.createpost.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.createpost.domain.usecase.EditPostUseCase;
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetContentFormUseCase;
import com.tokopedia.affiliate.feature.createpost.domain.usecase.SubmitPostUseCase;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;
import com.tokopedia.affiliate.feature.createpost.view.subscriber.EditPostSubscriber;
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
    private final EditPostUseCase editPostUseCase;

    @Inject
    CreatePostPresenter(GetContentFormUseCase getContentFormUseCase,
                        SubmitPostUseCase submitPostUseCase,
                        EditPostUseCase editPostUseCase) {
        this.getContentFormUseCase = getContentFormUseCase;
        this.submitPostUseCase = submitPostUseCase;
        this.editPostUseCase = editPostUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        getContentFormUseCase.unsubcribe();
        submitPostUseCase.unsubscribe();
        editPostUseCase.unsubscribe();
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
    public void fetchEditContentForm(String postId) {
        getView().showLoading();
        getContentFormUseCase.execute(
                GetContentFormUseCase.createEditRequestParams(postId),
                new GetContentFormSubscriber(getView(), true)
        );
    }

    @Override
    public void submitPost(String productId, String adId, String token, List<String> imageList,
                           int mainImageIndex) {
        getView().showLoading();
        submitPostUseCase.execute(
                SubmitPostUseCase.Companion.createRequestParams(
                        productId,
                        adId,
                        token,
                        imageList,
                        mainImageIndex
                ),
                new SubmitPostSubscriber(getView())
        );
    }

    @Override
    public void editPost(String postId, String token, List<String> imageList, int mainImageIndex) {
        getView().showLoading();
        editPostUseCase.execute(
                EditPostUseCase.Companion.createRequestParams(
                        postId,
                        token,
                        imageList,
                        mainImageIndex
                ),
                new EditPostSubscriber(getView())
        );
    }
}
