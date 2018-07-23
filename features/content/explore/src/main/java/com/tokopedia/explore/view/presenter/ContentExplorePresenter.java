package com.tokopedia.explore.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.explore.domain.interactor.GetExploreDataUseCase;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.subscriber.GetExploreDataSubscriber;

import javax.inject.Inject;

/**
 * @author by milhamj on 23/07/18.
 */

public class ContentExplorePresenter
        extends BaseDaggerPresenter<ContentExploreContract.View>
        implements ContentExploreContract.Presenter {

    private final GetExploreDataUseCase getExploreDataUseCase;
    private String cursor = "";
    private int categoryId = 0;

    @Inject
    public ContentExplorePresenter(GetExploreDataUseCase getExploreDataUseCase) {
        this.getExploreDataUseCase = getExploreDataUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        getExploreDataUseCase.unsubscribe();
    }

    @Override
    public void getExploreData() {
        //TODO milhamj do search?
        getExploreDataUseCase.execute(
                GetExploreDataUseCase.getVariables(categoryId, cursor, ""),
                new GetExploreDataSubscriber(getView())
        );
    }

    @Override
    public void updateCursor(String cursor) {
        this.cursor = cursor;
    }

    @Override
    public void updateCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
