package com.tokopedia.kyc.view.interfaces;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

public interface ActivityListener {
    void setHeaderTitle(String title);
    void addReplaceFragment(BaseDaggerFragment baseDaggerFragment, boolean replace, String tag);
}
