package com.tokopedia.tkpdcontent.feature.profile.view.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tkpdcontent.feature.profile.di.KolProfileComponent;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolViewModel;

import javax.inject.Inject;

/**
 * @author by milhamj on 19/02/18.
 */

public class KolPostFragment extends BaseDaggerFragment implements KolPostListener.View {
    @Inject
    KolPostListener.Presenter presenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(KolProfileComponent.class).inject(this);
    }

    //TODO milhamj do something with these actions
    @Override
    public void onGoToKolProfile(int page, int rowNumber, String url) {

    }

    @Override
    public void onOpenKolTooltip(int page, int rowNumber, String url) {

    }

    @Override
    public void onFollowKolClicked(int page, int rowNumber, int id) {
        presenter.followKol(id, rowNumber, this);
    }

    @Override
    public void onUnfollowKolClicked(int page, int rowNumber, int id) {
        presenter.unfollowKol(id, rowNumber, this);

    }

    @Override
    public void onLikeKolClicked(int page, int rowNumber, int id) {
        presenter.likeKol(id, rowNumber, this);
    }

    @Override
    public void onUnlikeKolClicked(int page, int rowNumber, int id) {
        presenter.unlikeKol(id, rowNumber, this);

    }

    @Override
    public void onGoToKolComment(int page, int rowNumber, KolViewModel kolViewModel) {

    }
}
