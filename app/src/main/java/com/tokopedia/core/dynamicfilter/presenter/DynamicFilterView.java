package com.tokopedia.core.dynamicfilter.presenter;

import android.support.v4.app.Fragment;

import com.tokopedia.core.dynamicfilter.model.DynamicFilterModel;

import java.util.List;
import java.util.Map;

/**
 * Created by noiz354 on 7/11/16.
 */
public interface DynamicFilterView {

    String EXTRA_FILTERS = "EXTRA_FILTERS";

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

    Map<String, Boolean> getSelectedPositions();

    Map<String, String> getSelectedFilter();

    void removeCheckedPosition(String key);

    void removeSelecfedFilter(String key);

    void finishThis();
}
