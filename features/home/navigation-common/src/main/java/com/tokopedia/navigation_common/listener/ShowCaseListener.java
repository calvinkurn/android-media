package com.tokopedia.navigation_common.listener;

import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;

/**
 * Created by meta on 02/08/18.
 */
public interface ShowCaseListener {
    void onReadytoShowBoarding(ArrayList<ShowCaseObject> showCaseObjects);
}
