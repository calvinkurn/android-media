package com.tokopedia.topads.common.view.listener;


import com.tokopedia.abstraction.base.view.model.StepperModel;

/**
 * Created by zulfikarrahman on 7/27/17.
 */
public interface StepperListener<T extends StepperModel> {

    void goToNextPage(T object);

    void finishPage();

    T getStepperModel();

}