package com.tokopedia.kyc.view.interfaces;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.model.ConfirmRequestDataContainer;

public interface ActivityListener {
    void setHeaderTitle(String title);
    void addReplaceFragment(BaseDaggerFragment baseDaggerFragment, boolean replace, String tag);
    void addReplaceFragmentWithCustAnim(BaseDaggerFragment baseDaggerFragment, boolean replace, String tag, int entryAnimId, int exitAnimId);
    void showHideActionbar(boolean show);
    ConfirmRequestDataContainer getDataContatainer();
    boolean isRetryValid();
}
