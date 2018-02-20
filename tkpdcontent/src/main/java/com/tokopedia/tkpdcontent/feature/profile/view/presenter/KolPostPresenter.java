package com.tokopedia.tkpdcontent.feature.profile.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;

/**
 * @author by milhamj on 20/02/18.
 */

public class KolPostPresenter extends BaseDaggerPresenter<KolPostListener.View>
        implements KolPostListener.Presenter {

    @Override
    public void followKol(int id, int rowNumber, KolPostListener.View kolListener) {

    }

    @Override
    public void unfollowKol(int id, int rowNumber, KolPostListener.View kolListener) {

    }

    @Override
    public void likeKol(int id, int rowNumber, KolPostListener.View kolListener) {

    }

    @Override
    public void unlikeKol(int id, int rowNumber, KolPostListener.View kolListener) {

    }
}
