package com.tokopedia.kol.feature.comment.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.feedcomponent.util.MentionTextHelper;
import com.tokopedia.feedcomponent.view.adapter.mention.MentionableUserAdapter;
import com.tokopedia.feedcomponent.view.custom.MentionEditText;
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel;
import com.tokopedia.kol.KolComponentInstance;
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
import com.tokopedia.unifycomponents.ProgressBarUnify;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 10/27/17.
 */

public class KolCommentFragment extends BaseDaggerFragment
        implements KolComment.View, KolComment.View.ViewHolder, MentionableUserAdapter.MentionAdapterListener {

    public static final String ARGS_TOTAL_COMMENT = "ARGS_TOTAL_COMMENT";
    public static final String ARGS_SERVER_ERROR_MSG = "ARGS_SERVER_ERROR_MSG";

    private RecyclerView listComment;
    private MentionEditText kolComment;
    private ImageView sendButton;
    private ImageView wishlist;

    private KolCommentAdapter adapter;
    private MentionableUserAdapter mentionAdapter;
    private ProgressBarUnify progressBar;

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

        presenter.getCommentFirstTime(getArguments().getInt(KolCommentActivity.ARGS_ID));
    }

    private void setHeader(KolCommentHeaderViewModel header) {
        adapter.addHeader(header);
    }

    private void prepareView() {
        adapter = new KolCommentAdapter(typeFactory);
        listComment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager
                .VERTICAL, false));
        listComment.setAdapter(adapter);
        sendButton.setOnClickListener(v -> {
            if (userSession != null && userSession.isLoggedIn()) {
                presenter.sendComment(
                        getArguments().getInt(KolCommentActivity.ARGS_ID),
                        kolComment.getRawText()
                );
            } else {
                RouteManager.route(getContext(), ApplinkConst.LOGIN);
            }

        });

        mentionAdapter = new MentionableUserAdapter(this);
        kolComment.setAdapter(mentionAdapter);
    }

    @Override
    public void openRedirectUrl(String url) {
        routeUrl(url);
    }

    @Override
    public void onGoToProfile(String url) {
        openRedirectUrl(url);
    }

    @Override
    public void onClickMentionedProfile(String id) {
        RouteManager.route(
                getContext(),
                ApplinkConst.PROFILE.replace(ApplinkConst.Profile.PARAM_USER_ID, id)
        );
    }

    @Override
    public void showLoading() {
        adapter.showLoading();
    }

    @Override
    public void onErrorGetCommentsFirstTime(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getContext(), getView(), errorMessage,
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
            NetworkErrorHelper.showEmptyState(getContext(), getView(), errorMessage,
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
    public void replyToUser(MentionableUserViewModel user) {
        if (!user.isShop()) {
            CharSequence userToMention = MentionTextHelper.createValidMentionText(user.toString());
            kolComment.append(userToMention);
        } else {
            StringBuilder mentionFormatBuilder = new StringBuilder();
            if (kolComment.getText().length() > 0 && kolComment.getText().charAt(kolComment.length() - 1) != ' ') mentionFormatBuilder.append(" ");
            mentionFormatBuilder
                    .append("@")
                    .append(user.getFullName())
                    .append(" ");
            kolComment.append(mentionFormatBuilder.toString());
        }
        kolComment.setSelection(kolComment.length());
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
                null,
                sendKolCommentDomain.getDomainUser().getPhoto(),
                sendKolCommentDomain.getDomainUser().getName(),
                sendKolCommentDomain.getComment(),
                sendKolCommentDomain.getTime(),
                sendKolCommentDomain.getDomainUser().isKol(),
                sendKolCommentDomain.canDeleteComment(),
                "",
                false
        ));

        kolComment.setText("");
        enableSendComment();
        KeyboardHandler.DropKeyboard(getContext(), kolComment);
        totalNewComment += 1;

        listComment.scrollToPosition(adapter.getItemCount() - 1);
        getActivity().setResult(Activity.RESULT_OK, getReturnIntent(totalNewComment));
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

    @Override
    public void shouldGetMentionableUser(@NotNull String keyword) {
        presenter.getMentionableUserByKeyword(keyword);
    }

    @Override
    public void showMentionUserSuggestionList(List<MentionableUserViewModel> userList) {
        mentionAdapter.setMentionableUser(userList);
    }

    private boolean isInfluencer() {
        return header != null
                && userSession != null
                && !TextUtils.isEmpty(header.getUserId())
                && userSession.getUserId().equals(header.getUserId());
    }

    private void showDeleteDialog(final String id, final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(R.string.prompt_delete_comment_kol);
        builder.setPositiveButton(com.tokopedia.kolcommon.R.string.kol_title_delete, (dialog, which) -> presenter.deleteComment(id, adapterPosition));
        builder.setNegativeButton(com.tokopedia.kolcommon.R.string.kol_title_cancel, (dialog, which) -> dialog.dismiss());

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

            getActivity().setResult(Activity.RESULT_OK, getReturnIntent(totalNewComment));
        }
    }

    private void setWishlist(boolean wishlisted) {
        if (wishlisted)
            ImageHandler.loadImageWithIdWithoutPlaceholder(wishlist, com.tokopedia.design.R.drawable.ic_wishlist_checked);
        else
            ImageHandler.loadImageWithIdWithoutPlaceholder(wishlist, com.tokopedia.design.R.drawable.ic_wishlist_unchecked);
    }

    private void routeUrl(String url) {
        if (RouteManager.isSupportApplink(getContext(), url)) {
            RouteManager.route(getContext(), url);
        } else {
            RouteManager.route(
                    getContext(),
                    String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
            );
        }
    }

    private Intent getReturnIntent(int totalNewComment) {
        Intent intent = new Intent();
        Bundle arguments = getArguments();
        if (arguments != null && arguments.size() > 0) intent.putExtras(arguments);
        intent.putExtra(ARGS_TOTAL_COMMENT, totalNewComment);
        return intent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
