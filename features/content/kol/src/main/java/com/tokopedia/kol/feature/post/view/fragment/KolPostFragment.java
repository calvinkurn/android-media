package com.tokopedia.kol.feature.post.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.Menus;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.KolRouter;
import com.tokopedia.kol.R;
import com.tokopedia.kol.common.util.PostMenuListener;
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity;
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment;
import com.tokopedia.kol.feature.post.di.DaggerKolProfileComponent;
import com.tokopedia.kol.feature.post.di.KolProfileModule;
import com.tokopedia.kol.feature.post.view.adapter.KolPostAdapter;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactoryImpl;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.EntryPointViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.kol.common.util.PostMenuUtilKt.createBottomMenu;

/**
 * @author by milhamj on 19/02/18.
 */

public class KolPostFragment extends BaseDaggerFragment implements
        KolPostListener.View,
        KolPostListener.View.ViewHolder,
        KolPostListener.View.Like {

    public static final String PARAM_IS_LIKED = "is_liked";
    public static final String PARAM_TOTAL_LIKES = "total_likes";
    public static final String PARAM_TOTAL_COMMENTS = "total_comments";

    public static final int IS_LIKE_TRUE = 1;
    public static final int IS_LIKE_FALSE = 0;

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_POST_ID = "post_id";
    private static final int KOL_COMMENT_CODE = 13;
    private static final int LOAD_MORE_THRESHOLD = 2;

    @Inject
    KolPostListener.Presenter presenter;

    @Inject
    UserSessionInterface userSession;

    protected KolPostAdapter adapter;
    protected KolPostTypeFactory typeFactory;
    protected boolean canLoadMore = true;

    private RecyclerView kolRecyclerView;
    private LinearLayoutManager layoutManager;
    private AbstractionRouter abstractionRouter;
    private KolRouter kolRouter;
    private String userId;
    private Intent resultIntent;

    public static KolPostFragment newInstance(String userId) {
        KolPostFragment fragment = new KolPostFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static KolPostFragment newInstanceFromFeed(String userId,
                                                      int postId,
                                                      Intent resultIntent,
                                                      Bundle bundle) {
        KolPostFragment fragment = new KolPostFragment();
        bundle.putString(PARAM_USER_ID, userId);
        bundle.putInt(PARAM_POST_ID, postId);
        fragment.setArguments(bundle);
        fragment.setResultIntent(resultIntent);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
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

        if (getActivity() != null
                && getActivity().getApplicationContext() != null
                && getActivity().getApplicationContext() instanceof
                KolRouter) {
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchDataFirstTime();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private void initVar() {
        userId = getArguments().getString(PARAM_USER_ID);
        typeFactory = new KolPostTypeFactoryImpl(this);
        adapter = new KolPostAdapter(typeFactory);
    }

    private void initView(View view) {
        kolRecyclerView = view.findViewById(R.id.kol_rv);

        if (kolRecyclerView.getItemAnimator() instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) kolRecyclerView.getItemAnimator())
                    .setSupportsChangeAnimations(false);
        }
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        kolRecyclerView.setLayoutManager(layoutManager);
        adapter.clearData();
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
                    fetchData();
                }
            }
        });
    }

    protected void fetchDataFirstTime() {
        presenter.initView(userId);
    }

    protected void fetchData() {
        presenter.getKolPost(userId);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null && getActivity().getApplication() != null) {
            DaggerKolProfileComponent.builder()
                    .kolComponent(KolComponentInstance.getKolComponent(getActivity()
                            .getApplication()))
                    .kolProfileModule(new KolProfileModule())
                    .build()
                    .inject(this);
        }
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
    public UserSessionInterface getUserSession() {
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
        adapter.showEmpty(true);
    }

    @Override
    public void onErrorGetProfileData(String message) {
        adapter.showErrorNetwork(message, this::fetchData);
    }

    @Override
    public void updateCursor(String lastCursor) {
        canLoadMore = !TextUtils.isEmpty(lastCursor);
        presenter.updateCursor(lastCursor);

        if (!canLoadMore
                && !adapter.isEmpty()
                && adapter.getList().get(0) instanceof KolPostViewModel) {
            KolPostViewModel kolPostViewModel = (KolPostViewModel) adapter.getList().get(0);
            adapter.showExplore(kolPostViewModel.getName());
        }
    }

    @Override
    public void onSuccessDeletePost(int rowNumber) {
        adapter.removeItem(rowNumber);
        if (isAdapterEmpty()) {
            adapter.clearData();
            fetchDataFirstTime();
        }

        ToasterNormal.make(getView(), getString(R.string.kol_post_deleted), BaseToaster.LENGTH_LONG)
                .setAction(R.string.title_ok, v -> {

                })
                .show();
    }

    @Override
    public void onErrorDeletePost(String message, int rowNumber, int id) {
        showError(message, v -> presenter.deletePost(rowNumber, id));
    }

    @Override
    public void onGoToKolProfile(int rowNumber, String userId, int postId) {
    }

    @Override
    public void onGoToKolProfileUsingApplink(int rowNumber, String applink) {
    }

    @Override
    public void onOpenKolTooltip(int rowNumber, String uniqueTrackingId, String url) {
        kolRouter.openRedirectUrl(getActivity(), url);
    }

    @Override
    public void trackContentClick(boolean hasMultipleContent, String activityId, String
            activityType, String position) {

    }

    @Override
    public void trackTooltipClick(boolean hasMultipleContent, String activityId, String
            activityType, String position) {

    }

    @Override
    public void onFollowKolClicked(int rowNumber, int id) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.followKol(id, rowNumber, this);
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onUnfollowKolClicked(int rowNumber, int id) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.unfollowKol(id, rowNumber, this);
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onLikeKolClicked(int rowNumber, int id, boolean hasMultipleContent,
                                 String activityType) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.likeKol(id, rowNumber, this);
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onUnlikeKolClicked(int rowNumber, int id, boolean hasMultipleContent,
                                   String activityType) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.unlikeKol(id, rowNumber, this);
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onGoToKolComment(int rowNumber, int id, boolean hasMultipleContent,
                                 String activityType) {
        Intent intent = KolCommentActivity.getCallingIntent(
                getContext(), id, rowNumber
        );
        startActivityForResult(intent, KOL_COMMENT_CODE);
    }

    @Override
    public void onEditClicked(boolean hasMultipleContent, String activityId,
                              String activityType) {

    }

    @Override
    public void onMenuClicked(int rowNumber, BaseKolViewModel element) {
        if (getContext() != null) {
            Menus menus = createBottomMenu(getContext(), element,
                    new PostMenuListener() {
                        @Override
                        public void onDeleteClicked() {
                            createDeleteDialog(rowNumber, element.getContentId()).show();
                        }

                        @Override
                        public void onReportClick() {

                        }

                        @Override
                        public void onEditClick() {

                        }
                    }
            );
            menus.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case KOL_COMMENT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    onSuccessAddDeleteKolComment(
                            data.getIntExtra(KolCommentActivity.ARGS_POSITION, -1),
                            data.getIntExtra(KolCommentFragment.ARGS_TOTAL_COMMENT, 0));
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
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_LIKE);

            if (getActivity() != null &&
                    getArguments() != null &&
                    getArguments().getInt(PARAM_POST_ID, -1) == kolPostViewModel.getContentId()) {

                if (resultIntent == null) {
                    resultIntent = new Intent();
                    resultIntent.putExtras(getArguments());
                }
                resultIntent.putExtra(
                        PARAM_IS_LIKED,
                        kolPostViewModel.isLiked() ? IS_LIKE_TRUE : IS_LIKE_FALSE);
                resultIntent.putExtra(PARAM_TOTAL_LIKES, kolPostViewModel.getTotalLike());
                getActivity().setResult(Activity.RESULT_OK, resultIntent);
            }
        }
    }

    @Override
    public void onLikeKolError(String message) {
        showError(message);
    }

    private boolean isAdapterEmpty() {
        return adapter.getItemCount() == 0
                || isFirstItemEntryPoint();
    }

    private boolean isFirstItemEntryPoint() {
        return adapter.getItemCount() == 1
                && adapter.getList().get(0) instanceof EntryPointViewModel;
    }

    private void showError(String message) {
        if (message == null) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        }
    }

    private void showError(String message, View.OnClickListener action) {
        ToasterError
                .make(getView(), message, ToasterError.LENGTH_LONG)
                .setAction(R.string.title_try_again, action)
                .show();
    }

    private void onSuccessAddDeleteKolComment(int rowNumber, int totalNewComment) {
        if (rowNumber != -1 &&
                adapter.getList().get(rowNumber) != null &&
                adapter.getList().get(rowNumber) instanceof KolPostViewModel) {
            KolPostViewModel kolPostViewModel =
                    ((KolPostViewModel) adapter.getList().get(rowNumber));
            kolPostViewModel.setTotalComment(
                    kolPostViewModel.getTotalComment() + totalNewComment);
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_COMMENT);

            if (getActivity() != null &&
                    getArguments() != null &&
                    getArguments().getInt(PARAM_POST_ID, -1) == kolPostViewModel.getContentId()) {

                if (resultIntent == null) {
                    resultIntent = new Intent();
                    resultIntent.putExtras(getArguments());
                }
                resultIntent.putExtra(PARAM_TOTAL_COMMENTS, kolPostViewModel.getTotalComment());
                getActivity().setResult(Activity.RESULT_OK, resultIntent);
            }
        }
    }

    public void setResultIntent(Intent resultIntent) {
        this.resultIntent = resultIntent;
    }

    private Dialog createDeleteDialog(int rowNumber, int id) {
        Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.kol_delete_post));
        dialog.setDesc(getString(R.string.kol_delete_post_desc));
        dialog.setBtnOk(getString(R.string.kol_title_delete));
        dialog.setBtnCancel(getString(R.string.kol_title_cancel));
        dialog.setOnOkClickListener(v -> {
            presenter.deletePost(rowNumber, id);
            dialog.dismiss();
        });
        dialog.setOnCancelClickListener(v -> dialog.dismiss());
        return dialog;
    }
}
