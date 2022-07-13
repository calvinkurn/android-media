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

    val itemList: LiveData<List<Visitable<*>>>
        get() = _itemList

    private val _itemList = MutableLiveData<List<Visitable<*>>>()

    fun getLayout(data: InstructionTabUiModel?) {
        data?.run {
            val items = mutableListOf<Visitable<*>>().apply {
                add(IngredientSectionTitle)
                addAll(ingredients)
                add(InstructionSectionTitle)
                add(instruction)
            }
            _itemList.postValue(items)
        }
    }
}