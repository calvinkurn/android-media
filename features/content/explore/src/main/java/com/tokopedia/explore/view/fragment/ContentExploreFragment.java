package com.tokopedia.explore.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
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
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.explore.R;
import com.tokopedia.explore.di.DaggerExploreComponent;
import com.tokopedia.explore.view.adapter.ExploreImageAdapter;
import com.tokopedia.explore.view.adapter.ExploreTagAdapter;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.viewmodel.ExploreImageViewModel;
import com.tokopedia.explore.view.viewmodel.ExploreTagViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 19/07/18.
 */

public class ContentExploreFragment extends BaseDaggerFragment implements ContentExploreContract.View {

    private static final int IMAGE_SPAN_COUNT = 3;

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

    private EndlessRecyclerViewScrollListener recyclerviewScrollListener;
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
        View view = inflater.inflate(R.layout.fragment_content_explore, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);

        List<ExploreImageViewModel> imageList = new ArrayList<>();
        imageList.add(new ExploreImageViewModel("http://www.mymbuzz.com/wp-content/uploads/sites/2/2017/03/Doctor-Who-Series-10-photos-3.jpeg"));
        imageList.add(new ExploreImageViewModel("http://www.scififantasynetwork.com/wp-content/uploads/2017/04/The-Pilot-banner.jpg"));
        imageList.add(new ExploreImageViewModel("http://downloads.bbc.co.uk/tv/isite-static/doctorwho/wallpapers/christmas-2015/day-1/doctor-gallifrey-16x9.jpg"));
        imageList.add(new ExploreImageViewModel("https://tse3.mm.bing.net/th?id=OIP.xPCr-zKH0ZUk-1Lx9rqkQgHaKd&pid=Api"));
        imageList.add(new ExploreImageViewModel("http://www.bang2write.com/wp-content/uploads/2017/07/tp-graphic-dr-whodunnit.jpg"));
        imageList.add(new ExploreImageViewModel("http://www.mymbuzz.com/wp-content/uploads/sites/2/2017/03/Doctor-Who-Series-10-photos-3.jpeg"));
        imageList.add(new ExploreImageViewModel("http://www.scififantasynetwork.com/wp-content/uploads/2017/04/The-Pilot-banner.jpg"));
        imageList.add(new ExploreImageViewModel("http://downloads.bbc.co.uk/tv/isite-static/doctorwho/wallpapers/christmas-2015/day-1/doctor-gallifrey-16x9.jpg"));
        imageList.add(new ExploreImageViewModel("https://tse3.mm.bing.net/th?id=OIP.xPCr-zKH0ZUk-1Lx9rqkQgHaKd&pid=Api"));
        imageList.add(new ExploreImageViewModel("http://www.bang2write.com/wp-content/uploads/2017/07/tp-graphic-dr-whodunnit.jpg"));
        imageList.add(new ExploreImageViewModel("http://www.mymbuzz.com/wp-content/uploads/sites/2/2017/03/Doctor-Who-Series-10-photos-3.jpeg"));
        imageList.add(new ExploreImageViewModel("http://www.scififantasynetwork.com/wp-content/uploads/2017/04/The-Pilot-banner.jpg"));
        imageList.add(new ExploreImageViewModel("http://downloads.bbc.co.uk/tv/isite-static/doctorwho/wallpapers/christmas-2015/day-1/doctor-gallifrey-16x9.jpg"));
        imageList.add(new ExploreImageViewModel("https://tse3.mm.bing.net/th?id=OIP.xPCr-zKH0ZUk-1Lx9rqkQgHaKd&pid=Api"));
        imageList.add(new ExploreImageViewModel("http://www.bang2write.com/wp-content/uploads/2017/07/tp-graphic-dr-whodunnit.jpg"));
        imageList.add(new ExploreImageViewModel("http://www.mymbuzz.com/wp-content/uploads/sites/2/2017/03/Doctor-Who-Series-10-photos-3.jpeg"));
        imageList.add(new ExploreImageViewModel("http://www.scififantasynetwork.com/wp-content/uploads/2017/04/The-Pilot-banner.jpg"));
        imageList.add(new ExploreImageViewModel("http://downloads.bbc.co.uk/tv/isite-static/doctorwho/wallpapers/christmas-2015/day-1/doctor-gallifrey-16x9.jpg"));
        imageList.add(new ExploreImageViewModel("https://tse3.mm.bing.net/th?id=OIP.xPCr-zKH0ZUk-1Lx9rqkQgHaKd&pid=Api"));
        imageList.add(new ExploreImageViewModel("http://www.bang2write.com/wp-content/uploads/2017/07/tp-graphic-dr-whodunnit.jpg"));
        imageList.add(new ExploreImageViewModel("http://www.mymbuzz.com/wp-content/uploads/sites/2/2017/03/Doctor-Who-Series-10-photos-3.jpeg"));
        imageList.add(new ExploreImageViewModel("http://www.scififantasynetwork.com/wp-content/uploads/2017/04/The-Pilot-banner.jpg"));
        imageList.add(new ExploreImageViewModel("http://downloads.bbc.co.uk/tv/isite-static/doctorwho/wallpapers/christmas-2015/day-1/doctor-gallifrey-16x9.jpg"));
        imageList.add(new ExploreImageViewModel("https://tse3.mm.bing.net/th?id=OIP.xPCr-zKH0ZUk-1Lx9rqkQgHaKd&pid=Api"));
        imageList.add(new ExploreImageViewModel("http://www.bang2write.com/wp-content/uploads/2017/07/tp-graphic-dr-whodunnit.jpg"));
        imageList.add(new ExploreImageViewModel("http://www.mymbuzz.com/wp-content/uploads/sites/2/2017/03/Doctor-Who-Series-10-photos-3.jpeg"));
        imageList.add(new ExploreImageViewModel("http://www.scififantasynetwork.com/wp-content/uploads/2017/04/The-Pilot-banner.jpg"));
        imageList.add(new ExploreImageViewModel("http://downloads.bbc.co.uk/tv/isite-static/doctorwho/wallpapers/christmas-2015/day-1/doctor-gallifrey-16x9.jpg"));
        imageList.add(new ExploreImageViewModel("https://tse3.mm.bing.net/th?id=OIP.xPCr-zKH0ZUk-1Lx9rqkQgHaKd&pid=Api"));
        imageList.add(new ExploreImageViewModel("http://www.bang2write.com/wp-content/uploads/2017/07/tp-graphic-dr-whodunnit.jpg"));
        imageList.add(new ExploreImageViewModel("http://www.mymbuzz.com/wp-content/uploads/sites/2/2017/03/Doctor-Who-Series-10-photos-3.jpeg"));
        imageList.add(new ExploreImageViewModel("http://www.scififantasynetwork.com/wp-content/uploads/2017/04/The-Pilot-banner.jpg"));
        imageList.add(new ExploreImageViewModel("http://downloads.bbc.co.uk/tv/isite-static/doctorwho/wallpapers/christmas-2015/day-1/doctor-gallifrey-16x9.jpg"));
        imageList.add(new ExploreImageViewModel("https://tse3.mm.bing.net/th?id=OIP.xPCr-zKH0ZUk-1Lx9rqkQgHaKd&pid=Api"));
        imageList.add(new ExploreImageViewModel("http://www.bang2write.com/wp-content/uploads/2017/07/tp-graphic-dr-whodunnit.jpg"));
        imageList.add(new ExploreImageViewModel("http://www.mymbuzz.com/wp-content/uploads/sites/2/2017/03/Doctor-Who-Series-10-photos-3.jpeg"));
        imageList.add(new ExploreImageViewModel("http://www.scififantasynetwork.com/wp-content/uploads/2017/04/The-Pilot-banner.jpg"));
        imageList.add(new ExploreImageViewModel("http://downloads.bbc.co.uk/tv/isite-static/doctorwho/wallpapers/christmas-2015/day-1/doctor-gallifrey-16x9.jpg"));
        imageList.add(new ExploreImageViewModel("https://tse3.mm.bing.net/th?id=OIP.xPCr-zKH0ZUk-1Lx9rqkQgHaKd&pid=Api"));
        imageList.add(new ExploreImageViewModel("http://www.bang2write.com/wp-content/uploads/2017/07/tp-graphic-dr-whodunnit.jpg"));
        loadImageData(imageList);

