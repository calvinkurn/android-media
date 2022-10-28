package com.tokopedia.home_account.explicitprofile.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.explicitprofile.ExplicitProfileConstant.EMPTY_PAGE_ICON_DISABLE
import com.tokopedia.home_account.explicitprofile.ExplicitProfileConstant.EMPTY_PAGE_ICON_ENABLE
import com.tokopedia.home_account.explicitprofile.ExplicitProfileConstant.EMPTY_PAGE_ID
import com.tokopedia.home_account.explicitprofile.ExplicitProfileConstant.EMPTY_PAGE_TITLE
import com.tokopedia.home_account.explicitprofile.data.*
import com.tokopedia.home_account.explicitprofile.domain.GetCategoriesUseCase
import com.tokopedia.home_account.explicitprofile.domain.SaveMultipleAnswersUseCase
import com.tokopedia.home_account.explicitprofile.wrapper.ExplicitProfileResult
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class ExplicitProfileViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val saveMultipleAnswersUseCase: SaveMultipleAnswersUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _explicitCategories = MutableLiveData<ExplicitProfileResult<CategoriesDataModel>>()
    val explicitCategories: LiveData<ExplicitProfileResult<CategoriesDataModel>>
        get() = _explicitCategories

    private val _saveAnswers = MutableLiveData<ExplicitProfileResult<ExplicitProfileSaveMultiAnswers>>()
    val saveAnswers: LiveData<ExplicitProfileResult<ExplicitProfileSaveMultiAnswers>>
        get() = _saveAnswers

    fun getAllCategories() {
        launchCatchError(coroutineContext, {
            _explicitCategories.value = ExplicitProfileResult.Loading()

            val response = getCategoriesUseCase(Unit)
            response.data.dataCategories.add(createStaticEmptyPage())

            _explicitCategories.value = ExplicitProfileResult.Success(response)
        }, {
            _explicitCategories.value = ExplicitProfileResult.Failure(MessageErrorException(it.message))
        })
    }

    fun saveShoppingPreferences(templatesDataModel: MutableList<TemplateDataModel>) {
        launchCatchError(coroutineContext, {
            _explicitCategories.value = ExplicitProfileResult.Loading()

            val paramsInput: MutableList<SaveMultipleAnswersParam> = mutableListOf()
            templatesDataModel.forEach { template ->

                val paramsSections: MutableList<SaveMultipleAnswersParam.InputParam.SectionsParam> = mutableListOf()
                template.sections.forEach { section ->

                    val paramsQuestions: MutableList<SaveMultipleAnswersParam.InputParam.SectionsParam.QuestionsParam> = mutableListOf()
                    section.questions.forEach { question ->
                        paramsQuestions.add(
                            SaveMultipleAnswersParam.InputParam.SectionsParam.QuestionsParam(
                                questionId = question.questionId,
                                answerValue = question.answerValue
                            )
                        )
                    }

                    paramsSections.add(
                        SaveMultipleAnswersParam.InputParam.SectionsParam(
                            sectionId = section.sectionId,
                            questions = paramsQuestions
                        )
                    )
                }

                paramsInput.add(
                    SaveMultipleAnswersParam(
                        SaveMultipleAnswersParam.InputParam(
                            templateId = template.id,
                            templateName = template.name,
                            sections = paramsSections
                        )
                    )
                )
            }

            val result = saveMultipleAnswersUseCase(paramsInput)
            _saveAnswers.value = ExplicitProfileResult.Success(result)

        }, {
            _saveAnswers.value = ExplicitProfileResult.Failure(MessageErrorException(it.message))
        })
    }

    private fun createStaticEmptyPage() : CategoryDataModel {
        return CategoryDataModel(
            idCategory = EMPTY_PAGE_ID,
            name = EMPTY_PAGE_TITLE,
            imageEnabled = EMPTY_PAGE_ICON_ENABLE,
            imageDisabled = EMPTY_PAGE_ICON_DISABLE,
        )
    }
}