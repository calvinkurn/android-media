package com.tokopedia.explore.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.explore.R;
import com.tokopedia.explore.di.DaggerExploreComponent;
import com.tokopedia.explore.view.adapter.ExploreImageAdapter;
import com.tokopedia.explore.view.adapter.ExploreTagAdapter;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.viewmodel.ExploreCategoryViewModel;
import com.tokopedia.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 19/07/18.
 */

public class ContentExploreFragment extends BaseDaggerFragment implements ContentExploreContract.View {

    private static final int IMAGE_SPAN_COUNT = 3;
    private static final int LOAD_MORE_THRESHOLD = 2;

    private View view;
    private SearchInputView searchInspiration;
    private RecyclerView exploreTagRv;
    private RecyclerView exploreImageRv;
    private ProgressBar progressBar;

    @Inject
    ContentExploreContract.Presenter presenter;

    @Inject
    ExploreTagAdapter tagAdapter;

    @Inject
    ExploreImageAdapter imageAdapter;

    private RecyclerView.OnScrollListener scrollListener;
    private boolean canLoadMore;

    public static ContentExploreFragment newInstance() {
        ContentExploreFragment fragment = new ContentExploreFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        BaseAppComponent baseAppComponent
                = ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent();
        DaggerExploreComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_content_explore, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GraphqlClient.init(getContext());
        initView();
        presenter.attachView(this);
        presenter.getExploreData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onSuccessGetExploreData(ExploreViewModel exploreViewModel) {
        loadImageData(exploreViewModel.getKolPostViewModelList());
        loadTagData(exploreViewModel.getTagViewModelList());
    }

    @Override
    public void onErrorGetExploreData() {

    }

    @Override
    public void updateCursor(String cursor) {
        canLoadMore = !TextUtils.isEmpty(cursor);

        presenter.updateCursor(cursor);
    }

    @Override
    public void updateCategoryId(int categoryId) {
        presenter.updateCategoryId(categoryId);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void initView() {
        searchInspiration = view.findViewById(R.id.search_inspiration);
        exploreTagRv = view.findViewById(R.id.explore_tag_rv);
        exploreImageRv = view.findViewById(R.id.explore_image_rv);
        progressBar = view.findViewById(R.id.progress_bar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        exploreTagRv.setLayoutManager(linearLayoutManager);
        tagAdapter.setListener(this);
        exploreTagRv.setAdapter(tagAdapter);

        //TODO milhamj
//        ViewCompat.setNestedScrollingEnabled(exploreImageRv, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                IMAGE_SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        exploreImageRv.setLayoutManager(gridLayoutManager);
        exploreImageRv.addOnScrollListener(onScrollListener(gridLayoutManager));
        imageAdapter.setListener(this);
        exploreImageRv.setAdapter(imageAdapter);
    }

    private void loadImageData(List<KolPostViewModel> kolPostViewModelList) {
        imageAdapter.addList(kolPostViewModelList);
    }

    private void loadTagData(List<ExploreCategoryViewModel> tagViewModelList) {
        tagAdapter.setList(tagViewModelList);
    }

    private boolean isLoading() {
        return progressBar.getVisibility() == View.VISIBLE;
    }

    private RecyclerView.OnScrollListener onScrollListener(GridLayoutManager layoutManager) {
        if (scrollListener == null) {
            scrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    int visibleThreshold = LOAD_MORE_THRESHOLD * layoutManager.getSpanCount();
                    if (lastVisibleItemPosition + visibleThreshold > imageAdapter.getItemCount()
                            && canLoadMore
                            && !isLoading()) {
                        presenter.getExploreData();
                    }
                }
            };
        }
        return scrollListener;
    }
}
