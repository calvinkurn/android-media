package com.tokopedia.groupchat.channel.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.groupchat.channel.domain.usecase.GetChannelListUseCase;
import com.tokopedia.groupchat.channel.view.listener.ChannelContract;
import com.tokopedia.groupchat.channel.view.subscriber.GetChannelFirstTimeSubscriber;
import com.tokopedia.groupchat.channel.view.subscriber.GetChannelSubscriber;
import com.tokopedia.groupchat.channel.view.subscriber.RefreshChannelSubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 2/3/18.
 */

public class ChannelPresenter extends BaseDaggerPresenter<ChannelContract.View> implements
        ChannelContract.Presenter {

    private GetChannelListUseCase getChannelListUseCase;

    @Inject
    public ChannelPresenter(GetChannelListUseCase getChannelListUseCase) {
        this.getChannelListUseCase = getChannelListUseCase;
    }


    @Override
    public void detachView() {
        super.detachView();
        getChannelListUseCase.unsubscribe();
    }

    @Override
    public void getChannelListFirstTime() {
        getView().showLoadingFull();
        getChannelListUseCase.execute(getChannelListUseCase.createParamFirstTime(),
                new GetChannelFirstTimeSubscriber(getView()));
    }

    @Override
    public void getChannelList(String lastCursor) {
        getChannelListUseCase.execute(getChannelListUseCase.createParam(lastCursor),
                new GetChannelSubscriber(getView()));
    }

    @Override
    public void refreshData() {
        getChannelListUseCase.execute(getChannelListUseCase.createParamFirstTime(),
                new RefreshChannelSubscriber(getView()));
    }
}
