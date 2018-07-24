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

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.explore.R;
import com.tokopedia.explore.di.DaggerExploreComponent;
import com.tokopedia.explore.view.adapter.ExploreImageAdapter;
import com.tokopedia.explore.view.adapter.ExploreCategoryAdapter;
import com.tokopedia.explore.view.adapter.factory.ExploreImageTypeFactory;
import com.tokopedia.explore.view.adapter.factory.ExploreImageTypeFactoryImpl;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.viewmodel.ExploreCategoryViewModel;
import com.tokopedia.explore.view.viewmodel.ExploreImageViewModel;
import com.tokopedia.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.graphql.data.GraphqlClient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 19/07/18.
 */

public class ContentExploreFragment extends BaseDaggerFragment implements ContentExploreContract.View {

    private static final int IMAGE_SPAN_COUNT = 3;
    private static final int IMAGE_SPAN_SINGLE = 1;
    private static final int LOAD_MORE_THRESHOLD = 2;

    private View view;
    private SearchInputView searchInspiration;
    private RecyclerView exploreCategoryRv;
    private RecyclerView exploreImageRv;

    @Inject
    ContentExploreContract.Presenter presenter;

    @Inject
    ExploreCategoryAdapter categoryAdapter;

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
        presenter.getExploreData(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onSuccessGetExploreData(ExploreViewModel exploreViewModel) {
        loadImageData(exploreViewModel.getExploreImageViewModelList());

        if (categoryAdapter.getList().isEmpty()) {
            loadTagData(exploreViewModel.getTagViewModelList());
        }
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
    public void clearData() {
        imageAdapter.clearData();
    }

    @Override
    public void onCategoryClicked(int position, int categoryId) {
        boolean isSameCategory = false;

        for (int i = 0; i < categoryAdapter.getList().size(); i++) {
            ExploreCategoryViewModel categoryViewModel = categoryAdapter.getList().get(i);
            if (categoryViewModel.isActive()) {
                categoryViewModel.setActive(false);
                categoryAdapter.notifyItemChanged(i);

                if (i == position) {
                    isSameCategory = true;
                }
                break;
            }
        }

        if (isSameCategory) {
            updateCategoryId(0);
        } else {
            updateCategoryId(categoryId);
            categoryAdapter.getList().get(position).setActive(true);
            categoryAdapter.notifyItemChanged(position);
        }

        updateCursor("");
        imageAdapter.clearData();
        presenter.getExploreData(true);
    }

    @Override
    public void showLoading() {
        if (!isLoading()) {
            imageAdapter.showLoading();
        }
    }

    @Override
    public void dismissLoading() {
        if (isLoading()) {
            imageAdapter.dismissLoading();
        }
    }

    private void initView() {
        searchInspiration = view.findViewById(R.id.search_inspiration);
        exploreCategoryRv = view.findViewById(R.id.explore_category_rv);
        exploreImageRv = view.findViewById(R.id.explore_image_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        exploreCategoryRv.setLayoutManager(linearLayoutManager);
        categoryAdapter.setListener(this);
        exploreCategoryRv.setAdapter(categoryAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                IMAGE_SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (imageAdapter.getList().get(position) instanceof ExploreImageViewModel) {
                    return IMAGE_SPAN_SINGLE;
                } else if (imageAdapter.getList().get(position) instanceof LoadingMoreModel) {
                    return IMAGE_SPAN_COUNT;
                }
                return 0;
            }
        });
        exploreImageRv.setLayoutManager(gridLayoutManager);
        exploreImageRv.addOnScrollListener(onScrollListener(gridLayoutManager));
        ExploreImageTypeFactory typeFactory = new ExploreImageTypeFactoryImpl(this);
        imageAdapter.setTypeFactory(typeFactory);
        exploreImageRv.setAdapter(imageAdapter);
    }

    private void loadImageData(List<ExploreImageViewModel> exploreImageViewModelList) {
        imageAdapter.addList(new ArrayList<>(exploreImageViewModelList));
    }

    private void loadTagData(List<ExploreCategoryViewModel> tagViewModelList) {
        categoryAdapter.setList(tagViewModelList);
    }

    private boolean isLoading() {
        return imageAdapter.isLoading();
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
                        presenter.getExploreData(false);
                    }
                }
            };
        }
        return scrollListener;
    }
}
