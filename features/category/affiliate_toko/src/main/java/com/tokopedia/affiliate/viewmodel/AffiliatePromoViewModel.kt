package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_HOME
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_PROMO_PERFORMA
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.model.response.AffiliateDiscoveryCampaignResponse
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateSSAShopUiModel
import com.tokopedia.affiliate.usecase.AffiliateAnnouncementUseCase
import com.tokopedia.affiliate.usecase.AffiliateDiscoveryCampaignUseCase
import com.tokopedia.affiliate.usecase.AffiliateGetUnreadNotificationUseCase
import com.tokopedia.affiliate.usecase.AffiliateSSAShopUseCase
import com.tokopedia.affiliate.usecase.AffiliateSearchUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.universal_sharing.tracker.PageType
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.universal_sharing.view.model.PageDetail
import com.tokopedia.universal_sharing.view.model.Product
import com.tokopedia.universal_sharing.view.model.Shop
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okio.IOException
import timber.log.Timber
import javax.inject.Inject

class AffiliatePromoViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val affiliateSearchUseCase: AffiliateSearchUseCase,
    private val affiliateValidateUseCaseUseCase: AffiliateValidateUserStatusUseCase,
    private val affiliateAffiliateAnnouncementUseCase: AffiliateAnnouncementUseCase,
    private val affiliateDiscoveryCampaignUseCase: AffiliateDiscoveryCampaignUseCase,
    private val affiliateSSAShopUseCase: AffiliateSSAShopUseCase,
    private val affiliateUnreadNotificationUseCase: AffiliateGetUnreadNotificationUseCase,
    private val graphqlRepository: GraphqlRepository
) : BaseViewModel() {
    private var progressBar = MutableLiveData<Boolean>()
    private var affiliateSearchData = MutableLiveData<AffiliateSearchData>()
    private var errorMessage = MutableLiveData<String>()
    private var validateUserState = MutableLiveData<String>()
    private var affiliateAnnouncement = MutableLiveData<AffiliateAnnouncementDataV2>()
    private var discoBanners = MutableLiveData<AffiliateDiscoveryCampaignResponse>()
    private var tokoNowBottomSheetData = MutableLiveData<GenerateAffiliateLinkEligibility?>()
    private val ssaShopList = MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()
    private val _unreadNotificationCount = MutableLiveData(Int.ZERO)
    fun getUnreadNotificationCount(): LiveData<Int> = _unreadNotificationCount

    companion object {
        private const val SUCCESS = 1
        private const val DEFAULT_SSA_PAGE_SIZE = 7
    }

    fun isUserLoggedIn(): Boolean {
        return userSessionInterface.isLoggedIn
    }

    fun getUserName(): String {
        return userSessionInterface.name
    }

    fun getUserProfilePicture(): String {
        return userSessionInterface.profilePicture
    }

    fun getSearch(productLink: String) {
        progressBar.value = true
        launchCatchError(
            block = {
                affiliateSearchData.value =
                    affiliateSearchUseCase.affiliateSearchWithLink(arrayListOf(productLink))
                progressBar.value = false
            },
            onError = {
                progressBar.value = false
                it.printStackTrace()
                errorMessage.value = it.localizedMessage
            }
        )
    }

    fun getDiscoBanners(
        page: Int,
        limit: Int = 20
    ) {
        launchCatchError(
            block = {
                discoBanners.value =
                    affiliateDiscoveryCampaignUseCase.getAffiliateDiscoveryCampaign(
                        page,
                        limit
                    )
            },
            onError = {
                it.printStackTrace()
            }
        )
    }

    fun getAffiliateValidateUser() {
        launchCatchError(
            block = {
                validateUserdata.value =
                    affiliateValidateUseCaseUseCase.validateUserStatus(userSessionInterface.email)
                progressBar.value = false
            },
            onError = {
                progressBar.value = false
                it.printStackTrace()
            }
        )
    }

    fun getAnnouncementInformation(isHome: Boolean) {
        val page = if (isHome) {
            PAGE_ANNOUNCEMENT_HOME
        } else {
            PAGE_ANNOUNCEMENT_PROMO_PERFORMA
        }
        launchCatchError(
            block = {
                affiliateAnnouncement.value =
                    affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(page)
            },
            onError = {
                it.printStackTrace()
            }
        )
    }

    fun getTokoNowBottomSheetInfo(pageId: String?) {
        val affiliateEligibilityCheckUseCase = AffiliateEligibilityCheckUseCase(graphqlRepository)
        viewModelScope.launch {
            try {
                tokoNowBottomSheetData.value = affiliateEligibilityCheckUseCase.apply {
                    params = AffiliateEligibilityCheckUseCase.createParam(
                        AffiliateInput(
                            pageType = PageType.SHOP.value,
                            pageDetail = PageDetail(
                                pageType = PageType.SHOP.value,
                                pageId = pageId.toString(),
                                siteId = "1",
                                verticalId = "1"
                            ),
                            product = Product(
                                productID = "",
                                catLevel1 = "",
                                catLevel2 = "",
                                catLevel3 = "",
                                productPrice = "",
                                maxProductPrice = "",
                                productStatus = ""
                            ),
                            shop = Shop(
                                shopID = pageId.toString(),
                                shopStatus = 1,
                                isOS = true,
                                isPM = false
                            )
                        )
                    )
                }.executeOnBackground()
            } catch (e: Exception) {
                errorMessage.value = e.localizedMessage
                progressBar.value = false
                Timber.e(e)
            }
        }
    }

    fun fetchSSAShopList() {
        viewModelScope.launch {
            try {
                affiliateSSAShopUseCase.getSSAShopList(
                    Int.ZERO,
                    DEFAULT_SSA_PAGE_SIZE
                ).getSSAShopList?.let {
                    if (it.data?.status == SUCCESS) {
                        ssaShopList.value =
                            it.data.shopData?.mapNotNull { ssaShop ->
                                AffiliateSSAShopUiModel(
                                    ssaShop,
                                    true
                                )
                            }
                    } else {
                        errorMessage.value = it.data?.error?.message.orEmpty()
                    }
                }
            } catch (e: IOException) {
                Timber.e(e)
            }
        }
    }

    fun fetchUnreadNotificationCount() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            Timber.e(e)
        }
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            _unreadNotificationCount.value =
                affiliateUnreadNotificationUseCase.getUnreadNotifications()
        }
    }

    fun resetNotificationCount() {
        _unreadNotificationCount.value = Int.ZERO
    }

    fun getSSAShopList(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> = ssaShopList
    fun setValidateUserType(onRegistered: String) {
        validateUserState.value = onRegistered
    }

    private var validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    fun getErrorMessage(): LiveData<String> = errorMessage
    fun getAffiliateSearchData(): LiveData<AffiliateSearchData> = affiliateSearchData
    fun progressBar(): LiveData<Boolean> = progressBar
    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getValidateUserType(): LiveData<String> = validateUserState
    fun getAffiliateAnnouncement(): LiveData<AffiliateAnnouncementDataV2> =
        affiliateAnnouncement

    fun getDiscoCampaignBanners(): LiveData<AffiliateDiscoveryCampaignResponse> = discoBanners
    fun getTokoNowBottomSheetData(): LiveData<GenerateAffiliateLinkEligibility?> =
        tokoNowBottomSheetData
}
