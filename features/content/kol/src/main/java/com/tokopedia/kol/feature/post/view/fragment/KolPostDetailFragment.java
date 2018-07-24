package com.tokopedia.kol.feature.post.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.KolRouter;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity;
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment;
import com.tokopedia.kol.feature.post.di.DaggerKolProfileComponent;
import com.tokopedia.kol.feature.post.di.KolProfileModule;
import com.tokopedia.kol.feature.post.view.activity.KolPostDetailActivity;
import com.tokopedia.kol.feature.post.view.adapter.KolPostAdapter;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactoryImpl;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by yfsx on 23/07/18.
 */
public class KolPostDetailFragment extends BaseDaggerFragment
        implements KolPostListener.View.ViewHolder {

    private static final int OPEN_KOL_COMMENT = 101;

    private RecyclerView recyclerView;
    private KolPostAdapter adapter;
    private AbstractionRouter abstractionRouter;
    private KolRouter kolRouter;

    @Inject
    UserSession userSession;

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

        KolPostTypeFactoryImpl typeFactory = new KolPostTypeFactoryImpl(this);
        typeFactory.setType(KolPostViewHolder.Type.EXPLORE);
        adapter = new KolPostAdapter(typeFactory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        populateData();
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
        startActivity(intent);
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

    }

    @Override
    public void onUnlikeKolClicked(int adapterPosition, int id) {

    }

    @Override
    public void onGoToKolComment(int rowNumber, int id) {
        Intent intent = KolCommentActivity.getCallingIntent(getContext(), id, rowNumber);
        startActivityForResult(intent, OPEN_KOL_COMMENT);
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
            default:
                break;
        }
    }

    private void populateData() {
        if (getArguments() != null &&
                getArguments().get(KolPostDetailActivity.PARAM_KOLPOST) != null) {

            KolPostViewModel kolPostViewModel = (KolPostViewModel)
                    getArguments().get(KolPostDetailActivity.PARAM_KOLPOST);
            List<Visitable> itemList = new ArrayList<>();
            itemList.add(kolPostViewModel);
            adapter.addList(itemList);
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
}
