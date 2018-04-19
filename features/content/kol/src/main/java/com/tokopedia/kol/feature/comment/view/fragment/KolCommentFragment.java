package com.tokopedia.kol.feature.comment.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.R;
import com.tokopedia.kol.analytics.KolTracking;
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

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

/**
 * @author by nisie on 10/27/17.
 */

public class KolCommentFragment extends BaseDaggerFragment implements KolComment.View {

    public static final String ARGS_TOTAL_COMMENT = "ARGS_TOTAL_COMMENT";
    RecyclerView listComment;
    KolCommentAdapter adapter;
    //TODO milhamj change this progress dialog into another loading
    ProgressDialog progressDialog;

    EditText kolComment;
    ImageView sendButton;
    TextView productName;
    TextView productPrice;
    ImageView productAvatar;
    ImageView wishlist;

    boolean isFromApplink;
    KolCommentHeaderViewModel header;

    @Inject
    KolComment.Presenter presenter;

    @Inject
    UserSession userSession;

    @Inject
    KolCommentTypeFactory typeFactory;

    int totalNewComment = 0;

    public static KolCommentFragment createInstance(Bundle bundle) {
        KolCommentFragment fragment = new KolCommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return KolTracking.Screen.SCREEN_KOL_COMMENTS;
    }

    @Override
    protected void initInjector() {
        DaggerKolCommentComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(getActivity().getApplication()))
                .kolCommentModule(new KolCommentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        listComment = (RecyclerView) parentView.findViewById(R.id.comment_list);
        kolComment = (EditText) parentView.findViewById(R.id.new_comment);
        sendButton = (ImageView) parentView.findViewById(R.id.send_but);
        productName = (TextView) parentView.findViewById(R.id.product_name);
        productPrice = (TextView) parentView.findViewById(R.id.price);
        productAvatar = (ImageView) parentView.findViewById(R.id.avatar);
        wishlist = (ImageView) parentView.findViewById(R.id.wishlist);
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
        listComment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        listComment.setAdapter(adapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendComment(getArguments().getInt(
                        KolCommentActivity.ARGS_ID),
                        kolComment.getText().toString());
            }
        });
    }

    @Override
    public void onGoToProfile(String url) {
        //TODO milhamj open profile with url?
//        startActivity(KolProfileWebViewActivity.getCallingIntent(getActivity(), url));
    }

    @Override
    public void showLoading() {
        adapter.showLoading();
    }

    @Override
    public void onErrorGetCommentsFirstTime(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper
                .RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getCommentFirstTime(getArguments().getInt(KolCommentActivity.ARGS_ID));
            }
        });
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
        //TODO milhamj tracking
//        UnifyTracking.eventKolCommentDetailLoadMore();
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
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessSendComment(SendKolCommentDomain sendKolCommentDomain) {
        //TODO milhamj tracking
//        UnifyTracking.eventKolCommentDetailSubmitComment();
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
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }

        progressDialog.show();
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
