package com.tokopedia.tkpd.dynamicfilter.presenter;

import android.support.v4.app.Fragment;

import com.tokopedia.tkpd.dynamicfilter.model.DynamicFilterModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.tkpd.myproduct.presenter.ImageGalleryImpl.Pair;

/**
 * Created by noiz354 on 7/11/16.
 */
public interface DynamicFilterView {
    String EXTRA_RESULT = "EXTRA_RESULT";
    int REQUEST_CODE = 219;
    void setFragment(Fragment fragment, String TAG, int layoutId);
    void setFragmentForFirstTime3(List<DynamicFilterModel.Filter> data);
    void setFragmentBasedOnData(DynamicFilterModel.Filter data);
    void putSelectedFilter(String key, String value);
    String getTextInput(String key);
    void saveTextInput(String key, String textInput);
    void removeTextInput(String key);
    void saveCheckedPosition(String key, Boolean status);
    Boolean getCheckedPosition(String key);
    Map<String, String> getSelectedFilter();
    void removeCheckedPosition(String key);
    void removeSelecfedFilter(String key);
    void finishThis();
}
