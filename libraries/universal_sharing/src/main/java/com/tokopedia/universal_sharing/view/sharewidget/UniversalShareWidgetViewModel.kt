package com.tokopedia.universal_sharing.view.sharewidget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.universal_sharing.view.model.LinkShareWidgetProperties
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class UniversalShareWidgetViewModel @Inject constructor(
    private val affiliateEligibilityUseCase: AffiliateEligibilityCheckUseCase
) : ViewModel() {

    private val _linkProperties = MutableLiveData<LinkShareWidgetProperties>()
    val linkProperties: LiveData<LinkShareWidgetProperties> = _linkProperties
    private val _linkerResult = MutableLiveData<LinkerResultWidget>()
    val linkerResult: LiveData<LinkerResultWidget> = _linkerResult

    private val _resultAffiliate = MutableLiveData<Result<GenerateAffiliateLinkEligibility>>()
    val resultAffiliate: LiveData<Result<GenerateAffiliateLinkEligibility>> = _resultAffiliate

    fun setData(linkProperties: LinkShareWidgetProperties) {
        _linkProperties.value = linkProperties
    }

    fun executeLinkRequest(linkerShareData: LinkerShareData) {
        viewModelScope.launch {
            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                    0,
                    linkerShareData,
                    object: ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult?) {
                            _linkerResult.value = LinkerResultWidget.Success(linkerShareData)

                        }

                        override fun onError(linkerError: LinkerError?) {
                            _linkerResult.value = LinkerResultWidget.Failed(linkerError)
                        }

                    }
                )
            )
        }
    }

    fun checkIsAffiliate(affiliatePDPInput: AffiliateInput) {
        viewModelScope.launch {
            try {
                val result = affiliateEligibilityUseCase.apply {
                    params = AffiliateEligibilityCheckUseCase.createParam(affiliatePDPInput)
                }.executeOnBackground()
                _resultAffiliate.value = Success(result)
            } catch (e: Exception) {
                _resultAffiliate.value = Fail(e)
            }
        }
    }
}

sealed class LinkerResultWidget {
    data class Success(val linkerShareResult: LinkerShareResult?): LinkerResultWidget()
    data class Failed(val error: LinkerError?): LinkerResultWidget()
}

