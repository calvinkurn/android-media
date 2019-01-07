package com.tokopedia.kol.feature.post.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.R;
import com.tokopedia.kol.analytics.KolEventTracking;
import com.tokopedia.kol.feature.createpost.view.activity.CreatePostImagePickerActivity;
import com.tokopedia.kol.feature.post.di.DaggerKolProfileComponent;
import com.tokopedia.kol.feature.post.di.KolProfileModule;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.listener.KolPostShopContract;
import com.tokopedia.kol.feature.post.view.viewmodel.EntryPointViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 23/08/18.
 */

public class KolPostShopFragment extends KolPostFragment implements KolPostShopContract.View,
        KolPostShopContract.View.Like {

    private static final int CREATE_POST = 888;
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_CREATE_POST_URL = "create_post_url";

    private EmptyResultViewModel emptyResultViewModel;
    private String shopId;
    private String createPostUrl;

    public static KolPostShopFragment newInstance(String shopId) {
        return newInstance(shopId, "");
    }

    public static KolPostShopFragment newInstance(String shopId, String createPostUrl) {
        KolPostShopFragment fragment = new KolPostShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_SHOP_ID, shopId);
        bundle.putString(PARAM_CREATE_POST_URL, createPostUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    KolPostShopContract.Presenter presenter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initVar();
        presenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected String getScreenName() {
        return KolEventTracking.Screen.SCREEN_SHOP_PAGE_FEED;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null && getActivity().getApplication() != null) {
            DaggerKolProfileComponent.builder()
                    .kolComponent(KolComponentInstance.getKolComponent(getActivity().getApplication()))
                    .kolProfileModule(new KolProfileModule())
                    .build()
                    .inject(this);
        }
    }

    private void initVar() {
        if (getArguments() != null) {
            shopId = getArguments().getString(PARAM_SHOP_ID);
            createPostUrl = getArguments().getString(PARAM_CREATE_POST_URL, "");
        }
        typeFactory.setType(KolPostViewHolder.Type.SHOP_PAGE);

    }

    @Override
    protected void fetchDataFirstTime() {
        presenter.initView(shopId);
    }

    @Override
    protected void fetchData() {
        presenter.getKolPostShop(shopId);
    }

    @Override
    public void onSuccessGetKolPostShop(List<Visitable> visitables, String lastCursor) {
        canLoadMore = !TextUtils.isEmpty(lastCursor);
        presenter.updateCursor(lastCursor);

        if (adapter.getList().isEmpty() && visitables.isEmpty()) {
            if (TextUtils.equals(getUserSession().getShopId(), shopId)) {
                adapter.showEmptyOwnShop(getEmptyResultViewModel());
            } else {
                adapter.showEmpty();
            }
        } else if (adapter.getList().isEmpty()
                && TextUtils.equals(userSession.getShopId(), shopId)) {
            adapter.addItem(new EntryPointViewModel(v -> goToCreatePost()));
            adapter.addList(visitables);
        } else {
            adapter.addList(visitables);
        }
    }

    @Override
    public void onErrorGetKolPostShop(String message) {
        adapter.showErrorNetwork(message, this::fetchData);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_POST) {
            adapter.clearData();
            fetchDataFirstTime();
        }
    }

    private void goToCreatePost() {
        if (getActivity() != null && !TextUtils.isEmpty(createPostUrl)) {
            startActivityForResult(
                    CreatePostImagePickerActivity.getInstance(
                            getActivity(),
                            createPostUrl),
                    CREATE_POST
            );
        }
    }

    private EmptyResultViewModel getEmptyResultViewModel() {
        if (emptyResultViewModel == null) {
            emptyResultViewModel = new EmptyResultViewModel();
            emptyResultViewModel.setIconRes(R.drawable.ic_empty_shop_feed);
            emptyResultViewModel.setTitle(getString(R.string.empty_own_feed_title));
            emptyResultViewModel.setContent(getString(R.string.empty_own_feed_subtitle));

            if (!TextUtils.isEmpty(createPostUrl)) {
                emptyResultViewModel.setButtonTitle(getString(R.string.empty_own_feed_button));
                emptyResultViewModel.setCallback(new BaseEmptyViewHolder.Callback() {
                    @Override
                    public void onEmptyContentItemTextClicked() {

                    }

                    @Override
                    public void onEmptyButtonClicked() {
                        goToCreatePost();
                    }
                });
            }
        }
        return emptyResultViewModel;
    }
}
