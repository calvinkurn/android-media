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

/**
 * @author by milhamj on 19/07/18.
 */

public class ExploreFragment extends BaseDaggerFragment {

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
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }


}
