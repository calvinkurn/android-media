package com.tokopedia.topads.common.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.StepperListener
import com.tokopedia.topads.keyword.view.model.TopAdsKeywordNewStepperModel

abstract class TopAdsNewBaseStepperFragment<T : TopAdsKeywordNewStepperModel> : BaseDaggerFragment() {
    protected var stepperModel: T? = null
    protected val stepperListener: StepperListener? by lazy {
        if (context is StepperListener) context as StepperListener else null
    }

    protected fun onNextClicked(){
        stepperModel?.let {  saveStepperModel(it)}
        gotoNextPage()
    }

    protected abstract fun initiateStepperModel()
    protected abstract fun saveStepperModel(stepperModel: T)
    protected abstract fun gotoNextPage()
    protected abstract fun populateView(stepperModel: T)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (savedInstanceState == null){
                stepperModel = it.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepperModel?.let {  populateView(it) }
    }
}