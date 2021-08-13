package com.tokopedia.talk.feature.write.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.talk.feature.write.data.mapper.TalkWriteMapper
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingForm
import com.tokopedia.talk.feature.write.data.model.DiscussionSubmitForm
import com.tokopedia.talk.feature.write.domain.usecase.DiscussionGetWritingFormUseCase
import com.tokopedia.talk.feature.write.domain.usecase.DiscussionSubmitFormUseCase
import com.tokopedia.talk.feature.write.presentation.uimodel.TalkWriteButtonState
import com.tokopedia.talk.feature.write.presentation.uimodel.TalkWriteCategory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TalkWriteViewModel @Inject constructor(private val dispatchers: CoroutineDispatchers,
                                             private val discussionGetWritingFormUseCase: DiscussionGetWritingFormUseCase,
                                             private val discussionSubmitFormUseCase: DiscussionSubmitFormUseCase,
                                             private val userSession: UserSessionInterface
): BaseViewModel(dispatchers.io) {

    var shopId = ""
    var isVariantSelected = false
    var availableVariants = ""

    private val productId = MutableLiveData<String>()
    val writeFormData: LiveData<Result<DiscussionGetWritingForm>> = Transformations.switchMap(productId) {
        getWriteFormData(it)
    }

    private val _buttonState = MediatorLiveData<TalkWriteButtonState>()
    val buttonState: LiveData<TalkWriteButtonState>
        get() = _buttonState

    private val _textFieldState = MediatorLiveData<Boolean>()
    val textFieldState: LiveData<Boolean>
        get() = _textFieldState

    private val isTextNotEmpty = MutableLiveData<Boolean>()

    private val _categoryChips: MutableLiveData<List<TalkWriteCategory>> = Transformations.map(writeFormData) {
        when(it) {
            is Success -> TalkWriteMapper.mapDiscussionCategoryToTalkCategory(it.data.categories)
            is Fail -> emptyList()
        }
    } as MutableLiveData<List<TalkWriteCategory>>
    val categoryChips: LiveData<List<TalkWriteCategory>>
        get() = _categoryChips

    private var selectedCategory: TalkWriteCategory? = null

    private val _submitFormResult = MutableLiveData<Result<DiscussionSubmitForm>>()
    val submitFormResult: LiveData<Result<DiscussionSubmitForm>>
        get() = _submitFormResult

    init {
        _buttonState.value = TalkWriteButtonState()
        _categoryChips.value = emptyList()
        _buttonState.addSource(categoryChips) {
            updateButtonFromCategories(it.any { category -> category.isSelected })
        }
        _buttonState.addSource(isTextNotEmpty) {
            updateButtonFromText(it)
        }
        _textFieldState.addSource(categoryChips) {
            updateTextFieldFromCategories(it.any { category -> category.isSelected })
        }
    }

    private fun getWriteFormData(productId: String) : LiveData<Result<DiscussionGetWritingForm>> {
        val result = MutableLiveData<Result<DiscussionGetWritingForm>>()
        launchCatchError(block = {
            discussionGetWritingFormUseCase.setParams(productId)
            val response = discussionGetWritingFormUseCase.executeOnBackground()
            result.postValue(Success(response.discussionGetWritingForm))
            shopId = response.discussionGetWritingForm.shopId
        }) {
            result.postValue(Fail(it))
        }
        return result
    }

    fun toggleCategory(category: TalkWriteCategory) {
        selectedCategory = if(selectedCategory == category) {
            null
        } else {
            category
        }
        _categoryChips.value?.forEach {
            if(it.categoryName == selectedCategory?.categoryName) {
                it.isSelected = !it.isSelected
            } else {
                it.isSelected = false
            }
        }
        _categoryChips.notifyObserver()
    }

    private fun updateButtonFromCategories(isAnyCategorySelected: Boolean) {
        _buttonState.value = _buttonState.value?.copy(isAnyCategorySelected = isAnyCategorySelected)
    }

    private fun updateTextFieldFromCategories(isAnyCategorySelected: Boolean) {
        _textFieldState.value = isAnyCategorySelected
    }

    private fun updateButtonFromText(isTextNotEmpty: Boolean) {
        _buttonState.value = _buttonState.value?.copy(isNotTextEmpty = isTextNotEmpty)
    }

    fun submitForm(text: String) {
        launchCatchError(block = {
            discussionSubmitFormUseCase.setParams(text, selectedCategory?.categoryName.orEmpty(), productId.value ?: "")
            val response = discussionSubmitFormUseCase.executeOnBackground()
            _submitFormResult.postValue(Success(response.discussionSubmitForm))
        }) {
            _submitFormResult.postValue(Fail(it))
        }
    }

    fun updateIsTextNotEmpty(isTextNotEmptyData: Boolean) {
        isTextNotEmpty.value = isTextNotEmptyData
    }

    fun setProductId(productId: String) {
        this.productId.value = productId
    }

    fun getProductId(): String? {
        return productId.value
    }

    fun refresh() {
        productId.notifyObserver()
    }

    fun getSelectedCategory(): TalkWriteCategory? {
        return selectedCategory
    }

    fun getUserId(): String {
        return userSession.userId
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}