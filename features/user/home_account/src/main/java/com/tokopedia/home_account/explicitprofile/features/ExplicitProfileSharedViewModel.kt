package com.tokopedia.home_account.explicitprofile.features

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.explicitprofile.data.TemplateDataModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExplicitProfileSharedViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _userAnswers = MutableSharedFlow<TemplateDataModel>()
    val userAnswers = _userAnswers.asSharedFlow()

    private val defaultUserAnswers: MutableList<TemplateDataModel> = mutableListOf()
    private val userAnswersCollection: MutableList<TemplateDataModel> = mutableListOf()

    /**
     * to save temporary data from BE on variable [defaultUserAnswers]
     * also clone data into [userAnswersCollection], and the variable used to collect latest user answesr
     */
    fun setDefaultTemplatesData(templateDataModel: TemplateDataModel) {
        val template = defaultUserAnswers.find { template ->
            template.id ==  templateDataModel.id
        }

        if (template == null) {
            defaultUserAnswers.add(deepCopyData(templateDataModel))
            userAnswersCollection.add(deepCopyData(templateDataModel))
        }
    }

    /**
     * used to compare between selected answers from user and default answers from BE
     * and the data used to update `Simpan` button enable stat on [ExplicitProfileFragment.onSelectionAnswersChange]
     */
    fun isAnswersSameWithDefault(): Boolean {
       return defaultUserAnswers.toString() == userAnswersCollection.toString()
    }

    /**
     * to update user answers
     * also update data on [userAnswersCollection]
     */
    fun onAnswerChange(templateDataModel: TemplateDataModel) {
        launch {
            userAnswersCollection.find {
                it.id == templateDataModel.id
            }?.sections = templateDataModel.sections

            _userAnswers.emit(templateDataModel)
        }
    }

    /**
     * to create json string format
     * used for clone data deeply and with unique addresses, because mutableList have issue when copy from another mutableList
     * --
     * detail issue :
     * if we have data A(id = 1) & B(id = 2)
     * then copy data A into B (B = A), B(id = 1) A(id = 1)
     * and I change data from B.id = 0
     * data A.id also change into 0 too
     * caused : address from both data is same
     */
    private fun deepCopyData(templateDataModel: TemplateDataModel): TemplateDataModel {
        val json = Gson().toJson(templateDataModel)
        return Gson().fromJson(json, TemplateDataModel::class.java)
    }
}