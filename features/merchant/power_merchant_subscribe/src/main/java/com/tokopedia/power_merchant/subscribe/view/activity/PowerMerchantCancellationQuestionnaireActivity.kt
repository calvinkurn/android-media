package com.tokopedia.power_merchant.subscribe.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireModel
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireStepperModel
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantCancellationQuestionnaireFragment
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantCancellationQuestionnaireIntroFragment

class PowerMerchantCancellationQuestionnaireActivity : BaseStepperActivity() {
    override fun getListFragment(): MutableList<Fragment> {
        val list = listOf(
                PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire(
                        "Question 2",
                        mutableListOf("Ch 1","Ch 2","Ch 3")
                ),
                PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire(
                        "Question 3",
                        mutableListOf("Ch 1","Ch 2","Ch 3")
                ),
                PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire(
                        "Question 4",
                        mutableListOf("Ch 1","Ch 2","Ch 3")
                ),
                PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire(
                        "Question 5",
                        mutableListOf("Ch 1","Ch 2","Ch 3")
                )
        )
        val fragmentList: ArrayList<Fragment> = ArrayList()
        val deactivateDate = "31 Agustus 2019"
        fragmentList.add(PowerMerchantCancellationQuestionnaireIntroFragment.createInstance(
                deactivateDate
        ))
        list.forEachIndexed { position, multipleChecklistQuestionnaire ->
            fragmentList.add(PowerMerchantCancellationQuestionnaireFragment.createInstance(
                    position,
                    multipleChecklistQuestionnaire
            ))
        }
        return  fragmentList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        stepperModel = PMCancellationQuestionnaireStepperModel()
        super.onCreate(savedInstanceState)
    }

    fun isFinalPage(): Boolean = currentPosition == listFragment.size


    override fun updateToolbarTitle() {}

    override fun updateToolbarTitle(title: String?) {}
}
