package com.tokopedia.tkpdcontent.feature.profile.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdcontent.KolComponentInstance;
import com.tokopedia.tkpdcontent.R;
import com.tokopedia.tkpdcontent.feature.profile.di.DaggerKolProfileComponent;
import com.tokopedia.tkpdcontent.feature.profile.di.KolProfileComponent;
import com.tokopedia.tkpdcontent.feature.profile.view.adapter.KolPostAdapter;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 19/02/18.
 */

public class KolPostFragment extends BaseDaggerFragment implements KolPostListener.View {
    private static final String PARAM_USER_ID = "user_id";

    @Inject
    KolPostListener.Presenter presenter;
    @Inject
    KolPostAdapter adapter;
    private RecyclerView kolRecyclerView;
    private LinearLayoutManager layoutManager;

    private String userId;

    public static KolPostFragment newInstance(String userId) {
        KolPostFragment fragment = new KolPostFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(
                R.layout.fragment_kol_post,
                container,
                false);
        presenter.attachView(this);
        initVar();
        initView(parentView);
        setViewListener();
        presenter.initView(userId);

        return parentView;
    }

    private void initVar() {
        userId = getArguments().getString(PARAM_USER_ID);
    }

    private void initView(View view) {
        kolRecyclerView = view.findViewById(R.id.kol_rv);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        kolRecyclerView.setLayoutManager(layoutManager);
        kolRecyclerView.setAdapter(adapter);
    }

    private void setViewListener() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerKolProfileComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(getActivity().getApplication()))
                .build()
                .inject(this);
    }

    @Override
    public void onSuccessGetProfileData(List<Visitable> visitableList) {
        NetworkErrorHelper.showSnackbar(getActivity(), "Success");
        adapter.addList(visitableList);
    }

    @Override
    public void onErrorGetProfileData(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
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
