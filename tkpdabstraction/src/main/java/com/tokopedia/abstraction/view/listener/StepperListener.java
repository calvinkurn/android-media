package com.tokopedia.abstraction.view.listener;

import com.tokopedia.abstraction.view.model.StepperModel;

/**
 * Created by zulfikarrahman on 7/27/17.
 */

public interface StepperListener {

    void goToNextPage(StepperModel object);

    void finishPage();
}
