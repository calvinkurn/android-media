package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.StepperListener
import com.tokopedia.topads.data.CreateManualAdsStepperModel

/**
 * Author errysuprayogi on 01,November,2019
 */
abstract class BaseStepperFragment<T : CreateManualAdsStepperModel>: BaseDaggerFragment() {
    protected var stepperModel: T? = null
    protected val stepperListener: StepperListener? by lazy {
        if (context is StepperListener) context as StepperListener else null
    }

    protected abstract fun initiateStepperModel()
    protected abstract fun saveStepperModel(stepperModel: T)
    protected abstract fun gotoNextPage()
    protected abstract fun populateView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (savedInstanceState == null){
                stepperModel = it.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA)
            }
        }
        initiateStepperModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateView()
    }
}