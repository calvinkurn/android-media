package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.SectionTitleUiModel.IngredientSectionTitle
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.SectionTitleUiModel.InstructionSectionTitle
import javax.inject.Inject

class TokoNowRecipeInstructionViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    val instructionItemList: LiveData<List<Visitable<*>>>
        get() = _instructionItemList

    private val _instructionItemList = MutableLiveData<List<Visitable<*>>>()

    fun getInstructionItems(data: InstructionTabUiModel?) {
        data?.run {
            val items = mutableListOf<Visitable<*>>().apply {
                add(IngredientSectionTitle)
                addAll(ingredients)
                add(InstructionSectionTitle)
                add(instruction)
            }
            _instructionItemList.postValue(items)
        }
    }
}