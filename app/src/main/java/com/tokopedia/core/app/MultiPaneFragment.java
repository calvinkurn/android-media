package com.tokopedia.core.app;

import android.app.Activity;
import android.app.Fragment;

import java.util.List;

/**
 * Created by Tkpd_Eka on 1/13/2015.
 */
public abstract class MultiPaneFragment extends Fragment{

    protected MultiPaneInterface listener;

    public MultiPaneFragment() {
    }

    protected void createDetailView(List<Fragment> fragmentList, int page){
        listener.onDetailPageRequest(fragmentList, page);
    }

    public interface MultiPaneInterface{
        void onDetailPageRequest(List<Fragment> fragmentList, int page);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (MultiPaneActivity)activity;
        } catch (Exception e) {
            e.printStackTrace();
//            throw new RuntimeException("activity is not multipane");
        }
    }
}
