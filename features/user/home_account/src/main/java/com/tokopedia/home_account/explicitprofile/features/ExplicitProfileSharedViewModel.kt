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

    fun setDefaultTemplatesData(templateDataModel: TemplateDataModel) {
        val template = defaultUserAnswers.find { template ->
            template.id ==  templateDataModel.id
        }

        if (template == null) {
            defaultUserAnswers.add(deepCopyData(templateDataModel))
            userAnswersCollection.add(deepCopyData(templateDataModel))
        }
    }

    fun isAnswersSameWithDefault(): Boolean {
       return defaultUserAnswers.toString() == userAnswersCollection.toString()
    }

    fun onAnswerChange(templateDataModel: TemplateDataModel) {
        launch {
            userAnswersCollection.find {
                it.id == templateDataModel.id
            }?.sections = templateDataModel.sections

            _userAnswers.emit(templateDataModel)
        }
    }

    private fun deepCopyData(templateDataModel: TemplateDataModel): TemplateDataModel {
        val json = Gson().toJson(templateDataModel)
        return Gson().fromJson(json, TemplateDataModel::class.java)
    }
}