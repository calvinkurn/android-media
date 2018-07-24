package com.tokopedia.kol.feature.post.view.fragment;

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
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.post.di.DaggerKolProfileComponent;
import com.tokopedia.kol.feature.post.di.KolProfileModule;
import com.tokopedia.kol.feature.post.view.activity.KolPostDetailActivity;
import com.tokopedia.kol.feature.post.view.adapter.KolPostAdapter;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactoryImpl;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by yfsx on 23/07/18.
 */
public class KolPostDetailFragment extends BaseDaggerFragment
        implements KolPostListener.View.ViewHolder {

    private KolPostViewModel kolPostViewModel;
    private RecyclerView recyclerView;
    private KolPostAdapter adapter;
    private AbstractionRouter abstractionRouter;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kol_post_detail, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity().getApplication() instanceof AbstractionRouter) {
            abstractionRouter = (AbstractionRouter) getActivity().getApplication();
        } else {
            throw new IllegalStateException("Application must be an instance of "
                    + AbstractionRouter.class.getSimpleName());
        }

        KolPostTypeFactory typeFactory = new KolPostTypeFactoryImpl(this);
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

    }

    @Override
    public void onGoToKolProfileUsingApplink(int rowNumber, String applink) {

    }

    @Override
    public void onOpenKolTooltip(int rowNumber, String url) {

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

    }

    private void populateData() {
        if (getArguments() != null &&
                getArguments().get(KolPostDetailActivity.PARAM_KOLPOST) != null) {
            kolPostViewModel = (KolPostViewModel) getArguments().get(KolPostDetailActivity.PARAM_KOLPOST);
            List<Visitable> itemList = new ArrayList<>();
            itemList.add(kolPostViewModel);
            adapter.addList(itemList);
        }
    }
}
