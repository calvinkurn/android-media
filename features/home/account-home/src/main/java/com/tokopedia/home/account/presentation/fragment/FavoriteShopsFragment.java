package com.tokopedia.home.account.presentation.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.home.account.R;

/**
 * @author okasurya on 7/26/18.
 */
public class FavoriteShopsFragment extends TkpdBaseV4Fragment {
    public static final String TAG = FavoriteShopsFragment.class.getSimpleName();

    public static Fragment newInstance() {
        return new FavoriteShopsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_shops, container, false);
        return view;
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }
}
