package com.tokopedia.tkpdcontent.feature.profile.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdcontent.KolComponentInstance;
import com.tokopedia.tkpdcontent.KolRouter;
import com.tokopedia.tkpdcontent.R;
import com.tokopedia.tkpdcontent.feature.profile.di.DaggerKolProfileComponent;
import com.tokopedia.tkpdcontent.feature.profile.di.KolProfileModule;
import com.tokopedia.tkpdcontent.feature.profile.view.adapter.KolPostAdapter;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolPostViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 19/02/18.
 */

public class KolPostFragment extends BaseDaggerFragment implements KolPostListener.View {
    private static final String PARAM_USER_ID = "user_id";
    private static final int KOL_COMMENT_CODE = 13;
    private static final int LOAD_MORE_THRESHOLD = 2;

    @Inject
    KolPostListener.Presenter presenter;
    @Inject
    KolPostAdapter adapter;
    @Inject
    UserSession userSession;
    private RecyclerView kolRecyclerView;
    private LinearLayoutManager layoutManager;

    private AbstractionRouter abstractionRouter;
    private KolRouter kolRouter;
    private String userId;
    private boolean canLoadMore = true;

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

        if (getActivity().getApplicationContext() instanceof KolRouter) {
            kolRouter = (KolRouter) getActivity().getApplicationContext();
        } else {
            throw new IllegalStateException("Application must be an instance of KolRouter!");
        }

        if (getActivity().getApplicationContext() instanceof AbstractionRouter) {
            abstractionRouter = (AbstractionRouter) getActivity().getApplicationContext();
        } else {
            throw new IllegalStateException("Application must be an instance of " +
                    "AbstractionRouter!");
        }

        return parentView;
    }

    private void initVar() {
        userId = getArguments().getString(PARAM_USER_ID);
    }

    private void initView(View view) {
        kolRecyclerView = view.findViewById(R.id.kol_rv);

        if (kolRecyclerView.getItemAnimator() instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) kolRecyclerView.getItemAnimator())
                    .setSupportsChangeAnimations(false);
        }
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        kolRecyclerView.setLayoutManager(layoutManager);
        kolRecyclerView.setAdapter(adapter);
    }

    private void setViewListener() {
        kolRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int topVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (topVisibleItemPosition >= adapter.getItemCount() - LOAD_MORE_THRESHOLD &&
                        canLoadMore &&
                        !adapter.isLoading()) {
                    presenter.getKolPost(userId);
                }
            }
        });
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerKolProfileComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(getActivity().getApplication()))
                .kolProfileModule(new KolProfileModule(this))
                .build()
                .inject(this);
    }

    @Override
    public KolRouter getKolRouter() {
        return kolRouter;
    }

    @Override
    public AbstractionRouter getAbstractionRouter() {
        return abstractionRouter;
    }

    @Override
    public UserSession getUserSession() {
        return userSession;
    }

    @Override
    public void showLoading() {
        adapter.removeErrorNetwork();
        adapter.removeEmpty();
        adapter.showLoading();
    }

    @Override
    public void hideLoading() {
        adapter.removeLoading();
    }

    @Override
    public void onSuccessGetProfileData(List<Visitable> visitableList) {
        adapter.addList(visitableList);
    }

    @Override
    public void onEmptyKolPost() {
        adapter.removeErrorNetwork();
        adapter.removeLoading();
        adapter.showEmpty();
    }

    @Override
    public void onErrorGetProfileData(String message) {
        adapter.showErrorNetwork(message, new ErrorNetworkModel.OnRetryListener() {
            @Override
            public void onRetryClicked() {
                presenter.getKolPost(userId);
            }
        });
    }

    @Override
    public void updateCursor(String lastCursor) {
        canLoadMore = !TextUtils.isEmpty(lastCursor);
        presenter.updateCursor(lastCursor);
    }

    @Override
    public void onGoToKolProfile(int page, int rowNumber, String url) {

    }

    @Override
    public void onOpenKolTooltip(int page, int rowNumber, String url) {
        ((KolRouter) getActivity().getApplication()).actionApplinkFromActivity(getActivity(), url);
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
    public void onGoToKolComment(int page, int rowNumber, KolPostViewModel kolPostViewModel) {
        Intent intent = kolRouter.getKolCommentActivity(
                getContext(), kolPostViewModel.getAvatar(), kolPostViewModel.getName(),
                kolPostViewModel.getReview(), kolPostViewModel.getTime(),
                String.valueOf(kolPostViewModel.getUserId()), kolPostViewModel.getKolImage(),
                kolPostViewModel.getContentName(), kolPostViewModel.getProductPrice(),
                kolPostViewModel.isWishlisted(), kolPostViewModel.getId(), rowNumber
        );
        startActivityForResult(intent, KOL_COMMENT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case KOL_COMMENT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    onSuccessAddDeleteKolComment(
                            data.getIntExtra(kolRouter.getKolCommentArgsPosition(), -1),
                            data.getIntExtra(kolRouter.getKolCommentArgsTotalComment(), 0));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLikeKolSuccess(int rowNumber) {
        if (adapter.getList().get(rowNumber) != null
                && adapter.getList().get(rowNumber) instanceof KolPostViewModel) {
            KolPostViewModel kolPostViewModel =
                    ((KolPostViewModel) adapter.getList().get(rowNumber));
            kolPostViewModel.setLiked(!kolPostViewModel.isLiked());
            if (kolPostViewModel.isLiked()) {
                kolPostViewModel.setTotalLike(kolPostViewModel.getTotalLike() + 1);
            } else {
                kolPostViewModel.setTotalLike(kolPostViewModel.getTotalLike() - 1);
            }
            adapter.notifyItemChanged(rowNumber);
        }
    }

    @Override
    public void onLikeKolError(String message) {
        showError(message);
    }

    private void showError(String message) {
        if (message == null) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        }
    }

    private void onSuccessAddDeleteKolComment(int rowNumber, int totalNewComment) {
        if (rowNumber != -1) {
            if (adapter.getList().get(rowNumber) != null
                    && adapter.getList().get(rowNumber) instanceof KolPostViewModel) {
                KolPostViewModel kolPostViewModel =
                        ((KolPostViewModel) adapter.getList().get(rowNumber));
                kolPostViewModel.setTotalComment(
                        kolPostViewModel.getTotalComment() + totalNewComment);
                adapter.notifyItemChanged(rowNumber);
            }
        }
    }
}
