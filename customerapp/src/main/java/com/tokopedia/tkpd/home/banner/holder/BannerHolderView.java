package com.tokopedia.tkpd.home.banner.holder;

import android.support.v4.app.Fragment;
import com.tokopedia.tkpd.home.banner.holder.Holder;
import com.tokopedia.tkpd.home.facade.FacadePromo;
import com.tokopedia.tkpd.home.fragment.FragmentBanner;

public class BannerHolderView implements Holder<FacadePromo.PromoItem> {

    @Override
    public Fragment createFragment(FacadePromo.PromoItem data) {
        return FragmentBanner.newInstance(data);
    }

}
