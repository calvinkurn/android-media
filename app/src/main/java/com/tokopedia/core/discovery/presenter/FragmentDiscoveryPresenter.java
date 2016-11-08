package com.tokopedia.core.discovery.presenter;

import android.content.Context;

import com.tokopedia.core.discovery.fragment.browseparent.ProductFragment;
import com.tokopedia.core.discovery.view.FragmentBrowseProductView;
import com.tokopedia.core.session.base.BaseImpl;

/**
 * Created by noiz354 on 3/24/16.
 */
public abstract class FragmentDiscoveryPresenter extends BaseImpl<FragmentBrowseProductView> {
    public static final String TAG = "MNORMANSYAH";

    public FragmentDiscoveryPresenter(FragmentBrowseProductView view) {
        super(view);
    }


    /**
     * this is FLAG to determine whether {@link ProductFragment}
     * @param TAG non empty String example is at {@link ProductFragment#TAG}
     */
    public abstract void setTAG(String TAG);

    public abstract void loadMore(Context context);

    public abstract void getTopAds(int page, String TAG, Context context);

    public abstract void getTopAds(int page, String TAG, Context context, int spanCount);

    public abstract void sendGTMNoResult(Context context);
}
