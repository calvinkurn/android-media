package com.tokopedia.navigation.presentation.base;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;

/**
 * Created by meta on 19/06/18.
 */
public abstract class BaseParentFragment extends TkpdBaseV4Fragment {

    public abstract int resLayout();
    public abstract void initView(View view);
    public abstract void loadData();

    boolean isLoaded = false;
    public View parentView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }
        loadData();
    }

    public void setTitle(String title) {

    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (resLayout() > 0) {
            parentView = inflater.inflate(resLayout(), container, false);
            setHasOptionsMenu(true);
            initView(parentView);
            return parentView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
