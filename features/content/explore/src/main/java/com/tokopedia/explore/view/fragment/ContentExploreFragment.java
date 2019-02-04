package com.tokopedia.explore.view.fragment;

import android.content.Intent;
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

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.explore.R;
import com.tokopedia.explore.analytics.ContentExloreEventTracking;
import com.tokopedia.explore.di.DaggerExploreComponent;
import com.tokopedia.explore.view.adapter.ExploreCategoryAdapter;
import com.tokopedia.explore.view.adapter.ExploreImageAdapter;
import com.tokopedia.explore.view.adapter.factory.ExploreImageTypeFactory;
import com.tokopedia.explore.view.adapter.factory.ExploreImageTypeFactoryImpl;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.viewmodel.ExploreCategoryViewModel;
import com.tokopedia.explore.view.viewmodel.ExploreImageViewModel;
import com.tokopedia.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.postdetail.view.activity.KolPostDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 19/07/18.
 */

public class ContentExploreFragment extends BaseDaggerFragment
        implements ContentExploreContract.View, SearchInputView.Listener,
        SearchInputView.ResetListener, SwipeToRefresh.OnRefreshListener {

    public static String PARAM_CATEGORY_ID = "category_id";
    public static String DEFAULT_CATEGORY = "0";
    public static int CATEGORY_POSITION_NONE = -1;

    private static final int IMAGE_SPAN_COUNT = 3;
    private static final int IMAGE_SPAN_SINGLE = 1;
    private static final int LOAD_MORE_THRESHOLD = 2;
    @Inject
    ContentExploreContract.Presenter presenter;
    @Inject
    ExploreCategoryAdapter categoryAdapter;
    @Inject
    ExploreImageAdapter imageAdapter;
    private SearchInputView searchInspiration;
    private RecyclerView exploreCategoryRv;
    private RecyclerView exploreImageRv;
    private SwipeToRefresh swipeToRefresh;
    private View appBarLayout;
    private AbstractionRouter abstractionRouter;
    private RecyclerView.OnScrollListener scrollListener;
    private int categoryId;
    private boolean canLoadMore;
    private boolean hasLoadedOnce;

    public static ContentExploreFragment newInstance(Bundle bundle) {
        ContentExploreFragment fragment = new ContentExploreFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return ContentExloreEventTracking.Screen.SCREEN_CONTENT_STREAM;
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
        View view = inflater.inflate(R.layout.fragment_content_explore, container, false);
        searchInspiration = view.findViewById(R.id.search_inspiration);
        exploreCategoryRv = view.findViewById(R.id.explore_category_rv);
        exploreImageRv = view.findViewById(R.id.explore_image_rv);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GraphqlClient.init(getContext());
        initVar();
        initView();
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        loadData();
    }

    private void initView() {
        dropKeyboard();
        searchInspiration.setListener(this);
        searchInspiration.setResetListener(this);
        searchInspiration.getSearchTextView().setOnClickListener(v -> {
            searchInspiration.getSearchTextView().setCursorVisible(true);
        });
        swipeToRefresh.setOnRefreshListener(this);

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
                } else if (imageAdapter.getList().get(position) instanceof EmptyModel) {
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

    private void initVar() {
        if (getArguments() != null) {
            categoryId = Integer.valueOf(getArguments().getString(
                    PARAM_CATEGORY_ID,
                    DEFAULT_CATEGORY)
            );
            presenter.updateCategoryId(categoryId);
        }

        if (getActivity().getApplicationContext() instanceof AbstractionRouter) {
            abstractionRouter = (AbstractionRouter) getActivity().getApplicationContext();
        } else {
            throw new IllegalStateException("Application must be an instance of " +
                    AbstractionRouter.class.getSimpleName());
        }
    }

    private void loadData() {
        if (getUserVisibleHint() && isAdded() && getActivity() != null && presenter != null) {
            if (!hasLoadedOnce) {
                presenter.getExploreData(true);
                hasLoadedOnce = !hasLoadedOnce;
            }
            abstractionRouter.getAnalyticTracker().sendScreen(getActivity(), getScreenName());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onSuccessGetExploreData(ExploreViewModel exploreViewModel, boolean clearData) {
        if (!exploreViewModel.getExploreImageViewModelList().isEmpty()) {
            loadImageData(exploreViewModel.getExploreImageViewModelList());
        } else if (clearData) {
            showEmpty();
        }

        if (categoryAdapter.getList().isEmpty()) {
            loadTagData(exploreViewModel.getTagViewModelList());
        }

        boolean isCategoryExist = false;
        for (int i = 0; i < categoryAdapter.getList().size(); i++) {
            ExploreCategoryViewModel categoryViewModel = categoryAdapter.getList().get(i);
            if (categoryViewModel.getId() == categoryId) {
                categoryViewModel.setActive(true);
                categoryAdapter.notifyItemChanged(i);
                exploreCategoryRv.scrollToPosition(i);
                isCategoryExist = true;
                break;
            }
        }
        if (!isCategoryExist && categoryId != Integer.valueOf(DEFAULT_CATEGORY)) {
            onCategoryReset();
        }
    }

    @Override
    public void onErrorGetExploreDataFirstPage(String message) {
        NetworkErrorHelper.showEmptyState(getContext(), getView(), () -> {
            presenter.getExploreData(true);
        });
    }

    @Override
    public void onErrorGetExploreDataMore() {
        canLoadMore = false;
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), () -> {
            presenter.getExploreData(false);
        }).showRetrySnackbar();
    }

    @Override
    public void updateCursor(String cursor) {
        canLoadMore = !TextUtils.isEmpty(cursor);
        presenter.updateCursor(cursor);
    }

    @Override
    public void updateCategoryId(int categoryId) {
        this.categoryId = categoryId;
        presenter.updateCategoryId(categoryId);
    }

    @Override
    public void updateSearch(String search) {
        presenter.updateSearch(search);
    }

    @Override
    public void clearData() {
        imageAdapter.clearData();
    }

    @Override
    public void onCategoryClicked(int position, int categoryId, String categoryName) {
        clearSearch();
        resetDataParam();
        imageAdapter.clearData();
        boolean isSameCategory = setAllCategoriesInactive(position);
        if (isSameCategory) {
            updateCategoryId(0);
            abstractionRouter.getAnalyticTracker().sendEventTracking(
                    ContentExloreEventTracking.Event.EXPLORE,
                    ContentExloreEventTracking.Category.EXPLORE_INSPIRATION,
                    ContentExloreEventTracking.Action.DESELECT_CATEGORY,
                    categoryName
            );

        } else {
            updateCategoryId(categoryId);
            abstractionRouter.getAnalyticTracker().sendEventTracking(
                    ContentExloreEventTracking.Event.EXPLORE,
                    ContentExloreEventTracking.Category.EXPLORE_INSPIRATION,
                    ContentExloreEventTracking.Action.FILTER_CATEGORY,
                    categoryName
            );

            if (position > 0) {
                categoryAdapter.getList().get(position).setActive(true);
                categoryAdapter.notifyItemChanged(position);
            }
        }
        presenter.getExploreData(true);
    }

    @Override
    public void onCategoryReset() {
        onCategoryClicked(CATEGORY_POSITION_NONE, Integer.valueOf(DEFAULT_CATEGORY), "");
    }

    @Override
    public void showRefreshing() {
        swipeToRefresh.setRefreshing(true);
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
        if (swipeToRefresh.isRefreshing()) {
            swipeToRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showEmpty() {
        imageAdapter.showEmpty();
    }

    @Override
    public void goToKolPostDetail(KolPostViewModel kolPostViewModel) {
        Intent intent = KolPostDetailActivity.getInstance(
                getContext(),
                String.valueOf(kolPostViewModel.getContentId())
        );
        startActivity(intent);
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                ContentExloreEventTracking.Event.EXPLORE,
                ContentExloreEventTracking.Category.EXPLORE_INSPIRATION,
                ContentExloreEventTracking.Action.CLICK_GRID_CONTENT,
                String.format(
                        ContentExloreEventTracking.EventLabel.CLICK_GRID_CONTENT_LABEL,
                        kolPostViewModel.getName(),
                        kolPostViewModel.getContentId()
                )
        );
    }

    @Override
    public void onSearchSubmitted(String text) {
        dropKeyboard();
        resetDataParam();
        setAllCategoriesInactive();
        imageAdapter.clearData();

        updateSearch(text);
        presenter.getExploreData(true);
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                ContentExloreEventTracking.Event.EXPLORE,
                ContentExloreEventTracking.Category.EXPLORE_INSPIRATION,
                ContentExloreEventTracking.Action.SEARCH,
                text
        );
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchReset() {
        resetDataParam();
        setAllCategoriesInactive();
        imageAdapter.clearData();
        presenter.getExploreData(true);
    }

    @Override
    public void onRefresh() {
        presenter.updateCursor("");
        presenter.getExploreData(true);
    }

    @Override
    public void dropKeyboard() {
        searchInspiration.getSearchTextView().setCursorVisible(false);
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void scrollToTop() {
        if (exploreImageRv != null) {
            exploreImageRv.scrollToPosition(0);
        }
    }

    @Override
    public void resetDataParam() {
        updateSearch("");
        updateCursor("");
        updateCategoryId(0);
    }

    private void loadImageData(List<ExploreImageViewModel> exploreImageViewModelList) {
        imageAdapter.addList(new ArrayList<>(exploreImageViewModelList));
    }

    private void loadTagData(List<ExploreCategoryViewModel> tagViewModelList) {
        categoryAdapter.setList(tagViewModelList);
        appBarLayout.setVisibility(View.VISIBLE);
    }

    private boolean isLoading() {
        return imageAdapter.isLoading() || swipeToRefresh.isRefreshing();
    }

    private void clearSearch() {
        searchInspiration.getSearchTextView().setText("");
        dropKeyboard();
    }

    private void setAllCategoriesInactive() {
        setAllCategoriesInactive(-1);
    }

    private boolean setAllCategoriesInactive(int position) {
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
        return isSameCategory;
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
                        abstractionRouter.getAnalyticTracker().sendEventTracking(
                                ContentExloreEventTracking.Event.EXPLORE,
                                ContentExloreEventTracking.Category.EXPLORE_INSPIRATION,
                                ContentExloreEventTracking.Action.LOAD_MORE,
                                ""
                        );
                    }
                }
            };
        }
        return scrollListener;
    }
}
