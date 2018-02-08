package com.tokopedia.core.app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tkpd_Eka on 3/19/2015.
 */
@Deprecated
public abstract class V2BaseFragment extends Fragment{

    protected View rootView;

    /**
     * @return View layout Id in R.layout.
     */
    protected abstract int getRootViewId();

    /**
     * Called on onCreateView after creating rootView and setting listener
     */
    protected abstract void onCreateView();

    /**
     * @return NormalViewHolder
     */
    protected abstract Object getHolder();

    /**
     * @param holder NormalViewHolder
     */
    protected abstract void setHolder(Object holder);

    /**
     * Set view to NormalViewHolder with findViewById
     */
    protected abstract void initView();

    /**
     * Set listener to NormalViewHolder
     */
    protected abstract void setListener();

    protected View findViewById(int id){
        return rootView.findViewById(id);
    }

    protected View getRootView(){
        return rootView;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getRootViewId() == 0)
            throw new RuntimeException("Needs layout ID");

        if(rootView == null){
            rootView = inflater.inflate(getRootViewId(), container, false);
            initView();
            rootView.setTag(getHolder());
        }
        else{
            setHolder(rootView.getTag());
        }
        setListener();
        onCreateView();
        return rootView;
    }

}
