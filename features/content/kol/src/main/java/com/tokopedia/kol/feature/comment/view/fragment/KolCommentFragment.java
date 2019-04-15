package com.tokopedia.kol.feature.comment.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.KolRouter;
import com.tokopedia.kol.R;
import com.tokopedia.kol.analytics.KolEventTracking;
import com.tokopedia.kol.feature.comment.di.DaggerKolCommentComponent;
import com.tokopedia.kol.feature.comment.di.KolCommentModule;
import com.tokopedia.kol.feature.comment.domain.model.SendKolCommentDomain;
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity;
import com.tokopedia.kol.feature.comment.view.adapter.KolCommentAdapter;
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

/**
 * @author by nisie on 10/27/17.
 */

public class KolCommentFragment extends BaseDaggerFragment
        implements KolComment.View, KolComment.View.ViewHolder {

    public static final String ARGS_TOTAL_COMMENT = "ARGS_TOTAL_COMMENT";
    public static final String ARGS_SERVER_ERROR_MSG = "ARGS_SERVER_ERROR_MSG";

    private RecyclerView listComment;
    private EditText kolComment;
    private ImageView sendButton;
    private ImageView wishlist;

    private KolCommentAdapter adapter;
    private ProgressBar progressBar;
    private KolRouter kolRouter;

    private boolean isFromApplink;
    private int totalNewComment = 0;
    private KolCommentHeaderViewModel header;

    @Inject
    KolComment.Presenter presenter;

    @Inject
    KolCommentTypeFactory typeFactory;

    private UserSessionInterface userSession;

    public static KolCommentFragment createInstance(Bundle bundle) {
        KolCommentFragment fragment = new KolCommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return KolEventTracking.Screen.SCREEN_KOL_COMMENTS;
    }

    @Override
    protected void initInjector() {
        DaggerKolCommentComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(getActivity().getApplication()))
                .kolCommentModule(new KolCommentModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getActivity());
        if (getArguments() != null) {
            if (getArguments().get(KolCommentActivity.ARGS_FROM_APPLINK) != null
                    && getArguments().getBoolean(KolCommentActivity.ARGS_FROM_APPLINK)) {
                isFromApplink = true;
            }
            totalNewComment = 0;
        } else if (savedInstanceState != null) {
            header = savedInstanceState.getParcelable(KolCommentActivity.ARGS_HEADER);
            totalNewComment = savedInstanceState.getInt(ARGS_TOTAL_COMMENT);
            isFromApplink = savedInstanceState.getBoolean(KolCommentActivity.ARGS_FROM_APPLINK);
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KolCommentActivity.ARGS_HEADER, header);
        outState.putBoolean(KolCommentActivity.ARGS_FROM_APPLINK, isFromApplink);
        outState.putInt(ARGS_TOTAL_COMMENT, totalNewComment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_kol_comment, container, false);
        listComment = parentView.findViewById(R.id.comment_list);
        kolComment = parentView.findViewById(R.id.new_comment);
        sendButton = parentView.findViewById(R.id.send_but);
        wishlist = parentView.findViewById(R.id.wishlist);
        progressBar = parentView.findViewById(R.id.progress_bar);
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity().getApplicationContext() instanceof KolRouter) {
            kolRouter = (KolRouter) getActivity().getApplicationContext();
        } else {
            throw new IllegalStateException("Application must be an instance of " +
                    KolRouter.class.getSimpleName());
        }

        presenter.getCommentFirstTime(getArguments().getInt(KolCommentActivity.ARGS_ID));
    }

    private void setHeader(KolCommentHeaderViewModel header) {
        adapter.addHeader(header);
    }

    private void prepareView() {
        adapter = new KolCommentAdapter(typeFactory);
        listComment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        listComment.setAdapter(adapter);
        sendButton.setOnClickListener(v -> {
            if (userSession != null && userSession.isLoggedIn()) {
                presenter.sendComment(
                        getArguments().getInt(KolCommentActivity.ARGS_ID),
                        kolComment.getText().toString()
                );
            } else {
                startActivity(kolRouter.getLoginIntent(getActivity()));
            }

        });


    }

    @Override
    public void openRedirectUrl(String url) {
        kolRouter.openRedirectUrl(getActivity(), url);
    }

    @Override
    public void onGoToProfile(String url) {
        openRedirectUrl(url);
    }

    @Override
    public void showLoading() {
        adapter.showLoading();
    }

    @Override
    public void onErrorGetCommentsFirstTime(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage,
                () -> presenter.getCommentFirstTime(
                        getArguments().getInt(KolCommentActivity.ARGS_ID))
        );
    }

    @Override
    public void onServerErrorGetCommentsFirstTime(String errorMessage) {
        if (getActivity() != null
                && getActivity().getIntent().getExtras() != null
                && getActivity().getIntent().getExtras().getBoolean(
                KolCommentActivity.ARGS_FROM_FEED, false)) {
            Intent intent = new Intent();
            intent.putExtra(ARGS_SERVER_ERROR_MSG, errorMessage);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage,
                    () -> presenter.getCommentFirstTime(
                            getArguments().getInt(KolCommentActivity.ARGS_ID))
            );
        }
    }

    @Override
    public void onSuccessGetCommentsFirstTime(KolComments kolComments) {
        header = kolComments.getHeaderViewModel();
        setHeader(header);

        ArrayList<Visitable> list = new ArrayList<>();
        list.addAll(kolComments.getListComments());
        Collections.reverse(list);
        adapter.setList(list);

        if (adapter.getHeader() != null)
            adapter.getHeader().setCanLoadMore(kolComments.isHasNextPage());

        adapter.notifyDataSetChanged();

        listComment.getLayoutManager().scrollToPosition(adapter.getItemCount() - 1);
    }


    @Override
    public void onSuccessGetComments(KolComments kolComments) {

        ArrayList<Visitable> list = new ArrayList<>();
        list.addAll(kolComments.getListComments());
        Collections.reverse(list);

        adapter.addList(list);

        if (adapter.getHeader() != null) {
            adapter.getHeader().setCanLoadMore(kolComments.isHasNextPage());
            adapter.getHeader().setLoading(false);
            adapter.notifyItemChanged(0);
        }
    }

    @Override
    public void removeLoading() {
        adapter.removeLoading();
    }

    @Override
    public void loadMoreComments() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                KolEventTracking.Category.FEED_CONTENT_COMMENT_DETAIL,
                KolEventTracking.Action.FEED_LOAD_MORE_COMMENTS,
                KolEventTracking.EventLabel.FEED_CONTENT_COMMENT_DETAIL_LOAD_MORE
        ));
        if (adapter.getHeader() != null) {
            adapter.getHeader().setLoading(true);
            adapter.notifyItemChanged(0);
        }
        presenter.loadMoreComments(getArguments().getInt(KolCommentActivity.ARGS_ID));
    }

    @Override
    public void onSuccessChangeWishlist() {
        setWishlist(true);
    }

    @Override
    public void updateCursor(String lastcursor) {
        presenter.updateCursor(lastcursor);
    }

    @Override
    public void onErrorLoadMoreComment(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);

        if (adapter.getHeader() != null) {
            adapter.getHeader().setLoading(false);
            adapter.getHeader().setCanLoadMore(true);
            adapter.notifyItemChanged(0);
        }
    }

    @Override
    public void onErrorSendComment(String errorMessage) {
        enableSendComment();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessSendComment(SendKolCommentDomain sendKolCommentDomain) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                KolEventTracking.Category.FEED_CONTENT_COMMENT_DETAIL,
                KolEventTracking.Action.FEED_SUBMIT_COMMENT,
                KolEventTracking.EventLabel.FEED_CONTENT_COMMENT_DETAIL_COMMENT
        ));
        adapter.addItem(new KolCommentViewModel(
                sendKolCommentDomain.getId(),
                String.valueOf(sendKolCommentDomain.getDomainUser().getId()),
                sendKolCommentDomain.getDomainUser().getPhoto(),
                sendKolCommentDomain.getDomainUser().getName(),
                sendKolCommentDomain.getComment(),
                sendKolCommentDomain.getTime(),
                sendKolCommentDomain.getDomainUser().isKol(),
                sendKolCommentDomain.canDeleteComment()
        ));

        kolComment.setText("");
        enableSendComment();
        KeyboardHandler.DropKeyboard(getActivity(), kolComment);
        totalNewComment += 1;

        listComment.scrollToPosition(adapter.getItemCount() - 1);
        Intent intent = new Intent();
        intent.putExtras(getActivity().getIntent().getExtras());
        intent.putExtra(ARGS_TOTAL_COMMENT, totalNewComment);
        getActivity().setResult(Activity.RESULT_OK, intent);

    }

    @Override
    public void dismissProgressDialog() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onDeleteCommentKol(String id, boolean canDeleteComment, int
            adapterPosition) {
        if (canDeleteComment || isInfluencer()) {
            showDeleteDialog(id, adapterPosition);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void enableSendComment() {
        sendButton.setClickable(true);
    }

    @Override
    public void disableSendComment() {
        sendButton.setClickable(false);
    }

    private boolean isInfluencer() {
        return header != null
                && userSession != null
                && !TextUtils.isEmpty(header.getUserId())
                && userSession.getUserId().equals(header.getUserId());
    }

    private void showDeleteDialog(final String id, final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.prompt_delete_comment_kol);
        builder.setPositiveButton(R.string.kol_title_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.deleteComment(id, adapterPosition);
            }
        });
        builder.setNegativeButton(R.string.kol_title_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onErrorDeleteComment(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessDeleteComment(int adapterPosition) {
        if (adapterPosition < adapter.getItemCount()) {
            adapter.deleteItem(adapterPosition);
            NetworkErrorHelper.showSnackbar(
                    getActivity(),
                    getString(R.string.success_delete_kol_comment));

            totalNewComment -= 1;
            Intent intent = new Intent();
            intent.putExtras(getActivity().getIntent().getExtras());
            intent.putExtra(ARGS_TOTAL_COMMENT, totalNewComment);
            getActivity().setResult(Activity.RESULT_OK, intent);
        }
    }

    private void setWishlist(boolean wishlisted) {
        if (wishlisted)
            ImageHandler.loadImageWithIdWithoutPlaceholder(wishlist, R.drawable.ic_wishlist_checked);
        else
            ImageHandler.loadImageWithIdWithoutPlaceholder(wishlist, R.drawable.ic_wishlist_unchecked);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
