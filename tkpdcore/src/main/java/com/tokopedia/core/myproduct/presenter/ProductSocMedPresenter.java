package com.tokopedia.core.myproduct.presenter;

import com.tokopedia.core.myproduct.fragment.AddProductFragment;

/**
 * Created by m.normansyah on 4/8/16.
 */
public interface ProductSocMedPresenter {
    String TAG = "ProductSocMedPresenter";
    String messageTAG = TAG+" : ";
    AddProductFragment getFragment(int position);
    int getCurrentFragmentPosition();
    void removeFragment(int position);
    void noitfyCompleted(int position);
    String PRODUCT_SOC_MED_DATA = "PRODUCT_SOC_MED_DATA";
}
