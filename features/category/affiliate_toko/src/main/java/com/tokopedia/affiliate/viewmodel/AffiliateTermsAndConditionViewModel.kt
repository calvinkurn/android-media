package com.tokopedia.affiliate.viewmodel


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.AffiliateHeaderItemData
import com.tokopedia.affiliate.model.AffiliateOnBoardingData
import com.tokopedia.affiliate.model.AffiliateTermsAndConditionData
import com.tokopedia.affiliate.model.raw.request.OnBoardingRequest
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTermsAndConditionModel
import com.tokopedia.affiliate.usecase.AffiliateOnBoardingUseCase
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AffiliateTermsAndConditionViewModel@Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val affiliateOnBoardingUseCase: AffiliateOnBoardingUseCase)
    :BaseViewModel() {
    private var onBoardingData = MutableLiveData<AffiliateOnBoardingData.OnBoardAffiliate>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var progressBar = MutableLiveData<Boolean>()

    fun affiliateOnBoarding(channels : ArrayList<OnBoardingRequest.Channel>) {
        launchCatchError(block = {
            progressBar.value = true
            onBoardingData.value = affiliateOnBoardingUseCase.affiliateOnBoarding(channels)
            progressBar.value = false
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it

            //TODO Remove Dummy
            onBoardingData.value = AffiliateOnBoardingData.OnBoardAffiliate(
                    AffiliateOnBoardingData.OnBoardAffiliate.Data(null,1))
        })
    }

    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun progressBar(): LiveData<Boolean> = progressBar
    fun getOnBoardingData() : LiveData<AffiliateOnBoardingData.OnBoardAffiliate> = onBoardingData
}