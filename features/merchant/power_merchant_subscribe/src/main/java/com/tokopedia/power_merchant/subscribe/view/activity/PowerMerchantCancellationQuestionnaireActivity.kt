package com.tokopedia.power_merchant.subscribe.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireModel
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireStepperModel
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantCancellationQuestionnaireMultipleCheckboxFragment
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantCancellationQuestionnaireIntroFragment

class PowerMerchantCancellationQuestionnaireActivity : BaseStepperActivity() {
    override fun getListFragment(): MutableList<Fragment> {
        val list = listOf(
                PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire(
                        "Question 2",
                        arrayListOf("Ch 1", "Ch 2", "Ch 3")
                ),
                PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire(
                        "Question 3",
                        arrayListOf("Ch 1", "Ch 2", "Ch 3")
                ),
                PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire(
                        "Question 4",
                        arrayListOf("Ch 1", "Ch 2", "Ch 3")
                ),
                PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire(
                        "Question 5",
                        arrayListOf("Ch 1", "Ch 2", "Ch 3")
                )
        )
        val fragmentList: ArrayList<Fragment> = ArrayList()
        val deactivateDate = "31 Agustus 2019"
        fragmentList.add(PowerMerchantCancellationQuestionnaireIntroFragment.createInstance(
                deactivateDate
        ))
        val pmCancellationQuestionnaireStepperModel = stepperModel as PMCancellationQuestionnaireStepperModel
        list.forEachIndexed { position, multipleChecklistQuestionnaire ->
            fragmentList.add(PowerMerchantCancellationQuestionnaireMultipleCheckboxFragment.createInstance(
                    position,
                    multipleChecklistQuestionnaire
            ))
            if (pmCancellationQuestionnaireStepperModel.listChoice.size - 1 < position) {
                pmCancellationQuestionnaireStepperModel.listChoice.add(ArrayList())
            }
        }
        return fragmentList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            stepperModel = savedInstanceState.getParcelable(STEPPER_MODEL_EXTRA)
        } else {
            stepperModel = PMCancellationQuestionnaireStepperModel()
        }
        super.onCreate(savedInstanceState)
    }

    fun isFinalPage(): Boolean = currentPosition == listFragment.size

    override fun updateToolbarTitle() {}

    override fun updateToolbarTitle(title: String?) {}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STEPPER_MODEL_EXTRA, stepperModel)
    }

    override fun onBackEvent() {
        finish()
    }
}
