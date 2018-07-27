package com.tokopedia.kol.feature.postdetail.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.KolRouter;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity;
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.post.di.DaggerKolProfileComponent;
import com.tokopedia.kol.feature.post.di.KolProfileModule;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactoryImpl;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.postdetail.view.activity.KolPostDetailActivity;
import com.tokopedia.kol.feature.postdetail.view.adapter.KolPostDetailAdapter;
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactoryImpl;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.IS_LIKE_TRUE;
import static com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.PARAM_IS_LIKED;
import static com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.PARAM_TOTAL_COMMENTS;
import static com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.PARAM_TOTAL_LIKES;

/**
 * @author by yfsx on 23/07/18.
 */

public class KolPostDetailFragment extends BaseDaggerFragment
        implements KolPostDetailContract.View, KolPostListener.View.Like,
        KolPostListener.View.ViewHolder, KolComment.View.ViewHolder, KolComment.View.SeeAll {

    private static final String EXTRA_IS_FOLLOWING = "is_following";
    private static final int IS_FOLLOWING_TRUE = 1;
    private static final int IS_FOLLOWING_FALSE = 0;
    private static final int OPEN_KOL_COMMENT = 101;
    private static final int OPEN_KOL_PROFILE = 13;
    private static final int DEFAULT_VALUE = -1;

    private Integer postId;
    private RecyclerView recyclerView;
    private ImageView userAvatar;
    private EditText replyEditText;
    private AbstractionRouter abstractionRouter;
    private KolRouter kolRouter;

    @Inject
    KolPostDetailContract.Presenter presenter;
    @Inject
    UserSession userSession;
    @Inject
    KolPostDetailAdapter adapter;

    public static KolPostDetailFragment getInstance(Bundle bundle) {
        KolPostDetailFragment fragment = new KolPostDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
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
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kol_post_detail, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        userAvatar = view.findViewById(R.id.user_avatar);
        replyEditText = view.findViewById(R.id.reply_edit_text);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity().getApplicationContext() instanceof AbstractionRouter) {
            abstractionRouter = (AbstractionRouter) getActivity().getApplication();
        } else {
            throw new IllegalStateException("Application must be an instance of "
                    + AbstractionRouter.class.getSimpleName());
        }

        if (getActivity().getApplicationContext() instanceof KolRouter) {
            kolRouter = (KolRouter) getActivity().getApplicationContext();
        } else {
            throw new IllegalStateException("Application must be an instance of "
                    + KolRouter.class.getSimpleName());
        }

        GraphqlClient.init(getContext());
        initVar();

        KolPostTypeFactoryImpl typeFactory = new KolPostTypeFactoryImpl(this);
        typeFactory.setType(KolPostViewHolder.Type.EXPLORE);
        adapter.setTypeFactory(new KolPostDetailTypeFactoryImpl(this, this, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        replyEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    return true;
                }
                return false;
            }
        });

        presenter.attachView(this);
        presenter.getCommentFirstTime(postId);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private void initVar() {
        if (getArguments() != null) {
            postId = getArguments().getInt(KolPostDetailActivity.PARAM_POST_ID);
        }
    }

    @Override
    public void onSuccessGetKolPostDetail(List<Visitable> list) {
        adapter.setList(list);
    }

    @Override
    public void onErrorGetKolPotDetail(String message) {
        NetworkErrorHelper.showEmptyState(
                getContext(),
                getView(),
                message,
                () -> presenter.getCommentFirstTime(postId)
        );
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

    @Override
    public UserSession getUserSession() {
        return userSession;
    }

    @Override
    public AbstractionRouter getAbstractionRouter() {
        return abstractionRouter;
    }

    @Override
    public void onGoToKolProfile(int rowNumber, String userId, int postId) {
        Intent intent = kolRouter.getTopProfileIntent(getContext(), userId);
        startActivityForResult(intent, OPEN_KOL_PROFILE);
    }

    @Override
    public void onGoToKolProfileUsingApplink(int rowNumber, String applink) {
        kolRouter.openRedirectUrl(getActivity(), applink);
    }

    @Override
    public void onOpenKolTooltip(int rowNumber, String url) {
        kolRouter.openRedirectUrl(getActivity(), url);
    }

    @Override
    public void onFollowKolClicked(int rowNumber, int id) {

    }

    @Override
    public void onUnfollowKolClicked(int rowNumber, int id) {

    }

    @Override
    public void onLikeKolClicked(int rowNumber, int id) {
        presenter.likeKol(id, rowNumber, this);
    }

    @Override
    public void onUnlikeKolClicked(int adapterPosition, int id) {
        presenter.unlikeKol(id, adapterPosition, this);
    }

    @Override
    public void onGoToKolComment(int rowNumber, int id) {
        Intent intent = KolCommentActivity.getCallingIntent(getContext(), id, rowNumber);
        startActivityForResult(intent, OPEN_KOL_COMMENT);
    }

    @Override
    public void onGoToProfile(String url) {
        kolRouter.openRedirectUrl(getActivity(), url);
    }

    @Override
    public boolean onDeleteCommentKol(String id, boolean canDeleteComment, int adapterPosition) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        switch (requestCode) {
            case OPEN_KOL_COMMENT:
                if (resultCode == Activity.RESULT_OK) {
                    onSuccessAddDeleteKolComment(
                            data.getIntExtra(KolCommentActivity.ARGS_POSITION, 0),
                            data.getIntExtra(KolCommentFragment.ARGS_TOTAL_COMMENT, 0)
                    );
                }
                break;
            case OPEN_KOL_PROFILE:
                if (resultCode == Activity.RESULT_OK) {
                    onSuccessFollowUnfollowFromProfile(
                            data.getIntExtra(EXTRA_IS_FOLLOWING, DEFAULT_VALUE)
                    );

                    updatePostState(
                            data.getIntExtra(PARAM_IS_LIKED, DEFAULT_VALUE),
                            data.getIntExtra(PARAM_TOTAL_LIKES, DEFAULT_VALUE),
                            data.getIntExtra(PARAM_TOTAL_COMMENTS, DEFAULT_VALUE)
                    );
                }
            default:
                break;
        }
    }

    private void onSuccessAddDeleteKolComment(int rowNumber, int totalNewComment) {
        if (adapter.getList().size() > rowNumber
                && adapter.getList().get(rowNumber) instanceof BaseKolViewModel) {
            BaseKolViewModel kolViewModel = (BaseKolViewModel) adapter.getList().get(rowNumber);
            kolViewModel.setTotalComment((
                    (BaseKolViewModel)
                            adapter.getList().get(rowNumber)).getTotalComment() +
                    totalNewComment);
            adapter.notifyItemChanged(rowNumber);
        }
    }

    private void onSuccessFollowUnfollowFromProfile(int isFollowing) {
        if (!adapter.getList().isEmpty()
                && adapter.getList().get(0) instanceof KolPostViewModel) {
            KolPostViewModel kolViewModel = (KolPostViewModel) adapter.getList().get(0);

            if (isFollowing != DEFAULT_VALUE) {
                kolViewModel.setFollowed(isFollowing == IS_FOLLOWING_TRUE);
                kolViewModel.setTemporarilyFollowed(isFollowing == IS_FOLLOWING_TRUE);
            }
            adapter.notifyItemChanged(0);
        }
    }

    private void updatePostState(int isLiked, int totalLike, int totalComment) {
        if (!adapter.getList().isEmpty()
                && adapter.getList().get(0) instanceof BaseKolViewModel) {
            BaseKolViewModel kolViewModel = (BaseKolViewModel) adapter.getList().get(0);

            if (isLiked != DEFAULT_VALUE) {
                kolViewModel.setLiked(isLiked == IS_LIKE_TRUE);
            }

            if (totalLike != DEFAULT_VALUE) {
                kolViewModel.setTotalLike(totalLike);
            }

            if (totalComment != DEFAULT_VALUE) {
                kolViewModel.setTotalComment(totalComment);
            }
            adapter.notifyItemChanged(0);
        }
    }

    private void showError(String message) {
        if (message == null) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        }
    }
}
