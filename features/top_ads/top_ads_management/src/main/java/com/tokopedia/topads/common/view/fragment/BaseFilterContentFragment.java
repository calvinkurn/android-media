package com.tokopedia.topads.common.view.fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.topads.common.view.listener.BaseFilterContentViewListener;

/**
 * @author normansyahputa on 5/26/17.
 *         <p>
 *         {@link com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsFilterContentFragment}
 */
public abstract class BaseFilterContentFragment<P> extends BaseDaggerFragment implements BaseFilterContentViewListener {
    protected BaseFilterContentFragment.Callback callback;
    /**
     * Sign for title filter list
     */
    private boolean active;

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void onStatusChanged(boolean active);

    }
}