        List<ExploreTagViewModel> tagList = new ArrayList<>();
        tagList.add(new ExploreTagViewModel("Fashion Ilham"));
        tagList.add(new ExploreTagViewModel("Fashion Jean"));
        tagList.add(new ExploreTagViewModel("Fashion Steven"));
        tagList.add(new ExploreTagViewModel("Fashion Nisie"));
        tagList.add(new ExploreTagViewModel("Fashion Yoas"));
        tagList.add(new ExploreTagViewModel("Fashion Hendri"));
        tagList.add(new ExploreTagViewModel("Fashion Atin"));
        loadTagData(tagList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
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

    private void initView(View view) {
        searchInspiration = view.findViewById(R.id.search_inspiration);
        exploreTagRv = view.findViewById(R.id.explore_tag_rv);
        exploreImageRv = view.findViewById(R.id.explore_image_rv);
        progressBar = view.findViewById(R.id.progress_bar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        exploreTagRv.setLayoutManager(linearLayoutManager);
        exploreTagRv.setAdapter(tagAdapter);

        ViewCompat.setNestedScrollingEnabled(exploreImageRv, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                IMAGE_SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        exploreImageRv.setLayoutManager(gridLayoutManager);
        exploreImageRv.setAdapter(imageAdapter);

        recyclerviewScrollListener = onScrollListener(gridLayoutManager);
        exploreImageRv.addOnScrollListener(recyclerviewScrollListener);
    }

    private void loadImageData(List<ExploreImageViewModel> imageViewModelList) {
        imageAdapter.setList(imageViewModelList);
    }

    private void loadTagData(List<ExploreTagViewModel> tagViewModelList) {
        tagAdapter.setList(tagViewModelList);
    }

    private boolean isLoading() {
        return progressBar.getVisibility() == View.VISIBLE;
    }

    private EndlessRecyclerViewScrollListener onScrollListener(GridLayoutManager layoutManager) {
        return new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (canLoadMore && !isLoading()) {

                }
            }
        };
    }
}
