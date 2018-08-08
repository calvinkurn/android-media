package com.tokopedia.abstraction.base.view.listener;

import com.tokopedia.abstraction.base.view.model.StepperModel;

/**
 * Created by zulfikarrahman on 7/27/17.
 */

public interface StepperListener {

    void goToNextPage(StepperModel object);

    void finishPage();
}
