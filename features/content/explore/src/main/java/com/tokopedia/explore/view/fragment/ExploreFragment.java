package com.tokopedia.explore.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.explore.R;
import com.tokopedia.explore.view.listener.ExploreFragmentListener;
import com.tokopedia.explore.view.viewmodel.ExploreImageViewModel;
import com.tokopedia.explore.view.viewmodel.ExploreTagViewModel;

import java.util.List;

/**
 * @author by milhamj on 19/07/18.
 */

public class ExploreFragment extends BaseDaggerFragment implements ExploreFragmentListener.View {

    private SearchInputView searchInspiration;
    private RecyclerView exploreTagRv;
    private RecyclerView exploreImageRv;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initView(View view) {
        searchInspiration = view.findViewById(R.id.search_inspiration);
        exploreTagRv = view.findViewById(R.id.explore_tag_rv);
        exploreImageRv = view.findViewById(R.id.explore_image_rv);


    }

    private void loadImageData(List<ExploreImageViewModel> imageViewModelList) {

    }

    private void loadTagData(List<ExploreTagViewModel> tagViewModelList) {

    }
}
