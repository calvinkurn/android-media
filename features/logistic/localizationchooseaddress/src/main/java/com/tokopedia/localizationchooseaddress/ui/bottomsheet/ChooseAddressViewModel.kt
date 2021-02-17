package com.tokopedia.localizationchooseaddress.ui.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressListModel
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseAddressViewModel @Inject constructor(private val chooseAddressRepo: ChooseAddressRepository,
                                                 private val chooseAddressMapper: ChooseAddressMapper) : ViewModel(){

    private val _chosenAddressList = MutableLiveData<Result<List<ChosenAddressModel>>>()
    val chosenAddressList: LiveData<Result<List<ChosenAddressModel>>>
        get() = _chosenAddressList

    fun getChosenAddressList() {
        viewModelScope.launch {
            val getChosenAddressList = chooseAddressRepo.getChosenAddressList()
            _chosenAddressList.value = Result.success(chooseAddressMapper.mapChosenAddressList(getChosenAddressList.response     ))
        }
    }

}